package app.users.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class EmailUtil {

    private final JavaMailSender mailSender;

    @Value("${email.from.address}")
    private String fromAddress;

    @Value("${email.from.name}")
    private String fromName;

    // 생성자 주입
    public EmailUtil(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @PostConstruct
    private void init() {
        log.info("=== 이메일 서비스 초기화 ===");
        log.info("발신자 이메일: {}", fromAddress);
        log.info("발신자 이름: {}", fromName);
    }

    /**
     * 인증번호 이메일 발송
     */
    public void sendVerificationCode(String toEmail, String verificationCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // 발신자 설정
            try {
                helper.setFrom(fromAddress, fromName);
            } catch (UnsupportedEncodingException e) {
                helper.setFrom(fromAddress);
                log.warn("발신자 이름 설정 실패, 이메일만 사용: {}", e.getMessage());
            }
            
            // 수신자 설정
            helper.setTo(toEmail);
            
            // 제목 설정
            helper.setSubject("[빌려가유] 이메일 인증번호");
            
            // 내용 설정 (HTML)
            String htmlContent = buildVerificationEmailContent(verificationCode);
            helper.setText(htmlContent, true);

            // 이메일 발송
            mailSender.send(message);
            
            log.info("이메일 발송 성공 - 수신자: {}", toEmail);
            
        } catch (MessagingException e) {
            log.error("이메일 발송 실패 (MessagingException) - 수신자: {}, 오류: {}", toEmail, e.getMessage());
            throw new RuntimeException("이메일 발송에 실패했습니다: " + e.getMessage());
        } catch (Exception e) {
            log.error("이메일 발송 실패 - 수신자: {}, 오류: {}", toEmail, e.getMessage());
            throw new RuntimeException("이메일 발송에 실패했습니다: " + e.getMessage());
        }
    }

//    /**
//     * 일반 이메일 발송 (필요시 사용)
//     */
//    public void sendEmail(String toEmail, String subject, String content) {
//        try {
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//            try {
//                helper.setFrom(fromAddress, fromName);
//            } catch (UnsupportedEncodingException e) {
//                helper.setFrom(fromAddress);
//                log.warn("발신자 이름 설정 실패, 이메일만 사용: {}", e.getMessage());
//            }
//            
//            helper.setTo(toEmail);
//            helper.setSubject(subject);
//            helper.setText(content, true);
//
//            mailSender.send(message);
//            
//            log.info("이메일 발송 성공 - 수신자: {}, 제목: {}", toEmail, subject);
//            
//        } catch (MessagingException e) {
//            log.error("이메일 발송 실패 (MessagingException) - 수신자: {}, 오류: {}", toEmail, e.getMessage());
//            throw new RuntimeException("이메일 발송에 실패했습니다: " + e.getMessage());
//        } catch (Exception e) {
//            log.error("이메일 발송 실패 - 수신자: {}, 오류: {}", toEmail, e.getMessage());
//            throw new RuntimeException("이메일 발송에 실패했습니다: " + e.getMessage());
//        }
//    }

    /**
     * 인증번호 이메일 HTML 템플릿
     */
    private String buildVerificationEmailContent(String verificationCode) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <title>이메일 인증</title>" +
                "</head>" +
                "<body style='font-family: Arial, sans-serif; background-color: #659F7C; padding: 20px;'>" +
                "    <div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; padding: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);'>" +
                "        <div style='text-align: center; margin-bottom: 30px;'>" +
                "            <h1 style='color: #659F7C; margin-bottom: 10px;'>빌려가유</h1>" +
                "            <h2 style='color: #333; margin-bottom: 20px;'>이메일 인증번호</h2>" +
                "        </div>" +
                "        <div style='background-color: #f8f9fa; padding: 20px; border-radius: 8px; text-align: center; margin-bottom: 20px;'>" +
                "            <p style='font-size: 16px; color: #666; margin-bottom: 15px;'>다음 인증번호를 입력해주세요:</p>" +
                "            <div style='font-size: 32px; font-weight: bold; color: #659F7C; letter-spacing: 5px; padding: 15px; background-color: #ffffff; border: 2px dashed #667eea; border-radius: 5px;'>" +
                verificationCode +
                "            </div>" +
                "        </div>" +
                "        <div style='text-align: center; color: #666; font-size: 14px;'>" +
                "            <p>이 인증번호는 <strong>3분 후</strong> 만료됩니다.</p>" +
                "            <p>본인이 요청하지 않았다면 이 메일을 무시해주세요.</p>" +
                "        </div>" +
                "        <hr style='border: none; border-top: 1px solid #eee; margin: 20px 0;'>" +
                "        <div style='text-align: center; color: #999; font-size: 12px;'>" +
                "            <p>&copy; 2025 빌려가유. All rights reserved.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }
}