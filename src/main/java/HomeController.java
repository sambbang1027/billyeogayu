import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String home(Model model) {
        model.addAttribute("title", "Billyeogayu");
        model.addAttribute("message", "Spring Legacy + JSP + MyBatis 프로젝트에 오신 것을 환영합니다!");
        model.addAttribute("currentTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return "home";
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }

}
