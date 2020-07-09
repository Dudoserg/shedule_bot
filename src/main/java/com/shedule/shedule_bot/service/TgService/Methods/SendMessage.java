package com.shedule.shedule_bot.service.TgService.Methods;

import com.shedule.shedule_bot.service.TgService.Objects.BaseKeyBoard;
import lombok.*;

@Getter
@Setter
@Builder()
@AllArgsConstructor(access = AccessLevel.PRIVATE) // If immutability is desired
public class SendMessage {
    @NonNull
    private String chat_id = null;
    @NonNull
    private String text = null;
    private String parse_mode = null;
    private Boolean disable_web_page_preview = null;
    private Boolean disable_notification = null;
    private Integer reply_to_message_id = null;
    private BaseKeyBoard reply_markup = null;

    public SendMessage(String chat_id, String text) {
        this.chat_id = chat_id;
        this.text = text;
    }

    private static SendMessageBuilder builder(){
        return new SendMessageBuilder();
    }
    public static SendMessageBuilder builder(String chat_id, String text){
        return builder().chat_id(chat_id).text(text);
    }
}
