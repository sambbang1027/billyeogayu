package app.domains.alarm.service;

import java.util.List;

import app.domains.alarm.dto.AlarmResponseDto;

public interface AlarmService {
    List<AlarmResponseDto> findAll();
    void markAsRead(Long alarmId);
    int getUnreadAlarmCount();
}