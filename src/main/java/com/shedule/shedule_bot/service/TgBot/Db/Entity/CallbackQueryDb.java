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
public class CallbackQueryDb {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long idDb;

    @Column(name = "сallbackQuery_id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserDb user;

    @ManyToOne
    @JoinColumn(name = "from_id", nullable = false)
    private FromDb from;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private MessageDb message;

    @Column(name = "callbackquery_inline_message_id")
    @JsonProperty("inline_message_id")
    private String inlineMessageId;

    @Column(name = "callbackquery_data")
    private String data;
    
    @Column(name = "callbackquery_chat_instance")
    @JsonProperty("chat_instance")
    private String chatInstance;
}
