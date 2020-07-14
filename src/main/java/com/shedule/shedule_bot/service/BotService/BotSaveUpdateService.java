package com.shedule.shedule_bot.service.BotService;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shedule.shedule_bot.service.TgBot.Db.Entity.UpdateDb;
import com.shedule.shedule_bot.service.TgBot.Db.Service.DbService;
import com.shedule.shedule_bot.service.TgBot.Entity.Update.Update;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BotSaveUpdateService {

    private static final Logger logger = Logger.getLogger(BotService.class);


    final DbService dbService;

    public BotSaveUpdateService(DbService dbService) {
        this.dbService = dbService;
    }


    public boolean save(Update update) {
        long m;
        String json = "";
        try {
            m = System.currentTimeMillis();
            ObjectMapper jacksonObjectMapper = new ObjectMapper();
            jacksonObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            jacksonObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            json = jacksonObjectMapper.writeValueAsString(update);

            UpdateDb updateDb = jacksonObjectMapper.readValue(json, UpdateDb.class);
            updateDb = dbService.save(updateDb);
            System.out.println("save update from tg" + String.format("%.2f", (System.currentTimeMillis() - m) / 1000.0));
            logger.info("successful save 'update' obj with IdDb #" + updateDb.getIdDb());
            return true;
        } catch (Exception e) {
            logger.error("error while saving next object: " + json);
            return false;
        }
    }
}
