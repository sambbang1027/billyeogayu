package app.domains.adminreservation.dao;

import app.domains.adminreservation.model.AdminReservationListDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminReservationRepository {
    int countAll(@Param("category") String category,
                 @Param("status") String status,
                 @Param("startDate") String startDate);
    List<AdminReservationListDto> findAllPaged(@Param("startRow") int startRow,
                                               @Param("endRow") int endRow,
                                               @Param("category") String category,
                                               @Param("status") String status,
                                               @Param("startDate") String startDate);
    int approve(@Param("id") long reservationId);
    int reject(@Param("id") long reservationId,
               @Param("reason") String reason);
    int complete(@Param("id") long reservationId);
    String getRejectReason(@Param("id") long reservationId);
}
