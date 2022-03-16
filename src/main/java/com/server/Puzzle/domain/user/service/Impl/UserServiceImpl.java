package com.server.Puzzle.domain.user.service.Impl;

import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.domain.UserLanguage;
import com.server.Puzzle.domain.user.dto.UserUpdateDto;
import com.server.Puzzle.domain.user.repository.UserLanguageRepository;
import com.server.Puzzle.domain.user.repository.UserRepository;
import com.server.Puzzle.domain.user.service.UserService;
import com.server.Puzzle.global.enumType.Language;
import com.server.Puzzle.global.exception.CustomException;
import com.server.Puzzle.global.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;

import static com.server.Puzzle.global.exception.ErrorCode.IS_ALREADY_USER;
import static com.server.Puzzle.global.exception.ErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final CurrentUserUtil currentUserUtil;
    private final UserRepository userRepository;
    private final UserLanguageRepository userLanguageRepo;

    @Transactional
    @Override
    public void logout() {
        currentUserUtil.getCurrentUser().updateRefreshToken(null);
    }

    @Override
    public void delete() {
        User currentUser = currentUserUtil.getCurrentUser();

        User savedUser = userRepository.findByName(currentUser.getName())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        userRepository.delete(savedUser);
    }

    @Transactional
    @Override
    public void infoRegister(UserUpdateDto userInfo) {
        User user = currentUserUtil.getCurrentUser();

        if(!user.isFirstVisited())
            throw new CustomException(IS_ALREADY_USER);

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


}