package com.shedule.shedule_bot.service.TgBot.Db.Repo;

import com.shedule.shedule_bot.service.TgBot.Db.Entity.UpdateDb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpdateDbRepo extends JpaRepository<UpdateDb, Long> {
}
