package com.shedule.shedule_bot.repo;

import com.shedule.shedule_bot.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepo extends JpaRepository<Group, Long> {
    Group findGroupByGroupId(String groupId);

}
