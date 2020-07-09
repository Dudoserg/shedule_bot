package com.shedule.shedule_bot.service.TgService.Entity.Update;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
	private String message_id;
	private From from;
	private Chat chat;
	private String date;
	private String text;
}
