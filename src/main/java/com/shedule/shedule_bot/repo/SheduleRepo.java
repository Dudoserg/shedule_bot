package com.shedule.shedule_bot.repo;

import com.shedule.shedule_bot.entity.Shedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SheduleRepo extends JpaRepository<Shedule, Long > {
    List<Shedule> findAllByGroupName(String groupName);
}
