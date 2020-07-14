package com.shedule.shedule_bot.service.TgBot.Db.Repo;

import com.shedule.shedule_bot.service.TgBot.Db.Entity.UserDb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDbRepo extends JpaRepository<UserDb, Long> {
    UserDb findByIdEqualsAndIs_botEqualsAndFirst_nameEqualsAndUsernameEquals
            (Integer id, Boolean is_bot, String firstName, String username);
}
