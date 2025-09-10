package app.users.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.users.util.EmailUtil;
import app.users.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailUtil emailUtil; // 실제 EmailUtil 사용

    @Autowired
    private RedisUtil redisUtil;

    private static final String EMAIL_VERIFICATION_PREFIX = "EMAIL_VERIFY:";
    private static final long VERIFICATION_TIMEOUT = 180; // 3분 (180초)

    @Override
    public void sendVerificationCode(String email) {
        try {
            // 이메일 형식 검증
            validateEmail(email);

            // 6자리 랜덤 인증번호 생성
            String verificationCode = generateVerificationCode();

            // Redis에 인증번호 저장 (3분 유효)
            String redisKey = EMAIL_VERIFICATION_PREFIX + email;
            redisUtil.setData(redisKey, verificationCode, VERIFICATION_TIMEOUT);

            // 실제 이메일 발송
            emailUtil.sendVerificationCode(email, verificationCode);

            log.info("이메일 인증번호 발송 완료 - 이메일: {}", email);

        } catch (Exception e) {
            log.error("이메일 인증번호 발송 실패 - 이메일: {}, 오류: {}", email, e.getMessage());
            throw new RuntimeException("이메일 인증번호 발송에 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public boolean verifyCode(String email, String inputCode) {
        try {
            // 이메일 형식 검증
            validateEmail(email);

            // 입력 코드 검증
            if (inputCode == null || inputCode.trim().isEmpty()) {
                log.warn("인증번호 미입력 - 이메일: {}", email);
                return false;
            }

            // Redis에서 저장된 인증번호 조회
            String redisKey = EMAIL_VERIFICATION_PREFIX + email;
            String storedCode = redisUtil.getData(redisKey);

            if (storedCode == null) {
                log.warn("인증번호 만료 또는 존재하지 않음 - 이메일: {}", email);
                return false;
            }

            // 인증번호 비교
            boolean isValid = storedCode.equals(inputCode.trim());

            if (isValid) {
                // 인증 성공 시 Redis에서 인증번호 삭제
                redisUtil.deleteData(redisKey);
                log.info("이메일 인증번호 확인 성공 - 이메일: {}", email);
            } else {
                log.warn("인증번호 불일치 - 이메일: {}, 입력값: {}", email, inputCode);
            }

            return isValid;

        } catch (Exception e) {
            log.error("이메일 인증번호 확인 실패 - 이메일: {}, 오류: {}", email, e.getMessage());
            return false;
        }
    }

    @Override
    public void resendVerificationCode(String email) {
        try {
            // 기존 인증번호 삭제
            String redisKey = EMAIL_VERIFICATION_PREFIX + email;
            redisUtil.deleteData(redisKey);

            // 새 인증번호 발송
            sendVerificationCode(email);

            log.info("이메일 인증번호 재발송 완료 - 이메일: {}", email);

        } catch (Exception e) {
            log.error("이메일 인증번호 재발송 실패 - 이메일: {}, 오류: {}", email, e.getMessage());
            throw new RuntimeException("이메일 인증번호 재발송에 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public long getRemainingTime(String email) {
        String redisKey = EMAIL_VERIFICATION_PREFIX + email;
        return redisUtil.getExpire(redisKey);
    }

    /**
     * 6자리 랜덤 인증번호 생성
     */
    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000; // 100000 ~ 999999
        return String.valueOf(code);
    }

    /**
     * 이메일 형식 검증
     */
    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }

        // 이메일 형식 검증
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다.");
        }
    }
}