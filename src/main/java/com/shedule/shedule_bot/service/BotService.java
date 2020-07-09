package com.shedule.shedule_bot.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.shedule.shedule_bot.entity.Faculty;
import com.shedule.shedule_bot.entity.Group;
import com.shedule.shedule_bot.entity.UserTg;
import com.shedule.shedule_bot.service.TgBot.Entity.Update.Chat;
import com.shedule.shedule_bot.service.TgBot.Entity.Update.Message;
import com.shedule.shedule_bot.service.TgBot.Entity.Update.Update;
import com.shedule.shedule_bot.service.TgBot.Methods.SendMessageObject;
import com.shedule.shedule_bot.service.TgBot.Objects.KeyboardButton;
import com.shedule.shedule_bot.service.TgBot.Objects.ReplyKeyboardMarkup;
import com.shedule.shedule_bot.service.TgBot.TgBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class BotService {
    @Autowired
    UserTgService userTgService;

    @Autowired
    SheduleService sheduleService;

    @Autowired
    FacultyService facultyService;

    @Autowired
    GroupService groupService;

    private final String BASE_URL = "api.telegram.org";

    public boolean receivedMessageFromUser(String token, Update update) throws Exception {
        if (update.getMessage() == null || update.getMessage().getChat() == null || update.getMessage().getChat().getId() == null) {
            return false;
        }
        final Message message = update.getMessage();
        final Chat chat = message.getChat();
        final String chatId = chat.getId();
        UserTg userTg = userTgService.getUserByChatId(chatId).orElse(null);
        // пользователь не существует
        if (userTg == null) {
            userTg = new UserTg();
            userTg.setChatId(chat.getId());
            userTg.setFirstName(chat.getFirst_name());
            userTg.setLastName(chat.getLast_name());
            userTg.setUsername(chat.getUsername());

            userTg = userTgService.save(userTg);
            // отправляем пользователю преветственное сообщение
            final boolean result = sendMessage(token, chatId,
                    "Здравствуйте, " + userTg.getUsername() +
                            ", данный бот поможет вам быстро и удобно узнать расписание занятий в вашем учебном заведении!");
        }

        // команды обрабатываются в первую очередь
        if (checkCommand(token, update, userTg))
            return true;


        Integer state = userTg.getState();
        switch (state) {
            case -1: {
                final boolean result = state_0(token, chatId);
                userTg.updateState(0);
                break;
            }
            case 0: {
                boolean result = false;
                if (message.getText().equals("Алтайский край")) {
                    result = state_1(token, chatId);
                    userTg.updateState(1);
                } else {
                    result = state_0(token, chatId);     // Повторно предлагаем выбрать субъект рф
                }
                break;
            }
            case 1: {
                boolean result = false;
                if (message.getText().equals("АлтГТУ - Алтайский государственный технический университет")) {
                    result = state_2(token, chatId);
                    userTg.updateState(2);
                } else {
                    result = state_1(token, chatId);     // Повторно предлагаем выбрать учебное заведение
                }
                break;
            }
            case 2: {
                boolean result = false;
                final String text = message.getText();  // тут должен быть отправлен один из факультетов
                final Faculty faculty = facultyService.getFacultyByName(text);
                if (faculty != null) {
                    result = state_3(token, chatId, faculty);
                    userTg.updateState(3);
                    userTg.setSaveFaculty_1(faculty);
                } else {
                    result = state_2(token, chatId);
                }

                break;
            }
            case 3: {
                boolean result = true;
                final Faculty savedFaculty = userTg.getSaveFaculty_1();
                // вычисляем максимально возможный курс на данном факультете
                final Integer minStartYearByFaculty = groupService.findMinStartYearByFaculty(savedFaculty);
                final int countCoursesInThisFacult = (LocalDate.now().getYear() - minStartYearByFaculty);
                // проверяем совпал ли наш выбор с возможными курсами
                // формируем список строк - возможных курсов
                final List<String> collect =
                        IntStream.range(1, countCoursesInThisFacult + 1)
                                .mapToObj(value -> value + " курс")
                                .collect(Collectors.toList());
                final boolean ok = collect.stream().anyMatch(s -> s.equals(message.getText()));
                if (ok) {
                    final Integer selectedCourse = Integer.valueOf(message.getText().split(" ")[0]);
                    result = state_4(token, chatId, savedFaculty, selectedCourse);
                    userTg.updateState(4);
                } else {
                    result = state_3(token, chatId, savedFaculty);
                }

                break;
            }
            default: {
                sendMessage(token, chatId, "Я таки не понимаю что ты отправляешь");
            }
        }
        userTg = userTgService.save(userTg);
        return true;
    }

    // state 4 Выберите группу
    private boolean state_4(String token, String chatId, Faculty faculty, Integer course) throws Exception {
        // Получаем все группы по данному факультету ( )
        List<Group> groupList = new ArrayList<>(faculty.getGroupSet());
        // Фильтруем группы по курсу
        groupList = groupList.stream()
                .filter(group -> (LocalDate.now().getYear() - group.getStartYear()) == course)
                .collect(Collectors.toList());
        groupList = groupList.stream()
                .sorted((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()))
                .collect(Collectors.toList());
        // Создаем кнопки клавы
        List<List<KeyboardButton>> keyboard = new ArrayList<>();
        final int size = ((int) (groupList.size() / 3)) * 3;
        for (int i = 0; i < size; i += 3) {
            keyboard.add(
                    new ArrayList<>(
                            Arrays.asList(
                                    new KeyboardButton(groupList.get(i).getName()),
                                    new KeyboardButton(groupList.get(i + 1).getName()),
                                    new KeyboardButton(groupList.get(i + 2).getName())
                            )
                    )
            );

        }
        List<KeyboardButton> lastRow = new ArrayList<>();
        for (int i = size; i < groupList.size(); i++) {
            lastRow.add(new KeyboardButton(groupList.get(i).getName()));
        }

        // создаем саму клаву
        ReplyKeyboardMarkup replyKeyboardMarkup
                = ReplyKeyboardMarkup.builder(keyboard)
                .resize_keyboard(true)
                .one_time_keyboard(true)
                .build();
        // создаем объект сообщение
        final SendMessageObject sendMessageObject =
                SendMessageObject.builder(chatId, "Выберите группу")
                        .reply_markup(replyKeyboardMarkup)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        return tgBot.sendMessage(token, sendMessageObject);
    }

    // state 3 Выберите курс
    private boolean state_3(String token, String chatId, Faculty faculty) throws Exception {
        // находим самый старый год обучения текущего факультета
        final Integer minStartYearByFaculty = groupService.findMinStartYearByFaculty(faculty);
        final int countCoursesInThisFacult = (LocalDate.now().getYear() - minStartYearByFaculty);

        // Создаем кнопки клавы
        List<List<KeyboardButton>> keyboard = new ArrayList<>();
        final int size = ((int) (countCoursesInThisFacult / 2)) * 2;
        for (int i = 0; i < size; i += 2) {
            keyboard.add(
                    new ArrayList<>(
                            Arrays.asList(
                                    new KeyboardButton((i + 1) + " курс"),
                                    new KeyboardButton((i + 2) + " курс")
                            )
                    )
            );
        }
        if (countCoursesInThisFacult % 2 != 0) {
            keyboard.add(new ArrayList<>(Collections.singletonList(new KeyboardButton(countCoursesInThisFacult + " курс"))));
        }

        // создаем саму клаву
        ReplyKeyboardMarkup replyKeyboardMarkup
                = ReplyKeyboardMarkup.builder(keyboard)
                .resize_keyboard(true)
                .one_time_keyboard(false)
                .build();
        // создаем объект сообщение
        final SendMessageObject sendMessageObject =
                SendMessageObject.builder(chatId, "Выберите курс")
                        .reply_markup(replyKeyboardMarkup)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        return tgBot.sendMessage(token, sendMessageObject);
    }

    //Выберите Факультет
    private boolean state_2(String token, String chatId) throws Exception {
        final List<Faculty> allFaculty = facultyService.getAllFaculty();
        // Создаем кнопки клавы
        List<List<KeyboardButton>> keyboard = new ArrayList<>();
        for (Faculty faculty : allFaculty) {
            keyboard.add(new ArrayList<>(Arrays.asList(new KeyboardButton(faculty.getFacultyName()))));
        }
        // создаем саму клаву
        ReplyKeyboardMarkup replyKeyboardMarkup
                = ReplyKeyboardMarkup.builder(keyboard)
                .resize_keyboard(true)
                .one_time_keyboard(false)
                .build();
        // создаем объект сообщение
        final SendMessageObject sendMessageObject =
                SendMessageObject.builder(chatId, "Выберите Факультет")
                        .reply_markup(replyKeyboardMarkup)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        return tgBot.sendMessage(token, sendMessageObject);
    }

    // Выберите учебное заведение
    private boolean state_1(String token, String chatId) throws Exception {
        // Создаем кнопки клавы
        List<List<KeyboardButton>> keyboard = new ArrayList<>(Arrays.asList(
                Collections.singletonList(new KeyboardButton("АлтГТУ - Алтайский государственный технический университет"))
        ));
        // создаем саму клаву
        ReplyKeyboardMarkup replyKeyboardMarkup
                = ReplyKeyboardMarkup.builder(keyboard)
                .resize_keyboard(true)
                .one_time_keyboard(false)
                .build();
        // создаем объект сообщение
        final SendMessageObject sendMessageObject =
                SendMessageObject.builder(chatId, "Выберите учебное заведение")
                        .reply_markup(replyKeyboardMarkup)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        return tgBot.sendMessage(token, sendMessageObject);
    }

    private boolean state_0(String token, String chatId) throws Exception {
        // Создаем кнопки клавы
        List<List<KeyboardButton>> keyboard = new ArrayList<>(Arrays.asList(
                Collections.singletonList(new KeyboardButton("Алтайский край"))
        ));
        // создаем саму клаву
        ReplyKeyboardMarkup replyKeyboardMarkup
                = ReplyKeyboardMarkup.builder(keyboard)
                .resize_keyboard(true)
                .one_time_keyboard(false)
                .build();
        // создаем объект сообщение
        final SendMessageObject sendMessageObject =
                SendMessageObject.builder(chatId, "Выберите субъект РФ в котором расположено учебное заведение")
                        .reply_markup(replyKeyboardMarkup)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        return tgBot.sendMessage(token, sendMessageObject);
    }

    private boolean checkCommand(String token, Update update, UserTg userTg) throws Exception {
        final String text = update.getMessage().getText();
        if (text.charAt(0) == '/') {
            // у нас команда
            switch (text) {
                case "/start": {
                    userTg.setState(-1);
                    userTg.setPrev_state(-1);
                    userTg = userTgService.save(userTg);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Сообщаем адрес телеграму, по которому будем принимать от него вебхуки
     *
     * @param token   токен бота
     * @param MY_HOST базовый адрес сервера
     * @return статус, результат запроса
     * @throws JsonProcessingException      JsonProcessingException
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public boolean setWebhook(String token, String MY_HOST) throws JsonProcessingException, UnsupportedEncodingException {
        TgBot tgBot = new TgBot();
        tgBot.setWebhook(token, MY_HOST);
        return true;
    }


    /**
     * Отправляем сообщение пользователю
     *
     * @param token   токен бота, от лица которого выполнится отправка сообщения
     * @param chat_id айдишник чата
     * @param text    отправляемый текст
     * @return статус отправки
     * @throws Exception Exception
     */
    public boolean sendMessage(String token, String chat_id, String text) throws Exception {
        // создаем объект сообщение
        final SendMessageObject sendMessageObject =
                SendMessageObject.builder(chat_id, text)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        return tgBot.sendMessage(token, sendMessageObject);
    }


    /**
     * Отправляет в чат сообщение (настраеваемый объект SendMessage)
     *
     * @param token токен бота
     * @return статус выполнения
     * @throws Exception Exception
     */
    public boolean sendKeyboard(String token)
            throws Exception {
        // Создаем кнопки клавы
        List<List<KeyboardButton>> keyboard = new ArrayList<>(Arrays.asList(
                Collections.singletonList(new KeyboardButton("currentDate")),
                Arrays.asList(new KeyboardButton("tomorrow"), new KeyboardButton("today")),
                Collections.singletonList(new KeyboardButton("kek"))
        ));
        // создаем саму клаву
        ReplyKeyboardMarkup replyKeyboardMarkup
                = ReplyKeyboardMarkup.builder(keyboard)
                .resize_keyboard(true)
                .one_time_keyboard(true)
                .build();
        // создаем объект сообщение
        final SendMessageObject sendMessageObject =
                SendMessageObject.builder("346755292", "text")
                        .reply_markup(replyKeyboardMarkup)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        return tgBot.sendMessage(token, sendMessageObject);
    }


}
