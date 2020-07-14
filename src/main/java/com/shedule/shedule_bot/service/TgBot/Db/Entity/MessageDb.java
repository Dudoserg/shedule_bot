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
public class MessageDb {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long idDb;

    @JsonProperty("message_id")
    private Integer messageId;

    @ManyToOne
    @JoinColumn(name = "from_id", nullable = false)
    private UserDb from;
//	private From from;

    //	Дата отправки сообщения (Unix time)
    private Integer date;

    @JsonProperty("edit_date")
    private Integer editDate;

    //	Диалог, в котором было отправлено сообщение
    @ManyToOne
    @JoinColumn(name = "chatdb_id", nullable = false)
    private ChatDb chat;

    // 	Опционально. Для пересланных сообщений: отправитель оригинального сообщения
    @ManyToOne
    @JoinColumn(name = "userdb_id_forward_from")
    @JsonProperty("forward_from")
    private UserDb forwardFrom;

    //	Опционально. Для пересланных сообщений: дата отправки оригинального сообщения
    @JsonProperty("forward_date")
    private Integer forwardDate;

    //	Опционально. Для ответов: оригинальное сообщение. Note that the Message object in this
    // 	field will not contain further reply_to_message fields even if it itself is a reply.
    @ManyToOne
    @JoinColumn(name = "messagedb_id_reply_to_message")
    @JsonProperty("reply_to_message")
    private MessageDb replyToMessage;

    //	Опционально. Для текстовых сообщений: текст сообщения, 0-4096 символов
    @Column(name = "messagedb_text")
    private String text;

/*    //	entities	Массив из MessageEntity	Опционально. Для текстовых сообщений: особые сущности в тексте сообщения.
    @OneToMany( mappedBy = "faculty", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MessageEntityDb> entities;

	//Optional. Inline keyboard attached to the message. login_url buttons are represented as ordinary url buttons.
    private InlineKeyboardMarkupDb reply_markup;*/

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
