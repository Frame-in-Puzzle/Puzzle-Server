package com.server.Puzzle.global.security;

import com.server.Puzzle.global.security.jwt.JwtExceptionFilter;
import com.server.Puzzle.global.security.jwt.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenFilter jwtTokenFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    @Override // 접근 가능
    public void configure(WebSecurity web) throws Exception {

        web.ignoring().antMatchers("/v2/api-docs")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/swagger-ui.html")
                .antMatchers("/configuration/**")
                .antMatchers("/webjars/**")
                .antMatchers("/public")
                .antMatchers("/api/**")
                .antMatchers("/health")
                // Un-secure H2 Database (for testing purposes, H2 console shouldn't be unprotected in production)
                .and()
                .ignoring()
                .antMatchers("/h2-console/**/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Disable CSRF (cross site request forgery)
        http
                .cors().and()
                .csrf().disable()
                .httpBasic().disable();

        // No session will be created or used by spring security
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Entry points
        http.authorizeRequests() // 권한을 허용할 메서드
                .antMatchers("/api/oauth/login/github").permitAll()
                .antMatchers("/api/attend/board/{boardId}").permitAll()
                .antMatchers("/api/board/{id}").permitAll()
                .antMatchers("/api/board/all/**").permitAll()
                .antMatchers("/api/board/filter/**").permitAll()
                .antMatchers("/api/board/create-url/**").permitAll()
                .antMatchers("/api/profile/{githubId}/**").permitAll()
                .antMatchers("/api/token/reissue").permitAll()
                .antMatchers("/api/board").permitAll()

                // 권한을 처리 할 메서드
                .antMatchers("/api/profile/update").hasAuthority("ROLE_USER")
                .antMatchers("/api/user/**").hasAuthority("ROLE_USER")
                .antMatchers("/api/attend/**").hasAuthority("ROLE_USER")

        http.authorizeRequests() // 권한 처리를 할 메서드
                .anyRequest().authenticated();

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtExceptionFilter, JwtTokenFilter.class);
    }

}
