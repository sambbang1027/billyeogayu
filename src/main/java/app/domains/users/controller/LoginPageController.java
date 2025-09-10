package app.domains.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import app.domains.users.model.Users;
import app.domains.users.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

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
            @RequestParam(value = "returnUrl", required = false) String returnUrl,
            HttpServletRequest request,
            HttpSession session,
            Model model) {

        log.info("=== 로그인 페이지 요청 ===");

        // 이미 로그인된 사용자는 메인 페이지로 리다이렉트
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            log.info("이미 로그인된 사용자 - 메인 페이지로 리다이렉트: {}", auth.getName());

            // 돌아갈 URL이 있으면 해당 페이지로, 없으면 메인으로
            String redirectUrl = (String) session.getAttribute("returnUrl");
            if (redirectUrl != null && !redirectUrl.trim().isEmpty()) {
                session.removeAttribute("returnUrl"); // 사용 후 제거
                log.info("저장된 returnUrl로 리다이렉트: {}", redirectUrl);
                return "redirect:" + redirectUrl;
            }
            return "redirect:/";
        }

        // returnUrl이 파라미터로 전달된 경우 세션에 저장
        if (returnUrl != null && !returnUrl.trim().isEmpty()) {
            session.setAttribute("returnUrl", returnUrl);
            log.info("returnUrl 세션에 저장: {}", returnUrl);
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
     * 통합 인증 페이지 (회원가입/아이디찾기/비밀번호재설정)
     */
    @GetMapping("/verification")
    public String verificationPage(
            @RequestParam(value = "purpose", required = false, defaultValue = "register") String purpose,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "success", required = false) String success,
            Model model) {

        log.info("=== 인증 페이지 요청 - purpose: {} ===", purpose);

        // 이미 로그인된 사용자는 메인 페이지로 리다이렉트
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            log.info("이미 로그인된 사용자 - 메인 페이지로 리다이렉트: {}", auth.getName());
            return "redirect:/";
        }

        // 에러 메시지 설정
        if (error != null) {
            model.addAttribute("error", "true");
            log.warn("본인 확인 오류 발생");
        }

        // 성공 메시지 설정
        if (success != null) {
            model.addAttribute("message", "회원가입이 완료되었습니다. 로그인해주세요.");
            log.info("회원가입 완료");
        }

        // purpose에 따라 페이지 정보 설정
        model.addAttribute("purpose", purpose);

        switch (purpose) {
            case "find-id":
                model.addAttribute("pageTitle", "아이디 찾기");
                model.addAttribute("pageDescription", "본인 확인을 통해 아이디를 찾으실 수 있습니다.");
                model.addAttribute("activePage", "find-id");
                break;
            case "reset-password":
                model.addAttribute("pageTitle", "비밀번호 재설정");
                model.addAttribute("pageDescription", "본인 확인을 통해 비밀번호를 재설정하실 수 있습니다.");
                model.addAttribute("activePage", "reset-password");
                break;
            case "register":
            default:
                model.addAttribute("pageTitle", "회원가입");
                model.addAttribute("pageDescription", "본인 확인을 진행해주세요.");
                model.addAttribute("activePage", "register");
                break;
        }

        return "verification";
    }

    /**
     * 아이디 찾기 페이지 (인증 방법 선택) - verification으로 리다이렉트
     */
    @GetMapping("/find-id")
    public String findIdPage() {
        log.info("=== 아이디 찾기 페이지 요청 - verification으로 리다이렉트 ===");
        return "redirect:/verification?purpose=find-id";
    }

    /**
     * 비밀번호 재설정 페이지 (인증 방법 선택) - verification으로 리다이렉트
     */
    @GetMapping("/reset-password")
    public String resetPasswordPage() {
        log.info("=== 비밀번호 재설정 페이지 요청 - verification으로 리다이렉트 ===");
        return "redirect:/verification?purpose=reset-password";
    }



    /**
     * 회원가입 - 회원정보 입력 페이지 (인증 완료 후)
     */
    @GetMapping("/register/info")
    public String registerInfoPage(
            @RequestParam(value = "error", required = false) String error,
            Model model) {

        log.info("=== 회원정보 입력 페이지 요청 ===");

        // 이미 로그인된 사용자는 메인 페이지로 리다이렉트
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return "redirect:/";
        }

        if (error != null) {
            model.addAttribute("error", "true");
        }

        return "/register-info";
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

//    /**
//     * 메인 페이지 (로그인 후)
//     */
//    @GetMapping("/")
//    public String home(Model model, HttpServletRequest request, HttpSession session) {
//        log.info("=== 메인 페이지 요청 ===");
//
//        try {
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
//                // 로그인된 사용자
//                String loginId = auth.getName();
//                Users user = usersService.getUserByLoginId(loginId);
//
//                if (user != null) {
//                    model.addAttribute("loginUser", user);
//                    model.addAttribute("isLoggedIn", true);
//                    log.info("로그인된 사용자 정보 설정: {} ({})", user.getName(), user.getLoginId());
//
//                    // 저장된 returnUrl이 있으면 해당 페이지로 리다이렉트
//                    String returnUrl = (String) session.getAttribute("returnUrl");
//                    if (returnUrl != null && !returnUrl.trim().isEmpty()) {
//                        session.removeAttribute("returnUrl"); // 사용 후 제거
//                        log.info("저장된 returnUrl로 리다이렉트: {}", returnUrl);
//                        return "redirect:" + returnUrl;
//                    }
//                } else {
//                    log.warn("인증된 사용자지만 사용자 정보를 찾을 수 없음: {}", loginId);
//                    model.addAttribute("isLoggedIn", false);
//                }
//            } else {
//                // 비로그인 사용자
//                model.addAttribute("isLoggedIn", false);
//                log.info("비로그인 사용자의 메인 페이지 접근");
//            }
//
//        } catch (Exception e) {
//            log.error("메인 페이지 처리 중 오류 발생", e);
//            model.addAttribute("isLoggedIn", false);
//        }
//
//        return "home";
//    }
//


    /**
     * 아이디 찾기 결과 페이지
     */
    @GetMapping("/find-id/result")
    public String findIdResultPage(
            @RequestParam(value = "authType", required = false) String authType,
            @RequestParam(value = "authKey", required = false) String authKey,
            @RequestParam(value = "foundId", required = false) String foundId,
            @RequestParam(value = "error", required = false) String error,
            Model model,
            RedirectAttributes redirectAttributes) {

        log.info("=== 아이디 찾기 결과 페이지 요청 ===");
        log.info("인증 타입: {}, 찾은 아이디: {}", authType, foundId);

        // 인증 정보가 없으면 아이디 찾기 첫 페이지로 리다이렉트
        if (authType == null || authKey == null || foundId == null) {
            log.warn("인증 정보 없음 - 아이디 찾기 페이지로 리다이렉트");
            redirectAttributes.addAttribute("error", "true");
            return "redirect:/verification?purpose=find-id";
        }

        if (error != null) {
            model.addAttribute("error", "true");
        }

        model.addAttribute("authType", authType);
        model.addAttribute("authKey", authKey);
        model.addAttribute("foundId", foundId);

        return "/find-id-result";
    }

    /**
     * 비밀번호 재설정 폼 페이지
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
            return "redirect:/verification?purpose=reset-password";
        }

        if (error != null) {
            model.addAttribute("error", "true");
        }

        model.addAttribute("authType", authType);
        model.addAttribute("authKey", authKey);
        model.addAttribute("userId", userId);

        return "/reset-password-form";
    }
}