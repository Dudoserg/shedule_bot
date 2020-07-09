package com.shedule.shedule_bot.service.TgBot.Objects;

import lombok.*;

@Getter
@Setter
public class KeyboardButton {
    private String text;
    private Boolean request_contact = false;
    private Boolean request_location = false;

    public KeyboardButton(String text) {
        this.text = text;
    }

    public KeyboardButton(String text, Boolean request_contact, Boolean request_location) {
        this.text = text;
        this.request_contact = request_contact;
        this.request_location = request_location;
    }
}
