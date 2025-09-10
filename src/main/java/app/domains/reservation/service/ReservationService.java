package app.domains.reservation.service;

import app.domains.asset.model.Asset;
import app.domains.reservation.model.BlockedRange;

import java.util.Date;
import java.util.List;

public interface ReservationService {

    Asset getAsset(Long assetId);

    List<BlockedRange> getBlockedRanges(Long assetId, Date from, Date to);

    /** 겹침 재검증 후 생성(원자적). true: 성공, false: 겹침/유효성 실패 */
    boolean apply(Long assetId, Long userId,
                  Date startAt, Date endAt,
                  String purpose,
                  String useZipcode, String useAddr1, String useAddr2);
}