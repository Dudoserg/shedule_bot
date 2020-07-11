package com.shedule.shedule_bot.service.BotService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ButtonTitleAction {
    private String title;
    private ButtonActionInterface buttonAction;
}
