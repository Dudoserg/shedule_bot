package com.shedule.shedule_bot.service.TgBot.Objects;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder()
@AllArgsConstructor(access = AccessLevel.PRIVATE) // If immutability is desired
public class ReplyKeyboardMarkup extends BaseKeyBoard {
    private List<List<KeyboardButton>> keyboard = new ArrayList<>();

    //  Опционально. Указывает клиенту подогнать высоту клавиатуры под количество кнопок
    //  (сделать её меньше, если кнопок мало). По умолчанию False, то есть клавиатура
    //  всегда такого же размера, как и стандартная клавиатура устройства.
    private boolean resize_keyboard = false;

    //  Опционально. Указывает клиенту скрыть клавиатуру после использования (после
    //  нажатия на кнопку). Её по-прежнему можно будет открыть через иконку в поле
    //  ввода сообщения. По умолчанию False.
    private boolean one_time_keyboard = false;


    public ReplyKeyboardMarkup(List<List<KeyboardButton>> keyboard) {
        this.keyboard = keyboard;
    }
    public ReplyKeyboardMarkup() {
        this.keyboard = new ArrayList<>();
    }

    private static ReplyKeyboardMarkupBuilder builder() {
        return new ReplyKeyboardMarkupBuilder();
    }

    public static ReplyKeyboardMarkupBuilder builder(List<List<KeyboardButton>> keyboard) {
        return builder().keyboard(keyboard);
    }

//    private boolean selective;
}
