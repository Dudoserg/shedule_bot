package com.shedule.shedule_bot.service.TgBot.Objects;

import com.shedule.shedule_bot.service.TgBot.Entity.Update.Message;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageResult {
    private String ok;
    private Message result;
}
