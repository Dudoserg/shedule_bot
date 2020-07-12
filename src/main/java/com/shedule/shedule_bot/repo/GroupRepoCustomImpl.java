package com.shedule.shedule_bot.repo;

import com.shedule.shedule_bot.entity.Db.Faculty;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class GroupRepoCustomImpl implements GroupRepoCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Integer findMinStartYearByFaculty(Faculty faculty) {
        final Object first = entityManager.createNativeQuery(
                "SELECT MIN(group_table.start_year) FROM group_table,  faculty WHERE group_table.faculty_id = faculty.id AND faculty.faculty_id = ?1")
                .setParameter(1, faculty.getFacultyId())
                .getSingleResult();
        if (first != null)
            return (Integer) first;
        return null;
    }
}
