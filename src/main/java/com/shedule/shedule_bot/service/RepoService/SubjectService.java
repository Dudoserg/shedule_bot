package com.shedule.shedule_bot.service.RepoService;

import com.shedule.shedule_bot.entity.Db.Shedule.Subject;

public interface SubjectService {
    Subject findByName(String subjectName);
}
