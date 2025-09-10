package app.domains.reservation.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import app.domains.asset.model.Asset;
import app.domains.reservation.model.BlockedRange;
import app.domains.reservation.service.ReservationService;
import app.users.model.Users;
import app.users.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ReservationController {

    private final ReservationService service;
    private final UsersService usersService;

    /**
     * 예약 신청 페이지 진입 - 모든 케이스 통합 처리
     */
    @GetMapping("/reservation/apply")
    public String showApply(@RequestParam(value="assetId", required=false) Long assetId,
                            @RequestParam(value="resourceId", required=false) Long resourceId,
                            @RequestParam(value="success", required=false) String success,
                            HttpServletRequest request,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        
        try {
            // 성공 메시지가 있는 경우 resource/list로 리다이렉트하면서 메시지 전달
            if ("true".equals(success)) {
                redirectAttributes.addFlashAttribute("successMessage", 
                    "예약 신청이 정상적으로 등록되었습니다. 승인 결과는 신청내역에서 확인해주세요.(1-2일이 소요될 수 있습니다.)");
                return "redirect:/asset/list";
            }
            
            // resourceId가 있으면 assetId로 사용 (하위 호환성)
            if (resourceId != null) {
                assetId = resourceId;
            }
            
            // 1) 세션에서 SecurityContext 수동 복원
            HttpSession session = request.getSession(false);
            if (session != null) {
                Object storedContext = session.getAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
                if (storedContext instanceof SecurityContext) {
                    SecurityContextHolder.setContext((SecurityContext) storedContext);
                    log.info("예약 페이지 - 세션에서 SecurityContext 복원 완료 - 세션ID: {}", session.getId());
                }
            }
            
            // 2) 로그인 확인
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
                log.warn("비로그인 사용자의 예약 신청 시도");
                return "redirect:/login";
            }
            
            // assetId가 없으면 자원 목록으로 리다이렉트
            if (assetId == null) {
                log.warn("자산 ID가 없음 - 자원 목록으로 리다이렉트");
                return "redirect:/asset/list";
            }
            
            // 3) 로그인된 사용자 정보 조회
            String loginId = auth.getName();
            Users loginUser = usersService.getUserByLoginId(loginId);
            
            if (loginUser == null) {
                log.error("로그인된 사용자 정보를 찾을 수 없음: {}", loginId);
                model.addAttribute("error", "사용자 정보를 찾을 수 없습니다. 다시 로그인해주세요.");
                return "redirect:/login";
            }
            
            log.info("예약 신청 페이지 접근 - 사용자: {}, 자산ID: {}", loginUser.getName(), assetId);
            
            Asset asset = service.getAsset(assetId);
            
            if (asset == null || asset.getIsDeleted() == 1) {
                model.addAttribute("error", "선택한 자원을 찾을 수 없습니다.");
                return "error/404";
            }

            // 신청자 정보 설정 (세션에서 가져온 사용자 정보 사용)
            String name = loginUser.getName();
            String birth = loginUser.getBirth() != null ? 
                          new SimpleDateFormat("yy/MM/dd").format(loginUser.getBirth()) : "";
            String phone = loginUser.getPhoneNumber() != null ? loginUser.getPhoneNumber() : "";

            // 자산 정보
            String assetModel = (asset.getCategory() != null && !asset.getCategory().trim().isEmpty()) 
                               ? asset.getCategory() : "정보 없음";

            model.addAttribute("assetId", asset.getAssetId());
            model.addAttribute("assetName", asset.getModelName());
            model.addAttribute("assetModel", assetModel);
            model.addAttribute("assetMaker", asset.getCompany());
            model.addAttribute("assetImage", asset.getImagePath());

            // 신청인 정보
            Map<String, Object> applicant = new HashMap<>();
            applicant.put("name", name);
            applicant.put("birth", birth);
            applicant.put("zipcode", "");
            applicant.put("addr1", "");
            applicant.put("addr2", "");
            model.addAttribute("applicant", applicant);

            String[] phoneArray = splitPhone(phone);
            model.addAttribute("phone1", phoneArray[0]);
            model.addAttribute("phone2", phoneArray[1]);
            model.addAttribute("phone3", phoneArray[2]);
            
            return "reservation";
            
        } catch (Exception e) {
            log.error("예약 신청 페이지 로딩 중 오류 발생", e);
            model.addAttribute("error", "페이지 로딩 중 오류가 발생했습니다: " + e.getMessage());
            return "error/500";
        }
    }

    /**
     * 예약된 날짜 조회 API
     */
    @GetMapping("/api/reservation/blocked")
    @ResponseBody
    public List<BlockedRange> apiBlocked(@RequestParam("assetId") Long assetId,
                                         @RequestParam("from") String from,
                                         @RequestParam("to") String to) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date fromDate = formatter.parse(from);
            Date toDate = formatter.parse(to);
            
            return service.getBlockedRanges(assetId, fromDate, toDate);
        } catch (ParseException e) {
            log.error("날짜 파싱 오류", e);
            return new ArrayList<>();
        }
    }

    /**
     * 예약 신청 제출
     */
    @PostMapping("/reservation/apply")
    public String submit(@RequestParam("assetId") Long assetId,
                         @RequestParam("reserveStartDate") String reserveStartDate,
                         @RequestParam("reserveEndDate") String reserveEndDate,
                         @RequestParam("reserveStartTime") String reserveStartTime,
                         @RequestParam("reserveEndTime") String reserveEndTime,
                         @RequestParam("purpose") String purpose,
                         @RequestParam("zipcode") String zipcode,
                         @RequestParam("addr1") String addr1,
                         @RequestParam(value="addr2", required=false) String addr2,
                         HttpServletRequest request,
                         Model model,
                         RedirectAttributes redirectAttributes) throws ParseException {

        try {
            // 1) 세션에서 SecurityContext 수동 복원
            HttpSession session = request.getSession(false);
            if (session != null) {
                Object storedContext = session.getAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
                if (storedContext instanceof SecurityContext) {
                    SecurityContextHolder.setContext((SecurityContext) storedContext);
                    log.info("예약 제출 - 세션에서 SecurityContext 복원 완료 - 세션ID: {}", session.getId());
                }
            }
            
            // 2) 로그인 확인
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
                log.warn("비로그인 사용자의 예약 신청 제출 시도");
                return "redirect:/login";
            }
            
            // 3) 로그인된 사용자 정보 조회
            String loginId = auth.getName();
            Users loginUser = usersService.getUserByLoginId(loginId);
            
            if (loginUser == null) {
                log.error("로그인된 사용자 정보를 찾을 수 없음: {}", loginId);
                return "redirect:/login";
            }
            
            Long userId = loginUser.getUserId();
            log.info("예약 신청 제출 - 사용자: {} (ID: {}), 자산ID: {}", loginUser.getName(), userId, assetId);

            // 날짜/시간 파싱
            Date startAt = parseDateTime(reserveStartDate, reserveStartTime);
            Date endAt = parseDateTime(reserveEndDate, reserveEndTime);

            boolean success = service.apply(assetId, userId, startAt, endAt, purpose, zipcode, addr1, 
                    (addr2 == null ? "" : addr2));

            if (success) {
                log.info("예약 신청 성공 - 사용자: {}, 자산ID: {}", loginUser.getName(), assetId);
                // 성공 시 success 파라미터와 함께 리다이렉트
                return "redirect:/reservation/apply?success=true";
            } else {
                log.warn("예약 신청 실패 - 사용자: {}, 자산ID: {}", loginUser.getName(), assetId);
                // 실패 시 기존 로직 유지
                restoreFormData(assetId, zipcode, addr1, addr2, purpose, model, loginUser);
                model.addAttribute("error", "해당 날짜는 이미 신청되었거나 입력값이 올바르지 않습니다. 다른 날짜를 선택해주세요.");
                return "reservation";
            }
            
        } catch (Exception e) {
            log.error("예약 신청 제출 중 오류 발생", e);
            model.addAttribute("error", "예약 신청 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
            return "reservation";
        }
    }

    /**
     * 실패 시 폼 데이터 복원
     */
    private void restoreFormData(Long assetId, String zipcode, String addr1, String addr2, 
                                String purpose, Model model, Users loginUser) {
        // 자산 정보 재설정
        Asset asset = service.getAsset(assetId);
        if (asset != null) {
            String assetModel = (asset.getCategory() != null && !asset.getCategory().trim().isEmpty())
                               ? asset.getCategory() : "정보 없음";
            
            model.addAttribute("assetId", asset.getAssetId());
            model.addAttribute("assetName", asset.getModelName());
            model.addAttribute("assetModel", assetModel);
            model.addAttribute("assetMaker", asset.getCompany());
            model.addAttribute("assetImage", asset.getImagePath());
        }

        // 신청인 정보 복원 (세션에서 가져온 사용자 정보 사용)
        Map<String, Object> applicant = new HashMap<>();
        applicant.put("name", loginUser.getName());
        applicant.put("birth", loginUser.getBirth() != null ? 
                      new SimpleDateFormat("yy/MM/dd").format(loginUser.getBirth()) : "");
        applicant.put("zipcode", zipcode);
        applicant.put("addr1", addr1);
        applicant.put("addr2", (addr2 == null ? "" : addr2));
        model.addAttribute("applicant", applicant);

        String phone = loginUser.getPhoneNumber() != null ? loginUser.getPhoneNumber() : "";
        String[] phoneArray = splitPhone(phone);
        model.addAttribute("phone1", phoneArray[0]);
        model.addAttribute("phone2", phoneArray[1]);
        model.addAttribute("phone3", phoneArray[2]);
        
        model.addAttribute("purpose", purpose);
    }

    // ===== 유틸리티 메서드 =====
    private static String[] splitPhone(String phone) {
        String[] result = {"", "", ""};
        if (phone == null || phone.isBlank()) return result;
        
        String digits = phone.replaceAll("[^0-9]", "");
        if (digits.length() >= 10) {
            result[0] = digits.substring(0, 3);
            result[1] = digits.substring(3, digits.length() - 4);
            result[2] = digits.substring(digits.length() - 4);
        }
        return result;
    }
    
    private static Date parseDateTime(String date, String time) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        formatter.setLenient(false);
        return formatter.parse(date + " " + time);
    }
}