package com.shedule.shedule_bot.service.TgBot.Db.Service;

import com.shedule.shedule_bot.service.TgBot.Db.Entity.CallbackQueryDb;
import com.shedule.shedule_bot.service.TgBot.Db.Entity.MessageDb;
import com.shedule.shedule_bot.service.TgBot.Db.Entity.UpdateDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DbService {

    @Autowired
    MessageDbService messageDbService;

    @Autowired
    CallbackQueryDbService callbackQueryDbService;
    @Autowired
    UpdateDbService updateDbService;

    public UpdateDb save(UpdateDb updateDb){

        MessageDb message = updateDb.getMessage();
        if(message != null){
            message = messageDbService.save(message);
            updateDb.setMessage(message);
        }
        /*CallbackQueryDb callback_query = updateDb.getCallback_query();
        if(callback_query != null){
            callback_query = callbackQueryDbService.save(callback_query);
            updateDb.setCallback_query(callback_query);
        }*/
        return updateDbService.save(updateDb);
    }
}
