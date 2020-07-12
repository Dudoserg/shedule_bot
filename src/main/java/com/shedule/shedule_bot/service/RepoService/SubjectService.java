package com.shedule.shedule_bot.service.RepoService;


import com.shedule.shedule_bot.entity.Db.Subject;
import com.shedule.shedule_bot.repo.SubjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectService {
    final
    SubjectRepo subjectRepo;

    public SubjectService(SubjectRepo subjectRepo) {
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
