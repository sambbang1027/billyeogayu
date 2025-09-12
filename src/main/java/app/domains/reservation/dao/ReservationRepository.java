package app.domains.reservation.dao;

import app.domains.reservation.model.BlockedRange;
import app.domains.reservation.model.Reservation;
import app.domains.resource.model.Resource;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ReservationRepository {

    Resource findAssetById(@Param("assetId") Long assetId);

    List<BlockedRange> findBlockedRanges(@Param("assetId") Long assetId,
                                         @Param("from") Date from,
                                         @Param("to") Date to);

    int countOverlap(@Param("assetId") Long assetId,
                     @Param("startAt") Date startAt,
                     @Param("endAt") Date endAt);

    /**
     * 시간별 겹침 체크 (동일한 name, category, company를 가진 모든 자산 대상)
     */
    int countOverlapByTime(@Param("name") String name,
                          @Param("category") String category,
                          @Param("company") String company,
                          @Param("startAt") Date startAt,
                          @Param("endAt") Date endAt);

    /**
     * 특정 name, category, company 조합의 보유대수 계산
     */
    int countAssetStock(@Param("name") String name,
                       @Param("category") String category,
                       @Param("company") String company);

    /**
     * 전체 기간의 예약 데이터를 한 번에 조회 (성능 최적화)
     */
    List<Reservation> findReservationsByAssetGroup(@Param("name") String name,
                                                  @Param("category") String category,
                                                  @Param("company") String company,
                                                  @Param("from") Date from,
                                                  @Param("to") Date to);

    int insertReservation(Reservation reservation);
}
