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
public class ChatDb {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long idDb;

	@Column(name = "chat_id")
	private String id ;

	@Column(name = "chat_first_name")
	@JsonProperty("first_name")
	private String firstName ;

	@Column(name = "chat_last_name")
	@JsonProperty("last_name")
	private String lastName ;

	@Column(name = "chat_username")
	private String username ;

	@Column(name = "chat_type")
	private String type ;
}
