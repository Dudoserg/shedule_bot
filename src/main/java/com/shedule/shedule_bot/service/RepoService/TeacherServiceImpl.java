package com.shedule.shedule_bot.service.RepoService;

import com.shedule.shedule_bot.entity.Db.Teacher;
import com.shedule.shedule_bot.entity.Db.TeacherRang;
import com.shedule.shedule_bot.repo.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService{

    final
    TeacherRepo teacherRepo;

    public TeacherServiceImpl(TeacherRepo teacherRepo) {
        this.teacherRepo = teacherRepo;
    }


    public Teacher getTeacherByName(String name, TeacherRang teacherRang) {
        Teacher teacher = teacherRepo.findAllByNameAndTeacherRang(name, teacherRang);
        if (teacher == null) {
            teacher = new Teacher(name, teacherRang);
            teacher = teacherRepo.save(teacher);
        }
        return teacher;

    }
}
