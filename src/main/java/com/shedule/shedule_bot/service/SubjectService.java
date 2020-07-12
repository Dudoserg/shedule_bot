package com.shedule.shedule_bot.service;


import com.shedule.shedule_bot.entity.Subject;
import com.shedule.shedule_bot.repo.SubjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectService {
    @Autowired
    SubjectRepo subjectRepo;

    public Subject findByName(String subjectName){
        Subject subject = subjectRepo.findBySubjectNameEquals(subjectName);
        if(subject == null){
            subject = new Subject(subjectName);
            subject = subjectRepo.save(subject);
        }
        return subject;
    }
}
