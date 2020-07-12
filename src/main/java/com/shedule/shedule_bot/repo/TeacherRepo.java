package com.shedule.shedule_bot.repo;

import com.shedule.shedule_bot.entity.Db.Teacher;
import com.shedule.shedule_bot.entity.Db.TeacherRang;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepo extends JpaRepository<Teacher, Long> {
    Teacher findAllByNameAndTeacherRang(String name, TeacherRang teacherRang);
}
