package com.shedule.shedule_bot.service.TgBot.Entity.Update;


import com.shedule.shedule_bot.service.TgBot.Objects.InlineKeyboardMarkup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Integer message_id;
    private User from;
//	private From from;

    //	Дата отправки сообщения (Unix time)
    private Integer date;
    private Integer edit_date;

    //	Диалог, в котором было отправлено сообщение
    private Chat chat;

    // 	Опционально. Для пересланных сообщений: отправитель оригинального сообщения
    private User forward_from;

    //	Опционально. Для пересланных сообщений: дата отправки оригинального сообщения
    private Integer forward_date;

    //	Опционально. Для ответов: оригинальное сообщение. Note that the Message object in this
    // 	field will not contain further reply_to_message fields even if it itself is a reply.
    private Message reply_to_message;

    //	Опционально. Для текстовых сообщений: текст сообщения, 0-4096 символов
    private String text;

    //	entities	Массив из MessageEntity	Опционально. Для текстовых сообщений: особые сущности в тексте сообщения.
    private List<MessageEntity> entities;

	//Optional. Inline keyboard attached to the message. login_url buttons are represented as ordinary url buttons.
    private InlineKeyboardMarkup reply_markup;

//	audio	Audio	Опционально. Информация об аудиофайле
//	document	Document	Опционально. Информация о файле
//	photo	Массив из PhotoSize	Опционально. Доступные размеры фото
//	sticker	Sticker	Опционально. Информация о стикере
//	video	Video	Опционально. Информация о видеозаписи
//	voice	Voice	Опционально. Информация о голосовом сообщении
//	caption	String	Опционально. Подпись к файлу, фото или видео, 0-200 символов
//	contact	Contact	Опционально. Информация об отправленном контакте
//	location	Location	Опционально. Информация о местоположении
//	venue	Venue	Опционально. Информация о месте на карте
//	new_chat_member	User	Опционально. Информация о пользователе, добавленном в группу
//	left_chat_member	User	Опционально. Информация о пользователе, удалённом из группы
//	new_chat_title	String	Опционально. Название группы было изменено на это поле
//	new_chat_photo	Массив из PhotoSize	Опционально. Фото группы было изменено на это поле
//	delete_chat_photo	True	Опционально. Сервисное сообщение: фото группы было удалено
//	group_chat_created	True	Опционально. Сервисное сообщение: группа создана
//	supergroup_chat_created	True	Опционально. Сервисное сообщение: супергруппа создана
//	channel_chat_created	True	Опционально. Сервисное сообщение: канал создан
//	migrate_to_chat_id	Integer	Опционально. Группа была преобразована в супергруппу с указанным идентификатором. Не превышает 1e13
//	migrate_from_chat_id	Integer	Опционально. Cупергруппа была создана из группы с указанным идентификатором. Не превышает 1e13
//	pinned_message	Message	Опционально. Указанное сообщение было прикреплено. Note that the Message object in this field will not contain further reply_to_message fields even if it is itself a reply.
}
