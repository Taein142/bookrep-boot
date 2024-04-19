package com.rep.book.bookrepboot.config;

import com.rep.book.bookrepboot.service.CustomUserDetailsServiceForUser;
import jakarta.servlet.DispatcherType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity(debug = true)
@Slf4j
public class UserSecurityConfig {


    /**
     * 정적 자원에 대한 보안 설정을 무시한다.
     * @return WebSecurityCustomizer
     */
    @Bean
    public WebSecurityCustomizer userWebSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**
     * 사용자 인증을 위한 UserDetailsService와 PasswordEncoder를 주입받는다.
     */
    @Autowired
    public CustomUserDetailsServiceForUser customUserDetailsServiceForUser;

    /**
     * SecurityBeansConfig를 주입받는다.
     */
    @Autowired
    public SecurityBeansConfig securityBeansConfig;

    /**
     * 사용자 인증을 위한 UserDetailsService와 PasswordEncoder를 설정한다.
     * @param auth AuthenticationManagerBuilder
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsServiceForUser).passwordEncoder(securityBeansConfig.passwordEncoder());
    }

    /**
     * 사용자 인증을 위한 SecurityFilterChain을 설정한다.
     * @param security HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception
     */
    @Bean
    @Order(1)
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity security) throws Exception {
        log.info("user-securityFilterChain");
        //csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // csrf 토큰을 쿠키에 저장
        security
                .securityMatcher("/user/**")
                .csrf(
                        AbstractHttpConfigurer::disable
                )
                .authorizeHttpRequests( // 인가 설정
                        (authorizeHttpRequests) ->
                                authorizeHttpRequests
                                        .requestMatchers("/user/sign-in-proc").permitAll()// forward 허용
                                        .anyRequest().hasAnyRole("USER", "ADMIN", "DRIVER", "SUPER_ADMIN")
                )
                .formLogin( // 로그인 설정
                        (formLogin) ->
                                formLogin
                                        .loginPage("/user/sign-in").permitAll() // 로그인 페이지
                                        .loginProcessingUrl("/user/sign-in-proc").permitAll() // 로그인 처리 페이지
                                        .successHandler(new CustomSuccessHandler()).permitAll() // 로그인 성공시 이동 페이지
                                        .failureUrl("/user/sign-in").permitAll()
                        // 로그인 실패시 이동 페이지
                )
                .logout(
                        (logout) ->
                                logout
                                        .logoutUrl("/user/sign-out")
                )
                .userDetailsService(customUserDetailsServiceForUser)
                .rememberMe(
                        (remember) -> remember
                                .userDetailsService(customUserDetailsServiceForUser)
                                .alwaysRemember(true)
                );
        return security.build();
    }

    /**
     * 관리자 인증을 위한 SecurityFilterChain을 설정한다.
     * @param security HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception
     */
    @Bean
    @Order(2)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity security) throws Exception {
        log.info("admin-securityFilterChain");
        //csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // csrf 토큰을 쿠키에 저장
        security
                .securityMatcher("/admin/**")
                .csrf(
                        AbstractHttpConfigurer::disable
                )
                .authorizeHttpRequests( // 인가 설정
                        (authorizeHttpRequests) ->
                                authorizeHttpRequests// forward 허용
                                        .anyRequest().hasAnyRole("ADMIN", "DRIVER", "SUPER_ADMIN")
                )
                .formLogin( // 로그인 설정
                        (formLogin) ->
                                formLogin
                                        .loginPage("/admin/admin-sign-in").permitAll() // 로그인 페이지
                                        .loginProcessingUrl("/admin/admin-sign-in-proc").permitAll() // 로그인 처리 페이지
                                        .successHandler(new CustomSuccessHandler()).permitAll() // 로그인 성공시 이동 페이지
                                        .failureUrl("/admin/admin-sign-in").permitAll()
                        // 로그인 실패시 이동 페이지
                )
                .logout(
                        (logout) ->
                                logout
                                        .logoutUrl("/admin/sign-out")
                )
                .userDetailsService(customUserDetailsServiceForUser)
                .rememberMe(
                        (remember) -> remember
                                .userDetailsService(customUserDetailsServiceForUser)
                                .alwaysRemember(true)
                );
        return security.build();
    }

    /**
     * 기본 인증을 위한 SecurityFilterChain을 설정한다.
     * @param security HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception
     */
    @Bean
    @Order(3)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity security) throws Exception {

        security
                .securityMatcher("/**")
                .csrf(
                        AbstractHttpConfigurer::disable
                )
                .authorizeHttpRequests(
                        (authorizeHttpRequests) -> authorizeHttpRequests
                                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                                .requestMatchers("/user/**").denyAll()
                                .requestMatchers("/admin/**").denyAll()
                                .requestMatchers("/error").permitAll()
                                .anyRequest().permitAll()
                );

        return security.build();
    }
}
