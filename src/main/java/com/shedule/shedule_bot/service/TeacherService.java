package com.shedule.shedule_bot.service;

import com.shedule.shedule_bot.entity.Teacher;
import com.shedule.shedule_bot.entity.TeacherRang;
import com.shedule.shedule_bot.repo.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {

    @Autowired
    TeacherRepo teacherRepo;


    public Teacher getTeacherByName(String name, TeacherRang teacherRang)  {
        Teacher teacher = teacherRepo.findAllByNameAndTeacherRang(name, teacherRang);
        if (teacher == null) {
            teacher = new Teacher(name, teacherRang);
            teacher = teacherRepo.save(teacher);
        }
        return teacher;

    }
}
