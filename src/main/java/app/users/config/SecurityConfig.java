package app.users.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import app.users.auth.CustomUserDetailsService;

@Configuration
@EnableWebSecurity(debug=false)
public class SecurityConfig {

    @Autowired
    CustomUserDetailsService customUserDetailsService;
    
    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setHideUserNotFoundExceptions(false);
        return authenticationProvider;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests((authz) -> authz
                // 관리자 전용 페이지
                .requestMatchers(new AntPathRequestMatcher("/sub")).hasRole("ADMIN")
                
                // 공개 리소스 (정적 파일)
                .requestMatchers(
                    new AntPathRequestMatcher("/"),
                    new AntPathRequestMatcher("/main"),
                    new AntPathRequestMatcher("/css/**"),
                    new AntPathRequestMatcher("/js/**"),
                    new AntPathRequestMatcher("/images/**")
                ).permitAll()
                
                // API 엔드포인트 (JWT 인증용)
                .requestMatchers(
                    new AntPathRequestMatcher("/api/login"),           // 로그인 API
                    new AntPathRequestMatcher("/api/register"),        // 회원가입 API
                    new AntPathRequestMatcher("/api/check-loginId"),   // ID 중복확인 API
                    new AntPathRequestMatcher("/api/check-email")      // 이메일 중복확인 API
                ).permitAll()
                
                // 폼 로그인 페이지
                .requestMatchers(
                    new AntPathRequestMatcher("/login"),
                    new AntPathRequestMatcher("/register")             // 회원가입 페이지
                ).permitAll()
                
                // 나머지는 인증 필요
                .anyRequest().authenticated()
            );

        // JWT 필터 추가 (UsernamePasswordAuthenticationFilter 앞에)
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 폼 로그인 설정
        http.formLogin((form) -> form
            .loginPage("/login")
            .permitAll()
            .defaultSuccessUrl("/")
            .failureUrl("/login")
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

    @Autowired
    AuthenticationFailureHandler customAuthenticationFailureHandler;
}