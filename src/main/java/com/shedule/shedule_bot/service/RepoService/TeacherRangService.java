package com.shedule.shedule_bot.service.RepoService;

import com.shedule.shedule_bot.entity.Db.TeacherRang;

public interface TeacherRangService {
    TeacherRang getByRangName(String rangName);
}
