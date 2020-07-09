package com.shedule.shedule_bot.repo;

import com.shedule.shedule_bot.entity.Faculty;
import org.springframework.data.jpa.repository.Query;

public interface GroupRepoCustom {
    Integer findMinStartYearByFaculty(Faculty faculty);
}
