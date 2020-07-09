package com.shedule.shedule_bot.service.TgBot.Entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 *  basic information about the bot in form of a User object.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetMe {
    private boolean ok;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Result{
        private Long id;
        private boolean is_bot;
        private String first_name;
        private String username;
        private boolean can_join_groups;
        private boolean can_read_all_group_messages;
        private boolean supports_inline_queries;
    }
    private Result result;
}
