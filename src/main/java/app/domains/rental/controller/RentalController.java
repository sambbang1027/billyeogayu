package app.domains.rental.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RentalController {

    /** 로그인 미구현 → 임시 세션 사용자 */
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class LoginUserMock {
        private int    userId;
        private String name;     // 성명
        private String phone;    // 010-1234-1234
        private String birth;    // 19750101
        private String zipcode;  // 주소(선택)
        private String addr1;
        private String addr2;
    }

    @GetMapping("/rental/apply")
    public String showRentalApplyPage(Model model, HttpSession session) {
        // 0) 로그인 대용 세션 주입(없을 때만)
        LoginUserMock login = (LoginUserMock) session.getAttribute("loginUser");
        if (login == null) {
            login = new LoginUserMock(
                    1,
                    "이농민",
                    "010-1234-1234",
                    "19750101",
                    "", "", ""
            );
            session.setAttribute("loginUser", login);
        }

        // 1) 화면에서 바로 쓰기 좋게 연락처 3등분
        String[] p = login.getPhone() != null ? login.getPhone().split("-") : new String[0];
        model.addAttribute("applicant", login);
        model.addAttribute("phone1", p.length > 0 ? p[0] : "");
        model.addAttribute("phone2", p.length > 1 ? p[1] : "");
        model.addAttribute("phone3", p.length > 2 ? p[2] : "");

        // 2) 더미 농기계 정보
        model.addAttribute("assetName",  "농용 트랙터");
        model.addAttribute("assetModel", "GMS2600");
        model.addAttribute("assetMaker","KOSA");
        // model.addAttribute("assetImage", "/static/img/tractor.png"); // 있으면 사용

        return "rental/apply"; // /WEB-INF/views/rental/apply.jsp
    }
}
