package app.domains.adminreservation.service;

import app.domains.adminreservation.model.AdminReservation;

import java.util.List;

public interface AdminReservationService {

    int countAll(String category, String status, String startDate);
    List<AdminReservation> getPage(int page, int size, String category, String status, String startDate);
    boolean approve(long reservationId);
    boolean reject(long reservationId, String reason);
    boolean complete(long reservationId);
    String getRejectReason(long reservationId);

}
