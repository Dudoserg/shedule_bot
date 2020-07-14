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
public class FromDb {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long idDb;

	@Column(name = "from_id")
	private String id;

	@Column(name = "from_is_bot")
	@JsonProperty("is_bot")
	private Boolean isBot;

	@Column(name = "from_first_name")
	@JsonProperty("first_name")
	private String firstName;

	@Column(name = "from_last_name")
	@JsonProperty("last_name")
	private String lastName;

	@Column(name = "from_username")
	private String username;

	@Column(name = "from_language_code")
	@JsonProperty("language_code")
	private String languageCode;
}
