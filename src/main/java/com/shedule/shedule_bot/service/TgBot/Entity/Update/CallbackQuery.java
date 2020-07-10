package com.shedule.shedule_bot.service.TgBot.Entity.Update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CallbackQuery {
    private String id;
    private User user;
    private From from;
    private Message message;
    private String inline_message_id;
    private String data;
    private String chat_instance;
}
