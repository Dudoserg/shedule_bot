package com.shedule.shedule_bot.repo;

import com.shedule.shedule_bot.entity.Db.Faculty;
import com.shedule.shedule_bot.entity.Db.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepo extends JpaRepository<Group, Long>, GroupRepoCustom {
    Group findGroupByGroupId(String groupId);

    List<Group> findGroupByFaculty(Faculty faculty);


}
