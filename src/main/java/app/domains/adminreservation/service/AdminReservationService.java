package app.domains.adminreservation.service;

import app.domains.adminreservation.model.AdminReservationListDto;

import java.util.List;

public interface AdminReservationService {

    int countAll(String category, String status, String startDate);
    List<AdminReservationListDto> getPage(int page, int size, String category, String status, String startDate);
    boolean approve(long reservationId);
    boolean reject(long reservationId, String reason);
    boolean complete(long reservationId);
    String getRejectReason(long reservationId);

}
