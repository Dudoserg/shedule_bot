package com.shedule.shedule_bot.service.RepoService;

import com.shedule.shedule_bot.entity.Db.Shedule.Day;
import com.shedule.shedule_bot.repo.DayRepo;
import org.springframework.stereotype.Service;

@Service
public class DayServiceImpl implements DayService {
    final
    DayRepo dayRepo;

    public DayServiceImpl(DayRepo dayRepo) {
        this.dayRepo = dayRepo;
    }

    public Day getByDayName(String dayName){
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
