package app.domains.reservation.service;

import app.domains.reservation.model.BlockedRange;
import app.domains.resource.model.Resource;

import java.util.Date;
import java.util.List;

public interface ReservationService {

    Resource getAsset(Long assetId);

    List<BlockedRange> getBlockedRanges(Long assetId, Date from, Date to);

    /** 겹침 재검증 후 생성(원자적). true: 성공, false: 겹침/유효성 실패 */
    boolean apply(Long assetId, Long userId,
                  Date startAt, Date endAt,
                  String purpose,
                  String useZipcode, String useAddr1, String useAddr2);
}