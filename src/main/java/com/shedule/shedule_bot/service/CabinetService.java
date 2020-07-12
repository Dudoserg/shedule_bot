package com.shedule.shedule_bot.service;

import com.shedule.shedule_bot.entity.Db.Cabinet;
import com.shedule.shedule_bot.repo.CabinetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CabinetService {
    @Autowired
    CabinetRepo cabinetRepo;

    public Cabinet findByTitle(String cabinetTitle){
        Cabinet cabinet = cabinetRepo.findAllByCabinetTitleEquals(cabinetTitle);
        if(cabinet == null){
            cabinet = new Cabinet(cabinetTitle);
            cabinet = cabinetRepo.save(cabinet);
        }
        return cabinet;
    }

}
