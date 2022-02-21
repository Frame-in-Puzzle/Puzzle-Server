package com.server.Puzzle.domain.user.service.Impl;

import com.server.Puzzle.domain.board.domain.BoardFile;
import com.server.Puzzle.domain.board.repository.BoardRepository;
import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.dto.MyBoardResponse;
import com.server.Puzzle.domain.user.dto.UserInfoDto;
import com.server.Puzzle.domain.user.repository.UserRepository;
import com.server.Puzzle.domain.user.service.ProfileService;
import com.server.Puzzle.global.exception.CustomException;
import com.server.Puzzle.global.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.server.Puzzle.global.exception.ErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {
    private final CurrentUserUtil currentUserUtil;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Override
    public UserInfoDto getProfile(String githubId) {
        User user = userRepository.findByGithubId(githubId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        return UserInfoDto.builder()
                .user(user)
                .build();
    }

    @Transactional
    @Override
    public void profileUpdate(UserInfoDto userInfo) {
        User user = currentUserUtil.getCurrentUser();

        user
                .updateName(userInfo.getName())
                .updateEmail(userInfo.getEmail())
                .updateImageUrl(userInfo.getImageUrl())
                .updateBio(userInfo.getBio())
                .updateUrl(userInfo.getUrl())
                .updateLanguage(userInfo.getLanguage())
                .updateField(userInfo.getField());
    }

    @Override
    public Page<MyBoardResponse> getMyBoard(String githubId, Pageable pageable) {
        User user = userRepository.findByGithubId(githubId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        return boardRepository.findBoardsByUser(user, pageable)
                .map(board -> MyBoardResponse.builder()
                        .title(board.getTitle())
                        .contents(board.getContents())
                        .date(board.getCreatedDate())
                        .status(board.getStatus())
                        .files(
                                board.getBoardFiles().stream().map(BoardFile::getUrl)
                                        .findFirst()
                                        .orElse(null))
                        .build());
    }

}
