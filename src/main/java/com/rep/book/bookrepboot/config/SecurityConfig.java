package com.rep.book.bookrepboot.config;

import com.rep.book.bookrepboot.service.CustomUserDetailsService;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.stream.Stream;

@Configuration
@EnableWebSecurity(debug = false)
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    /**
     * 허용 리스트
     */
    public static final String[] PERMIT_LIST = new String[] {
            "/",
            "/home",
            "/sign-in",
            "/sign-in-proc",
            "/sign-up",
            "/emailCheck",
            "/find-password",
            "/get-image",
            "/privacy-policy",
            "/terms",
            "/resources/**",
            "/WEB-INF/**",
            "https://code.jquery.com/jquery-3.7.1.min.js", // jquery
            "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css"
    };


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Autowired
    public CustomUserDetailsService customUserDetailsService;

    @Autowired
    public SecurityBeansConfig securityBeansConfig;


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(customUserDetailsService).passwordEncoder(securityBeansConfig.passwordEncoder());
    }

    /**
     * Security 설정
     * @param security HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception 예외
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception{
        log.info("securityFilterChain");
        security
                .csrf(
                        (csrf) ->
                                //csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // csrf 토큰을 쿠키에 저장
                                csrf.disable()
                )
                .formLogin( // 로그인 설정
                        (formLogin) ->
                                formLogin
                                        .loginPage("/sign-in").permitAll() // 로그인 페이지
                                        .loginProcessingUrl("/sign-in-proc").permitAll() // 로그인 처리 페이지
                                        .defaultSuccessUrl("/home", true).permitAll() // 로그인 성공시 이동 페이지
                                        .failureUrl("/sign-in").permitAll() // 로그인 실패시 이동 페이지
                )
                .authorizeHttpRequests( // 인가 설정
                        (authorizeHttpRequests) ->
                                authorizeHttpRequests
                                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll() // forward 허용
                                        .requestMatchers(
                                                Stream // permitList에 있는 경로는 모두 허용
                                                        .of(PERMIT_LIST)
                                                        .map(AntPathRequestMatcher::antMatcher)
                                                        .toArray(AntPathRequestMatcher[]::new)
                                        ).permitAll()
                                        .anyRequest().authenticated() // 나머지는 인증 필요
                )
                .rememberMe(
                        (rememberMe) -> rememberMe.alwaysRemember(true)
                )
                .logout(
                        (logout) ->
                                logout
                                        .logoutUrl("/sign-out")
                                        .deleteCookies("remember-me")
                );
        return security.build();
    }

    /**
     * 비밀번호 암호화
     * @return BCryptPasswordEncoder
     */
}
