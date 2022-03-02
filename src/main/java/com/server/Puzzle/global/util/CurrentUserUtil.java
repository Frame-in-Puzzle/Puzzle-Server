package com.server.Puzzle.global.util;

import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.repository.UserRepository;
import com.server.Puzzle.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import static com.server.Puzzle.global.exception.ErrorCode.UNAUTHORIZED_USER;
import static com.server.Puzzle.global.exception.ErrorCode.USER_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class CurrentUserUtil {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        String name = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal instanceof UserDetails) {
            name = ((UserDetails) principal).getUsername();
        } else{
            name = principal.toString();
        }
        return userRepository.findByName(name)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }
}