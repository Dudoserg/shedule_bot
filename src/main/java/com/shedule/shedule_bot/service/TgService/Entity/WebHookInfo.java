package com.shedule.shedule_bot.service.TgService.Entity;

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
public class WebHookInfo {
	private Boolean ok;
	private WebHookResult result;
	private String description;

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public class WebHookResult {
		private String url;
		private Boolean has_custom_certificate;
		private Integer pending_update_count;
		private Integer last_error_date;
		private String last_error_message;
		private Integer max_connections;
	}
}
