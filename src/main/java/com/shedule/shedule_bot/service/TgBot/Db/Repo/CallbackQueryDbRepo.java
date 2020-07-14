package com.shedule.shedule_bot.service.TgBot.Db.Repo;

import com.shedule.shedule_bot.service.TgBot.Db.Entity.CallbackQueryDb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallbackQueryDbRepo extends JpaRepository<CallbackQueryDb, Long> {
}
