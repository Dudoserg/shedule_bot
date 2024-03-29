package com.shedule.shedule_bot.service.TgBot.Db.Service;

import com.shedule.shedule_bot.service.TgBot.Db.Entity.ChatDb;
import com.shedule.shedule_bot.service.TgBot.Db.Repo.ChatDbRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatDbService {

    @Autowired
    ChatDbRepo chatDbRepo;

    public ChatDb save(ChatDb chatDb) {
        // сначала пробуем найти этот чат в базе данных
        ChatDb result = chatDbRepo.findByIdAndFirstNameAndLastNameAndUsernameAndType(
                chatDb.getId(),
                chatDb.getFirstName(),
                chatDb.getLastName(),
                chatDb.getUsername(),
                chatDb.getType()
        );
        if (result == null) {
            result = chatDbRepo.save(chatDb);
        }
        return result;
    }
}
