package com.shedule.shedule_bot.service.RepoService;

import com.shedule.shedule_bot.entity.Db.Shedule.Cabinet;
import com.shedule.shedule_bot.repo.CabinetRepo;
import org.springframework.stereotype.Service;

@Service
public class CabinetServiceImpl implements CabinetService{
    final
    CabinetRepo cabinetRepo;

    public CabinetServiceImpl(CabinetRepo cabinetRepo) {
        this.cabinetRepo = cabinetRepo;
    }

    public Cabinet getByTitle(String cabinetTitle){
        Cabinet cabinet = cabinetRepo.findAllByCabinetTitleEquals(cabinetTitle);
        if(cabinet == null){
            cabinet = new Cabinet(cabinetTitle);
            cabinet = cabinetRepo.save(cabinet);
        }
        return cabinet;
    }

}
