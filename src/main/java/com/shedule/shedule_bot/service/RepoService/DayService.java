package com.shedule.shedule_bot.service.RepoService;

import com.shedule.shedule_bot.entity.Db.Day;
import com.shedule.shedule_bot.repo.DayRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DayService {
    final
    DayRepo dayRepo;

    public DayService(DayRepo dayRepo) {
        this.dayRepo = dayRepo;
    }

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
