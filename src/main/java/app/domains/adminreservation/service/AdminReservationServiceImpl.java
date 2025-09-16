// 작성자 : 김민호
package app.domains.adminreservation.service;

import app.domains.adminreservation.dao.AdminReservationRepository;
import app.domains.adminreservation.model.AdminReservationListDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminReservationServiceImpl implements AdminReservationService {
    private final AdminReservationRepository repository;
    @Override
    public int countAll(String category, String status, String startDate) {
        return repository.countAll(category, status, startDate);
    }

    @Override
    public List<AdminReservationListDto> getPage(int page, int size, String category, String status, String startDate) {
        int startRow = (page - 1) * size + 1;
        int endRow   = page * size;
        return repository.findAllPaged(startRow, endRow, category, status, startDate);
    }

    @Override
    public boolean approve(long reservationId) {
        return repository.approve(reservationId) == 1;
    }

    @Override
    public boolean reject(long reservationId, String reason) {
        return repository.reject(reservationId, reason) == 1;
    }

    @Override
    public boolean complete(long reservationId) {
        return repository.complete(reservationId) == 1;
    }

    @Override
    public String getRejectReason(long reservationId) {
        String reason = repository.getRejectReason(reservationId);
        return reason == null ? "" : reason;
    }
}
