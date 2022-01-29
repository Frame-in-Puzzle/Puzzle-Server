package com.server.Puzzle.global.security.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenFilterConfiguer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtTokenProvider jwtTokenProdvider;

    public JwtTokenFilterConfiguer(JwtTokenProvider jwtTokenProdvider) {
        this.jwtTokenProdvider = jwtTokenProdvider;
    }

    public void configure(HttpSecurity httpSecurity) throws Exception{
        JwtTokenFilter customFilter = new JwtTokenFilter(jwtTokenProdvider);
        httpSecurity.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}