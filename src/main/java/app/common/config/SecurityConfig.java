package app.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import app.domains.users.auth.CustomUserDetailsService;

@Configuration
@EnableWebSecurity(debug=false)
public class SecurityConfig {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    AuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setHideUserNotFoundExceptions(false);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionFixation().changeSessionId()
                .invalidSessionUrl("/login?expired=true")
                .maximumSessions(1) // 동시 세션 1개로 제한
                .maxSessionsPreventsLogin(false) // 새 로그인 시 기존 세션 만료
            )
            .authorizeHttpRequests((authz) -> authz
                // 관리자 전용 페이지 (ROLE_ 접두사 자동 추가됨)
                .requestMatchers(new AntPathRequestMatcher("/admin")).hasRole("ADMIN")

                // 공개 리소스 (정적 파일)
                .requestMatchers(
                    new AntPathRequestMatcher("/"),
                    new AntPathRequestMatcher("/main"),
                    new AntPathRequestMatcher("/css/**"),
                    new AntPathRequestMatcher("/js/**"),
                    new AntPathRequestMatcher("/images/**"),
                    new AntPathRequestMatcher("/static/**")
                ).permitAll()

                // API 엔드포인트 (세션 기반 인증)
                .requestMatchers(
                    new AntPathRequestMatcher("/api/login"),           // 로그인 API
                    new AntPathRequestMatcher("/api/register"),        // 회원가입 API
                    new AntPathRequestMatcher("/api/check-loginId"),   // ID 중복확인 API
                    new AntPathRequestMatcher("/api/check-email"),     // 이메일 중복확인 API

                    new AntPathRequestMatcher("/api/email/send"),      // 이메일 인증번호 발송
                    new AntPathRequestMatcher("/api/email/verify"),    // 이메일 인증번호 확인
                    new AntPathRequestMatcher("/api/email/resend"),    // 이메일 인증번호 재발송
                    new AntPathRequestMatcher("/api/email/remaining-time"), // 인증 남은 시간 조회

                    new AntPathRequestMatcher("/api/sms/send"),        // SMS 인증번호 발송
                    new AntPathRequestMatcher("/api/sms/verify"),      // SMS 인증번호 확인
                    new AntPathRequestMatcher("/api/sms/resend"),      // SMS 인증번호 재발송

                    // 새로 추가된 API들
                    new AntPathRequestMatcher("/api/auth-complete"),   // 인증 완료 처리
                    new AntPathRequestMatcher("/api/reset-password")   // 비밀번호 재설정
                ).permitAll()

                // 폼 로그인 페이지 및 인증 관련 페이지
                .requestMatchers(
                    new AntPathRequestMatcher("/login"),
                    new AntPathRequestMatcher("/verification"),        // 통합 인증 페이지
                    new AntPathRequestMatcher("/register/**"),         // 회원가입 관련 페이지들
                    new AntPathRequestMatcher("/find-id/**"),          // 아이디 찾기 관련 페이지들
                    new AntPathRequestMatcher("/reset-password/**")    // 비밀번호 재설정 관련 페이지들
                ).permitAll()

                // 나머지는 인증 필요
                .anyRequest().authenticated()
            );

        // 폼 로그인 설정 (웹 페이지용)
        http.formLogin((form) -> form
            .loginPage("/login")
            .permitAll()
            .defaultSuccessUrl("/")
            .failureUrl("/login?error=true")
            .usernameParameter("userid")
            .passwordParameter("password")
            .failureHandler(customAuthenticationFailureHandler)
        );

        // 로그아웃 설정
        http.logout((logout) -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
        );

        return http.build();
    }
}