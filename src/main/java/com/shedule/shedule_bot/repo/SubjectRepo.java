package com.shedule.shedule_bot.repo;

import com.shedule.shedule_bot.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepo extends JpaRepository<Subject, Long> {
    Subject findBySubjectNameEquals(String subjectName);
}
