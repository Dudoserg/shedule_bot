package com.shedule.shedule_bot.service.TgBot.Db.Service;

import com.shedule.shedule_bot.service.TgBot.Db.Entity.UpdateDb;
import com.shedule.shedule_bot.service.TgBot.Db.Repo.UpdateDbRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateDbService {

    @Autowired
    UpdateDbRepo updateDbRepo;

    public UpdateDb save(UpdateDb updateDb) {
        return updateDbRepo.save(updateDb);
    }
}
