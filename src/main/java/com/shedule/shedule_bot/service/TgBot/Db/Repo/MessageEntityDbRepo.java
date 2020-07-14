package com.shedule.shedule_bot.service.TgBot.Db.Repo;

import com.shedule.shedule_bot.service.TgBot.Db.Entity.MessageEntityDb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageEntityDbRepo extends JpaRepository<MessageEntityDb, Long> {
}
