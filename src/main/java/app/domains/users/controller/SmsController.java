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

import app.domains.users.service.SmsService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/sms")
@Slf4j
public class SmsController {

    @Autowired
    private SmsService smsService;

    // 응답 DTO (기존 UsersController와 동일)
    public static class ApiResponse {
        public boolean success;
        public String message;
        public Object data;
        public long timestamp;

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
            this.timestamp = System.currentTimeMillis();
        }
    }

    /**
     * 인증번호 발송
     */
    @PostMapping("/send")
    public ResponseEntity<?> sendVerificationCode(@RequestParam("phoneNumber") String phoneNumber) {
        try {
            log.info("인증번호 발송 요청 - 전화번호: {}", phoneNumber);

            // 하이픈 포함 여부 검증
            if (phoneNumber.contains("-")) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "하이픈을 제거하고 번호를 입력해주세요.", null));
            }

            // 인증번호 발송
            smsService.sendVerificationCode(phoneNumber);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("phoneNumber", phoneNumber);
            responseData.put("timeout", 180); // 3분

            return ResponseEntity.ok(new ApiResponse(true, "인증번호가 발송되었습니다.", responseData));

        } catch (IllegalArgumentException e) {
            log.warn("인증번호 발송 실패 - 입력 오류: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage(), null));

        } catch (Exception e) {
            log.error("인증번호 발송 실패 - 시스템 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "인증번호 발송에 실패했습니다. 잠시 후 다시 시도해주세요.", null));
        }
    }

    /**
     * 인증번호 확인
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestParam("phoneNumber") String phoneNumber,
                                       @RequestParam("verificationCode") String verificationCode) {
        try {
            log.info("인증번호 확인 요청 - 전화번호: {}, 인증번호: {}", phoneNumber, verificationCode);

            boolean isValid = smsService.verifyCode(phoneNumber, verificationCode);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("phoneNumber", phoneNumber);
            responseData.put("verified", isValid);

            if (isValid) {
                return ResponseEntity.ok(new ApiResponse(true, "인증이 완료되었습니다.", responseData));
            } else {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "인증번호가 일치하지 않거나 만료되었습니다.", responseData));
            }

        } catch (IllegalArgumentException e) {
            log.warn("인증번호 확인 실패 - 입력 오류: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage(), null));

        } catch (Exception e) {
            log.error("인증번호 확인 실패 - 시스템 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "인증번호 확인에 실패했습니다. 잠시 후 다시 시도해주세요.", null));
        }
    }

    /**
     * 인증번호 재발송
     */
    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam("phoneNumber") String phoneNumber) {
        try {
            log.info("인증번호 재발송 요청 - 전화번호: {}", phoneNumber);

            smsService.resendVerificationCode(phoneNumber);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("phoneNumber", phoneNumber);
            responseData.put("timeout", 180); // 3분

            return ResponseEntity.ok(new ApiResponse(true, "인증번호가 재발송되었습니다.", responseData));

        } catch (IllegalArgumentException e) {
            log.warn("인증번호 재발송 실패 - 입력 오류: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage(), null));

        } catch (Exception e) {
            log.error("인증번호 재발송 실패 - 시스템 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "인증번호 재발송에 실패했습니다. 잠시 후 다시 시도해주세요.", null));
        }
    }

    /**
     * 인증 남은 시간 조회
     */
    @GetMapping("/remaining-time")
    public ResponseEntity<?> getRemainingTime(@RequestParam("phoneNumber") String phoneNumber) {
        try {
            long remainingTime = smsService.getRemainingTime(phoneNumber);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("phoneNumber", phoneNumber);
            responseData.put("remainingTime", remainingTime); // 초 단위
            responseData.put("expired", remainingTime <= 0);

            if (remainingTime > 0) {
                return ResponseEntity.ok(new ApiResponse(true, "인증 유효시간 조회 성공", responseData));
            } else {
                return ResponseEntity.ok(new ApiResponse(false, "인증번호가 만료되었습니다.", responseData));
            }

        } catch (Exception e) {
            log.error("인증 시간 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "인증 시간 조회에 실패했습니다.", null));
        }
    }
}