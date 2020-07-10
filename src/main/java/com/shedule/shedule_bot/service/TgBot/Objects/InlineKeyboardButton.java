package com.shedule.shedule_bot.service.TgBot.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InlineKeyboardButton  {
    // Текст на кнопке
    private String text;

    private InlineKeyboardButton() {
    }


    //  Опционально. URL, который откроется при нажатии на кнопку
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String url = null;

    public static InlineKeyboardButton createWithUrl(String text, String url) {
        final InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setUrl(url);
        return inlineKeyboardButton;
    }


    // 	Опционально. Данные, которые будут отправлены в callback_query при нажатии на кнопку
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String callback_data = null;

    public static InlineKeyboardButton createWithCallback_data(String text, String callback_data) {
        final InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setCallback_data(callback_data);
        return inlineKeyboardButton;
    }


    //Опционально. Если этот параметр задан, то при нажатии на кнопку приложение предложит
    // пользователю выбрать любой чат, откроет его и вставит в поле ввода сообщения юзернейм
    // бота и определённый запрос для встроенного режима. Если отправлять пустое поле, то
    // будет вставлен только юзернейм бота.
    //Примечание: это нужно для того, чтобы быстро переключаться между диалогом с ботом и встроенным
    // режимом с этим же ботом. Особенно полезно в сочетаниями с действиями switch_pm… – в этом случае
    // пользователь вернётся в исходный чат автоматически, без ручного выбора из списка.
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String switch_inline_query = null;

    public static InlineKeyboardButton createWithSwitch_inline_query(String text, String switch_inline_query) {
        final InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setSwitch_inline_query(switch_inline_query);
        return inlineKeyboardButton;
    }


}
