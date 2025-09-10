package app.domains.reservation.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.domains.reservation.dao.ReservationRepository;
import app.domains.reservation.model.BlockedRange;
import app.domains.reservation.model.Reservation;
import app.domains.resource.model.Resource;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository repo;

    @Override
    public Resource getAsset(Long assetId) {
        return repo.findAssetById(assetId);
    }

    @Override
    public List<BlockedRange> getBlockedRanges(Long assetId, Date from, Date to) {
        return repo.findBlockedRanges(assetId, from, to);
    }

    @Override
    @Transactional
    public boolean apply(Long assetId, Long userId,
                        Date startAt, Date endAt,
                        String purpose,
                        String useZipcode, String useAddr1, String useAddr2) {

        // 기본 유효성
        if (assetId == null || userId == null) {
			return false;
		}
        if (startAt == null || endAt == null || !startAt.before(endAt)) {
			return false;
		}
        if (purpose == null || purpose.trim().isEmpty() || useZipcode == null || useZipcode.trim().isEmpty()) {
			return false;
		}
        if (useAddr1 == null || useAddr1.trim().isEmpty()) {
			return false;
		}
        if (useAddr2 == null) {
			useAddr2 = "";
		}

        // PURPOSE 길이 체크 (DB는 100 BYTE 제한)
        if (purpose.trim().length() > 100) {
            return false;
        }

        // 겹침 재검증(동일 트랜잭션)
        int overlap = repo.countOverlap(assetId, startAt, endAt);
        if (overlap > 0) {
			return false;
		}

        // === 주소 파생값 생성 ===
        String[] parsed = parseKoreanAddress(useAddr1);
        String addressState = parsed[0]; // 시/도
        String addressCity = parsed[1]; // 시/군/구(또는 시+구)
        String addressFull = buildFullAddress(useZipcode, useAddr1, useAddr2);

        Reservation r = Reservation.builder()
                .assetId(assetId)
                .userId(userId)
                .startAt(startAt)
                .endAt(endAt)
                .purpose(purpose.trim())
                .address(addressFull)
                .addressState(addressState)
                .addressCity(addressCity)
                .status("PENDING")
                .build();

        return repo.insertReservation(r) == 1;
    }

    private static String buildFullAddress(String zipcode, String addr1, String addr2) {
        String base = addr1.trim() + (addr2.isBlank() ? "" : " " + addr2.trim());
        return "(" + zipcode.trim() + ") " + base;
    }

    /**
     * 개선된 한국 주소 파서:
     * - "서울특별시 강남구 ..." → ["서울특별시","강남구"]
     * - "경기도 성남시 분당구 ..." → ["경기도","성남시 분당구"]
     * - "서울 강남구 ..." → ["서울특별시","강남구"]
     * - "부산 해운대구 ..." → ["부산광역시","해운대구"]
     */
    private static String[] parseKoreanAddress(String addr1) {
        String a = addr1 == null ? "" : addr1.trim();
        if (a.isEmpty()) {
			return new String[]{"기타", "기타"};
		}

        String[] token = a.split("\\s+");
        if (token.length == 0) {
			return new String[]{"기타", "기타"};
		}

        // 시/도 정규화 및 판별
        String sido = normalizeSido(token[0]);
        if (sido.equals("기타")) {
            // 시/도를 찾지 못한 경우
            return new String[]{"기타", token.length >= 2 ? token[0] + " " + token[1] : token[0]};
        }

        // 시/군/구 처리
        String sigungu = "기타";
        if (token.length >= 2) {
            sigungu = token[1];

            // "성남시 분당구"처럼 시+구가 함께 나오는 경우 처리
            if (token.length >= 3) {
                String thirdToken = token[2];
                if (thirdToken.endsWith("구") || thirdToken.endsWith("군")) {
                    sigungu = token[1] + " " + token[2];
                }
            }
        }

        return new String[]{sido, sigungu};
    }

    /**
     * 시/도명을 정규화하는 메서드
     */
    private static String normalizeSido(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "기타";
        }

        String normalized = input.trim();

        // 특별시/광역시 처리
        switch (normalized) {
            case "서울": case "서울시":
                return "서울특별시";
            case "부산": case "부산시":
                return "부산광역시";
            case "대구": case "대구시":
                return "대구광역시";
            case "인천": case "인천시":
                return "인천광역시";
            case "광주": case "광주시":
                return "광주광역시";
            case "대전": case "대전시":
                return "대전광역시";
            case "울산": case "울산시":
                return "울산광역시";
            case "세종": case "세종시":
                return "세종특별자치시";
            default:
                // 이미 완전한 형태인지 확인
                if (normalized.endsWith("특별시") ||
                    normalized.endsWith("광역시") ||
                    normalized.endsWith("특별자치시") ||
                    normalized.endsWith("도")) {
                    return normalized;
                }

                // 도 단위 처리
                if (normalized.equals("경기")) {
					return "경기도";
				}
                if (normalized.equals("강원")) {
					return "강원도";
				}
                if (normalized.equals("충북") || normalized.equals("충청북도")) {
					return "충청북도";
				}
                if (normalized.equals("충남") || normalized.equals("충청남도")) {
					return "충청남도";
				}
                if (normalized.equals("전북") || normalized.equals("전라북도")) {
					return "전라북도";
				}
                if (normalized.equals("전남") || normalized.equals("전라남도")) {
					return "전라남도";
				}
                if (normalized.equals("경북") || normalized.equals("경상북도")) {
					return "경상북도";
				}
                if (normalized.equals("경남") || normalized.equals("경상남도")) {
					return "경상남도";
				}
                if (normalized.equals("제주")) {
					return "제주특별자치도";
				}

                return "기타";
        }
    }
}