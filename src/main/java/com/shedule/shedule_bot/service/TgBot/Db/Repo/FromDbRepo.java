package com.shedule.shedule_bot.service.TgBot.Db.Repo;

import com.shedule.shedule_bot.service.TgBot.Db.Entity.FromDb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FromDbRepo extends JpaRepository<FromDb, Long> {
}
