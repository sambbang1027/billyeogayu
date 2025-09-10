package app.domains.alarm.dao;

import app.domains.alarm.model.Alarm;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AlarmRepository {
    
    // XML에서 정의된 메서드들
    Alarm findById(Long id);
    List<Alarm> findAll();
    void insert(Alarm alarm);

}