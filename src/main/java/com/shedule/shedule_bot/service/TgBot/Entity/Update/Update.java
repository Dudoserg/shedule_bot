package com.shedule.shedule_bot.service.TgBot.Entity.Update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Update {
    private String update_id;
    private Message message;
    //	Опционально. New incoming inline query
    //	private InlineQuery inline_query;

    //	Опционально. The result of an inline query that was chosen by a user and sent to their chat partner.
    //	private ChosenInlineResult chosen_inline_result;

    //Опционально. New incoming callback query
    private CallbackQuery callback_query;
}
