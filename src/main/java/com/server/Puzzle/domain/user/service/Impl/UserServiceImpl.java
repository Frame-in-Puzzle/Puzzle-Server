package com.server.Puzzle.domain.user.service.Impl;

import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.domain.UserLanguage;
import com.server.Puzzle.domain.user.dto.UserUpdateDto;
import com.server.Puzzle.domain.user.repository.UserLanguageRepository;
import com.server.Puzzle.domain.user.repository.UserRepository;
import com.server.Puzzle.domain.user.service.UserService;
import com.server.Puzzle.global.enumType.Language;
import com.server.Puzzle.global.exception.CustomException;
import com.server.Puzzle.global.security.jwt.JwtTokenProvider;
import com.server.Puzzle.global.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.server.Puzzle.global.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final CurrentUserUtil currentUserUtil;
    private final UserRepository userRepository;
    private final UserLanguageRepository userLanguageRepo;
    private final JwtTokenProvider jwtTokenProvider;

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
    public void infoRegistration(UserUpdateDto userInfo) {
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

    @Transactional
    @Override
    public Map<String, String> reissueToken(String refreshToken) {

        User currentUser = currentUserUtil.getCurrentUser();

        User user = userRepository.findByGithubId(currentUser.getGithubId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (user.getRefreshToken() == null) {
            throw new CustomException(NOT_LOGGED_IN);
        }

        if(!(user.getRefreshToken().equals(refreshToken) && jwtTokenProvider.validateToken(refreshToken))) {
            throw new CustomException(UNABLE_TO_ISSUANCE_REFRESHTOKEN);
        }

        Map<String, String> map = new HashMap<>();

        String newAccessToken = jwtTokenProvider.createToken(user.getGithubId(), user.getRoles());
        String newRefreshToken = jwtTokenProvider.createRefreshToken();

        user.updateRefreshToken(newRefreshToken);

        map.put("RefreshToken", "Bearer " + newRefreshToken);
        map.put("AccessToken", "Bearer " + newAccessToken);

        return map;
    }


}