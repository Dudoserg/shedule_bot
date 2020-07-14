package com.shedule.shedule_bot.service.TgBot.Db.Service;

import com.shedule.shedule_bot.service.TgBot.Db.Entity.ChatDb;
import com.shedule.shedule_bot.service.TgBot.Db.Entity.MessageDb;
import com.shedule.shedule_bot.service.TgBot.Db.Entity.UserDb;
import com.shedule.shedule_bot.service.TgBot.Db.Repo.MessageDbRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageDbService {

    @Autowired
    MessageDbRepo messageDbRepo;

    @Autowired
    ChatDbService chatDbService;

    @Autowired
    UserDbService userDbService;

    public MessageDb save(MessageDb messageDb) {
        // сначала пробуем найти объект в базе
        ChatDb chat = messageDb.getChat();
        UserDb from = messageDb.getFrom();
        UserDb forward_from = messageDb.getForwardFrom();
        MessageDb reply_to_message = messageDb.getReplyToMessage();
        if(chat != null){
            chat = chatDbService.save(chat);
            messageDb.setChat(chat);
        }
        if(from != null){
            from = userDbService.save(from);
            messageDb.setFrom(from);
        }
        if(forward_from != null){
            forward_from = userDbService.save(forward_from);
            messageDb.setForwardFrom(forward_from);
        }
        if(reply_to_message != null){
            reply_to_message = this.save(reply_to_message);
            messageDb.setReplyToMessage(reply_to_message);
        }

        MessageDb result = messageDbRepo.findByMessageIdAndFromAndDateAndEditDateAndChat(
                messageDb.getMessageId(),
                from,
                messageDb.getDate(),
                messageDb.getEditDate(),
                chat
        );
        if(result == null){
            // создаем
            result = messageDbRepo.save(messageDb);
        }
        return result;
    }
}
