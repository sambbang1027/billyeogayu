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
            HttpSession session = request.getSession(false);
            if (session == null) {
                return null;
            }
            
            SecurityContext securityContext = (SecurityContext) session
                    .getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
            
            if (securityContext == null) {
                securityContext = SecurityContextHolder.getContext();
            }
            
            Authentication authentication = securityContext.getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return null;
            }
            
            String username = authentication.getName();
            if ("anonymousUser".equals(username)) {
                return null;
            }
            
            return usersService.getUserByLoginId(username);
            
        } catch (Exception e) {
            log.warn("사용자 인증 정보 조회 실패", e);
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
        
        log.info("=== 내 예약 내역 페이지 요청 ===");
        log.info("요청 URL: /my/reservations");
        log.info("세션 ID: {}", request.getSession(false) != null ? request.getSession(false).getId() : "없음");
        
        Users currentUser = getCurrentUser(request);
        
        log.info("현재 사용자: {}", currentUser != null ? currentUser.getEmail() : "null");
        
        // 🔍 추가 디버깅 정보
        if (currentUser != null) {
            log.info("=== 현재 로그인 사용자 상세 정보 ===");
            log.info("사용자 ID: {}", currentUser.getUserId());
            log.info("사용자 이메일: {}", currentUser.getEmail());
            log.info("사용자 이름: {}", currentUser.getName());
            log.info("사용자 로그인 ID: {}", currentUser.getLoginId());
        }
        
        if (currentUser == null) {
            log.warn("인증되지 않은 사용자 - 로그인 페이지로 리다이렉트");
            return "redirect:/login";
        }
        
        try {
            // 상태값 변환 (소문자 -> 대문자)
            String convertedStatus = null;
            if (status != null && !status.trim().isEmpty()) {
                convertedStatus = status.toUpperCase();
                log.info("상태값 변환: {} -> {}", status, convertedStatus);
            }
            
            log.info("내 예약 내역 조회 - userId: {}, status: {}, startDate: {}, endDate: {}, category: {}", 
                    currentUser.getUserId(), convertedStatus, startDate, endDate, category);
            
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
            
            // 예약 목록 조회 (변환된 상태값 사용)
            List<MyReservation> reservations = myService.getMyReservationsWithFilter(
                currentUser.getUserId(), convertedStatus, parsedStartDate, parsedEndDate, category);
            
            // 예약 현황 요약
            Map<String, Object> reservationSummary = myService.getReservationSummary(currentUser.getUserId());
            
            // 사용 통계도 추가 (JSP에서 usageStatistics도 표시하므로)
            Map<String, Object> usageStatistics = myService.getMyUsageStatistics(currentUser.getUserId());
            
            // 디버깅 로그 추가
            log.info("=== 통계 데이터 디버깅 ===");
            log.info("reservationSummary: {}", reservationSummary);
            log.info("usageStatistics: {}", usageStatistics);
            
            log.info("조회된 예약 건수: {}", reservations != null ? reservations.size() : 0);
            
            // 🔍 각 예약의 사용자 ID 확인
            if (reservations != null && !reservations.isEmpty()) {
                log.info("=== 조회된 예약 목록 상세 ===");
                for (int i = 0; i < Math.min(reservations.size(), 10); i++) { // 최대 10개만 로그
                    MyReservation reservation = reservations.get(i);
                    log.info("예약 #{}: ID={}, 사용자ID={}, 농기계={}, 상태={}", 
                        i+1, 
                        reservation.getReservationId(),
                        reservation.getUserId(),
                        reservation.getAssetName(),
                        reservation.getStatus());
                }
                if (reservations.size() > 10) {
                    log.info("... 나머지 {} 건의 예약이 더 있습니다.", reservations.size() - 10);
                }
            }
            
            model.addAttribute("user", currentUser);
            model.addAttribute("reservations", reservations);
            model.addAttribute("reservationSummary", reservationSummary);
            model.addAttribute("usageStatistics", usageStatistics); 
            model.addAttribute("currentStatus", status); // 원본 값 그대로 전달 (선택된 옵션 유지용)
            model.addAttribute("currentStartDate", startDate);
            model.addAttribute("currentEndDate", endDate);
            model.addAttribute("currentCategory", category);
            
            log.info("JSP 반환: my/myReservations");
            return "my/myReservations";
            
        } catch (Exception e) {
            log.error("예약 내역 조회 중 오류 발생 - userId: {}", currentUser.getUserId(), e);
            model.addAttribute("error", "예약 내역을 불러오는 중 오류가 발생했습니다.");
            return "error/500";
        }
    }
    
    /**
     * 예약 상세 정보 조회 (AJAX)
     */
    @GetMapping("/reservations/{reservationId}")
    @ResponseBody
    public ResponseEntity<MyReservation> getReservationDetail(@PathVariable("reservationId") Long reservationId,
            HttpServletRequest request) {
        Users currentUser = getCurrentUser(request);
        
        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            log.info("예약 상세 정보 조회 - reservationId: {}, userId: {}", reservationId, currentUser.getUserId());
            
            MyReservation reservation = myService.getMyReservationDetail(reservationId, currentUser.getUserId());
            
            if (reservation == null) {
                log.warn("예약을 찾을 수 없음 - reservationId: {}, userId: {}", reservationId, currentUser.getUserId());
                return ResponseEntity.notFound().build();
            }
            
            return ResponseEntity.ok(reservation);
            
        } catch (Exception e) {
            log.error("예약 상세 정보 조회 중 오류 발생 - reservationId: {}, userId: {}", 
                     reservationId, currentUser.getUserId(), e);
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 예약 취소 (AJAX)
     */

    @PostMapping("/reservations/{reservationId}/cancel")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> cancelReservation(@PathVariable("reservationId") Long reservationId,
                                                               HttpServletRequest request) {
        Users currentUser = getCurrentUser(request);
        Map<String, Object> response = new HashMap<>();
        
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
                response.put("message", "예약이 성공적으로 취소되었습니다.");
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
    	
    	System.out.println("=== 받은 파라미터 확인 ===");
        System.out.println("usageStatus: [" + usageStatus + "]");
        System.out.println("usageStatus 길이: " + (usageStatus != null ? usageStatus.length() : "null"));
        System.out.println("usageStatus가 ACTIVE와 같은가: " + "ACTIVE".equals(usageStatus));
        
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
            
            // 사용 내역 조회 (예약 테이블에서 사용이 시작되었거나 완료된 것들)
            List<MyReservation> usageHistory = myService.getMyUsageHistoryWithFilter(
                currentUser.getUserId(), parsedStartDate, parsedEndDate, category, usageStatus);
            
            // 사용 통계
            Map<String, Object> usageStatistics = myService.getMyUsageStatistics(currentUser.getUserId());
            
            // ✅ 예약 현황 요약도 추가 (JSP에서 reservationSummary도 표시하므로)
            Map<String, Object> reservationSummary = myService.getReservationSummary(currentUser.getUserId());
            
            // 🔍 디버깅 로그 추가
            log.info("=== 통계 데이터 디버깅 ===");
            log.info("usageStatistics: {}", usageStatistics);
            log.info("reservationSummary: {}", reservationSummary);
            
            model.addAttribute("user", currentUser);
            model.addAttribute("usageHistory", usageHistory);
            model.addAttribute("usageStatistics", usageStatistics);
            model.addAttribute("reservationSummary", reservationSummary);
            model.addAttribute("currentStartDate", startDate);
            model.addAttribute("currentEndDate", endDate);
            model.addAttribute("currentCategory", category);
            model.addAttribute("currentUsageStatus", usageStatus);
            
            return "my/myUsageHistory"; // JSP 파일명 수정
            
        } catch (Exception e) {
            log.error("사용 내역 조회 중 오류 발생 - userId: {}", currentUser.getUserId(), e);
            model.addAttribute("error", "사용 내역을 불러오는 중 오류가 발생했습니다.");
            return "error/500";
        }
    }
}