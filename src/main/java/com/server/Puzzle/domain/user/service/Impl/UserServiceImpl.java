package com.server.Puzzle.domain.user.service.Impl;

import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.repository.UserRepository;
import com.server.Puzzle.domain.user.service.UserService;
import com.server.Puzzle.global.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final CurrentUserUtil currentUserUtil;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void logout() {
        currentUserUtil.getCurrentUser().updateRefreshToken(null);
    }

    @Override
    public void delete() {
        User currentUser = currentUserUtil.getCurrentUser();

        User savedUser = userRepository.findByName(currentUser.getName())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));

        userRepository.delete(savedUser);
    }
}