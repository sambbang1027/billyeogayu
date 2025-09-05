package app.domains.list.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/resource")
public class ListController {

    @GetMapping("/list")
    public String asset(Model model,
                        @RequestParam(value = "q", required = false, defaultValue = "") String q,
                        @RequestParam(value = "filter", required = false, defaultValue = "") String filter,
                        @RequestParam(value = "page", required = false, defaultValue = "1") int page) {

        // 임시 데이터로 테스트해봄. 나중에 삭제.
        List<Map<String, Object>> all = new ArrayList<>();
        all.add(make(1L,  "농용 트랙터", "트랙터",   5, true,  ""));
        all.add(make(2L,  "비료 살포기", "살포기",   3, true,  ""));
        all.add(make(3L,  "논두렁 조성기", "조성기", 2, true,  ""));
        all.add(make(4L,  "트랙터",     "트랙터",   7, true,  ""));
        all.add(make(5L,  "논두렁 예초기","예초기", 5, false, ""));
        all.add(make(6L,  "콤바인",     "콤바인",   5, false, ""));
        all.add(make(7L,  "이앙기",     "이앙기",   5, true,  ""));
        all.add(make(8L,  "존디어",     "트랙터",   5, false, ""));
        all.add(make(9L,  "로터리",     "부속작업기",2, true,  ""));
        all.add(make(10L, "퇴비 살포기","살포기",   4, true,  ""));
        all.add(make(11L, "분무기",     "방제",     6, true,  ""));
        all.add(make(12L, "동력 예초기","예초기",   3, false, ""));
        all.add(make(13L, "스키드로더", "로더",     1, true,  ""));
        all.add(make(14L, "경운기 B-220","경운기",  2, true,  ""));
        all.add(make(15L, "트랙터 T300","트랙터",   3, true,  ""));
        all.add(make(16L, "콜바인 CX-5","콤바인",   2, true,  ""));
        all.add(make(17L, "파종기",     "파종",     4, true,  ""));
        all.add(make(18L, "집초기",     "수확",     2, false, ""));
        all.add(make(19L, "곡물 건조기","건조",     1, true,  ""));
        all.add(make(20L, "집하기",     "수확",     2, true,  ""));
        all.add(make(21L, "이식기",     "이앙기",   2, true,  ""));
        all.add(make(22L, "포크레인",   "중장비",   1, false, ""));
        all.add(make(23L, "굴삭기",     "중장비",   1, true,  ""));
        all.add(make(24L, "트레일러",   "운반",     3, true,  ""));
        all.add(make(25L, "구형 트랙터","트랙터",   2, false, ""));
        all.add(make(26L, "관리기",     "관리",     4, true,  ""));
        all.add(make(27L, "탈곡기",     "수확",     2, true,  ""));
        all.add(make(28L, "로더 버킷",  "부속",     5, true,  ""));
        all.add(make(29L, "제초기",     "예초기",   3, true,  ""));
        all.add(make(30L, "광폭 트랙터","트랙터",   1, false, ""));
        all.add(make(31L, "소형 트랙터","트랙터",   2, true,  ""));
        all.add(make(32L, "비닐멀칭기", "피복",     3, true,  ""));
        all.add(make(33L, "배토기",     "토양",     2, true,  ""));
        all.add(make(34L, "로타베이터","토양",     2, true,  ""));
        all.add(make(35L, "방제 드론",  "방제",     1, true,  ""));
        all.add(make(36L, "진동 수확기","수확",     1, false, ""));
        all.add(make(37L, "작물 선별기","선별",     2, true,  ""));
        all.add(make(38L, "소형 콤바인","콤바인",   1, true,  ""));
        all.add(make(39L, "지게차",     "운반",     1, false, ""));
        all.add(make(40L, "전동 카트",  "운반",     3, true,  ""));

        // 1) 검색/필터 먼저 적용
        String query = q == null ? "" : q.trim().toLowerCase(Locale.ROOT);
        boolean onlyAvailable = "available".equalsIgnoreCase(filter);

        List<Map<String, Object>> filtered = all.stream()
                .filter(m -> {
                    if (onlyAvailable) {
                        Object r = m.get("rentable");
                        if (!(r instanceof Boolean) || !((Boolean) r)) return false;
                    }
                    if (query.isEmpty()) return true;
                    String name = String.valueOf(m.getOrDefault("name","")).toLowerCase(Locale.ROOT);
                    String cat  = String.valueOf(m.getOrDefault("category","")).toLowerCase(Locale.ROOT);
                    return name.contains(query) || cat.contains(query);
                })
                // 보기 좋게 이름순(또는 id순) 정렬
                .sorted(Comparator.comparing(m -> String.valueOf(m.get("name"))))
                .collect(Collectors.toList());

        // 2) 그 결과에 대해 페이징
        final int pageSize = 12;
        int totalItems  = filtered.size();
        int totalPages  = Math.max(1, (int) Math.ceil(totalItems / (double) pageSize));

        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        int from = (page - 1) * pageSize;
        int to   = Math.min(from + pageSize, totalItems);
        List<Map<String, Object>> pageItems = filtered.subList(from, to);

        // 3) 모델
        model.addAttribute("pageTitle", "자산 관리");
        model.addAttribute("contentPage", "/WEB-INF/views/resource/list.jsp");
        model.addAttribute("activePage", "asset");

        model.addAttribute("items", pageItems);

        Map<String, Object> pageInfo = new HashMap<>();
        pageInfo.put("number", page);
        pageInfo.put("totalPages", totalPages);
        model.addAttribute("page", pageInfo);

        log.info("resource/list q='{}' filter='{}' page={}/{} -> {} items",
                query, filter, page, totalPages, pageItems.size());

        return "layout/user/main";
    }

    private Map<String, Object> make(Long id, String name, String category,
                                     int stock, boolean rentable, String imageUrl) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", id);
        m.put("name", name);
        m.put("category", category);
        m.put("stock", stock);
        m.put("rentable", rentable);
        m.put("imageUrl", imageUrl);
        return m;
    }
}
