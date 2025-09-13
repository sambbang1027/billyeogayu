package app.domains.alarm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import app.domains.alarm.model.Alarm;

@Mapper
public interface AlarmRepository {

    // XML에서 정의된 메서드들
    Alarm findById(Long id);
    List<Alarm> findAll();
    void insert(Alarm alarm);
    
    // 읽음 처리 관련 메서드들
    void markAsRead(@Param("id") Long alarmId);
    int getUnreadCount();

}