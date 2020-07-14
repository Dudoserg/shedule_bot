package com.shedule.shedule_bot.service.RepoService;

import com.shedule.shedule_bot.entity.Db.Shedule.Group;
import com.shedule.shedule_bot.entity.SheduleDay;

import java.time.LocalDate;
import java.util.List;

public interface SheduleService {
    Long getCountRow();
    SheduleDay getSheduleByDate(Group group, LocalDate localDate);
    SheduleDay getSheduleToday(Group group);
    SheduleDay getSheduleTomorrow(Group group);
    List<SheduleDay> getSheduleFirstWeek(Group groupSheduleShow) throws Exception;
    List<SheduleDay> getSheduleSecondWeek(Group groupSheduleShow) throws Exception;
}
