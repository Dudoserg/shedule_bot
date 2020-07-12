package com.shedule.shedule_bot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shedule.shedule_bot.service.BotService.BotService;
import com.shedule.shedule_bot.service.TgBot.Entity.Update.Update;
import com.shedule.shedule_bot.service.TgBot.Objects.SendMessageResult;
import javassist.NotFoundException;
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

    private final String TOKEN = "1277316228:AAFhTvIAwAxg-J5XWbqLCQfglbWkO3DmXm0";
    private final String HOST_URL = "https://a92fba1732cb.ngrok.io";
    final BotService botService;

    public BotController(BotService botService) {
        this.botService = botService;
    }

    @RequestMapping(value = "tg/setwebhook", method = GET)
    public ResponseEntity<Object> account() throws NotFoundException, UnsupportedEncodingException, JsonProcessingException {

        final boolean result =
                botService.setWebhook(TOKEN, HOST_URL);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "tg/{token}", method = {GET, POST})
    private ResponseEntity<Object> tg(
            @PathVariable String token,
            @RequestBody Update update
    )   {

        if (!token.equals(TOKEN))
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
//        botService.sendMessage(TOKEN, update.getMessage().getChat().getId(), "ты послал: " + update.getMessage().getText());
        try {
            botService.receivedMessageFromUser(token, update);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Object>(update, HttpStatus.OK);
    }

    @RequestMapping(value = "tg/sendMessage/{message}", method = {GET, POST})
    private ResponseEntity<Object> sendMessage(
            @PathVariable String message
    ) throws Exception {
        final SendMessageResult result = botService.sendMessage(TOKEN, "346755292", message);
        return new ResponseEntity<Object>("ok", HttpStatus.OK);
    }


    @RequestMapping(value = "tg/sendKeyboard", method = {GET, POST})
    private ResponseEntity<Object> sendKeyboard(
    ) throws Exception {
        final SendMessageResult result = botService.sendKeyboard(TOKEN);
        return new ResponseEntity<Object>("ok", HttpStatus.OK);
    }
}
