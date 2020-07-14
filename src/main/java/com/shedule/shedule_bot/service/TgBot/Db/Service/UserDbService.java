package com.shedule.shedule_bot.service.TgBot.Db.Service;

import com.shedule.shedule_bot.service.TgBot.Db.Entity.ChatDb;
import com.shedule.shedule_bot.service.TgBot.Db.Entity.UserDb;
import com.shedule.shedule_bot.service.TgBot.Db.Repo.UserDbRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDbService {

    @Autowired
    UserDbRepo userDbRepo;

    public UserDb save(UserDb userDb) {
        // Сначала пробуем найти объект в базе
        UserDb result = userDbRepo.findByIdAndIsBotAndFirstNameAndUsername(
                userDb.getId(),
                userDb.getIsBot(),
                userDb.getFirstName(),
                userDb.getUsername()
        );
        if(result == null){
            result = userDbRepo.save(userDb);
        }
        return result;
    }
}
