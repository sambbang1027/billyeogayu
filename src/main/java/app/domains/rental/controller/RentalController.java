package app.domains.rental.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import app.domains.asset.model.Asset;
import app.domains.rental.model.BlockedRange;
import app.domains.rental.service.RentalService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RentalController {

    private final RentalService service;

    /* ===============================================================
     * [HARDCODE 전용] - 로그인 연동 시 false로 변경 후 관련 코드 제거
     * =============================================================== */
    private static final boolean USE_HARDCODE = true;
    private static final Long   HC_USER_ID    = 5L;
    private static final String HC_NAME       = "김철수";
    private static final String HC_BIRTH      = "90/01/01";
    private static final String HC_PHONE      = "010-1234-5678";
    private static final Long   HC_ASSET_ID   = 52L;

    /**
     * GET /rental/apply 접근 시 올바른 경로로 리다이렉트
     */
    @GetMapping("/rental/apply")
    public String guardGetOnPostEndpoint(@RequestParam(value="assetId", required=false) Long assetId,
                                         @RequestParam(value="resourceId", required=false) Long resourceId,
                                         @RequestParam(value="success", required=false) String success,
                                         Model model,
                                         RedirectAttributes redirectAttributes) {
        
        // 성공 메시지가 있는 경우 resource/list로 리다이렉트하면서 메시지 전달
        if ("true".equals(success)) {
            redirectAttributes.addFlashAttribute("successMessage", 
                "임대 신청이 정상적으로 등록되었습니다. 승인 결과는 신청내역에서 확인해주세요.(1-2일이 소요될 수 있습니다.)");
            return "redirect:/resource/list";
        }
        
        Long id = (resourceId != null) ? resourceId : assetId;
        
        if (id == null && USE_HARDCODE) {
            id = HC_ASSET_ID;
        }

        return (id != null) ? "redirect:/rent/apply?resourceId=" + id : "redirect:/resource/list";
    }

    /**
     * 신청 페이지 진입
     */
    @GetMapping("/rent/apply")
    public String showApply(@RequestParam("resourceId") Long assetId,
                            HttpSession session,
                            Model model) {

        Asset asset = service.getAsset(assetId);
        if (asset == null || "Y".equalsIgnoreCase(asset.getIsDeleted())) {
            model.addAttribute("error", "선택한 자원을 찾을 수 없습니다.");
            return "error/404";
        }

        // 신청자 정보 설정
        String name, birth, phone;
        if (USE_HARDCODE) {
            name = HC_NAME;
            birth = HC_BIRTH;
            phone = HC_PHONE;
        } else {
            // [LOGIN 연동 시 세션에서 가져오기]
            name = (String) session.getAttribute("USER_NAME");
            birth = (String) session.getAttribute("USER_BIRTH");
            phone = (String) session.getAttribute("USER_PHONE");
            
            // 세션값이 없으면 기본값 설정
            if (name == null) name = "";
            if (birth == null) birth = "";
            if (phone == null) phone = "";
        }

        // 자산 정보
        String assetModel = (asset.getCategory() != null && !asset.getCategory().trim().isEmpty()) 
                           ? asset.getCategory() : "정보 없음";

        model.addAttribute("assetId", asset.getAssetId());
        model.addAttribute("assetName", asset.getName());
        model.addAttribute("assetModel", assetModel);
        model.addAttribute("assetMaker", asset.getCompany());
        model.addAttribute("assetImage", asset.getImage());

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
        
        return "apply";
    }

    /**
     * 예약된 날짜 조회 API
     */
    @GetMapping("/api/rental/blocked")
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
            return new ArrayList<>();
        }
    }

    /**
     * 임대 신청 제출
     */
    @PostMapping("/rental/apply")
    public String submit(@RequestParam("assetId") Long assetId,
                         @RequestParam("rentStartDate") String rentStartDate,
                         @RequestParam("rentEndDate") String rentEndDate,
                         @RequestParam("rentStartTime") String rentStartTime,
                         @RequestParam("rentEndTime") String rentEndTime,
                         @RequestParam("purpose") String purpose,
                         @RequestParam("zipcode") String zipcode,
                         @RequestParam("addr1") String addr1,
                         @RequestParam(value="addr2", required=false) String addr2,
                         HttpSession session,
                         Model model,
                         RedirectAttributes redirectAttributes) throws ParseException {

        // 사용자 ID
        Long userId;
        if (USE_HARDCODE) {
            userId = HC_USER_ID;
        } else {
            // [LOGIN 연동 시] 세션에서 사용자 ID 가져오기
            userId = (Long) session.getAttribute("USER_ID");
            if (userId == null) {
                // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
                return "redirect:/login";
            }
        }

        // 날짜/시간 파싱
        Date startAt = parseDateTime(rentStartDate, rentStartTime);
        Date endAt = parseDateTime(rentEndDate, rentEndTime);

        boolean success = service.apply(assetId, userId, startAt, endAt, purpose, zipcode, addr1, 
                (addr2 == null ? "" : addr2));

        if (success) {
            // 성공 시 success 파라미터와 함께 리다이렉트
            return "redirect:/rental/apply?success=true";
        } else {
            // 실패 시 기존 로직 유지
            restoreFormData(assetId, zipcode, addr1, addr2, purpose, model);
            model.addAttribute("error", "해당 날짜는 이미 신청되었거나 입력값이 올바르지 않습니다. 다른 날짜를 선택해주세요.");
            return "apply";
        }
    }

    /**
     * 실패 시 폼 데이터 복원
     */
    private void restoreFormData(Long assetId, String zipcode, String addr1, String addr2, 
                                String purpose, Model model) {
        // 자산 정보 재설정
        Asset asset = service.getAsset(assetId);
        if (asset != null) {
            String assetModel = (asset.getCategory() != null && !asset.getCategory().trim().isEmpty())
                               ? asset.getCategory() : "정보 없음";
            
            model.addAttribute("assetId", asset.getAssetId());
            model.addAttribute("assetName", asset.getName());
            model.addAttribute("assetModel", assetModel);
            model.addAttribute("assetMaker", asset.getCompany());
            model.addAttribute("assetImage", asset.getImage());
        }

        // 신청인 정보 복원
        Map<String, Object> applicant = new HashMap<>();
        if (USE_HARDCODE) {
            applicant.put("name", HC_NAME);
            applicant.put("birth", HC_BIRTH);
        } else {
            applicant.put("name", "");
            applicant.put("birth", "");
        }
        applicant.put("zipcode", zipcode);
        applicant.put("addr1", addr1);
        applicant.put("addr2", (addr2 == null ? "" : addr2));
        model.addAttribute("applicant", applicant);

        String phone = USE_HARDCODE ? HC_PHONE : "";
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