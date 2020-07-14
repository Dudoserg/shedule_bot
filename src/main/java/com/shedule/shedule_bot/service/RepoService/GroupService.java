package com.shedule.shedule_bot.service.RepoService;

import com.shedule.shedule_bot.entity.Db.Shedule.Faculty;
import com.shedule.shedule_bot.entity.Db.Shedule.Group;

import java.util.List;

public interface GroupService {
    List<Group> getGroupByFaculty(Faculty faculty);
    Integer findMinStartYearByFaculty(Faculty faculty);
}
