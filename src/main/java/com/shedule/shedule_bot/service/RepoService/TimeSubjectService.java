package com.shedule.shedule_bot.service.RepoService;

import com.shedule.shedule_bot.entity.Db.Shedule.TimeSubject;

import java.util.List;

public interface TimeSubjectService {
    TimeSubject getByStartEnd(Integer timeStart, Integer timeEnd);
    List<TimeSubject> getAll();
}
