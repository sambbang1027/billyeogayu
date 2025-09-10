package app.domains.alarm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import app.domains.alarm.dto.AlarmResponseDto;
import app.domains.alarm.service.AlarmService;

@Controller
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @GetMapping("/alarms")
    @ResponseBody
    public List<AlarmResponseDto> getAlarms() {
        return alarmService.findAll();
    }
}