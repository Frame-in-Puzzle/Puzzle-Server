package com.server.Puzzle.global.security.authentication;

import com.server.Puzzle.domain.user.repository.UserRepository;
import com.server.Puzzle.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.server.Puzzle.global.exception.ErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String githubId) throws UsernameNotFoundException {
       return (UserDetails) userRepository.findByGithubId(githubId)
               .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }
}
