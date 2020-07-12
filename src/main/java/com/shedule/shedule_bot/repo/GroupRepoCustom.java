package com.shedule.shedule_bot.repo;

import com.shedule.shedule_bot.entity.Db.Faculty;

public interface GroupRepoCustom {
    Integer findMinStartYearByFaculty(Faculty faculty);
}
