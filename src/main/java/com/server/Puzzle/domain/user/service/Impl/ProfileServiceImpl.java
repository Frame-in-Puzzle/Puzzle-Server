package com.server.Puzzle.domain.user.service.Impl;

import com.server.Puzzle.domain.board.domain.BoardFile;
import com.server.Puzzle.domain.board.repository.BoardRepository;
import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.domain.UserLanguage;
import com.server.Puzzle.domain.user.dto.UserBoardResponse;
import com.server.Puzzle.domain.user.dto.UserResponseDto;
import com.server.Puzzle.domain.user.dto.UserUpdateDto;
import com.server.Puzzle.domain.user.repository.UserLanguageRepository;
import com.server.Puzzle.domain.user.repository.UserRepository;
import com.server.Puzzle.domain.user.service.ProfileService;
import com.server.Puzzle.global.enumType.Language;
import com.server.Puzzle.global.exception.CustomException;
import com.server.Puzzle.global.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;

import static com.server.Puzzle.global.exception.ErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {

    private final CurrentUserUtil currentUserUtil;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final UserLanguageRepository userLanguageRepo;

    @Override
    public UserResponseDto getProfile(String githubId) {

        userRepository.findByGithubId(githubId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        UserResponseDto user = userRepository.findByUser(githubId);

        return user;
    }

    @Transactional
    @Override
    public void profileUpdate(UserUpdateDto userInfo) {

        User user = currentUserUtil.getCurrentUser();

        List<Language> languageList = userInfo.getLanguage();

        userLanguageRepo.deleteAllByUserId(user.getId());

        for (Language language : languageList) {
            userLanguageRepo.save(UserLanguage.builder()
                    .user(user)
                    .language(language)
                    .build());
        }

        user
                .updateName(userInfo.getName())
                .updateEmail(userInfo.getEmail())
                .updateImageUrl(userInfo.getImageUrl())
                .updateBio(userInfo.getBio())
                .updateUrl(userInfo.getUrl())
                .updateIsFirstVisited(false)
                .updateField(userInfo.getField());
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
                        .date(board.getCreatedDate())
                        .status(board.getStatus())
                        .thumbnail(
                                board.getBoardFiles().stream().map(BoardFile::getUrl)
                                        .findFirst()
                                        .orElse(null))
                        .build());
    }

}
