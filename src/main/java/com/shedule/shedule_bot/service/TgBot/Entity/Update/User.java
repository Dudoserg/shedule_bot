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
    // Unique identifier for this user or bot
    private Integer id;
    // True, if this user is a bot
    private Boolean is_bot;
    //User's or bot's first name
    private String first_name;
    //Optional. User's or bot's last name
    private String last_name;
    //Optional. User's or bot's username
    private String username;
    //Optional. IETF language tag of the user's language
    private String language_code;
    //Optional. True, if the bot can be invited to groups. Returned only in getMe.
    private Boolean can_join_groups;
    //Optional. True, if privacy mode is disabled for the bot. Returned only in getMe.
    private Boolean can_read_all_group_messages;
    //Optional. True, if the bot supports inline queries. Returned only in getMe.
    private Boolean supports_inline_queries;
}
