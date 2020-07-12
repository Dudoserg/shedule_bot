package com.shedule.shedule_bot.service.BotService;

import com.shedule.shedule_bot.service.TgBot.Entity.Update.Chat;
import com.shedule.shedule_bot.service.TgBot.Entity.Update.Message;
import com.shedule.shedule_bot.service.TgBot.Entity.Update.Update;
import com.shedule.shedule_bot.service.TgBot.Objects.SendMessageResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class State_8 {
    List<List<String>> keyboard = new ArrayList<>();

    public State_8() {
        keyboard.add(Collections.singletonList("Назад"));
        keyboard.add(Arrays.asList("На сегодня", "На завтра"));
        keyboard.add(Arrays.asList("Первая неделя", "Вторая неделя"));
        keyboard.add(Collections.singletonList("Расписание на конкретную дату"));
    }

    public boolean verifyPossibleButtons(String str) {
        for (List<String> row : this.keyboard) {
            for (String button : row) {
                if (button.equals(str))
                    return true;
            }
        }
        return false;
    }

    public void execute(String token, Update update) throws Exception {
//        SendMessageResult result = null;
//
//        // в этом состойнии мы должны получить Message, а ни что-то другое
//        if (update.getMessage() == null || update.getMessage().getChat() == null || update.getMessage().getChat().getId() == null) {
//            throw new Exception("Данный случай пока что не обработан (state 8)");
//        }
//        final Message message = update.getMessage();
//        final Chat chat = message.getChat();
//        final String chatId = chat.getId();
//
//        // валидация ответа
//        boolean isValidate = true;
//        if (isValidate) {
//            result = state_9(token, chatId, LocalDate.now());
//            userTg.updateState(9);          // переход в состояние 9
//            userTg.setSaveIdOfMessageWithCalendar_1(result.getResult().getMessage_id());
//        } else {
//            sendMessage(token, chatId, "Возможно произошла ошибка, пожалуйста, повторите ввод.");
//            result = state_8(token, chatId, userTg);    // заново отправляем вопрос про день
//        }
    }

    public void today(String token, Update update){
        // выводим сообщение с расписанием на текущий день, переходим в главное меню, в состояние 5

    }

}
