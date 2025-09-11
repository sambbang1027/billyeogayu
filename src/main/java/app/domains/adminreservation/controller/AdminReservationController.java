package app.domains.adminreservation.controller;

import app.domains.adminreservation.model.AdminReservation;
import app.domains.adminreservation.model.AdminReservationListDto;
import app.domains.adminreservation.service.AdminReservationService;
import app.domains.asset.service.AssetService;
import app.domains.users.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    private final AdminReservationService reservationService;

    @PostMapping("/list")
    @ResponseBody
    public Map<String, Object> list(@RequestParam(name = "page", defaultValue = "1") int page,
                                    @RequestParam(name = "pageSize", defaultValue = "10") int size,
                                    @RequestParam(name = "category", required = false) String category,
                                    @RequestParam(name = "status", required = false) String status,
                                    @RequestParam(name = "startDate", required = false) String startDate) {
        int total = reservationService.countAll(category, status, startDate);
        List<AdminReservationListDto> items = reservationService.getPage(page, size, category, status, startDate);

        Map<String, Object> res = new HashMap<>();
        res.put("items", items);
        res.put("page", page);
        res.put("size", size);
        res.put("totalCount", total);
        res.put("totalPages", (int)Math.ceil(total / (double)size));

        return res;
    }

    @GetMapping("/approve/{id}")
    @ResponseBody
    public Map<String, Object> approve(@PathVariable("id") long id) {
        boolean ok = reservationService.approve(id);
        return Map.of("success", ok);
    }

    @PostMapping("/reject/{id}")
    @ResponseBody
    public Map<String, Object> reject(@PathVariable("id") Long id,
                                      @RequestParam("rejectReason") String reason) {
        boolean ok = reservationService.reject(id, reason);
        return Map.of("success", ok);
    }

    @GetMapping("/complete/{id}")
    @ResponseBody
    public Map<String, Object> complete(@PathVariable("id") long id) {
        boolean ok = reservationService.complete(id);
        return Map.of("success", ok);
    }

    @GetMapping("/reject-reason/{id}")
    @ResponseBody
    public Map<String, Object> getRejectReason(@PathVariable("id") long id) {
        String reason = reservationService.getRejectReason(id);
        return Map.of("success", true, "reason", reason);
    }
}
