package com.shedule.shedule_bot.service.TgBot.Entity.Update;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageEntity {
    //  Type of the entity. One of mention (@username),
    //  hashtag,
    //  bot_command,
    //  url,
    //  email,
    //  bold (bold text),
    //  italic (italic text),
    //  code (monowidth string),
    //  pre (monowidth block),
    //  text_link (for clickable text URLs)
    private  String type;
    //  Offset in UTF-16 code units to the start of the entity
    private Integer offset;
    //  Length of the entity in UTF-16 code units
    private Integer length;
    private String url;
}
