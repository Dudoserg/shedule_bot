package com.shedule.shedule_bot.service.TgService.Entity.Update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class From {
//	  "id": 346755292,
//			  "is_bot": false,
//			  "first_name": "Sergey",
//			  "last_name": "Pankratov",
//			  "username": "DudoSerg",
//			  "language_code": "ru"
	private String id;
	private Boolean is_bot;
	private String first_name;
	private String last_name;
	private String username;
	private String language_code;
}
