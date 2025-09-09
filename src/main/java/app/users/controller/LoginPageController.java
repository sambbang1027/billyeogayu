package app.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import app.users.service.UsersService;
import app.users.model.Users;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class LoginPageController {

    @Autowired
    private UsersService usersService;

    /**
     * 로그인 페이지 표시
     */
    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "expired", required = false) String expired,
            @RequestParam(value = "userid", required = false) String userid,
            HttpServletRequest request,
            Model model) {
        
        log.info("=== 로그인 페이지 요청 ===");
        
        // 이미 로그인된 사용자는 메인 페이지로 리다이렉트
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            log.info("이미 로그인된 사용자 - 메인 페이지로 리다이렉트: {}", auth.getName());
            return "redirect:/";
        }
        
        // 에러 메시지 설정
        if (error != null) {
            model.addAttribute("error", "true");
            log.warn("로그인 오류 발생");
        }
        
        if (expired != null) {
            model.addAttribute("error", "expired");
            log.info("세션 만료로 인한 로그인 페이지 이동");
        }
        
        // 로그아웃 성공 메시지
        if (logout != null) {
            model.addAttribute("message", "성공적으로 로그아웃되었습니다.");
            log.info("로그아웃 완료");
        }
        
        // 이전에 입력했던 사용자 ID 유지
        if (userid != null) {
            model.addAttribute("userid", userid);
        }
        
        return "/login";
    }

    /**
     * 회원가입 페이지 (본인 확인 방법 선택)
     */
    @GetMapping("/register")
    public String registerPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "success", required = false) String success,
            Model model) {
        
        log.info("=== 회원가입 페이지 요청 ===");
        
        // 이미 로그인된 사용자는 메인 페이지로 리다이렉트
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            log.info("이미 로그인된 사용자 - 메인 페이지로 리다이렉트: {}", auth.getName());
            return "redirect:/";
        }
        
        // 에러 메시지 설정
        if (error != null) {
            model.addAttribute("error", "true");
            log.warn("회원가입 오류 발생");
        }
        
        // 성공 메시지 설정
        if (success != null) {
            model.addAttribute("message", "회원가입이 완료되었습니다. 로그인해주세요.");
            log.info("회원가입 완료");
        }
        
        return "/register";
    }

    /**
     * 회원가입 - 이메일 인증 페이지
     */
    @GetMapping("/register/email")
    public String emailAuthPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "step", required = false, defaultValue = "1") String step,
            Model model) {
        
        log.info("=== 회원가입 이메일 인증 페이지 요청 ===");
        log.info("인증 단계: {}", step);
        
        // 이미 로그인된 사용자는 메인 페이지로 리다이렉트
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return "redirect:/";
        }
        
        if (error != null) {
            model.addAttribute("error", "true");
        }
        
        model.addAttribute("step", step);
        model.addAttribute("authType", "email");
        model.addAttribute("purpose", "register");
        
        return "users/email-auth";
    }

    /**
     * 회원가입 - 휴대폰 인증 페이지
     */
    @GetMapping("/register/phone")
    public String phoneAuthPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "step", required = false, defaultValue = "1") String step,
            Model model) {
        
        log.info("=== 회원가입 휴대폰 인증 페이지 요청 ===");
        log.info("인증 단계: {}", step);
        
        // 이미 로그인된 사용자는 메인 페이지로 리다이렉트
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return "redirect:/";
        }
        
        if (error != null) {
            model.addAttribute("error", "true");
        }
        
        model.addAttribute("step", step);
        model.addAttribute("authType", "phone");
        model.addAttribute("purpose", "register");
        
        return "users/phone-auth";
    }

    /**
     * 회원가입 - 회원정보 입력 페이지 (인증 완료 후)
     */
    @GetMapping("/register/form")
    public String registerFormPage(
            @RequestParam(value = "authType", required = false) String authType,
            @RequestParam(value = "authKey", required = false) String authKey,
            @RequestParam(value = "error", required = false) String error,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        log.info("=== 회원정보 입력 페이지 요청 ===");
        log.info("인증 타입: {}, 인증 키: {}", authType, authKey);
        
        // 이미 로그인된 사용자는 메인 페이지로 리다이렉트
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return "redirect:/";
        }
        
        // 인증 정보가 없으면 회원가입 첫 페이지로 리다이렉트
        if (authType == null || authKey == null) {
            log.warn("인증 정보 없음 - 회원가입 페이지로 리다이렉트");
            redirectAttributes.addAttribute("error", "true");
            return "redirect:/register";
        }
        
        if (error != null) {
            model.addAttribute("error", "true");
        }
        
        model.addAttribute("authType", authType);
        model.addAttribute("authKey", authKey);
        
        return "users/register-form";
    }

    /**
     * 아이디 찾기 페이지 (본인 확인 방법 선택)
     */
    @GetMapping("/find-id")
    public String findIdPage(
            @RequestParam(value = "error", required = false) String error,
            Model model) {
        
        log.info("=== 아이디 찾기 페이지 요청 ===");
        
        if (error != null) {
            model.addAttribute("error", "true");
        }
        
        return "users/find-id";
    }

    /**
     * 아이디 찾기 - 이메일 인증 페이지
     */
    @GetMapping("/find-id/email")
    public String findIdEmailAuthPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "step", required = false, defaultValue = "1") String step,
            Model model) {
        
        log.info("=== 아이디 찾기 이메일 인증 페이지 요청 ===");
        
        if (error != null) {
            model.addAttribute("error", "true");
        }
        
        model.addAttribute("step", step);
        model.addAttribute("authType", "email");
        model.addAttribute("purpose", "find-id");
        
        return "users/email-auth";
    }

    /**
     * 아이디 찾기 - 휴대폰 인증 페이지
     */
    @GetMapping("/find-id/phone")
    public String findIdPhoneAuthPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "step", required = false, defaultValue = "1") String step,
            Model model) {
        
        log.info("=== 아이디 찾기 휴대폰 인증 페이지 요청 ===");
        
        if (error != null) {
            model.addAttribute("error", "true");
        }
        
        model.addAttribute("step", step);
        model.addAttribute("authType", "phone");
        model.addAttribute("purpose", "find-id");
        
        return "users/phone-auth";
    }

    /**
     * 아이디 찾기 - 결과 페이지 (인증 완료 후)
     */
    @GetMapping("/find-id/result")
    public String findIdResultPage(
            @RequestParam(value = "authType", required = false) String authType,
            @RequestParam(value = "authKey", required = false) String authKey,
            @RequestParam(value = "foundId", required = false) String foundId,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        log.info("=== 아이디 찾기 결과 페이지 요청 ===");
        log.info("인증 타입: {}, 찾은 아이디: {}", authType, foundId);
        
        // 인증 정보가 없으면 아이디 찾기 첫 페이지로 리다이렉트
        if (authType == null || authKey == null) {
            log.warn("인증 정보 없음 - 아이디 찾기 페이지로 리다이렉트");
            redirectAttributes.addAttribute("error", "true");
            return "redirect:/find-id";
        }
        
        model.addAttribute("authType", authType);
        model.addAttribute("authKey", authKey);
        model.addAttribute("foundId", foundId);
        
        return "users/find-id-result";
    }

    /**
     * 비밀번호 재설정 페이지 (본인 확인 방법 선택)
     */
    @GetMapping("/reset-password")
    public String resetPasswordPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "success", required = false) String success,
            Model model) {
        
        log.info("=== 비밀번호 재설정 페이지 요청 ===");
        
        if (error != null) {
            model.addAttribute("error", "true");
        }
        
        if (success != null) {
            model.addAttribute("message", "비밀번호가 재설정되었습니다. 새 비밀번호로 로그인해주세요.");
        }
        
        return "users/reset-password";
    }

    /**
     * 비밀번호 재설정 - 이메일 인증 페이지
     */
    @GetMapping("/reset-password/email")
    public String resetPasswordEmailAuthPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "step", required = false, defaultValue = "1") String step,
            Model model) {
        
        log.info("=== 비밀번호 재설정 이메일 인증 페이지 요청 ===");
        
        if (error != null) {
            model.addAttribute("error", "true");
        }
        
        model.addAttribute("step", step);
        model.addAttribute("authType", "email");
        model.addAttribute("purpose", "reset-password");
        
        return "users/email-auth";
    }

    /**
     * 비밀번호 재설정 - 휴대폰 인증 페이지
     */
    @GetMapping("/reset-password/phone")
    public String resetPasswordPhoneAuthPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "step", required = false, defaultValue = "1") String step,
            Model model) {
        
        log.info("=== 비밀번호 재설정 휴대폰 인증 페이지 요청 ===");
        
        if (error != null) {
            model.addAttribute("error", "true");
        }
        
        model.addAttribute("step", step);
        model.addAttribute("authType", "phone");
        model.addAttribute("purpose", "reset-password");
        
        return "users/phone-auth";
    }

    /**
     * 비밀번호 재설정 - 새 비밀번호 입력 페이지 (인증 완료 후)
     */
    @GetMapping("/reset-password/form")
    public String resetPasswordFormPage(
            @RequestParam(value = "authType", required = false) String authType,
            @RequestParam(value = "authKey", required = false) String authKey,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "error", required = false) String error,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        log.info("=== 비밀번호 재설정 폼 페이지 요청 ===");
        log.info("인증 타입: {}, 사용자 ID: {}", authType, userId);
        
        // 인증 정보가 없으면 비밀번호 재설정 첫 페이지로 리다이렉트
        if (authType == null || authKey == null || userId == null) {
            log.warn("인증 정보 없음 - 비밀번호 재설정 페이지로 리다이렉트");
            redirectAttributes.addAttribute("error", "true");
            return "redirect:/reset-password";
        }
        
        if (error != null) {
            model.addAttribute("error", "true");
        }
        
        model.addAttribute("authType", authType);
        model.addAttribute("authKey", authKey);
        model.addAttribute("userId", userId);
        
        return "users/reset-password-form";
    }

    /**
     * 로그아웃 처리 (페이지 기반)
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        log.info("=== 로그아웃 요청 ===");
        
        try {
            // 현재 인증 정보 가져오기
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                log.info("로그아웃 사용자: {}", auth.getName());
            }
            
            // 세션 무효화
            if (session != null) {
                log.info("세션 무효화: {}", session.getId());
                session.invalidate();
            }
            
            // SecurityContext 초기화
            SecurityContextHolder.clearContext();
            
            redirectAttributes.addAttribute("logout", "true");
            log.info("=== 로그아웃 완료 ===");
            
        } catch (Exception e) {
            log.error("로그아웃 처리 중 오류 발생", e);
        }
        
        return "redirect:/login";
    }

    /**
     * 메인 페이지 (로그인 후)
     */
    @GetMapping("/")
    public String home(Model model, HttpServletRequest request) {
        log.info("=== 메인 페이지 요청 ===");
        
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
                // 로그인된 사용자
                String loginId = auth.getName();
                Users user = usersService.getUserByLoginId(loginId);
                
                if (user != null) {
                    model.addAttribute("loginUser", user);
                    model.addAttribute("isLoggedIn", true);
                    log.info("로그인된 사용자 정보 설정: {} ({})", user.getName(), user.getLoginId());
                } else {
                    log.warn("인증된 사용자지만 사용자 정보를 찾을 수 없음: {}", loginId);
                    model.addAttribute("isLoggedIn", false);
                }
            } else {
                // 비로그인 사용자
                model.addAttribute("isLoggedIn", false);
                log.info("비로그인 사용자의 메인 페이지 접근");
            }
            
        } catch (Exception e) {
            log.error("메인 페이지 처리 중 오류 발생", e);
            model.addAttribute("isLoggedIn", false);
        }
        
        return "home";
    }
}