// 작성자 : 황요한
package app.domains.users.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.domains.users.service.EmailService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/email")
@Slf4j
public class EmailController {

    @Autowired
    private EmailService emailService;

    /**
     * 이메일 인증번호 발송
     */
    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendVerificationCode(@RequestParam("email") String email) {
        Map<String, Object> response = new HashMap<>();

        try {
            log.info("이메일 인증번호 발송 요청 - 이메일: {}", email);

            emailService.sendVerificationCode(email);

            response.put("success", true);
            response.put("message", "이메일 인증번호가 발송되었습니다.");
            response.put("email", email);
            response.put("timeout", 180); // 3분

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("이메일 인증번호 발송 실패 - 입력 오류: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            log.error("이메일 인증번호 발송 실패 - 시스템 오류: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "이메일 인증번호 발송에 실패했습니다. 잠시 후 다시 시도해주세요.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 이메일 인증번호 확인
     */
    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyCode(@RequestParam("email") String email,
                                                        @RequestParam("code") String code) {
        Map<String, Object> response = new HashMap<>();

        try {
            log.info("이메일 인증번호 확인 요청 - 이메일: {}, 코드: {}", email, code);

            boolean isValid = emailService.verifyCode(email, code);

            response.put("success", isValid);
            response.put("email", email);
            response.put("verified", isValid);

            if (isValid) {
                response.put("message", "이메일 인증이 완료되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "인증번호가 일치하지 않거나 만료되었습니다.");
                return ResponseEntity.badRequest().body(response);
            }

        } catch (IllegalArgumentException e) {
            log.warn("이메일 인증번호 확인 실패 - 입력 오류: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            log.error("이메일 인증번호 확인 실패 - 시스템 오류: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "이메일 인증번호 확인에 실패했습니다. 잠시 후 다시 시도해주세요.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 이메일 인증번호 재발송
     */
    @PostMapping("/resend")
    public ResponseEntity<Map<String, Object>> resendVerificationCode(@RequestParam("email") String email) {
        Map<String, Object> response = new HashMap<>();

        try {
            log.info("이메일 인증번호 재발송 요청 - 이메일: {}", email);

            emailService.resendVerificationCode(email);

            response.put("success", true);
            response.put("message", "인증번호가 재발송되었습니다.");
            response.put("email", email);
            response.put("timeout", 180); // 3분

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("이메일 인증번호 재발송 실패 - 입력 오류: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            log.error("이메일 인증번호 재발송 실패 - 시스템 오류: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "인증번호 재발송에 실패했습니다. 잠시 후 다시 시도해주세요.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 인증 남은 시간 조회
     */
    @GetMapping("/remaining-time")
    public ResponseEntity<Map<String, Object>> getRemainingTime(@RequestParam("email") String email) {
        Map<String, Object> response = new HashMap<>();

        try {
            long remainingTime = emailService.getRemainingTime(email);

            response.put("email", email);
            response.put("remainingTime", remainingTime); // 초 단위
            response.put("expired", remainingTime <= 0);

            if (remainingTime > 0) {
                response.put("success", true);
                response.put("message", "인증 유효시간 조회 성공");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "인증번호가 만료되었습니다.");
                return ResponseEntity.ok(response);
            }

        } catch (Exception e) {
            log.error("인증 시간 조회 실패: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "인증 시간 조회에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}