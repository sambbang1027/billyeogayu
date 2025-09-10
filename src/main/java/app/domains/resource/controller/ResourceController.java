package app.domains.resource.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import app.domains.resource.model.Resource;
import app.domains.resource.service.ResourceService;
import app.domains.users.model.Users;
import app.domains.users.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ResourceController {

    private final ResourceService service;
    private final UsersService usersService;

    @GetMapping("/resource/list")
    public String listAssets(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "page", defaultValue = "1") int page,
            HttpServletRequest request,
            Model model) {

        try {
            // 1) 세션에서 SecurityContext 수동 복원
            HttpSession session = request.getSession(false);
            if (session != null) {
                Object storedContext = session.getAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
                if (storedContext instanceof SecurityContext) {
                    SecurityContextHolder.setContext((SecurityContext) storedContext);
                    log.info("세션에서 SecurityContext 복원 완료 - 세션ID: {}", session.getId());
                }
            }

            // 2) 로그인 사용자 정보 세션에서 가져오기
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isLoggedIn = false;
            Users loginUser = null;

            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
                try {
                    String loginId = auth.getName();
                    loginUser = usersService.getUserByLoginId(loginId);
                    if (loginUser != null) {
                        isLoggedIn = true;
                        log.info("로그인된 사용자: {} (ID: {})", loginUser.getName(), loginUser.getUserId());
                    }
                } catch (Exception e) {
                    log.warn("사용자 정보 조회 실패: {}", e.getMessage());
                }
            } else {
                log.info("비로그인 사용자 또는 익명 사용자");
            }

            // 3) 한 페이지 카드 개수(그리드 4x3 기준)
            final int pageSize = 12;

            // 파라미터 정리
            if (q != null && q.trim().isEmpty()) {
				q = null;
			}
            if (filter != null && filter.trim().isEmpty()) {
				filter = null;
			}
            if (page < 1) {
				page = 1;
			}

            // 4) 전체 개수 / 총 페이지
            int total = service.getAssetCount(q, filter);
            int totalPages = Math.max(1, (int) Math.ceil((double) total / pageSize));

            // 5) 현재 페이지 보정
            page = Math.min(Math.max(page, 1), totalPages);

            // 6) 목록 조회
            List<Resource> items = service.getAssets(q, filter, page, pageSize);

            // 7) 페이지 블록(버튼 20개 단위)
            final int blockSize = 20;
            int startPage = ((page - 1) / blockSize) * blockSize + 1;
            int endPage = Math.min(startPage + blockSize - 1, totalPages);
            boolean hasPrevBlock = startPage > 1;
            boolean hasNextBlock = endPage < totalPages;

            // 8) 화살표용 이전/다음 페이지
            int prevPage = Math.max(1, page - 1);
            int nextPage = Math.min(totalPages, page + 1);

            // 9) 모델에 데이터 추가
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

            // 10) 로그인 정보 추가
            model.addAttribute("isLoggedIn", isLoggedIn);
            if (loginUser != null) {
                model.addAttribute("loginUser", loginUser);
            }

            return "resource"; // /WEB-INF/views/list.jsp

        } catch (Exception e) {
            // 오류 정보를 모델에 추가
            model.addAttribute("error", "데이터를 불러오는 중 오류가 발생했습니다: " + e.getMessage());
            model.addAttribute("q", q);
            model.addAttribute("filter", filter);
            model.addAttribute("page", 1);
            model.addAttribute("totalPages", 1);
            model.addAttribute("isLoggedIn", false);

            log.error("자원 목록 조회 중 오류 발생", e);
            return "resource"; // 오류가 있어도 일단 페이지는 보여줌
        }
    }
}