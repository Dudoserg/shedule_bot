package com.shedule.shedule_bot.service;

import com.shedule.shedule_bot.entity.Day;
import com.shedule.shedule_bot.repo.DayRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DayService {
    @Autowired
    DayRepo dayRepo;

    public Day findByDayNameEquals(String dayName){
        Day day = dayRepo.findByDayNameEquals(dayName);;
        if(day == null){
            day = new Day(dayName);
            day = dayRepo.save(day);
        }
        return day;
    };
    Day findByDayOfWeekEquals(Integer dayOfWeek){
        return dayRepo.findByDayOfWeekEquals(dayOfWeek);
    }
}
