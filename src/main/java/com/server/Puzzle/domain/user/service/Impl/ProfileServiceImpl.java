package com.server.Puzzle.domain.user.service.Impl;

import com.server.Puzzle.domain.board.domain.BoardField;
import com.server.Puzzle.domain.board.domain.BoardFile;
import com.server.Puzzle.domain.board.repository.BoardRepository;
import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.domain.UserLanguage;
import com.server.Puzzle.domain.user.dto.*;
import com.server.Puzzle.domain.user.repository.UserLanguageRepository;
import com.server.Puzzle.domain.user.repository.UserRepository;
import com.server.Puzzle.domain.user.service.ProfileService;
import com.server.Puzzle.global.enumType.Language;
import com.server.Puzzle.global.exception.CustomException;
import com.server.Puzzle.global.util.AwsS3Util;
import com.server.Puzzle.global.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.server.Puzzle.global.exception.ErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {

    @Value("${cloud.aws.url}")
    private String s3Url;

    private static final String GITHUB_IMAGE_URL = "https://avatars.githubusercontent.com/";

    private final CurrentUserUtil currentUserUtil;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final UserLanguageRepository userLanguageRepo;
    private final AwsS3Util awsS3Util;

    @Override
    public UserProfileResponse getProfile(String githubId) {

        userRepository.findByGithubId(githubId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        UserProfileResponse user = userRepository.findByUser(githubId);

        return user;
    }

    @Transactional
    @Override
    public void profileUpdate(ProfileUpdateDto profileUpdateDto) {

        User user = currentUserUtil.getCurrentUser();

        List<Language> languageList = profileUpdateDto.getLanguages();

        userLanguageRepo.deleteAllByUserId(user.getId());

        saveLanguage(languageList, user);

        user
                .updateName(profileUpdateDto.getName())
                .updateEmail(profileUpdateDto.getEmail())
                .updateBio(profileUpdateDto.getBio())
                .updateIsFirstVisited(false)
                .updateField(profileUpdateDto.getField());
    }

    @Transactional
    @Override
    public String profileImageUpdate(MultipartFile multipartFile) {
        User currentUser = currentUserUtil.getCurrentUser();
        String profileImageUrl = currentUser.getImageUrl();

        if (!profileImageUrl.substring(38).equals(GITHUB_IMAGE_URL)) {
            awsS3Util.deleteS3(profileImageUrl);
        }

        String fileName = awsS3Util.putS3(multipartFile);
        String s3fileURL = s3Url + fileName;
        currentUser.updateImageUrl(s3fileURL);

        return s3fileURL;
    }

    @Override
    public Page<UserBoardResponse> getUserBoard(String githubId, Pageable pageable) {

        User user = userRepository.findByGithubId(githubId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        return boardRepository.findBoardsByUser(user, pageable)
                .map(board -> UserBoardResponse.builder()
                        .boardId(board.getId())
                        .title(board.getTitle())
                        .contents(board.getContents())
                        .introduce(board.getIntroduce())
                        .date(board.getCreatedDate())
                        .purpose(board.getPurpose())
                        .status(board.getStatus())
                        .thumbnail(
                                board.getBoardFiles().stream()
                                        .map(BoardFile::getUrl)
                                        .findFirst()
                                        .orElse(null))
                        .fields(board.getBoardFields().stream()
                                .map(BoardField::getField)
                                .collect(Collectors.toList()))
                        .build());
    }

    private void saveLanguage(List<Language> languages, User user) {
        for (Language language : languages) {
            userLanguageRepo.save(UserLanguage.builder()
                    .user(user)
                    .language(language)
                    .build());
        }
    }

}
