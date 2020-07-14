package com.shedule.shedule_bot.repo;

import com.shedule.shedule_bot.entity.Db.Shedule.SubjectType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectTypeRepo extends JpaRepository<SubjectType, Long> {
    SubjectType findByNameEquals(String name);
}
