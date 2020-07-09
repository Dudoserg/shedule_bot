package com.shedule.shedule_bot.service.TgService.Objects;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder()
@AllArgsConstructor(access = AccessLevel.PRIVATE) // If immutability is desired
public class ReplyKeyboardMarkup  extends BaseKeyBoard {
    private List<List<KeyboardButton>> keyboard = new ArrayList<>();
    private boolean resize_keyboard = false;
    private boolean one_time_keyboard = false;


    public ReplyKeyboardMarkup(List<List<KeyboardButton>> keyboard) {
        this.keyboard = keyboard;
    }

    private static ReplyKeyboardMarkupBuilder builder(){
        return new ReplyKeyboardMarkupBuilder();
    }
    public static ReplyKeyboardMarkupBuilder builder(List<List<KeyboardButton>> keyboard){
        return builder().keyboard(keyboard);
    }

//    private boolean selective;
}
