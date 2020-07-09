package com.shedule.shedule_bot.service.TgBot.Methods;

import com.shedule.shedule_bot.service.TgBot.Objects.BaseKeyBoard;
import lombok.*;

@Getter
@Setter
@Builder()
@AllArgsConstructor(access = AccessLevel.PRIVATE) // If immutability is desired
public class SendMessageObject {
    @NonNull
    private String chat_id = null;
    @NonNull
    private String text = null;
    private String parse_mode = null;
    private Boolean disable_web_page_preview = null;
    private Boolean disable_notification = null;
    private Integer reply_to_message_id = null;
    private BaseKeyBoard reply_markup = null;

    public SendMessageObject(String chat_id, String text) {
        this.chat_id = chat_id;
        this.text = text;
    }

    private static SendMessageObjectBuilder builder(){
        return new SendMessageObjectBuilder();
    }
    public static SendMessageObjectBuilder builder(String chat_id, String text){
        return builder().chat_id(chat_id).text(text);
    }
}
