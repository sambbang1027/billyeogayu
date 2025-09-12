package app.domains.reservation.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

            log.info("예약 신청 페이지 접근 - 사용자: {}, 자산ID: {}", loginUser.getName(), assetId);

            
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
            log.error("예약 신청 페이지 로딩 중 오류 발생", e);
            model.addAttribute("error", "페이지 로딩 중 오류가 발생했습니다: " + e.getMessage());
            return "error/500";
        }
    }

    /**
     * 예약된 날짜 조회 API (기존 호환성 유지)
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
     * 시간대별 예약 가능 여부 조회 API (새로 추가)
     */
    @GetMapping("/api/reservation/timeslots")
    @ResponseBody
    public List<TimeSlotAvailability> apiTimeSlots(@RequestParam("assetId") Long assetId,
                                                   @RequestParam("from") String from,
                                                   @RequestParam("to") String to) {
        log.info("=== /api/reservation/timeslots API 호출 ===");
        log.info("요청 파라미터 - assetId: {}, from: {}, to: {}", assetId, from, to);
        
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = formatter.parse(from);
            Date toDate = formatter.parse(to);
            
            log.info("파싱된 날짜 - fromDate: {}, toDate: {}", fromDate, toDate);
            
            List<TimeSlotAvailability> result = service.getTimeSlotAvailability(assetId, fromDate, toDate);
            
            log.info("서비스 호출 결과 - 슬롯 개수: {}", result.size());
            
            if (!result.isEmpty()) {
                log.info("첫 번째 슬롯: 시작시간={}, 종료시간={}, 가용={}/{}", 
                        result.get(0).getStartTime(), result.get(0).getEndTime(),
                        result.get(0).getAvailableCount(), result.get(0).getTotalCount());
            }
            
            return result;
            
        } catch (ParseException e) {
            log.error("날짜 파싱 오류", e);
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("시간대별 예약 현황 조회 중 오류 발생", e);
            return new ArrayList<>();
        }
    }

    /**
     * 테스트용 시간대별 예약 가능 여부 조회 API
     * 실제 데이터 대신 더미 데이터 반환하여 프론트엔드 테스트
     */
    @GetMapping("/api/reservation/timeslots-test")
    @ResponseBody
    public List<TimeSlotAvailability> apiTimeSlotsTest(@RequestParam("assetId") Long assetId,
                                                       @RequestParam("from") String from,
                                                       @RequestParam("to") String to) {
        log.info("=== 테스트용 /api/reservation/timeslots-test API 호출 ===");
        log.info("요청 파라미터 - assetId: {}, from: {}, to: {}", assetId, from, to);
        
        List<TimeSlotAvailability> testSlots = new ArrayList<>();
        
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = formatter.parse(from);
            Date toDate = formatter.parse(to);
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(fromDate);
            
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(toDate);
            
            while (!cal.after(endCal)) {
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                    
                    // 9:00 ~ 18:00 시간대 생성 (점심시간 제외)
                    for (int hour = 9; hour <= 18; hour++) {
                        for (int minute : new int[]{0, 30}) {
                            if (hour == 18 && minute == 30) break;
                            if ((hour == 12) || (hour == 13 && minute == 0)) continue;
                            
                            Calendar slotCal = (Calendar) cal.clone();
                            slotCal.set(Calendar.HOUR_OF_DAY, hour);
                            slotCal.set(Calendar.MINUTE, minute);
                            slotCal.set(Calendar.SECOND, 0);
                            slotCal.set(Calendar.MILLISECOND, 0);
                            
                            Date slotStart = slotCal.getTime();
                            slotCal.add(Calendar.MINUTE, 30);
                            Date slotEnd = slotCal.getTime();
                            
                            // 테스트용 가용성 설정 (일부 시간대는 예약 불가능하게 설정)
                            int availableCount = 1; // 기본 가용
                            
                            // 9시, 10시, 11시대는 예약 불가능하게 설정
                            if (hour >= 9 && hour <= 11) {
                                availableCount = 0;
                            }
                            
                            TimeSlotAvailability slot = TimeSlotAvailability.builder()
                                    .startTime(slotStart)
                                    .endTime(slotEnd)
                                    .availableCount(availableCount)
                                    .totalCount(1)
                                    .build();
                            
                            testSlots.add(slot);
                            
                            log.debug("테스트 슬롯 생성: {}:{} - 가용: {}", 
                                     hour, (minute == 0 ? "00" : "30"), availableCount);
                        }
                    }
                }
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
            
            log.info("테스트 슬롯 생성 완료 - 총 {} 개", testSlots.size());
            
            // 처음 몇 개 슬롯 로그 출력
            if (!testSlots.isEmpty()) {
                log.info("=== 테스트 슬롯 샘플 (처음 5개) ===");
                for (int i = 0; i < Math.min(5, testSlots.size()); i++) {
                    TimeSlotAvailability slot = testSlots.get(i);
                    log.info("슬롯 {}: {} ~ {}, 가용: {}/{}", 
                            i + 1, slot.getStartTime(), slot.getEndTime(), 
                            slot.getAvailableCount(), slot.getTotalCount());
                }
            }
            
            return testSlots;
            
        } catch (ParseException e) {
            log.error("날짜 파싱 오류", e);
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("테스트 시간대별 예약 현황 조회 중 오류 발생", e);
            return new ArrayList<>();
        }
    }

    /**
     * 예약 신청 제출 (중복 제출 방지 포함)
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
                    log.info("예약 제출 - 세션에서 SecurityContext 복원 완료 - 세션ID: {}", session.getId());
                }
            }

            // 2) 로그인 확인
            auth = SecurityContextHolder.getContext().getAuthentication();
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
            log.info("예약 신청 제출 - 사용자: {} (ID: {}), 자산ID: {}", 
                    loginUser.getName(), userId, assetId);

            // 4) 기본 입력값 검증
            if (reserveStartDate == null || reserveStartDate.trim().isEmpty() ||
                reserveEndDate == null || reserveEndDate.trim().isEmpty() ||
                reserveStartTime == null || reserveStartTime.trim().isEmpty() ||
                reserveEndTime == null || reserveEndTime.trim().isEmpty()) {
                
                log.warn("필수 날짜/시간 정보 누락");
                restoreFormData(assetId, zipcode, addr1, addr2, purpose, model, loginUser);
                model.addAttribute("error", "예약 날짜와 시간을 모두 선택해주세요.");
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

            // 5) 날짜/시간 파싱
            Date startAt = parseDateTime(reserveStartDate, reserveStartTime);
            Date endAt = parseDateTime(reserveEndDate, reserveEndTime);

            // 6) 날짜 유효성 검증
            Date now = new Date();
            if (startAt.before(now)) {
                log.warn("과거 날짜로 예약 시도 - 시작일: {}", startAt);
                restoreFormData(assetId, zipcode, addr1, addr2, purpose, model, loginUser);
                model.addAttribute("error", "과거 날짜로는 예약할 수 없습니다.");
                return "reservation";
            }

            if (!startAt.before(endAt)) {
                log.warn("잘못된 날짜 범위 - 시작일: {}, 종료일: {}", startAt, endAt);
                restoreFormData(assetId, zipcode, addr1, addr2, purpose, model, loginUser);
                model.addAttribute("error", "종료일은 시작일보다 늦어야 합니다.");
                return "reservation";
            }

            // 7) 예약 신청 처리
            boolean success = service.apply(assetId, userId, startAt, endAt, purpose, zipcode, addr1,
                    (addr2 == null ? "" : addr2));

            if (success) {
                log.info("예약 신청 성공 - 사용자: {}, 자산ID: {}", 
                        loginUser.getName(), assetId);
                
                // 성공 시 성공 메시지와 함께 리다이렉트
                redirectAttributes.addFlashAttribute("successMessage",
                    "예약 신청이 정상적으로 등록되었습니다. 승인 결과는 신청내역에서 확인해주세요.(1-2일이 소요될 수 있습니다.)");
                
                return "redirect:/resource/list";
            } else {
                log.warn("예약 신청 실패 - 사용자: {}, 자산ID: {}", loginUser.getName(), assetId);
                
                restoreFormData(assetId, zipcode, addr1, addr2, purpose, model, loginUser);
                model.addAttribute("error", "해당 시간대는 이미 예약이 가득 찼거나 입력값이 올바르지 않습니다. 다른 시간대를 선택해주세요.");
                return "reservation";
            }

        } catch (ParseException e) {
            log.error("날짜 파싱 오류", e);
            
            if (auth != null) {
                restoreFormData(assetId, zipcode, addr1, addr2, purpose, model, 
                               usersService.getUserByLoginId(auth.getName()));
            }
            model.addAttribute("error", "날짜 형식이 올바르지 않습니다.");
            return "reservation";
            
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

    private static Date parseDateTime(String date, String time) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        formatter.setLenient(false);
        return formatter.parse(date + " " + time);
    }
}