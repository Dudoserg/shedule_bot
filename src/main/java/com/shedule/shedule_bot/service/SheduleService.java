package com.shedule.shedule_bot.service;

import com.shedule.shedule_bot.entity.Shedule;
import com.shedule.shedule_bot.repo.SheduleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SheduleService {

    @Autowired
    SheduleRepo sheduleRepo;

//    public List<Shedule> findAllByGroupName(String groupName){
//        final List<Shedule> allByGroup_name = sheduleRepo.findAllByGroupName(groupName);
//        return allByGroup_name;
//    }

    public Long getCountRow(){
        return sheduleRepo.getCountRow();
    }
}
