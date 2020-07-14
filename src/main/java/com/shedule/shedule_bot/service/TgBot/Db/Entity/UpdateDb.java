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
public class UpdateDb {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long idDb;


    @Column(name = "updatedb_update_id")
    @JsonProperty("update_id")
    private String updateId;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private MessageDb message;
    //	Опционально. New incoming inline query
    //	private InlineQuery inline_query;

    //	Опционально. The result of an inline query that was chosen by a user and sent to their chat partner.
    //	private ChosenInlineResult chosen_inline_result;

    //Опционально. New incoming callback query
/*    @ManyToOne
    @JoinColumn(name = "callbackquery_id", nullable = false)
    private CallbackQueryDb callback_query;*/
}
