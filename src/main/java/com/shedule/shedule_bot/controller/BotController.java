package com.shedule.shedule_bot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shedule.shedule_bot.service.BotService.BotSaveUpdateService;
import com.shedule.shedule_bot.service.BotService.BotService;
import com.shedule.shedule_bot.service.TgBot.Db.Entity.UpdateDb;
import com.shedule.shedule_bot.service.TgBot.Db.Service.DbService;
import com.shedule.shedule_bot.service.TgBot.Entity.Update.Update;
import com.shedule.shedule_bot.service.TgBot.Objects.SendMessageResult;
import javassist.NotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class BotController {


    final BotSaveUpdateService botSaveUpdateService;
    final BotService botService;

    public BotController(BotService botService, BotSaveUpdateService botSaveUpdateService) {
        this.botService = botService;
        this.botSaveUpdateService = botSaveUpdateService;
    }

    private final String TOKEN = "1277316228:AAFhTvIAwAxg-J5XWbqLCQfglbWkO3DmXm0";
    private final String HOST_URL = "https://d62cda669526.ngrok.io";


    @RequestMapping(value = "tg/setwebhook", method = GET)
    public ResponseEntity<Object> setwebhook() throws NotFoundException, UnsupportedEncodingException, JsonProcessingException {

        final boolean result =
                botService.setWebhook(TOKEN, HOST_URL);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "tg/{token}", method = {GET, POST})
    private ResponseEntity<Object> webhook(
            @PathVariable String token,
            @RequestBody Update update
    ) {
        long m;
        if (!token.equals(TOKEN))
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
//        botService.sendMessage(TOKEN, update.getMessage().getChat().getId(), "ты послал: " + update.getMessage().getText());
        try {
            m = System.currentTimeMillis();
            botService.receivedMessageFromUser(token, update);
            System.out.println(" get update from tg" + String.format("%.2f", (System.currentTimeMillis() - m) / 1000.0));
            botSaveUpdateService.save(update);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Object>(update, HttpStatus.OK);
    }

    @RequestMapping(value = "tg/sendMessage/{message}", method = {GET, POST})
    private ResponseEntity<Object> testSendMessage(
            @PathVariable String message
    ) throws Exception {
        final SendMessageResult result = botService.sendMessage(TOKEN, "346755292", message);
        return new ResponseEntity<Object>("ok", HttpStatus.OK);
    }


    @RequestMapping(value = "tg/sendKeyboard", method = {GET, POST})
    private ResponseEntity<Object> testSendKeyboard(
    ) throws Exception {
        final SendMessageResult result = botService.sendKeyboard(TOKEN);
        return new ResponseEntity<Object>("ok", HttpStatus.OK);
    }
}
