package app.users.auth;

import java.io.IOException;
import java.net.URLEncoder;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	@Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {

        String message;

        if (e instanceof UsernameNotFoundException){
            message = "USER_NOT_FOUND";
        }
        else if (e instanceof BadCredentialsException) {
            message = "WRONG_PASSWORD";
        }
        else if ( e instanceof InternalAuthenticationServiceException) {
            message = "내부 시스템 문제가 발생되었습니다.";
        }
        else if (e instanceof AuthenticationCredentialsNotFoundException) {
            message = "인증요청이 거부되었습니다.";
        }
        else {
            message = "알 수 없는 이유로 로그인이 안되고 있습니다.";
        }

        log.info(">>>>" + e.toString() + ", msg:" + message);

        message = URLEncoder.encode(message, "UTF-8"); //한글 인코딩 깨지는 문제 방지

        setDefaultFailureUrl("/login?error=true&exception=" + message); // security-context에 등록된 form 경로

        super.onAuthenticationFailure(request, response, e);
    }
}
