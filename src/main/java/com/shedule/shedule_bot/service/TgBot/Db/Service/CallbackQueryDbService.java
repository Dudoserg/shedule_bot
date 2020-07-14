package com.shedule.shedule_bot.service.TgBot.Db.Service;

import com.shedule.shedule_bot.service.TgBot.Db.Entity.CallbackQueryDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CallbackQueryDbService {




    public CallbackQueryDb save(CallbackQueryDb callback_query) {
        // Сначала проверяем в базе
        return callback_query;
    }
}
