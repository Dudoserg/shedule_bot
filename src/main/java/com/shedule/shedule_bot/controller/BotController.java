package com.shedule.shedule_bot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shedule.shedule_bot.service.TgService.BotService;
import com.shedule.shedule_bot.service.TgService.Entity.Update.Update;
import com.shedule.shedule_bot.service.TgService.Methods.SendMessage;
import com.shedule.shedule_bot.service.TgService.Objects.KeyboardButton;
import com.shedule.shedule_bot.service.TgService.Objects.ReplyKeyboardMarkup;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class BotController {

    private final String TOKEN = "1277316228:AAFhTvIAwAxg-J5XWbqLCQfglbWkO3DmXm0";
    private final String HOST_URL = " https://bae626acdd2f.ngrok.io";
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
    ) throws Exception {
        if (!token.equals(TOKEN))
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        botService.sendMessage(TOKEN, update.getMessage().getChat().getId(), "ты послал: " + update.getMessage().getText());
        return new ResponseEntity<Object>(update, HttpStatus.OK);
    }

    @RequestMapping(value = "tg/sendMessage/{message}", method = {GET, POST})
    private ResponseEntity<Object> sendMessage(
            @PathVariable String message
    ) throws Exception {
        botService.sendMessage(TOKEN, "346755292", message);
        return new ResponseEntity<Object>("ok", HttpStatus.OK);
    }


    @RequestMapping(value = "tg/sendKeyboard", method = {GET, POST})
    private ResponseEntity<Object> sendKeyboard(
    ) throws Exception {
        List<List<KeyboardButton>> keyboard = new ArrayList<>(Arrays.asList(
                Collections.singletonList(new KeyboardButton("currentDate")),
                Arrays.asList(new KeyboardButton("tomorrow"), new KeyboardButton("today")),
                Collections.singletonList(new KeyboardButton("kek"))
        ));

        ReplyKeyboardMarkup replyKeyboardMarkup
                = ReplyKeyboardMarkup.builder(keyboard)
                .resize_keyboard(true)
                .one_time_keyboard(true)
                .build();

        final SendMessage sendMessageObject =
                SendMessage.builder("346755292", "text")
                        .reply_markup(replyKeyboardMarkup)
                        .build();

        botService.sendMessage(TOKEN, sendMessageObject);

        return new ResponseEntity<Object>("ok", HttpStatus.OK);
    }
}
