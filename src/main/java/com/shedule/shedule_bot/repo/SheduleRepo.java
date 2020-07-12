package com.shedule.shedule_bot.repo;

import com.shedule.shedule_bot.entity.Group;
import com.shedule.shedule_bot.entity.Shedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface SheduleRepo extends JpaRepository<Shedule, Long > {
//    List<Shedule> findAllByGroupName(String groupName);

    // Получить количество записей в таблице
    @Query(value = "SELECT COUNT(*) FROM SHEDULE", nativeQuery = true)
    Long getCountRow();

    // получить уникальные названия групп
//    List<Shedule> findDistinctByGroupName(String groupName);

    // group dayOfWeek weekNumber
    List<Shedule> findAllByGroupEqualsAndDayOfWeekEqualsAndWeekEquals(Group group, Integer dayOfWeek, Integer weekNumber);

    List<Shedule> findAllByGroupEqualsAndWeekEquals(Group group, Integer week);

}
