package com.shedule.shedule_bot.repo;

import com.shedule.shedule_bot.entity.TimeSubject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeSubjectRepo extends JpaRepository<TimeSubject, Long> {
    TimeSubject findAllByTimeStartEqualsAndTimeEndEquals(Integer timeStart, Integer timeEnd);

    List<TimeSubject> findAll();
}
