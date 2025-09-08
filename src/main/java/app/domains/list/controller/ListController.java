package app.domains.list.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.domains.asset.model.Asset;
import app.domains.list.service.ListService;

@Controller
public class ListController {

    @Autowired
    private ListService service;

    @GetMapping("/resource/list")
    public String listAssets(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {

        int pageSize = 8; // 한 페이지 8개
        int total = service.getAssetCount(q, filter);
        int totalPages = (int) Math.ceil((double) total / pageSize);

        List<Asset> items = service.getAssets(q, filter, page, pageSize);

        model.addAttribute("items", items);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("q", q);
        model.addAttribute("filter", filter);

        return "resource/list"; // JSP
    }
}
