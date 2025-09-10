package app.users.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException e) throws IOException, ServletException {

        String message = getErrorMessage(e);
        
        log.info("인증 실패: {}, 메시지: {}", e.toString(), message);

        // API 요청인지 확인 (Content-Type이 JSON이거나 Accept 헤더에 JSON이 포함된 경우)
        String contentType = request.getContentType();
        String acceptHeader = request.getHeader("Accept");
        String requestURI = request.getRequestURI();
        
        boolean isApiRequest = (contentType != null && contentType.contains("application/json")) ||
                              (acceptHeader != null && acceptHeader.contains("application/json")) ||
                              (requestURI != null && requestURI.startsWith("/api/"));

        if (isApiRequest) {
            handleApiAuthenticationFailure(response, message, e);
        } else {
            handleFormAuthenticationFailure(request, response, e, message);
        }
    }

    /**
     * API 요청 실패 처리 (JSON 응답)
     */
    private void handleApiAuthenticationFailure(HttpServletResponse response, String message, 
                                              AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> data = new HashMap<>();
        data.put("success", false);
        data.put("message", message);
        data.put("errorCode", getErrorCode(exception));
        data.put("timestamp", System.currentTimeMillis());

        response.getWriter().write(objectMapper.writeValueAsString(data));
    }

    /**
     * 폼 로그인 실패 처리 (리다이렉트)
     */
    private void handleFormAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                               AuthenticationException exception, String message) 
                                               throws IOException, ServletException {
        try {
            String encodedMessage = URLEncoder.encode(message, "UTF-8");
            setDefaultFailureUrl("/login?error=true&exception=" + encodedMessage);
            super.onAuthenticationFailure(request, response, exception);
        } catch (Exception e) {
            log.error("폼 로그인 실패 처리 중 오류 발생", e);
            response.sendRedirect("/login?error=true");
        }
    }

    /**
     * 예외에 따른 에러 메시지 반환
     */
    private String getErrorMessage(AuthenticationException exception) {
        if (exception instanceof UsernameNotFoundException) {
            return "존재하지 않는 사용자입니다.";
        } else if (exception instanceof BadCredentialsException) {
            return "아이디 또는 비밀번호가 잘못되었습니다.";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            return "내부 시스템 문제가 발생했습니다.";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            return "인증 요청이 거부되었습니다.";
        } else {
            return "알 수 없는 이유로 로그인에 실패했습니다.";
        }
    }

    /**
     * 예외에 따른 에러 코드 반환
     */
    private String getErrorCode(AuthenticationException exception) {
        if (exception instanceof UsernameNotFoundException) {
            return "USER_NOT_FOUND";
        } else if (exception instanceof BadCredentialsException) {
            return "INVALID_CREDENTIALS";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            return "INTERNAL_ERROR";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            return "CREDENTIALS_NOT_FOUND";
        } else {
            return "UNKNOWN_ERROR";
        }
    }
}