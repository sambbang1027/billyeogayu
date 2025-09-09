package app.domains.alarm.service;

import app.domains.alarm.dao.AlarmRepository;
import app.domains.alarm.dto.AlarmResponseDto;
import app.domains.alarm.model.Alarm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlarmServiceImpl implements AlarmService {
    
    @Autowired
    private AlarmRepository alarmRepository;
    
    @Override
    public List<AlarmResponseDto> findAll() {
        List<Alarm> alarms = alarmRepository.findAll();
        return alarms.stream()
                .map(AlarmResponseDto::from)
                .collect(Collectors.toList());
    }
}