// 작성자 : 황요한
package app.domains.users.service;

public interface SmsService {

    /**
     * 인증번호 발송
     */
    void sendVerificationCode(String phoneNumber);

    /**
     * 인증번호 확인
     */
    boolean verifyCode(String phoneNumber, String inputCode);

    /**
     * 인증번호 재발송
     */
    void resendVerificationCode(String phoneNumber);

    /**
     * 남은 인증 시간 조회 (초 단위)
     */
    long getRemainingTime(String phoneNumber);
}