package com.shedule.shedule_bot.service.TgBot.Methods;


import com.shedule.shedule_bot.service.TgBot.Objects.InlineKeyboardMarkup;
import lombok.*;

@Getter
@Setter
@Builder()
@AllArgsConstructor(access = AccessLevel.PRIVATE) // If immutability is desired
//  Use this method to edit text messages sent by the bot or via the bot (for inline bots). On success, the edited Message is returned.
public class EditMessageText_Method {

    //  Required if inline_message_id is not specified. Unique identifier for the target chat
    //  or username of the target channel (in the format @channelusername)
    private String chat_id;
    //  Required if inline_message_id is not specified. Unique identifier of the sent message
    private Integer message_id;
    //  Required if chat_id and message_id are not specified. Identifier of the inline message
    private String inline_message_id;
    //  New text of the message
    private String text;
    //  Send Markdown or HTML, if you want Telegram apps to show bold, italic,
    //  fixed-width text or inline URLs in your bot's message.
    private String parse_mode;
    //  Disables link previews for links in this message
    private Boolean disable_web_page_preview;
    //  A JSON-serialized object for an inline keyboard.
    private InlineKeyboardMarkup reply_markup;

    public EditMessageText_Method(String chat_id, Integer message_id, String text) {
        this.chat_id = chat_id;
        this.message_id = message_id;
        this.text = text;
    }

    private static EditMessageText_MethodBuilder builder() {
        return new EditMessageText_MethodBuilder();
    }

    public static EditMessageText_MethodBuilder builder(String chat_id, Integer message_id, String text) {
        return builder().chat_id(chat_id).message_id(message_id).text(text);
    }
}
