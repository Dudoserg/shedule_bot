package com.shedule.shedule_bot.service.RepoService;

import com.shedule.shedule_bot.entity.Db.SubjectType;

public interface SubjectTypeService {
    SubjectType getByName(String name);
}
