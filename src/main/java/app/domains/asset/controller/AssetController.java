package app.domains.asset.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/asset")
public class AssetController {
    @GetMapping("/list")
    public String asset(Model model) {
        model.addAttribute("pageTitle", "자산 관리");
        model.addAttribute("contentPage", "/WEB-INF/views/asset/assetList.jsp");
        return "layout/admin/main";
    }
}
