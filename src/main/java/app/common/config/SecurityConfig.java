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
                // 공개 리소스 (정적 파일 및 로그인 관련) - 먼저 설정
                .requestMatchers(
                    new AntPathRequestMatcher("/"),
                    new AntPathRequestMatcher("/main"),
                    new AntPathRequestMatcher("/css/**"),
                    new AntPathRequestMatcher("/js/**"),
                    new AntPathRequestMatcher("/images/**"),
                    new AntPathRequestMatcher("/static/**"),
                    new AntPathRequestMatcher("/assets/**")
                ).permitAll()

                // 인증 없이 접근 가능한 로그인/회원가입 관련 페이지
                .requestMatchers(
                    new AntPathRequestMatcher("/login"),             // 로그인 페이지
                    new AntPathRequestMatcher("/verification"),      // 통합 인증 페이지 (회원가입/아이디찾기/비밀번호재설정 첫 단계)
                    new AntPathRequestMatcher("/find-id"),           // 아이디 찾기 페이지 (verification으로 리다이렉트)
                    new AntPathRequestMatcher("/reset-password")     // 비밀번호 재설정 페이지 (verification으로 리다이렉트)
                ).permitAll()

                // 공개 API (인증 불필요)
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
                    new AntPathRequestMatcher("/api/auth-complete"),   // 인증 완료 처리 API
                    new AntPathRequestMatcher("/api/reset-password")   // 비밀번호 재설정 API
                ).permitAll()

                // 관리자 전용 페이지와 API - 가장 구체적인 규칙을 먼저 설정
                .requestMatchers(
                    new AntPathRequestMatcher("/admin/**"),            // 모든 admin 하위 경로
                    new AntPathRequestMatcher("/api/admin/**")         // 관리자 API
                ).hasRole("ADMIN")

                // 인증 완료 후 접근 가능한 페이지들 (세션 기반 인증 확인 필요)
                .requestMatchers(
                    new AntPathRequestMatcher("/register/info"),     // 회원가입 정보 입력 페이지 (인증 완료 후)
                    new AntPathRequestMatcher("/find-id/result"),    // 아이디 찾기 결과 페이지 (인증 완료 후)
                    new AntPathRequestMatcher("/reset-password/form") // 비밀번호 재설정 폼 페이지 (인증 완료 후)
                ).authenticated()

                // 사용자 전용 페이지 (로그인 필요, 일반 사용자도 접근 가능)
                .requestMatchers(
                    new AntPathRequestMatcher("/my/**"),             // 마이페이지
                    new AntPathRequestMatcher("/profile/**"),        // 프로필 관리
                    new AntPathRequestMatcher("/reservation/**"),    // 예약 관리
                    new AntPathRequestMatcher("/resource/**"),       // 리소스 목록 및 관리
                    new AntPathRequestMatcher("/dashboard")          // 사용자 대시보드
                ).hasAnyRole("COMMON", "ADMIN")

                // 사용자 전용 API (로그인 필요)
                .requestMatchers(
                    new AntPathRequestMatcher("/api/user/**"),         // 사용자 API
                    new AntPathRequestMatcher("/api/profile"),         // 프로필 API
                    new AntPathRequestMatcher("/api/auth/check"),      // 인증 확인 API
                    new AntPathRequestMatcher("/api/reservation/**"),  // 예약 API
                    new AntPathRequestMatcher("/api/my/**")            // 마이페이지 API
                ).hasAnyRole("COMMON", "ADMIN")

                // 그 외 모든 요청은 인증 필요
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/perform_login")  // 폼 로그인 처리 URL 추가
                .defaultSuccessUrl("/", true)
                .failureHandler(customAuthenticationFailureHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout=true")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(exceptions -> exceptions
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    // 권한 부족 시 403 페이지 또는 로그인 페이지로 리다이렉트
                    String requestedUrl = request.getRequestURI();
                    
                    // 현재 요청 URL을 세션에 저장 (로그인 후 원래 페이지로 돌아가기 위함)
                    request.getSession().setAttribute("returnUrl", requestedUrl);
                    
                    // Ajax 요청인 경우 JSON 응답
                    if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                        response.setStatus(403);
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().write("{\"error\":\"접근 권한이 없습니다.\"}");
                    } else {
                        // 일반 요청인 경우 로그인 페이지로 리다이렉트
                        response.sendRedirect("/login?error=access_denied&returnUrl=" + requestedUrl);
                    }
                })
                .authenticationEntryPoint((request, response, authException) -> {
                    // 인증되지 않은 사용자의 접근 시
                    String requestedUrl = request.getRequestURI();
                    request.getSession().setAttribute("returnUrl", requestedUrl);
                    
                    if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                        response.setStatus(401);
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().write("{\"error\":\"로그인이 필요합니다.\"}");
                    } else {
                        response.sendRedirect("/login?returnUrl=" + requestedUrl);
                    }
                })
            );

        return http.build();
    }
}