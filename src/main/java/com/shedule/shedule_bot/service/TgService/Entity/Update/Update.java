package com.shedule.shedule_bot.service.TgService.Entity.Update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Update {
	private String update_id;
	private Message message;
}
