// 작성자 : 이해든, 황요한
package app.domains.reservation.dao;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import app.domains.reservation.model.BlockedRange;
import app.domains.reservation.model.Reservation;
import app.domains.resource.model.Resource;

@Mapper
public interface ReservationRepository {

    /**
     * 자산 조회
     */
    Resource findAssetById(@Param("assetId") Long assetId);

    /**
     * 오라클 호환 자산 그룹 잠금: 사용 가능한 자산들을 잠금과 함께 조회
     * @param name 자산명
     * @param category 카테고리
     * @param company 제조사
     * @return 잠금된 자산 목록
     */
    List<Resource> lockAssetGroup(@Param("name") String name, 
                                 @Param("category") String category, 
                                 @Param("company") String company);

    /**
     * 잠금된 자산 개수 확인
     * @param name 자산명
     * @param category 카테고리
     * @param company 제조사
     * @return 잠금된 자산 수량
     */
    int countLockedAssets(@Param("name") String name, 
                         @Param("category") String category, 
                         @Param("company") String company);

    /**
     * 예약된 날짜 범위 조회 (기존 호환성 유지)
     */
    List<BlockedRange> findBlockedRanges(@Param("assetId") Long assetId, 
                                        @Param("from") LocalDateTime from, 
                                        @Param("to") LocalDateTime to);

    /**
     * 기존 겹치는 예약 개수 확인 (기존 호환성 유지)
     */
    int countOverlap(@Param("assetId") Long assetId, 
                    @Param("startAt") LocalDateTime startAt, 
                    @Param("endAt") LocalDateTime endAt);

    /**
     * 시간별 겹침 체크 (동일한 name, category, company를 가진 모든 자산 대상)
     */
    int countOverlapByTime(@Param("name") String name, 
                          @Param("category") String category, 
                          @Param("company") String company, 
                          @Param("startAt") LocalDateTime startAt, 
                          @Param("endAt") LocalDateTime endAt);

    /**
     * 보유대수 계산 (modelName, category, company가 같은 자산의 개수) - 사용 가능한 자산만 카운트
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
                                                   @Param("from") LocalDateTime from, 
                                                   @Param("to") LocalDateTime to);

    /**
     * 예약 생성
     */
    int insertReservation(Reservation reservation);
}