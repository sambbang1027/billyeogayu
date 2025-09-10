package app.users.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

@Component
@Slf4j
public class SmsUtil {

    @Value("${coolsms.apikey}")
    private String apiKey;

    @Value("${coolsms.apisecret}")
    private String apiSecretKey;

    @Value("${coolsms.fromnumber}")
    private String senderPhone;

    private DefaultMessageService messageService;

    @PostConstruct
    private void init() {
        log.info("=== CoolSMS 초기화 시작 ===");
        log.info("API Key: {}", apiKey != null ? apiKey.substring(0, Math.min(4, apiKey.length())) + "****" : "null");
        log.info("API Secret: {}", apiSecretKey != null ? "****" : "null");
        log.info("발신번호: {}", senderPhone);

        if (apiKey == null || apiKey.contains("${")) {
            log.error("API 키가 제대로 로드되지 않았습니다: {}", apiKey);
            return;
        }

        if (apiKey.length() != 16) {
            log.error("API 키 길이가 잘못되었습니다. 현재: {}자, 필요: 16자", apiKey.length());
            return;
        }

        try {
            this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
            log.info("CoolSMS 서비스 초기화 완료");
        } catch (Exception e) {
            log.error("CoolSMS 초기화 실패: {}", e.getMessage());
        }
    }

    /**
     * 인증번호 SMS 발송
     */
    public void sendVerificationCode(String to, String verificationCode) {
        try {
            Message message = new Message();
            // 발신번호 (application.properties에서 설정)
            message.setFrom(senderPhone);
            // 수신번호
            message.setTo(to);
            // 메시지 내용
            message.setText("[빌려가유] 회원가입 인증번호는 " + verificationCode + " 입니다. 3분 내에 입력해주세요.");

            SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

            log.info("SMS 발송 성공 - 수신번호: {}, 메시지ID: {}", to, response.getMessageId());

        } catch (Exception e) {
            log.error("SMS 발송 실패 - 수신번호: {}, 오류: {}", to, e.getMessage());
            throw new RuntimeException("SMS 발송에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * 일반 SMS 발송 (필요시 사용)
     */
    public void sendMessage(String to, String content) {
        try {
            Message message = new Message();
            message.setFrom(senderPhone);
            message.setTo(to);
            message.setText(content);

            SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

            log.info("SMS 발송 성공 - 수신번호: {}, 메시지ID: {}", to, response.getMessageId());

        } catch (Exception e) {
            log.error("SMS 발송 실패 - 수신번호: {}, 오류: {}", to, e.getMessage());
            throw new RuntimeException("SMS 발송에 실패했습니다: " + e.getMessage());
        }
    }
}