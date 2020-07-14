package com.shedule.shedule_bot.repo;

import com.shedule.shedule_bot.entity.Db.Shedule.TeacherRang;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRangRepo extends JpaRepository<TeacherRang, Long> {
    TeacherRang findAllByRangNameEquals(String rangName);
}
