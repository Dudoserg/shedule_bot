package com.shedule.shedule_bot.service.TgBot.Entity.Update;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Результат /getwebhook
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TgResult {
	private Boolean ok;
	private Boolean result;
	private String description;

	public static TgResult createError(String message){
		TgResult tgResult = new TgResult();
		tgResult.setDescription(message);
		tgResult.setOk(false);
		tgResult.setResult(false);
		return tgResult;
	}
}
