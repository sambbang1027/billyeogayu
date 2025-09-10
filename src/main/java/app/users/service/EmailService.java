package app.users.service;

public interface EmailService {

    /**
     * 이메일 인증번호 발송
     */
    void sendVerificationCode(String email);

    /**
     * 이메일 인증번호 확인
     */
    boolean verifyCode(String email, String inputCode);

    /**
     * 이메일 인증번호 재발송
     */
    void resendVerificationCode(String email);

    /**
     * 인증 남은 시간 조회 (초 단위)
     */
    long getRemainingTime(String email);
}