package com.shedule.shedule_bot.service;

import com.shedule.shedule_bot.entity.Faculty;
import com.shedule.shedule_bot.entity.Group;
import com.shedule.shedule_bot.repo.GroupRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    @Autowired
    GroupRepo groupRepo;

    public List<Group> getGroupByFaculty(Faculty faculty){
        return groupRepo.findGroupByFaculty(faculty);
    }
    public Integer findMinStartYearByFaculty(Faculty faculty){
        return groupRepo.findMinStartYearByFaculty(faculty);
    }
}
