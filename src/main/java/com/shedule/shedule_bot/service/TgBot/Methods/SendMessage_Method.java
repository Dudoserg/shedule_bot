package com.shedule.shedule_bot.service.TgBot.Methods;

import com.shedule.shedule_bot.service.TgBot.Objects.BaseKeyBoard;
import lombok.*;

@Getter
@Setter
@Builder()
@AllArgsConstructor(access = AccessLevel.PRIVATE) // If immutability is desired
public class SendMessage_Method {
    @NonNull
    private String chat_id = null;
    @NonNull
    private String text = null;
    //Send Markdown or HTML, if you want Telegram apps to show bold, italic,
    // fixed-width text or inline URLs in your bot's message.
    private String parse_mode = null;
    //Disables link previews for links in this message
    private Boolean disable_web_page_preview = null;
    //Sends the message silently. iOS users will not receive a notification,
    // Android users will receive a notification with no sound.
    private Boolean disable_notification = null;
    // If the message is a reply, ID of the original message
    private Integer reply_to_message_id = null;
    // Additional interface options. A JSON-serialized object for an inline keyboard,
    // custom reply keyboard, instructions to hide reply keyboard or to force a reply from the user.
    private BaseKeyBoard reply_markup = null;

    public SendMessage_Method(String chat_id, String text) {
        this.chat_id = chat_id;
        this.text = text;
    }

    private static SendMessage_MethodBuilder builder(){
        return new SendMessage_MethodBuilder();
    }
    public static SendMessage_MethodBuilder builder(String chat_id, String text){
        return builder().chat_id(chat_id).text(text);
    }
}
