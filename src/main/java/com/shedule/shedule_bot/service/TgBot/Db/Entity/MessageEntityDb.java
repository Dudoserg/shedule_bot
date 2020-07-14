package com.shedule.shedule_bot.service.TgBot.Db.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class MessageEntityDb {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long idDb;

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
    @Column(name = "messageentitydb_type")
    private  String type;
    //  Offset in UTF-16 code units to the start of the entity
    @Column(name = "messageentitydb_offset")
    private Integer offset;
    //  Length of the entity in UTF-16 code units
    @Column(name = "messageentitydb_length")
    private Integer length;

    @Column(name = "messageentitydb_url")
    private String url;
}
