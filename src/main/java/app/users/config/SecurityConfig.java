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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import app.users.auth.CustomUserDetailsService;

@Configuration
@EnableWebSecurity(debug=false)
public class SecurityConfig {
    
    // 3. provider 등록
    @Autowired
    CustomUserDetailsService customUserDetailsService;

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

    // 4. custom login, logout
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 4-1
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests((authz) -> authz
                .requestMatchers(new AntPathRequestMatcher("/sub")).hasRole("ADMIN")
                .requestMatchers(
                    new AntPathRequestMatcher("/"),
                    new AntPathRequestMatcher("/main"),
                    new AntPathRequestMatcher("/css/**"),
                    new AntPathRequestMatcher("/js/**"),
                    new AntPathRequestMatcher("/images/**")
                ).permitAll()
                .anyRequest().authenticated()
            );

        // 4-2
        // 로그인폼
        http.formLogin((form) -> form
            .loginPage("/login")
            .permitAll()
            .defaultSuccessUrl("/")
            .failureUrl("/login")
            .usernameParameter("userid")
            .passwordParameter("password")
            .failureHandler(customAuthenticationFailureHandler) // 5
        );

        // 로그아웃
        http.logout((logout) -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
        );

        return http.build();
    }

    // 5. 사용자화 메시지
    @Autowired
    AuthenticationFailureHandler customAuthenticationFailureHandler;
}