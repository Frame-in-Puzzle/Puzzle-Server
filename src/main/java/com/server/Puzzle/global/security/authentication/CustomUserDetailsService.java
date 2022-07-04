package com.server.Puzzle.global.security.authentication;

import com.server.Puzzle.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String githubId) throws UsernameNotFoundException {
       return (UserDetails) userRepository.findUserAndRolesByGithubId(githubId);
    }
    
}
