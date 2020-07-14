package com.shedule.shedule_bot.service.RepoService;

import com.shedule.shedule_bot.entity.Db.Shedule.SubjectType;
import com.shedule.shedule_bot.repo.SubjectTypeRepo;
import org.springframework.stereotype.Service;

@Service
public class SubjectTypeServiceImpl implements SubjectTypeService{
    final
    SubjectTypeRepo subjectTypeRepo;

    public SubjectTypeServiceImpl(SubjectTypeRepo subjectTypeRepo) {
        this.subjectTypeRepo = subjectTypeRepo;
    }

    public SubjectType getByName(String name) {
        SubjectType subjectType = subjectTypeRepo.findByNameEquals(name);
        if (subjectType == null) {
            subjectType = new SubjectType(name);
            subjectType = subjectTypeRepo.save(subjectType);
        }
        return subjectType;
    }
}
