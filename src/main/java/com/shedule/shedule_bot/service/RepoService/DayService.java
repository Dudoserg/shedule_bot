package com.shedule.shedule_bot.service.RepoService;

import com.shedule.shedule_bot.entity.Db.Shedule.Day;

public interface DayService {
    Day getByDayName(String dayName);
}
