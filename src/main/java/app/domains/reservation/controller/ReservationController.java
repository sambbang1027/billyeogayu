package app.domains.reservation.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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

import app.domains.reservation.model.BlockedRange;
import app.domains.reservation.model.TimeSlotAvailability;
import app.domains.reservation.service.ReservationService;
import app.domains.resource.model.Resource;
import app.domains.users.model.Users;
import app.domains.users.service.UsersService;
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
     * 임대 신청 신청 페이지 진입 - 모든 케이스 통합 처리
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
                    "임대 신청 신청이 정상적으로 등록되었습니다. 승인 결과는 신청내역에서 확인해주세요.(1-2일이 소요될 수 있습니다.)");
                return "redirect:/resource/list";
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
                    log.info("임대 신청 페이지 - 세션에서 SecurityContext 복원 완료 - 세션ID: {}", session.getId());
                }
            }

            // 2) 로그인 확인
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
                log.warn("비로그인 사용자의 임대 신청 신청 시도");
                return "redirect:/login";
            }

            // assetId가 없으면 자원 목록으로 리다이렉트
            if (assetId == null) {
                log.warn("자산 ID가 없음 - 자원 목록으로 리다이렉트");
                return "redirect:/resource/list";
            }

            // 3) 로그인된 사용자 정보 조회
            String loginId = auth.getName();
            Users loginUser = usersService.getUserByLoginId(loginId);

            if (loginUser == null) {
                log.error("로그인된 사용자 정보를 찾을 수 없음: {}", loginId);
                model.addAttribute("error", "사용자 정보를 찾을 수 없습니다. 다시 로그인해주세요.");
                return "redirect:/login";
            }

            log.info("임대 신청 신청 페이지 접근 - 사용자: {}, 자산ID: {}", loginUser.getName(), assetId);

            
            Resource asset = service.getAsset(assetId);
            

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
            log.error("임대 신청 신청 페이지 로딩 중 오류 발생", e);
            model.addAttribute("error", "페이지 로딩 중 오류가 발생했습니다: " + e.getMessage());
            return "error/500";
        }
    }

    /**
     * 날짜별 임대 가능 여부 체크 API (달력 비활성화용)
     */
    @GetMapping("/api/reservation/date-availability")
    @ResponseBody
    public Map<String, Object> getDateAvailability(@RequestParam("assetId") Long assetId,
                                                   @RequestParam("from") String from,
                                                   @RequestParam("to") String to) {
        log.info("=== 날짜별 가용성 체크 API 호출 ===");
        log.info("요청 파라미터 - assetId: {}, from: {}, to: {}", assetId, from, to);
        
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fromDate = LocalDate.parse(from, dateFormatter);
            LocalDate toDate = LocalDate.parse(to, dateFormatter);
            
            LocalDateTime fromDateTime = fromDate.atStartOfDay();
            LocalDateTime toDateTime = toDate.atTime(23, 59, 59);
            
            // 전체 기간의 시간대별 데이터 조회
            List<TimeSlotAvailability> allSlots = service.getTimeSlotAvailability(assetId, fromDateTime, toDateTime);
            
            // 날짜별로 그룹화하고 가용성 계산
            Map<String, Boolean> dateAvailabilityMap = new HashMap<>();
            Map<String, List<TimeSlotAvailability>> dateGroups = new HashMap<>();
            
            // 날짜별로 그룹화
            for (TimeSlotAvailability slot : allSlots) {
                String dateKey = slot.getStartTime().toLocalDate().toString(); // "2025-09-16"
                dateGroups.computeIfAbsent(dateKey, k -> new ArrayList<>()).add(slot);
            }
            
            // 각 날짜별 가용성 판단
            for (Map.Entry<String, List<TimeSlotAvailability>> entry : dateGroups.entrySet()) {
                String dateKey = entry.getKey();
                List<TimeSlotAvailability> daySlots = entry.getValue();
                
                // 해당 날짜의 가용 시간대 개수 계산
                long availableSlotCount = daySlots.stream()
                    .filter(slot -> slot.getAvailableCount() > 0)
                    .count();
                
                // 최소 2개 이상의 시간대가 가용해야 해당 날짜 활성화
                boolean isDateAvailable = availableSlotCount >= 2;
                
                dateAvailabilityMap.put(dateKey, isDateAvailable);
                
                log.debug("날짜 {}: 전체 {}개 슬롯 중 {}개 가용 → {}", 
                         dateKey, daySlots.size(), availableSlotCount, 
                         isDateAvailable ? "활성화" : "비활성화");
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("dateAvailability", dateAvailabilityMap);
            result.put("minAvailableSlots", 2); // 프론트엔드에서 참고용
            
            log.info("날짜별 가용성 체크 완료 - {} 개 날짜 처리", dateAvailabilityMap.size());
            
            return result;
            
        } catch (Exception e) {
            log.error("날짜별 가용성 체크 중 오류 발생", e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("dateAvailability", new HashMap<>());
            return errorResult;
        }
    }

    /**
     * 임대 신청된 날짜 조회 API (기존 호환성 유지)
     */
    @GetMapping("/api/reservation/blocked")
    @ResponseBody
    public List<Map<String, Object>> apiBlocked(@RequestParam("assetId") Long assetId,
                                                @RequestParam("from") String from,
                                                @RequestParam("to") String to) {
        try {
            // LocalDateTime으로 파싱
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime fromDateTime = LocalDateTime.parse(from, formatter);
            LocalDateTime toDateTime = LocalDateTime.parse(to, formatter);

            List<BlockedRange> blockedRanges = service.getBlockedRanges(assetId, fromDateTime, toDateTime);
            
            // LocalDateTime을 문자열로 변환해서 반환
            List<Map<String, Object>> result = new ArrayList<>();
            DateTimeFormatter responseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            
            for (BlockedRange range : blockedRanges) {
                Map<String, Object> map = new HashMap<>();
                map.put("startAt", range.getStartAt().format(responseFormatter));
                map.put("endAt", range.getEndAt().format(responseFormatter));
                result.add(map);
            }
            
            return result;
        } catch (Exception e) {
            log.error("날짜 파싱 오류", e);
            return new ArrayList<>();
        }
    }

    /**
     * 시간대별 임대 신청 가능 여부 조회 API (새로 추가)
     */
    @GetMapping("/api/reservation/timeslots")
    @ResponseBody
    public List<Map<String, Object>> apiTimeSlots(@RequestParam("assetId") Long assetId,
                                                  @RequestParam("from") String from,
                                                  @RequestParam("to") String to) {
        log.info("=== /api/reservation/timeslots API 호출 ===");
        log.info("요청 파라미터 - assetId: {}, from: {}, to: {}", assetId, from, to);
        
        try {
            // 날짜만 파싱해서 LocalDateTime으로 변환
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fromDate = LocalDate.parse(from, dateFormatter);
            LocalDate toDate = LocalDate.parse(to, dateFormatter);
            
            // 시작일은 00:00:00, 종료일은 23:59:59로 설정
            LocalDateTime fromDateTime = fromDate.atStartOfDay();
            LocalDateTime toDateTime = toDate.atTime(23, 59, 59);
            
            log.info("파싱된 날짜 - fromDateTime: {}, toDateTime: {}", fromDateTime, toDateTime);
            
            List<TimeSlotAvailability> result = service.getTimeSlotAvailability(assetId, fromDateTime, toDateTime);
            
            log.info("서비스 호출 결과 - 슬롯 개수: {}", result.size());
            
            // LocalDateTime을 문자열로 변환해서 Map으로 반환
            List<Map<String, Object>> jsonResult = new ArrayList<>();
            DateTimeFormatter responseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            
            for (TimeSlotAvailability slot : result) {
                Map<String, Object> map = new HashMap<>();
                map.put("startTime", slot.getStartTime().format(responseFormatter));
                map.put("endTime", slot.getEndTime().format(responseFormatter));
                map.put("availableCount", slot.getAvailableCount());
                map.put("totalCount", slot.getTotalCount());
                jsonResult.add(map);
            }
            
            if (!jsonResult.isEmpty()) {
                log.info("첫 번째 슬롯: 시작시간={}, 반납시간={}, 가용={}/{}", 
                        jsonResult.get(0).get("startTime"), jsonResult.get(0).get("endTime"),
                        jsonResult.get(0).get("availableCount"), jsonResult.get(0).get("totalCount"));
            }
            
            return jsonResult;
            
        } catch (Exception e) {
            log.error("시간대별 임대 신청 현황 조회 중 오류 발생", e);
            return new ArrayList<>();
        }
    }

    /**
     * 테스트용 시간대별 임대 신청 가능 여부 조회 API
     * 실제 데이터 대신 더미 데이터 반환하여 프론트엔드 테스트
     */
    @GetMapping("/api/reservation/timeslots-test")
    @ResponseBody
    public List<Map<String, Object>> apiTimeSlotsTest(@RequestParam("assetId") Long assetId,
                                                      @RequestParam("from") String from,
                                                      @RequestParam("to") String to) {
        log.info("=== 테스트용 /api/reservation/timeslots-test API 호출 ===");
        log.info("요청 파라미터 - assetId: {}, from: {}, to: {}", assetId, from, to);
        
        List<Map<String, Object>> testSlots = new ArrayList<>();
        
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fromDate = LocalDate.parse(from, dateFormatter);
            LocalDate toDate = LocalDate.parse(to, dateFormatter);
            
            LocalDate currentDate = fromDate;
            DateTimeFormatter responseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            
            while (!currentDate.isAfter(toDate)) {
                // 주말 제외
                if (currentDate.getDayOfWeek().getValue() != 6 && currentDate.getDayOfWeek().getValue() != 7) {
                    
                    // 9:00 ~ 18:00 시간대 생성 (점심시간 제외)
                    for (int hour = 9; hour <= 18; hour++) {
                        for (int minute : new int[]{0, 30}) {
                            if (hour == 18 && minute == 30) break;
                            if ((hour == 12) || (hour == 13 && minute == 0)) continue;
                            
                            LocalDateTime slotStart = currentDate.atTime(hour, minute);
                            LocalDateTime slotEnd = slotStart.plusMinutes(30);
                            
                            // 테스트용 가용성 설정 (일부 시간대는 임대 신청 불가능하게 설정)
                            int availableCount = 1; // 기본 가용
                            
                            // 9시, 10시, 11시대는 임대 신청 불가능하게 설정
                            if (hour >= 9 && hour <= 11) {
                                availableCount = 0;
                            }
                            
                            Map<String, Object> slot = new HashMap<>();
                            slot.put("startTime", slotStart.format(responseFormatter));
                            slot.put("endTime", slotEnd.format(responseFormatter));
                            slot.put("availableCount", availableCount);
                            slot.put("totalCount", 1);
                            
                            testSlots.add(slot);
                            
                            log.debug("테스트 슬롯 생성: {}:{} - 가용: {}", 
                                     hour, (minute == 0 ? "00" : "30"), availableCount);
                        }
                    }
                }
                currentDate = currentDate.plusDays(1);
            }
            
            log.info("테스트 슬롯 생성 완료 - 총 {} 개", testSlots.size());
            
            // 처음 몇 개 슬롯 로그 출력
            if (!testSlots.isEmpty()) {
                log.info("=== 테스트 슬롯 샘플 (처음 5개) ===");
                for (int i = 0; i < Math.min(5, testSlots.size()); i++) {
                    Map<String, Object> slot = testSlots.get(i);
                    log.info("슬롯 {}: {} ~ {}, 가용: {}/{}", 
                            i + 1, slot.get("startTime"), slot.get("endTime"), 
                            slot.get("availableCount"), slot.get("totalCount"));
                }
            }
            
            return testSlots;
            
        } catch (Exception e) {
            log.error("테스트 시간대별 임대 신청 현황 조회 중 오류 발생", e);
            return new ArrayList<>();
        }
    }

    /**
     * 임대 신청 신청 제출 (중복 제출 방지 포함)
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
                         RedirectAttributes redirectAttributes) {

        // 세션과 인증 정보를 메서드 최상단에서 선언
        HttpSession session = null;
        Authentication auth = null;
        
        try {
            // 1) 세션에서 SecurityContext 수동 복원
            session = request.getSession(false);
            if (session != null) {
                Object storedContext = session.getAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
                if (storedContext instanceof SecurityContext) {
                    SecurityContextHolder.setContext((SecurityContext) storedContext);
                    log.info("임대 신청 제출 - 세션에서 SecurityContext 복원 완료 - 세션ID: {}", session.getId());
                }
            }

            // 2) 로그인 확인
            auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
                log.warn("비로그인 사용자의 임대 신청 신청 제출 시도");
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
            log.info("임대 신청 신청 제출 - 사용자: {} (ID: {}), 자산ID: {}", 
                    loginUser.getName(), userId, assetId);

            // 4) 기본 입력값 검증
            if (reserveStartDate == null || reserveStartDate.trim().isEmpty() ||
                reserveEndDate == null || reserveEndDate.trim().isEmpty() ||
                reserveStartTime == null || reserveStartTime.trim().isEmpty() ||
                reserveEndTime == null || reserveEndTime.trim().isEmpty()) {
                
                log.warn("필수 날짜/시간 정보 누락");
                restoreFormData(assetId, zipcode, addr1, addr2, purpose, model, loginUser);
                model.addAttribute("error", "임대 신청 날짜와 시간을 모두 선택해주세요.");
                return "reservation";
            }

            if (purpose == null || purpose.trim().isEmpty()) {
                log.warn("사용 목적 누락");
                restoreFormData(assetId, zipcode, addr1, addr2, purpose, model, loginUser);
                model.addAttribute("error", "사용 목적을 입력해주세요.");
                return "reservation";
            }

            if (zipcode == null || zipcode.trim().isEmpty() ||
                addr1 == null || addr1.trim().isEmpty()) {
                
                log.warn("주소 정보 누락");
                restoreFormData(assetId, zipcode, addr1, addr2, purpose, model, loginUser);
                model.addAttribute("error", "우편번호와 기본 주소를 입력해주세요.");
                return "reservation";
            }

            // 5) 날짜/시간 파싱 - LocalDateTime 반환
            LocalDateTime startAt = parseDateTime(reserveStartDate, reserveStartTime);
            LocalDateTime endAt = parseDateTime(reserveEndDate, reserveEndTime);

            // 6) 날짜 유효성 검증
            LocalDateTime now = LocalDateTime.now();
            if (startAt.isBefore(now)) {
                log.warn("과거 날짜로 임대 신청 시도 - 시작일: {}", startAt);
                restoreFormData(assetId, zipcode, addr1, addr2, purpose, model, loginUser);
                model.addAttribute("error", "과거 날짜로는 임대 신청할 수 없습니다.");
                return "reservation";
            }

            if (!startAt.isBefore(endAt)) {
                log.warn("잘못된 날짜 범위 - 시작일: {}, 반납일: {}", startAt, endAt);
                restoreFormData(assetId, zipcode, addr1, addr2, purpose, model, loginUser);
                model.addAttribute("error", "반납일은 시작일보다 늦어야 합니다.");
                return "reservation";
            }

            // 7) 임대 신청 신청 처리
            boolean success = service.apply(assetId, userId, startAt, endAt, purpose, zipcode, addr1,
                    (addr2 == null ? "" : addr2));

            if (success) {
                log.info("임대 신청 신청 성공 - 사용자: {}, 자산ID: {}", 
                        loginUser.getName(), assetId);
                
                // 성공 시 성공 메시지와 함께 리다이렉트
                redirectAttributes.addFlashAttribute("successMessage",
                    "임대 신청 신청이 정상적으로 등록되었습니다. 승인 결과는 신청내역에서 확인해주세요.(1-2일이 소요될 수 있습니다.)");
                
                return "redirect:/resource/list";
            } else {
                log.warn("임대 신청 신청 실패 - 사용자: {}, 자산ID: {}", loginUser.getName(), assetId);
                
                restoreFormData(assetId, zipcode, addr1, addr2, purpose, model, loginUser);
                model.addAttribute("error", "해당 시간대는 이미 임대 신청이 가득 찼거나 입력값이 올바르지 않습니다. 다른 시간대를 선택해주세요.");
                return "reservation";
            }

        } catch (Exception e) {
            log.error("임대 신청 신청 제출 중 오류 발생", e);
            
            if (auth != null) {
                restoreFormData(assetId, zipcode, addr1, addr2, purpose, model, 
                               usersService.getUserByLoginId(auth.getName()));
            }
            model.addAttribute("error", "임대 신청 신청 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
            return "reservation";
        }
    }

    /**
     * 실패 시 폼 데이터 복원
     */
    private void restoreFormData(Long assetId, String zipcode, String addr1, String addr2,
                                String purpose, Model model, Users loginUser) {
        // 자산 정보 재설정
        Resource asset = service.getAsset(assetId);
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
        if (phone == null || phone.isBlank()) {
            return result;
        }

        String digits = phone.replaceAll("[^0-9]", "");
        if (digits.length() >= 10) {
            result[0] = digits.substring(0, 3);
            result[1] = digits.substring(3, digits.length() - 4);
            result[2] = digits.substring(digits.length() - 4);
        }
        return result;
    }

    // 날짜/시간 파싱 메서드 - LocalDateTime 반환
    private static LocalDateTime parseDateTime(String date, String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(date + " " + time, formatter);
    }
}