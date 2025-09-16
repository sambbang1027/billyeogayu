// 작성자 : 이해든, 황요한
package app.domains.reservation.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import app.domains.reservation.dao.ReservationRepository;
import app.domains.reservation.model.BlockedRange;
import app.domains.reservation.model.Reservation;
import app.domains.reservation.model.TimeSlotAvailability;
import app.domains.resource.model.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository repo;

    @Override
    public Resource getAsset(Long assetId) {
        return repo.findAssetById(assetId);
    }

    @Override
    public List<BlockedRange> getBlockedRanges(Long assetId, LocalDateTime from, LocalDateTime to) {
        return repo.findBlockedRanges(assetId, from, to);
    }

    @Override
    public List<TimeSlotAvailability> getTimeSlotAvailability(Long assetId, LocalDateTime from, LocalDateTime to) {
        log.info("=== getTimeSlotAvailability 시작 ===");
        log.info("파라미터 - assetId: {}, from: {}, to: {}", assetId, from, to);
        
        // 해당 자산의 정보 조회
        Resource asset = repo.findAssetById(assetId);
        if (asset == null) {
            log.warn("자산을 찾을 수 없습니다. assetId: {}", assetId);
            return new ArrayList<>();
        }
        
        log.info("자산 정보 조회 성공 - name: {}, category: {}, company: {}", 
                asset.getModelName(), asset.getCategory(), asset.getCompany());
        
        // 동일한 name, category, company를 가진 자산들의 총 수량 조회 (사용 가능한 자산만)
        int totalStock = repo.countAssetStock(asset.getModelName(), asset.getCategory(), asset.getCompany());
        log.info("총 사용 가능한 자산 수: {}", totalStock);
        
        if (totalStock == 0) {
            log.warn("사용 가능한 자산이 없습니다. name: {}, category: {}, company: {}", 
                    asset.getModelName(), asset.getCategory(), asset.getCompany());
            return new ArrayList<>();
        }
        
        // 핵심 최적화: 전체 기간의 예약 데이터를 한 번에 조회
        List<Reservation> allReservations = repo.findReservationsByAssetGroup(
            asset.getModelName(), asset.getCategory(), asset.getCompany(), from, to);
        
        log.info("전체 예약 데이터 조회 완료 - 예약 건수: {}", allReservations.size());
        
        // 시간대별 예약 카운트 맵 생성 (메모리에서 처리)
        Map<String, Integer> reservationCountMap = new HashMap<>();
        for (Reservation reservation : allReservations) {
            List<String> overlappingSlots = getOverlappingTimeSlots(reservation.getStartAt(), reservation.getEndAt());
            for (String slotKey : overlappingSlots) {
                reservationCountMap.merge(slotKey, 1, Integer::sum);
            }
        }
        
        List<TimeSlotAvailability> timeSlots = new ArrayList<>();
        
        // 30분 단위로 시간대를 생성 (9:00 ~ 18:00, 점심시간 제외)
        LocalDateTime current = from.toLocalDate().atTime(0, 0); // 시작일 00:00부터
        LocalDateTime end = to.toLocalDate().atTime(23, 59, 59); // 종료일 23:59:59까지
        
        log.info("날짜 범위 설정 - 시작: {}, 반납: {}", current, end);
        
        while (!current.isAfter(end)) {
            // 주말 제외
            if (current.getDayOfWeek().getValue() == 6 || current.getDayOfWeek().getValue() == 7) {
                log.debug("{} 주말이므로 건너뜀", current.toLocalDate());
                current = current.plusDays(1).toLocalDate().atTime(0, 0);
                continue;
            }
            
            log.debug("=== {} 날짜 처리 시작 ===", current.toLocalDate());
            
            // 하루 동안의 30분 단위 슬롯 생성
            for (int hour = 9; hour <= 18; hour++) {
                for (int minute : new int[]{0, 30}) {
                    // 18:30은 제외
                    if (hour == 18 && minute == 30) break;
                    
                    // 점심시간 제외 (12:00, 12:30, 13:00)
                    if ((hour == 12) || (hour == 13 && minute == 0)) {
                        continue;
                    }
                    
                    LocalDateTime slotStart = current.toLocalDate().atTime(hour, minute);
                    LocalDateTime slotEnd = slotStart.plusMinutes(30);
                    
                    // 메모리에서 예약 수 확인 (DB 쿼리 없음)
                    String slotKey = createSlotKey(slotStart, slotEnd);
                    int reservedCount = reservationCountMap.getOrDefault(slotKey, 0);
                    
                    int availableCount = Math.max(0, totalStock - reservedCount);
                    
                    log.debug("시간대 {}:{} - 총:{}, 예약:{}, 가용:{}", 
                             hour, (minute == 0 ? "00" : "30"), totalStock, reservedCount, availableCount);
                    
                    TimeSlotAvailability slot = TimeSlotAvailability.builder()
                            .startTime(slotStart)
                            .endTime(slotEnd)
                            .availableCount(availableCount)
                            .totalCount(totalStock)
                            .build();
                    
                    timeSlots.add(slot);
                }
            }
            
            // 다음 날로 이동
            current = current.plusDays(1).toLocalDate().atTime(0, 0);
        }
        
        log.info("시간대별 예약 가능성 조회 완료. 총 {} 개 슬롯 생성", timeSlots.size());
        
        // 처음 몇 개 슬롯 상세 로그
        if (!timeSlots.isEmpty()) {
            log.info("=== 생성된 슬롯 샘플 (처음 5개) ===");
            for (int i = 0; i < Math.min(5, timeSlots.size()); i++) {
                TimeSlotAvailability slot = timeSlots.get(i);
                log.info("슬롯 {}: {} ~ {}, 가용: {}/{}", 
                        i + 1, slot.getStartTime(), slot.getEndTime(), 
                        slot.getAvailableCount(), slot.getTotalCount());
            }
        }
        
        return timeSlots;
    }

    // 예약이 겹치는 시간 슬롯들의 키 목록 반환 (LocalDateTime 버전)
    private List<String> getOverlappingTimeSlots(LocalDateTime reservationStart, LocalDateTime reservationEnd) {
        List<String> overlappingSlots = new ArrayList<>();
        
        LocalDateTime current = reservationStart;
        
        // 30분 단위로 겹치는 슬롯 찾기
        while (!current.isAfter(reservationEnd)) {
            int hour = current.getHour();
            int minute = current.getMinute();
            
            // 업무시간 내의 슬롯만 처리
            if (hour >= 9 && hour <= 18 && !(hour == 12 || (hour == 13 && minute == 0))) {
                LocalDateTime slotStart = current.toLocalDate().atTime(hour, minute < 30 ? 0 : 30);
                LocalDateTime slotEnd = slotStart.plusMinutes(30);
                
                // 예약 시간과 슬롯이 겹치는지 확인
                if (isTimeOverlap(reservationStart, reservationEnd, slotStart, slotEnd)) {
                    String slotKey = createSlotKey(slotStart, slotEnd);
                    overlappingSlots.add(slotKey);
                }
            }
            
            current = current.plusMinutes(30);
        }
        
        return overlappingSlots;
    }

    // 시간 겹침 확인 (LocalDateTime 버전)
    private boolean isTimeOverlap(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }

    // 슬롯 키 생성 (LocalDateTime 버전)
    private String createSlotKey(LocalDateTime startTime, LocalDateTime endTime) {
        return startTime.toLocalDate() + "-" + 
               String.format("%02d:%02d", startTime.getHour(), startTime.getMinute());
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean apply(Long assetId, Long userId,
                        LocalDateTime startAt, LocalDateTime endAt,
                        String purpose,
                        String useZipcode, String useAddr1, String useAddr2) {

        log.info("=== 예약 신청 시작 ===");
        log.info("파라미터 - assetId: {}, userId: {}, 시간: {} ~ {}", assetId, userId, startAt, endAt);

        try {
            // 기본 유효성 검증
            if (assetId == null || userId == null) {
                log.warn("필수 파라미터 누락: assetId={}, userId={}", assetId, userId);
                return false;
            }
            
            if (startAt == null || endAt == null || !startAt.isBefore(endAt)) {
                log.warn("잘못된 날짜 범위: startAt={}, endAt={}", startAt, endAt);
                return false;
            }
            
            if (purpose == null || purpose.trim().isEmpty() || useZipcode == null || useZipcode.trim().isEmpty()) {
                log.warn("필수 입력값 누락: purpose 또는 zipcode가 비어있음");
                return false;
            }
            
            if (useAddr1 == null || useAddr1.trim().isEmpty()) {
                log.warn("주소 정보 누락: addr1이 비어있음");
                return false;
            }
            
            if (useAddr2 == null) {
                useAddr2 = "";
            }

            // PURPOSE 길이 체크 (DB는 100 BYTE 제한)
            if (purpose.trim().length() > 100) {
                log.warn("목적 텍스트가 너무 깁니다: {} 글자", purpose.trim().length());
                return false;
            }

            // 해당 자산 정보 조회
            Resource asset = repo.findAssetById(assetId);
            if (asset == null) {
                log.warn("자산을 찾을 수 없습니다: assetId={}", assetId);
                return false;
            }

            log.info("자산 정보 - name: {}, category: {}, company: {}", 
                    asset.getModelName(), asset.getCategory(), asset.getCompany());

            // ===== 오라클 호환 락 적용 =====
            // 1단계: 사용 가능한 자산들을 잠금과 함께 조회
            log.info("비관적 락 획득 시도...");
            List<Resource> lockedAssets = repo.lockAssetGroup(asset.getModelName(), asset.getCategory(), asset.getCompany());
            
            if (lockedAssets == null || lockedAssets.isEmpty()) {
                log.warn("잠금할 수 있는 자산이 없습니다.");
                return false;
            }
            
            log.info("비관적 락 획득 완료 - 잠금된 자산 수: {}", lockedAssets.size());

            // 2단계: 잠금된 자산 개수 확인 (실제 사용 가능한 재고)
            int totalStock = repo.countLockedAssets(asset.getModelName(), asset.getCategory(), asset.getCompany());
            int reservedCount = repo.countOverlapByTime(asset.getModelName(), asset.getCategory(), 
                                                       asset.getCompany(), startAt, endAt);
            
            log.info("락 획득 후 검증 - 자산: {}, 총 재고: {}, 예약된 수: {}, 사용 가능: {}", 
                    asset.getModelName(), totalStock, reservedCount, (totalStock - reservedCount));
            
            if (reservedCount >= totalStock) {
                log.warn("예약 가능한 자산이 없습니다. 총 재고: {}, 예약된 수: {}", totalStock, reservedCount);
                return false;
            }

            // === 주소 파생값 생성 ===
            String[] parsed = parseKoreanAddress(useAddr1);
            String addressState = parsed[0]; // 시/도
            String addressCity = parsed[1]; // 시/군/구(또는 시+구)
            String addressFull = buildFullAddress(useZipcode, useAddr1, useAddr2);

            log.debug("주소 파싱 결과 - 시/도: {}, 시/군/구: {}, 전체주소: {}", 
                     addressState, addressCity, addressFull);

            // 예약 객체 생성
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

            // DB에 예약 삽입 (락이 보장된 상태에서 실행)
            log.info("예약 데이터 삽입 시도...");
            int insertResult = repo.insertReservation(r);
            boolean success = insertResult == 1;
            
            if (success) {
                log.info("예약 신청 성공 - userId: {}, assetId: {}, 기간: {} ~ {}", 
                        userId, assetId, startAt, endAt);
            } else {
                log.error("예약 신청 실패 - DB 삽입 실패. userId: {}, assetId: {}", userId, assetId);
            }
            
            return success;

        } catch (Exception e) {
            log.error("예약 신청 중 예외 발생", e);
            throw e; // 트랜잭션 롤백을 위해 예외 재발생
        }
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