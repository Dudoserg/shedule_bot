package com.shedule.shedule_bot.service;

import com.shedule.shedule_bot.entity.Db.SubjectType;
import com.shedule.shedule_bot.repo.SubjectTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectTypeService {
    @Autowired
    SubjectTypeRepo subjectTypeRepo;

    public SubjectType getByName(String name) {
        SubjectType subjectType = subjectTypeRepo.findByNameEquals(name);
        if (subjectType == null) {
            subjectType = new SubjectType(name);
            subjectType = subjectTypeRepo.save(subjectType);
        }
        return subjectType;
    }
}
