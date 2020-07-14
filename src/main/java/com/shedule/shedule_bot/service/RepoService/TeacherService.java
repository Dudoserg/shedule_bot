package com.shedule.shedule_bot.service.RepoService;

import com.shedule.shedule_bot.entity.Db.Shedule.Teacher;
import com.shedule.shedule_bot.entity.Db.Shedule.TeacherRang;

public interface TeacherService {
    Teacher getTeacherByName(String name, TeacherRang teacherRang);
}
