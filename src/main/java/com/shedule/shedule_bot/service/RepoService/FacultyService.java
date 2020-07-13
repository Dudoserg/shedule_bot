package com.shedule.shedule_bot.service.RepoService;

import com.shedule.shedule_bot.entity.Db.Faculty;

import java.util.List;

public interface FacultyService {
    List<Faculty> getAllFaculty();
    Faculty getFacultyByName(String name);
}
