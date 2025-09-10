package app.domains.reservation.dao;

import app.domains.asset.model.Asset;
import app.domains.reservation.model.BlockedRange;
import app.domains.reservation.model.Reservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ReservationRepository {

    Asset findAssetById(@Param("assetId") Long assetId);

    List<BlockedRange> findBlockedRanges(@Param("assetId") Long assetId,
                                         @Param("from") Date from,
                                         @Param("to") Date to);

    int countOverlap(@Param("assetId") Long assetId,
                     @Param("startAt") Date startAt,
                     @Param("endAt") Date endAt);

    int insertReservation(Reservation reservation);
}