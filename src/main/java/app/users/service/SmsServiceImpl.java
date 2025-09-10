package app.users.service;


import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.users.util.RedisUtil;
import app.users.util.SmsUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsUtil smsUtil;

    @Autowired
    private RedisUtil redisUtil;

    private static final String VERIFICATION_PREFIX = "SMS_VERIFY:";
    private static final long VERIFICATION_TIMEOUT = 180; // 3분 (180초)

    @Override
    public void sendVerificationCode(String phoneNumber) {
        try {
            // 전화번호 형식 검증
            validatePhoneNumber(phoneNumber);

            // 6자리 랜덤 인증번호 생성
            String verificationCode = generateVerificationCode();

            // Redis에 인증번호 저장 (3분 유효)
            String redisKey = VERIFICATION_PREFIX + phoneNumber;
            redisUtil.setData(redisKey, verificationCode, VERIFICATION_TIMEOUT);

            // SMS 발송
            smsUtil.sendVerificationCode(phoneNumber, verificationCode);

            log.info("인증번호 발송 완료 - 전화번호: {}", phoneNumber);

        } catch (Exception e) {
            log.error("인증번호 발송 실패 - 전화번호: {}, 오류: {}", phoneNumber, e.getMessage());
            throw new RuntimeException("인증번호 발송에 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public boolean verifyCode(String phoneNumber, String inputCode) {
        try {
            // 전화번호 형식 검증
            validatePhoneNumber(phoneNumber);

            // 입력 코드 검증
            if (inputCode == null || inputCode.trim().isEmpty()) {
                log.warn("인증번호 미입력 - 전화번호: {}", phoneNumber);
                return false;
            }

            // Redis에서 저장된 인증번호 조회
            String redisKey = VERIFICATION_PREFIX + phoneNumber;
            String storedCode = redisUtil.getData(redisKey);

            if (storedCode == null) {
                log.warn("인증번호 만료 또는 존재하지 않음 - 전화번호: {}", phoneNumber);
                return false;
            }

            // 인증번호 비교
            boolean isValid = storedCode.equals(inputCode.trim());

            if (isValid) {
                // 인증 성공 시 Redis에서 인증번호 삭제
                redisUtil.deleteData(redisKey);
                log.info("인증번호 확인 성공 - 전화번호: {}", phoneNumber);
            } else {
                log.warn("인증번호 불일치 - 전화번호: {}, 입력값: {}", phoneNumber, inputCode);
            }

            return isValid;

        } catch (Exception e) {
            log.error("인증번호 확인 실패 - 전화번호: {}, 오류: {}", phoneNumber, e.getMessage());
            return false;
        }
    }

    @Override
    public void resendVerificationCode(String phoneNumber) {
        try {
            // 기존 인증번호 삭제
            String redisKey = VERIFICATION_PREFIX + phoneNumber;
            redisUtil.deleteData(redisKey);

            // 새 인증번호 발송
            sendVerificationCode(phoneNumber);

            log.info("인증번호 재발송 완료 - 전화번호: {}", phoneNumber);

        } catch (Exception e) {
            log.error("인증번호 재발송 실패 - 전화번호: {}, 오류: {}", phoneNumber, e.getMessage());
            throw new RuntimeException("인증번호 재발송에 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public long getRemainingTime(String phoneNumber) {
        String redisKey = VERIFICATION_PREFIX + phoneNumber;
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
     * 전화번호 형식 검증
     */
    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("전화번호는 필수입니다.");
        }

        // 하이픈 제거
        phoneNumber = phoneNumber.replace("-", "").replace(" ", "");

        // 한국 휴대폰 번호 형식 검증 (010, 011, 016, 017, 018, 019)
        if (!phoneNumber.matches("^01[0-9]\\d{7,8}$")) {
            throw new IllegalArgumentException("올바른 휴대폰 번호 형식이 아닙니다. (01X-XXXX-XXXX)");
        }
    }
}