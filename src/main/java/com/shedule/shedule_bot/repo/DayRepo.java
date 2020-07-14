package com.shedule.shedule_bot.repo;

import com.shedule.shedule_bot.entity.Db.Shedule.Day;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayRepo extends JpaRepository<Day, Long> {
    Day findByDayNameEquals(String dayName);
    Day findByDayOfWeekEquals(Integer dayOfWeek);

}
