package com.shedule.shedule_bot.service.TgBot.Entity.Update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String first_name;
    private String last_name;
    private String username;
}
