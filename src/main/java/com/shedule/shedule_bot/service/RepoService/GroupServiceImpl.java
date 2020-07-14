package com.shedule.shedule_bot.service.RepoService;

import com.shedule.shedule_bot.entity.Db.Shedule.Faculty;
import com.shedule.shedule_bot.entity.Db.Shedule.Group;
import com.shedule.shedule_bot.repo.GroupRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService{
    final
    GroupRepo groupRepo;

    public GroupServiceImpl(GroupRepo groupRepo) {
        this.groupRepo = groupRepo;
    }

    public List<Group> getGroupByFaculty(Faculty faculty){
        return groupRepo.findGroupByFaculty(faculty);
    }
    public Integer findMinStartYearByFaculty(Faculty faculty){
        return groupRepo.findMinStartYearByFaculty(faculty);
    }
}
