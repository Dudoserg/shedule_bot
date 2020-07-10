package com.shedule.shedule_bot.service.TgBot.Objects;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
public class InlineKeyboardMarkup extends BaseKeyBoard {
    private List<List<InlineKeyboardButton>> inline_keyboard;

    public InlineKeyboardMarkup(List<List<InlineKeyboardButton>> inline_keyboard) {
        this.inline_keyboard = inline_keyboard;
    }
}
