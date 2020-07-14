package com.shedule.shedule_bot.service.TgBot.Db.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class UserDb {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long idDb;

    // Unique identifier for this user or bot
    @Column(name = "user_id")
    private Integer id;
    // True, if this user is a bot
    @Column(name = "user_is_bot")
    @JsonProperty("is_bot")
    private Boolean isBot;
    //User's or bot's first name
    @Column(name = "user_first_name")
    @JsonProperty("first_name")
    private String firstName;
    //Optional. User's or bot's last name
    @Column(name = "user_last_name")
    @JsonProperty("last_name")
    private String lastName;
    //Optional. User's or bot's username
    @Column(name = "user_username")
    private String username;
    //Optional. IETF language tag of the user's language
    @Column(name = "user_language_code")
    @JsonProperty("language_code")
    private String languageCode;
    //Optional. True, if the bot can be invited to groups. Returned only in getMe.
    @Column(name = "user_can_join_groups")
    @JsonProperty("can_join_groups")
    private Boolean canJoinGroups;
    //Optional. True, if privacy mode is disabled for the bot. Returned only in getMe.
    @Column(name = "user_can_read_all_group_messages")
    @JsonProperty("can_read_all_group_messages")
    private Boolean canReadAllGroupMessages;
    //Optional. True, if the bot supports inline queries. Returned only in getMe.
    @Column(name = "user_supports_inline_queries")
    @JsonProperty("supports_inline_queries")
    private Boolean supportsInlineQueries;
}
