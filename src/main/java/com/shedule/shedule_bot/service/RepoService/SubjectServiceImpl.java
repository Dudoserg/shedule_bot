package com.shedule.shedule_bot.service.RepoService;


import com.shedule.shedule_bot.entity.Db.Shedule.Subject;
import com.shedule.shedule_bot.repo.SubjectRepo;
import org.springframework.stereotype.Service;

@Service
public class SubjectServiceImpl implements SubjectService{
    final
    SubjectRepo subjectRepo;

    public SubjectServiceImpl(SubjectRepo subjectRepo) {
        this.subjectRepo = subjectRepo;
    }

    public Subject findByName(String subjectName){
        Subject subject = subjectRepo.findBySubjectNameEquals(subjectName);
        if(subject == null){
            subject = new Subject(subjectName);
            subject = subjectRepo.save(subject);
        }
        return subject;
    }
}
