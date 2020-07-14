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
        MessageDb result = messageDbRepo.findByEdit_dateEqualsAndMessage_idEqualsAndFromEqualsAndDateEqualsAndChatEquals(
                messageDb.getEdit_date(),
                messageDb.getMessage_id(),
                messageDb.getFrom(),
                messageDb.getDate(),
                messageDb.getChat()
        );
        if(result == null){
            // создаем
            ChatDb chat = messageDb.getChat();
            if(chat != null){
                chat = chatDbService.save(chat);
                messageDb.setChat(chat);
            }
            UserDb forward_from = messageDb.getForward_from();
            if(forward_from != null){
                forward_from = userDbService.save(forward_from);
                messageDb.setForward_from(forward_from);
            }
            MessageDb reply_to_message = messageDb.getReply_to_message();
            if(reply_to_message != null){
                reply_to_message = this.save(reply_to_message);
                messageDb.setReply_to_message(reply_to_message);
            }
            result = messageDbRepo.save(messageDb);
        }
        return result;
    }
}
