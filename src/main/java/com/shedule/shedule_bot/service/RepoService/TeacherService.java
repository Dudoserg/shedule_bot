package com.shedule.shedule_bot.service.RepoService;

import com.shedule.shedule_bot.entity.Db.Teacher;
import com.shedule.shedule_bot.entity.Db.TeacherRang;

public interface TeacherService {
    Teacher getTeacherByName(String name, TeacherRang teacherRang);
}
