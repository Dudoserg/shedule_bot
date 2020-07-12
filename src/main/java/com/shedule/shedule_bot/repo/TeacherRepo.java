package com.shedule.shedule_bot.repo;

import com.shedule.shedule_bot.entity.Teacher;
import com.shedule.shedule_bot.entity.TeacherRang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherRepo extends JpaRepository<Teacher, Long> {
    Teacher findAllByNameAndTeacherRang(String name, TeacherRang teacherRang);
}
