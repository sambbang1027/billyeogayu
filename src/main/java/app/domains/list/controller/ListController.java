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

        System.out.println("★★★ Controller 호출됨! ★★★");
        System.out.println("원본 파라미터 - q: '" + q + "', filter: '" + filter + "', page: " + page);

        try {
            // 1) 한 페이지 카드 개수(그리드 4x3 기준)
            final int pageSize = 12;

            // 파라미터 정리
            if (q != null && q.trim().isEmpty()) q = null;
            if (filter != null && filter.trim().isEmpty()) filter = null;
            if (page < 1) page = 1;

            System.out.println("정리된 파라미터 - q: '" + q + "', filter: '" + filter + "', page: " + page);

            // 2) 전체 개수 / 총 페이지
            int total = service.getAssetCount(q, filter);
            System.out.println("전체 자산 그룹 수: " + total);
            
            int totalPages = Math.max(1, (int) Math.ceil((double) total / pageSize));
            System.out.println("총 페이지 수: " + totalPages);

            // 3) 현재 페이지 보정
            page = Math.min(Math.max(page, 1), totalPages);
            System.out.println("보정된 페이지: " + page);

            // 4) 목록 조회
            List<Asset> items = service.getAssets(q, filter, page, pageSize);

            // === 결과 검증 ===
            System.out.println("=== 최종 조회 결과 ===");
            System.out.println("총 개수: " + total);
            System.out.println("총 페이지: " + totalPages);
            System.out.println("현재 페이지: " + page);
            System.out.println("조회된 항목 수: " + (items != null ? items.size() : 0));

            if (items != null && !items.isEmpty()) {
                System.out.println("첫 번째 항목 정보:");
                Asset first = items.get(0);
                System.out.println("  - ID: " + first.getAssetId());
                System.out.println("  - 이름: " + first.getName());
                System.out.println("  - 카테고리: " + first.getCategory());
                System.out.println("  - 회사: " + first.getCompany());
                System.out.println("  - 전체재고: " + first.getTotalStock());
                System.out.println("  - 사용가능재고: " + first.getAvailableStock());
                System.out.println("  - 대여중재고: " + first.getRentedStock());
                System.out.println("  - 임대가능여부: " + first.isRentable());
                System.out.println("  - 삭제여부: " + first.getIsDeleted());
            }

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

            System.out.println("=== Controller 정상 완료 ===");
            return "list"; // /WEB-INF/views/list.jsp

        } catch (Exception e) {
            System.err.println("=== Controller에서 오류 발생 ===");
            System.err.println("오류 타입: " + e.getClass().getSimpleName());
            System.err.println("오류 메시지: " + e.getMessage());
            e.printStackTrace();
            
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