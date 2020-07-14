package com.shedule.shedule_bot.service.RepoService;

import com.shedule.shedule_bot.entity.Db.Shedule.SubjectType;

public interface SubjectTypeService {
    SubjectType getByName(String name);
}
