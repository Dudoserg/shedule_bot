package com.shedule.shedule_bot.repo;

import com.shedule.shedule_bot.entity.Db.Shedule.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepo extends JpaRepository<Faculty, Long> {
    Faculty findFacultyByFacultyId(String s);
    Faculty findFacultyByFacultyName(String name);
}
