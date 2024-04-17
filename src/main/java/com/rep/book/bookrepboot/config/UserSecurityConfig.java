package com.rep.book.bookrepboot.config;

import com.rep.book.bookrepboot.service.CustomUserDetailsServiceForUser;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
public class UserSecurityConfig {

    @Autowired
    public UserSecurityConfig(CustomUserDetailsServiceForUser customUserDetailsServiceForUser, SecurityBeansConfig securityBeansConfig) {
        this.customUserDetailsServiceForUser = customUserDetailsServiceForUser;
        this.securityBeansConfig = securityBeansConfig;
    }

    /**
     * 허용 리스트
     */
    public static final String[] PERMIT_LIST_USER = new String[] {
//            "/",
//            "/home",
//            "/sign-in",
//            "/sign-in-proc",
//            "/sign-up",
//            "/emailCheck",
//            "/find-password",
//            "/get-image",
//            "/privacy-policy",
//            "/terms",
//            "/resources/**",
//            "classpath:/resources/**",
//            "classpath/resources/css/**",
//            "classpath:/resources/images/**",
//            "/WEB-INF/**",
//            "https://code.jquery.com/jquery-3.7.1.min.js", // jquery
//            "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css",
//            "http://www.thymeleaf.org",
//            "/admin/admin-sign-in"
    };


    @Bean
    public WebSecurityCustomizer userWebSecurityCustomizer(){
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Autowired
    public CustomUserDetailsServiceForUser customUserDetailsServiceForUser;


    @Autowired
    public SecurityBeansConfig securityBeansConfig;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(customUserDetailsServiceForUser).passwordEncoder(securityBeansConfig.passwordEncoder());
    }


    /**
     * Security 설정
     * @param security HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception 예외
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity security) throws Exception{
        log.info("user-securityFilterChain");
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
                                        .successHandler(new CustomSuccessHandler()).permitAll() // 로그인 성공시 이동 페이지
                                        .failureUrl("/sign-in").permitAll()
                                        // 로그인 실패시 이동 페이지
                )
//                .formLogin(
//                        (formLogin) ->
//                                formLogin
//                                        .loginPage("/admin/admin-sign-in").permitAll() // 로그인 페이지
//                                        .loginProcessingUrl("/admin/admin-sign-in").permitAll() // 로그인 처리 페이지
//                                        .successHandler(new CustomSuccessHandler()).permitAll() // 로그인 성공시 이동 페이지
//                                        .failureUrl("/admin/admin-sign-in").permitAll()
//                )
                .authorizeHttpRequests( // 인가 설정
                        (authorizeHttpRequests) ->
                                authorizeHttpRequests
                                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll() // forward 허용
                                        .requestMatchers(
                                                Stream // permitList에 있는 경로는 모두 허용
                                                        .of(PERMIT_LIST_USER)
                                                        .map(AntPathRequestMatcher::antMatcher)
                                                        .toArray(AntPathRequestMatcher[]::new)
                                        ).permitAll()
                                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN", "DRIVER", "SUPER_ADMIN")
                                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "DRIVER", "SUPER_ADMIN")
                                        .anyRequest().permitAll()
                )
                .logout(
                        (logout) ->
                                logout
                                        .logoutUrl("/sign-out")
                )
                .userDetailsService(customUserDetailsServiceForUser)
                .rememberMe(
                        (remember) -> remember
                                .userDetailsService(customUserDetailsServiceForUser)
                                .alwaysRemember(true)
                );
        return security.build();
    }

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity security) throws Exception{
        log.info("admin-securityFilterChain");
        security
                .csrf(
                        (csrf) ->
                                //csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // csrf 토큰을 쿠키에 저장
                                csrf.disable()
                )
                .formLogin( // 로그인 설정
                        (formLogin) ->
                                formLogin
                                        .loginPage("/admin-sign-in").permitAll() // 로그인 페이지
                                        .loginProcessingUrl("/admin-sign-in").permitAll() // 로그인 처리 페이지
                                        .successHandler(new CustomSuccessHandler()).permitAll() // 로그인 성공시 이동 페이지
                                        .failureUrl("/admin/admin-sign-in").permitAll()
                        // 로그인 실패시 이동 페이지
                )
//                .formLogin(
//                        (formLogin) ->
//                                formLogin
//                                        .loginPage("/admin/admin-sign-in").permitAll() // 로그인 페이지
//                                        .loginProcessingUrl("/admin/admin-sign-in").permitAll() // 로그인 처리 페이지
//                                        .successHandler(new CustomSuccessHandler()).permitAll() // 로그인 성공시 이동 페이지
//                                        .failureUrl("/admin/admin-sign-in").permitAll()
//                )
                .authorizeHttpRequests( // 인가 설정
                        (authorizeHttpRequests) ->
                                authorizeHttpRequests// forward 허용
                                        .requestMatchers(
                                                Stream // permitList에 있는 경로는 모두 허용
                                                        .of(PERMIT_LIST_USER)
                                                        .map(AntPathRequestMatcher::antMatcher)
                                                        .toArray(AntPathRequestMatcher[]::new)
                                        ).permitAll()
                                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "DRIVER", "SUPER_ADMIN")
                )
                .logout(
                        (logout) ->
                                logout
                                        .logoutUrl("/sign-out")
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
     * 비밀번호 암호화
     * @return BCryptPasswordEncoder
     */
}
