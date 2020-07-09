package com.shedule.shedule_bot.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.shedule.shedule_bot.entity.UserTg;
import com.shedule.shedule_bot.service.TgBot.Entity.Update.Chat;
import com.shedule.shedule_bot.service.TgBot.Entity.Update.Message;
import com.shedule.shedule_bot.service.TgBot.Entity.Update.Update;
import com.shedule.shedule_bot.service.TgBot.Methods.SendMessageObject;
import com.shedule.shedule_bot.service.TgBot.Objects.KeyboardButton;
import com.shedule.shedule_bot.service.TgBot.Objects.ReplyKeyboardMarkup;
import com.shedule.shedule_bot.service.TgBot.TgBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class BotService {
    @Autowired
    UserTgService userTgService;

    @Autowired
    SheduleService sheduleService;

    private final String BASE_URL = "api.telegram.org";

    public boolean receivedMessageFromUser(String token, Update update) throws Exception {
        if (update.getMessage() == null || update.getMessage().getChat() == null || update.getMessage().getChat().getId() == null) {
            return false;
        }
        final Message message = update.getMessage();
        final Chat chat = message.getChat();
        final String chatId = chat.getId();
        UserTg userTg = userTgService.getUserByChatId(chatId).orElse(null);
        // пользователь не существует
        if (userTg == null) {
            userTg = new UserTg();
            userTg.setChatId(chat.getId());
            userTg.setFirstName(chat.getFirst_name());
            userTg.setLastName(chat.getLast_name());
            userTg.setUsername(chat.getUsername());

            userTg = userTgService.save(userTg);
            // отправляем пользователю преветственное сообщение
            final boolean result = sendMessage(token, chatId,
                    "Здравствуйте, " + userTg.getUsername() +
                            ", данный бот поможет вам быстро и удобно узнать расписание занятий в вашем учебном заведении!");
        }

        Integer state = userTg.getState();
        switch (state) {
            case -1: {
                final boolean result = state_0(token, chatId);
                userTg.updateState(0);
                break;
            }
            case 0: {
                boolean result = false;
                if (message.getText().equals("Алтайский край")) {
                    userTg.updateState(1);
                    result = state_1(token, chatId);
                } else {
                    result = state_0(token, chatId);     // Повторно предлагаем выбрать субъект рф
                }
                break;
            }
            case 1: {
                boolean result = false;
                if (message.getText().equals("АлтГТУ - Алтайский государственный технический университет")) {
                    userTg.updateState(2);
//                    result = state_2(token, chatId);
                } else {
                    result = state_1(token, chatId);     // Повторно предлагаем выбрать учебное заведение
                }
            }
            default:{
                sendMessage(token, chatId, "Я таки не понимаю что ты отправляешь");
            }
        }
        userTg = userTgService.save(userTg);
        return true;
    }


    // Выберите учебное заведение
    private boolean state_1(String token, String chatId) throws Exception {
        // Создаем кнопки клавы
        List<List<KeyboardButton>> keyboard = new ArrayList<>(Arrays.asList(
                Collections.singletonList(new KeyboardButton("АлтГТУ - Алтайский государственный технический университет"))
        ));
        // создаем саму клаву
        ReplyKeyboardMarkup replyKeyboardMarkup
                = ReplyKeyboardMarkup.builder(keyboard)
                .resize_keyboard(true)
                .one_time_keyboard(true)
                .build();
        // создаем объект сообщение
        final SendMessageObject sendMessageObject =
                SendMessageObject.builder(chatId, "Выберите учебное заведение")
                        .reply_markup(replyKeyboardMarkup)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        return tgBot.sendMessage(token, sendMessageObject);
    }

    private boolean state_0(String token, String chatId) throws Exception {
        // Создаем кнопки клавы
        List<List<KeyboardButton>> keyboard = new ArrayList<>(Arrays.asList(
                Collections.singletonList(new KeyboardButton("Алтайский край"))
        ));
        // создаем саму клаву
        ReplyKeyboardMarkup replyKeyboardMarkup
                = ReplyKeyboardMarkup.builder(keyboard)
                .resize_keyboard(true)
                .one_time_keyboard(false)
                .build();
        // создаем объект сообщение
        final SendMessageObject sendMessageObject =
                SendMessageObject.builder(chatId, "Выберите субъект РФ в котором расположено учебное заведение")
                        .reply_markup(replyKeyboardMarkup)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        return tgBot.sendMessage(token, sendMessageObject);
    }


    /**
     * Сообщаем адрес телеграму, по которому будем принимать от него вебхуки
     *
     * @param token   токен бота
     * @param MY_HOST базовый адрес сервера
     * @return статус, результат запроса
     * @throws JsonProcessingException      JsonProcessingException
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public boolean setWebhook(String token, String MY_HOST) throws JsonProcessingException, UnsupportedEncodingException {
        TgBot tgBot = new TgBot();
        tgBot.setWebhook(token, MY_HOST);
        return true;
    }


    /**
     * Отправляем сообщение пользователю
     *
     * @param token   токен бота, от лица которого выполнится отправка сообщения
     * @param chat_id айдишник чата
     * @param text    отправляемый текст
     * @return статус отправки
     * @throws Exception Exception
     */
    public boolean sendMessage(String token, String chat_id, String text) throws Exception {
        // создаем объект сообщение
        final SendMessageObject sendMessageObject =
                SendMessageObject.builder(chat_id, text)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        return tgBot.sendMessage(token, sendMessageObject);
    }


    /**
     * Отправляет в чат сообщение (настраеваемый объект SendMessage)
     *
     * @param token токен бота
     * @return статус выполнения
     * @throws Exception Exception
     */
    public boolean sendKeyboard(String token)
            throws Exception {
        // Создаем кнопки клавы
        List<List<KeyboardButton>> keyboard = new ArrayList<>(Arrays.asList(
                Collections.singletonList(new KeyboardButton("currentDate")),
                Arrays.asList(new KeyboardButton("tomorrow"), new KeyboardButton("today")),
                Collections.singletonList(new KeyboardButton("kek"))
        ));
        // создаем саму клаву
        ReplyKeyboardMarkup replyKeyboardMarkup
                = ReplyKeyboardMarkup.builder(keyboard)
                .resize_keyboard(true)
                .one_time_keyboard(true)
                .build();
        // создаем объект сообщение
        final SendMessageObject sendMessageObject =
                SendMessageObject.builder("346755292", "text")
                        .reply_markup(replyKeyboardMarkup)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        return tgBot.sendMessage(token, sendMessageObject);
    }


}
