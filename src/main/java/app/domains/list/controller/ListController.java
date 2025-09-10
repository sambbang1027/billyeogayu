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

        try {
            // 1) 한 페이지 카드 개수(그리드 4x3 기준)
            final int pageSize = 12;

            // 파라미터 정리
            if (q != null && q.trim().isEmpty()) q = null;
            if (filter != null && filter.trim().isEmpty()) filter = null;
            if (page < 1) page = 1;

            // 2) 전체 개수 / 총 페이지
            int total = service.getAssetCount(q, filter);
            int totalPages = Math.max(1, (int) Math.ceil((double) total / pageSize));

            // 3) 현재 페이지 보정
            page = Math.min(Math.max(page, 1), totalPages);

            // 4) 목록 조회
            List<Asset> items = service.getAssets(q, filter, page, pageSize);

            // 5) 페이지 블록(버튼 20개 단위)
            final int blockSize = 20;
            int startPage = ((page - 1) / blockSize) * blockSize + 1;
            int endPage = Math.min(startPage + blockSize - 1, totalPages);
            boolean hasPrevBlock = startPage > 1;
            boolean hasNextBlock = endPage < totalPages;

            // 6) 화살표용 이전/다음 페이지
            int prevPage = Math.max(1, page - 1);
            int nextPage = Math.min(totalPages, page + 1);

            // 7) 모델에 데이터 추가
            model.addAttribute("items", items);
            model.addAttribute("page", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("q", q);
            model.addAttribute("filter", filter);

            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            model.addAttribute("hasPrevBlock", hasPrevBlock);
            model.addAttribute("hasNextBlock", hasNextBlock);
            model.addAttribute("prevBlockPage", startPage - 1);
            model.addAttribute("nextBlockPage", endPage + 1);

            model.addAttribute("prevPage", prevPage);
            model.addAttribute("nextPage", nextPage);

            return "list"; // /WEB-INF/views/list.jsp

        } catch (Exception e) {
            // 오류 정보를 모델에 추가
            model.addAttribute("error", "데이터를 불러오는 중 오류가 발생했습니다: " + e.getMessage());
            model.addAttribute("q", q);
            model.addAttribute("filter", filter);
            model.addAttribute("page", 1);
            model.addAttribute("totalPages", 1);
            
            return "list"; // 오류가 있어도 일단 페이지는 보여줌
        }
    }
}