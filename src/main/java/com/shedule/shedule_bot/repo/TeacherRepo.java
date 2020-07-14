package com.shedule.shedule_bot.repo;

import com.shedule.shedule_bot.entity.Db.Shedule.Teacher;
import com.shedule.shedule_bot.entity.Db.Shedule.TeacherRang;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepo extends JpaRepository<Teacher, Long> {
    Teacher findAllByNameAndTeacherRang(String name, TeacherRang teacherRang);
}
