package com.shedule.shedule_bot.service.TgBot.Db.Repo;

import com.shedule.shedule_bot.service.TgBot.Db.Entity.ChatDb;
import com.shedule.shedule_bot.service.TgBot.Db.Entity.FromDb;
import com.shedule.shedule_bot.service.TgBot.Db.Entity.MessageDb;
import com.shedule.shedule_bot.service.TgBot.Db.Entity.UserDb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageDbRepo extends JpaRepository<MessageDb, Long> {
    MessageDb findByEdit_dateEqualsAndMessage_idEqualsAndFromEqualsAndDateEqualsAndChatEquals(
            Integer edit_Date, Integer message_id, UserDb from, Integer date,  ChatDb chat);
}
