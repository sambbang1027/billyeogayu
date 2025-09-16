// 작성자 : 황요한, 이원석
package app.domains.alarm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import app.domains.alarm.dto.AlarmResponse;
import app.domains.alarm.service.AlarmService;

@Controller
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @GetMapping("/alarms")
    @ResponseBody
    public List<AlarmResponse> getAlarms() {
        return alarmService.findAll();
    }

    @PutMapping("/alarms/{id}/read")
    @ResponseBody
    public void markAsRead(@PathVariable("id") Long id) {
        alarmService.markAsRead(id);
    }

    @GetMapping("/alarms/unread-count")
    @ResponseBody
    public int getUnreadCount() {
        return alarmService.getUnreadAlarmCount();
    }
}