package app.domains.asset.controller;

import app.common.util.DownloadCSV;
import app.domains.asset.model.*;
import app.domains.asset.service.AssetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/admin/asset")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @GetMapping("/list")
    public String asset(@RequestParam(name = "assetStatus", required = false) String assetStatus,
                        @RequestParam(name = "category",     required = false) String category,
                        @RequestParam(name = "company",      required = false) String company,
                        @RequestParam(name = "location",     required = false) String location,

                        @RequestParam(name = "field",   required = false) String field,
                        @RequestParam(name = "keyword", required = false) String keyword,

                        @RequestParam(name = "page",     defaultValue = "1")  int page,
                        @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,

                        Model model) {

        String kwLike = toLikePattern(keyword);

        int totalCount = assetService.countAssets(assetStatus, category, company, location, field, kwLike);
        page     = Math.max(1, page);
        pageSize = Math.max(1, pageSize);
        int totalPages = Math.max(1, (int) Math.ceil(totalCount / (double) pageSize));
        if (page > totalPages) page = totalPages;

        int startRow = (page - 1) * pageSize + 1;
        int endRow   = page * pageSize;

        // 목록 조회
        List<AssetDto> pageList =
                assetService.findAssetsPaged(assetStatus, category, company, location, field, kwLike, startRow, endRow);

        // 필터 옵션 데이터
        var opts = assetService.loadFilterOptions();
        model.addAttribute("categories", opts.getCategories());
        model.addAttribute("companies",  opts.getCompanies());
        model.addAttribute("locations",  opts.getLocations());

        model.addAttribute("assets", pageList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("page", page);

        model.addAttribute("assetStatus", assetStatus);
        model.addAttribute("category", category);
        model.addAttribute("company", company);
        model.addAttribute("location", location);
        model.addAttribute("field", field);
        model.addAttribute("keyword", keyword);

        model.addAttribute("pageTitle", "자산 관리");
        model.addAttribute("contentPage", "/WEB-INF/views/asset/assetList.jsp");
        model.addAttribute("activePage", "asset");

        return "layout/admin/main";
    }

    @GetMapping("/detail/{id}")
    @ResponseBody
    public AssetPartsDto getAssetDetail(@PathVariable("id") Long id) {
        Asset asset = assetService.getAssetDetail(id);
        List<Part> parts = assetService.getPartsByAssetId(id);

        return new AssetPartsDto(asset, parts);
    }

    @PostMapping("/register")
    public String registerAsset(@RequestBody AssetPartsDto dto) {
        System.out.println("asset = " + dto.getAsset());
        System.out.println("parts = " + dto.getParts());

        assetService.registerAsset(dto);

        return "redirect:/admin/asset/list";
    }

    @PostMapping("/update")
    public String updateAsset(@RequestBody AssetPartsUpdateDto dto) {
        assetService.updateAsset(dto);

        return "redirect:/admin/asset";
    }

    @PostMapping("/delete/{assetId}")
    public String deleteAsset(@PathVariable("assetId") long assetId,
                              @RequestParam("deletedBy") String deletedBy) {
        assetService.deleteAsset(assetId, deletedBy);
        return "redirect:/admin/asset";
    }

    @PostMapping("/delete/part")
    public String deletePart(@RequestParam("partId") long partId) {
        assetService.deletePart(partId);
        return "redirect:/admin/asset";
    }

    @PostMapping(value = "/file-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file,
                                      HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("파일이 비어 있습니다.");
            }

            // uuid 기반 파일명 만들기
            String original = file.getOriginalFilename();
            String extension = "";
            if (original != null && original.contains(".")) {
                extension = original.substring(original.lastIndexOf("."));
            }

            String fileName = UUID.randomUUID().toString().replace("-", "") + extension;

            Path repoDir = Paths.get(projectRoot(), "src", "main", "webapp", "static", "model-image");
            Files.createDirectories(repoDir);
            Path repoDest = repoDir.resolve(fileName);

            // 런타임 경로에 저장
            String realBase = request.getServletContext().getRealPath("/static/model-image");
            Path deployDir = (realBase != null) ? Paths.get(realBase) : null;
            if (deployDir != null) Files.createDirectories(deployDir);
            Path deployDest = (deployDir != null) ? deployDir.resolve(fileName) : null;

            try (InputStream in = file.getInputStream()) {
                Files.copy(in, repoDest, StandardCopyOption.REPLACE_EXISTING);
            }
            if (deployDest != null) {
                Files.copy(repoDest, deployDest, StandardCopyOption.REPLACE_EXISTING);
            }

            // 프로젝트 경로에 저장
            String fileUrl = "/static/model-image/" + fileName;

            result.put("success", true);
            result.put("originalName", original);
            result.put("url", fileUrl);
            result.put("fileName", fileName);
            result.put("repoPath", repoDest.toString());
            if (deployDest != null) result.put("deployPath", deployDest.toString());
            return result;

        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            return result;
        }
    }

    @GetMapping("/export")
    public void exportCsv(@RequestParam(name = "assetStatus", required = false) String assetStatus,
                          @RequestParam(name = "category",     required = false) String category,
                          @RequestParam(name = "company",      required = false) String company,
                          @RequestParam(name = "location",     required = false) String location,
                          @RequestParam(name = "field",        required = false) String field,
                          @RequestParam(name = "keyword",      required = false) String keyword,
                          HttpServletResponse resp) throws Exception {

        String kwLike = toLikePattern(keyword);

        final int chunkSize = 1000;
        int totalCount = assetService.countAssets(assetStatus, category, company, location, field, kwLike);
        int totalPages = Math.max(1, (int)Math.ceil(totalCount / (double)chunkSize));

        DownloadCSV.send(resp, "자원리스트.csv", csv -> {
            csv.header("No","종류","제조사","모델명","부품","위치","점검 예정일","상태");

            int seq = 0;
            for (int page = 1; page <= totalPages; page++) {
                int startRow = (page - 1) * chunkSize + 1;
                int endRow   = page * chunkSize;

                List<AssetDto> list = assetService.findAssetsPaged(
                        assetStatus, category, company, location, field, kwLike, startRow, endRow
                );

                for (AssetDto a : list) {
                    seq++;
                    String no    = String.valueOf(seq);
                    String kind  = nz(a.getCategory());
                    String comp  = nz(a.getCompany());
                    String model = nz(a.getModelName());
                    String part  = nz(a.getUsageTime());
                    String loc   = nz(a.getLocation());
                    String exp   = fmtDate(a.getExpectedMaintenanceDate());
                    String stat  = mapStatusLabel(nz(a.getAssetStatus()));

                    csv.row(no, kind, comp, model, part, loc, exp, stat);
                }

                try {
                    csv.flush();
                } catch (Exception ignore) {}
            }
        });
    }

    private static String nz(Object o) {
        return o == null ? "" : String.valueOf(o).trim();
    }

    private static String fmtDate(Object v) {
        if (v == null) return "";
        String s = String.valueOf(v).trim();
        int t = s.indexOf('T');
        String d = (t > 0 ? s.substring(0, t) : s).replace('/', '-');
        if (d.matches("\\d{4}-\\d{2}-\\d{2}")) return d.replace('-', '.'); // yyyy.MM.dd
        return s;
    }

    private static String mapStatusLabel(String raw) {
        String up = raw == null ? "" : raw.toUpperCase(Locale.ROOT);
        return switch (up) {
            case "AVAILABLE" -> "사용 가능";
            case "USING", "MAINTENANCE_REQUIRED" -> "사용 중";
            case "MAINTAINING" -> "사용 불가";
            default -> raw;
        };
    }

    /** LIKE 안전 패턴: \, %, _ 이스케이프 + 앞뒤 % 붙임 */
    private static String toLikePattern(String raw) {
        if (raw == null) return null;
        String s = raw.trim();
        if (s.isEmpty()) return null;

        s = s.replace("\\", "\\\\")
                .replace("%",  "\\%")
                .replace("_",  "\\_");
        return "%" + s + "%";
    }

    /** 프로젝트 루트 계산: 실행 위치가 달라도 '…/src/…'를 찾을 때까지 상위로 타고 올라감 */
    private String projectRoot() {
        Path cur = Paths.get("").toAbsolutePath();
        Path p = cur;
        while (p != null) {
            if (Files.exists(p.resolve("src"))) return p.toString();
            p = p.getParent();
        }
        // 그래도 못 찾으면 현재 디렉터리 반환(로컬 IDE 실행이면 대개 루트가 맞음)
        return cur.toString();
    }
}
