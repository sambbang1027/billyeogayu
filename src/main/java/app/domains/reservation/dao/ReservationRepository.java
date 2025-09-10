package app.domains.reservation.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import app.domains.asset.model.Asset;
import app.domains.reservation.model.BlockedRange;
import app.domains.reservation.model.Reservation;

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