package com.shedule.shedule_bot.service;

import com.shedule.shedule_bot.entity.TeacherRang;
import com.shedule.shedule_bot.repo.TeacherRangRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherRangService {
    @Autowired
    TeacherRangRepo teacherRangRepo;


    public TeacherRang getByRangName(String rangName){
         TeacherRang teacherRang = teacherRangRepo.findAllByRangNameEquals(rangName);
        if(teacherRang == null){
            teacherRang = new TeacherRang(rangName);
            teacherRang = teacherRangRepo.save(teacherRang);
        }
        return teacherRang;
    }
}
