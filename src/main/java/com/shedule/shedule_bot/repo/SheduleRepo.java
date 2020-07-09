package com.shedule.shedule_bot.repo;

import com.shedule.shedule_bot.entity.Shedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface SheduleRepo extends JpaRepository<Shedule, Long > {
    List<Shedule> findAllByGroupName(String groupName);

    @Query(value = "SELECT COUNT(*) FROM SHEDULE", nativeQuery = true)
    Long getCountRow();

    List<Shedule> findDistinctByGroupName(String groupName);
}
