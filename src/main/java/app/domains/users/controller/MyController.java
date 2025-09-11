package app.domains.users.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import app.domains.users.model.Users;
import app.domains.users.model.MyReservation;
import app.domains.users.model.MyUsageHistory;
import app.domains.users.service.MyService;
import app.domains.users.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * 내 정보 관련 Controller
 */
@Controller
@RequestMapping("/my")
@Slf4j
public class MyController {
    
    @Autowired
    private MyService myService;
    
    @Autowired
    private UsersService usersService;
    
    /**
     * 현재 로그인한 사용자 정보 조회
     */
    private Users getCurrentUser(HttpServletRequest request) {
        try {
            // 세션에서 SecurityContext 복원
            HttpSession session = request.getSession(false);
            if (session != null) {
                Object storedContext = session.getAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
                if (storedContext instanceof SecurityContext) {
                    SecurityContextHolder.setContext((SecurityContext) storedContext);
                }
            }
            
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
                return null;
            }
            
            String loginId = auth.getName();
            return usersService.getUserByLoginId(loginId);
            
        } catch (Exception e) {
            log.error("사용자 정보 조회 중 오류 발생", e);
            return null;
        }
    }
    

    
    /**
     * 내 예약 내역 페이지
     */
    @GetMapping("/reservations")
    public String reservations(@RequestParam(value = "status", required = false) String status,
                              @RequestParam(value = "startDate", required = false) String startDate,
                              @RequestParam(value = "endDate", required = false) String endDate,
                              @RequestParam(value = "category", required = false) String category,
                              HttpServletRequest request, Model model) {
        Users currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        try {
            log.info("내 예약 내역 조회 - userId: {}, status: {}, startDate: {}, endDate: {}, category: {}", 
                    currentUser.getUserId(), status, startDate, endDate, category);
            
            // 날짜 파싱
            Date parsedStartDate = null;
            Date parsedEndDate = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            if (startDate != null && !startDate.trim().isEmpty()) {
                parsedStartDate = dateFormat.parse(startDate);
            }
            if (endDate != null && !endDate.trim().isEmpty()) {
                parsedEndDate = dateFormat.parse(endDate);
            }
            
            // 예약 목록 조회
            List<MyReservation> reservations = myService.getMyReservationsWithFilter(
                currentUser.getUserId(), status, parsedStartDate, parsedEndDate, category);
            
            // 예약 현황 요약
            Map<String, Object> reservationSummary = myService.getReservationSummary(currentUser.getUserId());
            
            model.addAttribute("user", currentUser);
            model.addAttribute("reservations", reservations);
            model.addAttribute("reservationSummary", reservationSummary);
            model.addAttribute("currentStatus", status);
            model.addAttribute("currentStartDate", startDate);
            model.addAttribute("currentEndDate", endDate);
            model.addAttribute("currentCategory", category);
            
            return "my/reservations";
            
        } catch (Exception e) {
            log.error("예약 내역 조회 중 오류 발생 - userId: {}", currentUser.getUserId(), e);
            model.addAttribute("error", "예약 내역을 불러오는 중 오류가 발생했습니다.");
            return "error/500";
        }
    }
    
    /**
     * 예약 상세 정보
     */
    @GetMapping("/reservations/{reservationId}")
    public String reservationDetail(@PathVariable Long reservationId,
                                   HttpServletRequest request, Model model) {
        Users currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        try {
            log.info("예약 상세 정보 조회 - reservationId: {}, userId: {}", reservationId, currentUser.getUserId());
            
            MyReservation reservation = myService.getMyReservationDetail(reservationId, currentUser.getUserId());
            
            if (reservation == null) {
                model.addAttribute("error", "예약 정보를 찾을 수 없습니다.");
                return "error/404";
            }
            
            model.addAttribute("user", currentUser);
            model.addAttribute("reservation", reservation);
            
            return "my/reservation-detail";
            
        } catch (Exception e) {
            log.error("예약 상세 정보 조회 중 오류 발생 - reservationId: {}, userId: {}", 
                     reservationId, currentUser.getUserId(), e);
            model.addAttribute("error", "예약 상세 정보를 불러오는 중 오류가 발생했습니다.");
            return "error/500";
        }
    }
    
    /**
     * 예약 취소 (AJAX)
     */
    @PostMapping("/reservations/{reservationId}/cancel")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> cancelReservation(@PathVariable Long reservationId,
                                                                HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        Users currentUser = getCurrentUser(request);
        if (currentUser == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(401).body(response);
        }
        
        try {
            log.info("예약 취소 요청 - reservationId: {}, userId: {}", reservationId, currentUser.getUserId());
            
            boolean success = myService.cancelMyReservation(reservationId, currentUser.getUserId());
            
            if (success) {
                response.put("success", true);
                response.put("message", "예약이 취소되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "예약을 취소할 수 없습니다. (이미 승인된 예약이거나 존재하지 않는 예약입니다)");
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            log.error("예약 취소 중 오류 발생 - reservationId: {}, userId: {}", 
                     reservationId, currentUser.getUserId(), e);
            response.put("success", false);
            response.put("message", "예약 취소 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 내 사용 내역 페이지
     */
    @GetMapping("/usage-history")
    public String usageHistory(@RequestParam(value = "startDate", required = false) String startDate,
                              @RequestParam(value = "endDate", required = false) String endDate,
                              @RequestParam(value = "category", required = false) String category,
                              @RequestParam(value = "usageStatus", required = false) String usageStatus,
                              HttpServletRequest request, Model model) {
        Users currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        try {
            log.info("내 사용 내역 조회 - userId: {}, startDate: {}, endDate: {}, category: {}, usageStatus: {}", 
                    currentUser.getUserId(), startDate, endDate, category, usageStatus);
            
            // 날짜 파싱
            Date parsedStartDate = null;
            Date parsedEndDate = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            if (startDate != null && !startDate.trim().isEmpty()) {
                parsedStartDate = dateFormat.parse(startDate);
            }
            if (endDate != null && !endDate.trim().isEmpty()) {
                parsedEndDate = dateFormat.parse(endDate);
            }
            
            // 사용 내역 조회
            List<MyUsageHistory> usageHistory = myService.getMyUsageHistoryWithFilter(
                currentUser.getUserId(), parsedStartDate, parsedEndDate, category, usageStatus);
            
            // 사용 통계
            Map<String, Object> usageStatistics = myService.getMyUsageStatistics(currentUser.getUserId());
            
            model.addAttribute("user", currentUser);
            model.addAttribute("usageHistory", usageHistory);
            model.addAttribute("usageStatistics", usageStatistics);
            model.addAttribute("currentStartDate", startDate);
            model.addAttribute("currentEndDate", endDate);
            model.addAttribute("currentCategory", category);
            model.addAttribute("currentUsageStatus", usageStatus);
            
            return "my/usage-history";
            
        } catch (Exception e) {
            log.error("사용 내역 조회 중 오류 발생 - userId: {}", currentUser.getUserId(), e);
            model.addAttribute("error", "사용 내역을 불러오는 중 오류가 발생했습니다.");
            return "error/500";
        }
    }
    
    /**
     * 내 정보 API (AJAX용)
     */
    @GetMapping("/api/summary")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getApiSummary(HttpServletRequest request) {
        Users currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            Map<String, Object> summary = myService.getDashboardSummary(currentUser.getUserId());
            return ResponseEntity.ok(summary);
            
        } catch (Exception e) {
            log.error("API 요약 정보 조회 중 오류 발생 - userId: {}", currentUser.getUserId(), e);
            return ResponseEntity.status(500).build();
        }
    }
}