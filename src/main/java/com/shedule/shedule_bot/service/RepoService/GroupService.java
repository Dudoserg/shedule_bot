package com.shedule.shedule_bot.service.RepoService;

import com.shedule.shedule_bot.entity.Db.Faculty;
import com.shedule.shedule_bot.entity.Db.Group;
import com.shedule.shedule_bot.repo.GroupRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    final
    GroupRepo groupRepo;

    public GroupService(GroupRepo groupRepo) {
        this.groupRepo = groupRepo;
    }

    public List<Group> getGroupByFaculty(Faculty faculty){
        return groupRepo.findGroupByFaculty(faculty);
    }
    public Integer findMinStartYearByFaculty(Faculty faculty){
        return groupRepo.findMinStartYearByFaculty(faculty);
    }
}
