// 작성자 : 황요한
package app.domains.users.util;


import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 데이터 저장 (만료시간 설정)
     */
    public void setData(String key, String value, long timeout) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
            log.info("Redis 저장 성공 - key: {}, timeout: {}초", key, timeout);
        } catch (Exception e) {
            log.error("Redis 저장 실패 - key: {}, 오류: {}", key, e.getMessage());
            throw new RuntimeException("Redis 저장에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * 데이터 조회
     */
    public String getData(String key) {
        try {
            String value = redisTemplate.opsForValue().get(key);
            log.info("Redis 조회 - key: {}, value: {}", key, value != null ? "존재" : "없음");
            return value;
        } catch (Exception e) {
            log.error("Redis 조회 실패 - key: {}, 오류: {}", key, e.getMessage());
            return null;
        }
    }

    /**
     * 데이터 삭제
     */
    public void deleteData(String key) {
        try {
            redisTemplate.delete(key);
            log.info("Redis 삭제 완료 - key: {}", key);
        } catch (Exception e) {
            log.error("Redis 삭제 실패 - key: {}, 오류: {}", key, e.getMessage());
        }
    }

    /**
     * 데이터 존재 여부 확인
     */
    public boolean hasKey(String key) {
        try {
            Boolean exists = redisTemplate.hasKey(key);
            return exists != null && exists;
        } catch (Exception e) {
            log.error("Redis 존재 확인 실패 - key: {}, 오류: {}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 만료시간 조회 (초 단위)
     */
    public long getExpire(String key) {
        try {
            Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
            return expire != null ? expire : -1;
        } catch (Exception e) {
            log.error("Redis 만료시간 조회 실패 - key: {}, 오류: {}", key, e.getMessage());
            return -1;
        }
    }
}