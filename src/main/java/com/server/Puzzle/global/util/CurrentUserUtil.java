package com.server.Puzzle.global.util;

import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentUserUtil {

    private final UserRepository userRepository;

    public static String getCurrentName (){
        String name  = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            name  = ((UserDetails) principal).getUsername();
        } else{
            name  = principal.toString();
        }
        return name ;
    }

    public User getCurrentUser() {
        String name  = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal instanceof UserDetails) {
            name  = ((UserDetails) principal).getUsername();
        } else{
            name  = principal.toString();
        }
        return userRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));
    }
}