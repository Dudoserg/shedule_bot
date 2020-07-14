package com.shedule.shedule_bot.service.TgBot.Db.Repo;

import com.shedule.shedule_bot.service.TgBot.Db.Entity.ChatDb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatDbRepo extends JpaRepository<ChatDb, Long> {
    ChatDb findByIdEqualsAndFirst_nameEqualsAndLast_nameEqualsAndUsernameEqualsAndTypeEquals(
            String id, String first_name, String last_name, String userName, String type);
}
