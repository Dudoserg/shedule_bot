package com.shedule.shedule_bot.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.shedule.shedule_bot.entity.Faculty;
import com.shedule.shedule_bot.entity.Group;
import com.shedule.shedule_bot.entity.UserTg;
import com.shedule.shedule_bot.service.TgBot.Entity.Update.*;
import com.shedule.shedule_bot.service.TgBot.Methods.SendMessageObject;
import com.shedule.shedule_bot.service.TgBot.Objects.InlineKeyboardButton;
import com.shedule.shedule_bot.service.TgBot.Objects.InlineKeyboardMarkup;
import com.shedule.shedule_bot.service.TgBot.Objects.KeyboardButton;
import com.shedule.shedule_bot.service.TgBot.Objects.ReplyKeyboardMarkup;
import com.shedule.shedule_bot.service.TgBot.TgBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
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
//        if (update.getMessage() == null || update.getMessage().getChat() == null || update.getMessage().getChat().getId() == null) {
//            return false;
//        }
//        final Message message = update.getMessage();
//        final Chat chat = message.getChat();
//        final String chatId = chat.getId();

        // Получаем пользователя по данным из сообщения ( в зависимости от типа, по разному достаем айдишник пользователя )
        UserTg userTg = this.getUser(token, update);

        // команды обрабатываются в первую очередь
        if (checkCommand(token, update, userTg))
            return true;

        Integer state = userTg.getState();
        switch (state) {
            case -1: {
                if (update.getMessage() == null || update.getMessage().getChat() == null || update.getMessage().getChat().getId() == null) {
                    return false;
                }
                final Message message = update.getMessage();
                final Chat chat = message.getChat();
                final String chatId = chat.getId();

                final boolean result = state_0(token, chatId);
                userTg.updateState(0);
                break;
            }
            case 0: {
                boolean result = false;
                // в этом состойнии мы должны получить Message, а ни что-то другое
                if (update.getMessage() == null || update.getMessage().getChat() == null || update.getMessage().getChat().getId() == null) {
                    throw new Exception("Данный случай пока что не обработан (state 0)");
                }
                final Message message = update.getMessage();
                final Chat chat = message.getChat();
                final String chatId = chat.getId();

                if (message.getText().equals("Алтайский край")) {
                    result = state_1(token, chatId);
                    userTg.updateState(1);
                } else {
                    sendMessage(token, chatId, "Возможно произошла ошибка, пожалуйста, повторите ввод.");
                    result = state_0(token, chatId);     // Повторно предлагаем выбрать субъект рф
                }
                break;
            }
            case 1: {
                boolean result = false;
                // в этом состойнии мы должны получить Message, а ни что-то другое
                if (update.getMessage() == null || update.getMessage().getChat() == null || update.getMessage().getChat().getId() == null) {
                    throw new Exception("Данный случай пока что не обработан (state 1)");
                }
                final Message message = update.getMessage();
                final Chat chat = message.getChat();
                final String chatId = chat.getId();

                if (message.getText().equals("АлтГТУ - Алтайский государственный технический университет")) {
                    result = state_2(token, chatId);
                    userTg.updateState(2);
                } else {
                    sendMessage(token, chatId, "Возможно произошла ошибка, пожалуйста, повторите ввод.");
                    result = state_1(token, chatId);     // Повторно предлагаем выбрать учебное заведение
                }
                break;
            }
            case 2: {
                boolean result = false;
                // в этом состойнии мы должны получить Message, а ни что-то другое
                if (update.getMessage() == null || update.getMessage().getChat() == null || update.getMessage().getChat().getId() == null) {
                    throw new Exception("Данный случай пока что не обработан (state 2)");
                }
                final Message message = update.getMessage();
                final Chat chat = message.getChat();
                final String chatId = chat.getId();

                final Faculty faculty = facultyService.getFacultyByName(message.getText());
                if (faculty != null) {
                    result = state_3(token, chatId, faculty);
                    userTg.updateState(3);
                    userTg.setSaveFaculty_1(faculty);
                } else {
                    sendMessage(token, chatId, "Возможно произошла ошибка, пожалуйста, повторите ввод.");
                    result = state_2(token, chatId);
                }
                break;
            }
            case 3: {
                boolean result = false;
                // в этом состойнии мы должны получить Message, а ни что-то другое
                if (update.getMessage() == null || update.getMessage().getChat() == null || update.getMessage().getChat().getId() == null) {
                    throw new Exception("Данный случай пока что не обработан (state 3)");
                }
                final Message message = update.getMessage();
                final Chat chat = message.getChat();
                final String chatId = chat.getId();

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
                    userTg.setSaveCourse_1(selectedCourse);
                } else {
                    sendMessage(token, chatId, "Возможно произошла ошибка, пожалуйста, повторите ввод.");
                    result = state_3(token, chatId, savedFaculty);
                }
                break;
            }
            case 4: {
                boolean result = false;
                // в этом состойнии мы должны получить Message, а ни что-то другое
                if (update.getMessage() == null || update.getMessage().getChat() == null || update.getMessage().getChat().getId() == null) {
                    throw new Exception("Данный случай пока что не обработан (state 4)");
                }
                final Message message = update.getMessage();
                final Chat chat = message.getChat();
                final String chatId = chat.getId();

                final String groupName = message.getText(); // извлекаем из сообщения пользователя текст, в нем должна быть группа
                // проверяем что такой выбор был
                final List<Group> groupsByFacultAndCourse =
                        this.getGroupsByFacultAndCourse(userTg.getSaveFaculty_1(), userTg.getSaveCourse_1());
                // По сообщению от пользователя получаем из базы группу (если существует)
                final Group selectedGroup = groupsByFacultAndCourse.stream()
                        .filter(group -> group.getName().equals(groupName))
                        .findFirst().orElse(null);
                if (selectedGroup != null) {
                    if (userTg.getFlagCurrentGroup()) {
                        // переходим в состояние 5, если flag_currentGroup == true
                        result = state_5(token, chatId, userTg);
                        userTg.updateState(5);
                        userTg.setSavedGroup_1(selectedGroup);
                    } else if (userTg.getFlagAnotherGroup()) {
                        // переходим в состояние 8, если flag_anotherGroup == true
                        result = state_8(token, chatId, userTg);
                        userTg.updateState(8);
                        userTg.setSavedAnotherGroup_1(selectedGroup);
                    }
                } else {
                    // ошибка ввода
                    sendMessage(token, chatId, "Возможно произошла ошибка, пожалуйста, повторите ввод.");
                    result = state_4(token, chatId, userTg.getSaveFaculty_1(), userTg.getSaveCourse_1());
                }
                break;
            }
            case 5: {
                boolean result = false;

                // в этом состойнии мы должны получить Message, а ни что-то другое
                if (update.getMessage() == null || update.getMessage().getChat() == null || update.getMessage().getChat().getId() == null) {
                    throw new Exception("Данный случай пока что не обработан (state 5)");
                }
                final Message message = update.getMessage();
                final Chat chat = message.getChat();
                final String chatId = chat.getId();

                final boolean isVerify = this.verifyAnswerByKeyboard(keyboard_state_5, message.getText());
                switch (message.getText()) {
                    case "Расписание моей группы": {
                        // просто переходим на 8е состояние
                        result = state_8(token, chatId, userTg);
                        userTg.updateState(8);
                        break;
                    }
                    case "Расписание другой группы": {
                        result = state_0(token, chatId);
                        userTg.setFlagAnotherGroup(true);
                        userTg.setFlagCurrentGroup(false);
                        userTg.updateState(0);
                        break;
                    }
                    case "Настройки": {
                        break;
                    }
                }
                break;
            }
            case 8: {
                boolean result;

                // в этом состойнии мы должны получить Message, а ни что-то другое
                if (update.getMessage() == null || update.getMessage().getChat() == null || update.getMessage().getChat().getId() == null) {
                    throw new Exception("Данный случай пока что не обработан (state 8)");
                }
                final Message message = update.getMessage();
                final Chat chat = message.getChat();
                final String chatId = chat.getId();

                // валидация ответа
                boolean isValidate = true;
                if (isValidate) {
                    result = state_9(token, chatId, LocalDate.now());
                    userTg.updateState(9);          // переход в состояние 9
                } else {
                    sendMessage(token, chatId, "Возможно произошла ошибка, пожалуйста, повторите ввод.");
                    result = state_8(token, chatId, userTg);    // заново отправляем вопрос про день
                }
                break;
            }
            case 9: {
                // в этом состойнии мы должны получить Callback_query, а ни что-то другое
                if (update.getCallback_query() == null || update.getCallback_query().getFrom() == null
                        || update.getCallback_query().getFrom().getId() == null) {
                    throw new Exception("Данный случай пока что не обработан (state 9)");
                }
                final CallbackQuery callbackQuery = update.getCallback_query();
                final From from = callbackQuery.getFrom();
                final String chatId = from.getId();
                
                break;
            }
            default: {
                //sendMessage(token, chatId, "Я таки не понимаю что ты отправляешь");
                throw new Exception("Данный случай пока что не обработан (default)");
            }
        }
        userTg = userTgService.save(userTg);
        return true;
    }

    // state_9 отправляем календарь
    // нужно запомнить айдишник сообщения, в котором отправили календарь
    // нужно запомнить отправленный месяц
    private boolean state_9(String token, String chatId, LocalDate l) throws Exception {
        List<List<InlineKeyboardButton>> keyboardList = new ArrayList<>();
        final LocalDate localDate = LocalDate.of(l.getYear(), l.getMonth(), 1);
        String mounth_str = localDate.getMonth().getDisplayName(TextStyle.FULL, new Locale("ru"));
        String year_str = String.valueOf(localDate.getYear());

        keyboardList.add(
                Collections.singletonList(
                        InlineKeyboardButton.createWithCallback_data(
                                mounth_str + " " + year_str,
                                "#calendar#nothing#" + mounth_str + " " + year_str
                        )
                )
        );

        keyboardList.add(
                Arrays.asList(
                        InlineKeyboardButton.createWithCallback_data("Пн", "#calendar#nothing#Пн"),
                        InlineKeyboardButton.createWithCallback_data("Вт", "#calendar#nothing#Вт"),
                        InlineKeyboardButton.createWithCallback_data("Ср", "#calendar#nothing#Ср"),
                        InlineKeyboardButton.createWithCallback_data("Чт", "#calendar#nothing#Чт"),
                        InlineKeyboardButton.createWithCallback_data("Пт", "#calendar#nothing#Пт"),
                        InlineKeyboardButton.createWithCallback_data("Сб", "#calendar#nothing#Сб"),
                        InlineKeyboardButton.createWithCallback_data("Вс", "#calendar#nothing#Вс")
                )
        );
        // пропуски ( которые являются днями прошлого месяца )
        keyboardList.add(new ArrayList<>());
        for (int i = 0; i < localDate.getDayOfWeek().getValue(); i++) {
            keyboardList.get(keyboardList.size() - 1).add(InlineKeyboardButton.createWithCallback_data("-", "#calendar#command#-"));
        }
        LocalDate copyDate = localDate;
        for (int i = 0; i < localDate.lengthOfMonth(); i++) {
            List<InlineKeyboardButton> lastRow = keyboardList.get(keyboardList.size() - 1);
            if (lastRow.size() == 7) {
                keyboardList.add(new ArrayList<>());
                lastRow = keyboardList.get(keyboardList.size() - 1);
            }
            lastRow.add(InlineKeyboardButton.createWithCallback_data(String.valueOf(i + 1), "#calendar#confirm#" + String.valueOf(i + 1)));
        }
        // пропуски чтобы добить до конца недели
        while (keyboardList.get(keyboardList.size() - 1).size() != 7) {
            keyboardList.get(keyboardList.size() - 1).add(InlineKeyboardButton.createWithCallback_data("-", "#calendar#nothing#"));
        }
        // пагинация месяцев
        keyboardList.add(
                Arrays.asList(
                        InlineKeyboardButton.createWithCallback_data("<", "#calendar#command#<" + localDate.toString()),
                        InlineKeyboardButton.createWithCallback_data(">", "#calendar#command#>" + localDate.toString())
                )
        );
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(keyboardList);
        // создаем объект сообщение
        final SendMessageObject sendMessageObject =
                SendMessageObject.builder(chatId, "Выберите дату:")
                        .reply_markup(inlineKeyboardMarkup)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        return tgBot.sendMessage(token, sendMessageObject);

    }

    // state_8 "На какой день вывести расписание?"
    // (в зависимости от флагов выведем расписание той или иной группы)
    private boolean state_8(String token, String chatId, UserTg userTg) throws Exception {
        // Создаем кнопки клавы
        List<List<KeyboardButton>> keyboard = new ArrayList<>(Arrays.asList(
                Arrays.asList(new KeyboardButton("На сегодня"), new KeyboardButton("На завтра")),
                Arrays.asList(new KeyboardButton("Первая неделя"), new KeyboardButton("Вторая неделя")),
                Collections.singletonList(new KeyboardButton("Расписание на конкретную дату"))
        ));
        // создаем саму клаву
        ReplyKeyboardMarkup replyKeyboardMarkup
                = ReplyKeyboardMarkup.builder(keyboard)
                .resize_keyboard(true)
                .one_time_keyboard(true)
                .build();
        // создаем объект сообщение
        final SendMessageObject sendMessageObject =
                SendMessageObject.builder(chatId, "На какой день вывести расписание?")
                        .reply_markup(replyKeyboardMarkup)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        return tgBot.sendMessage(token, sendMessageObject);
    }

    /**
     * Проверяем, правильно ли ответил пользователь
     *
     * @param keyboard List<List<KeyboardButton>> список кнопок, с возможными ответами
     * @param text     String ответ пользователя
     * @return да\нет
     */
    public boolean verifyAnswerByKeyboard(List<List<KeyboardButton>> keyboard, String text) {
        for (List<KeyboardButton> row : keyboard) {
            for (KeyboardButton keyboardButton : row) {
                if (keyboardButton.getText().equals(text))
                    return true;
            }
        }
        return false;
    }

    List<List<KeyboardButton>> keyboard_state_5 = new ArrayList<>(Arrays.asList(
            Collections.singletonList(new KeyboardButton("Расписание моей группы")),
            Collections.singletonList(new KeyboardButton("Расписание другой группы")),
            Collections.singletonList(new KeyboardButton("Настройки"))
    ));

    // state 5 Выберите пункт меню:
    private boolean state_5(String token, String chatId, UserTg usertg) throws Exception {
        // создаем саму клаву
        ReplyKeyboardMarkup replyKeyboardMarkup
                = ReplyKeyboardMarkup.builder(keyboard_state_5)
                .resize_keyboard(true)
                .one_time_keyboard(false)
                .build();
        // создаем объект сообщение
        final SendMessageObject sendMessageObject =
                SendMessageObject.builder(chatId, "Выберите пункт меню:")
                        .reply_markup(replyKeyboardMarkup)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        return tgBot.sendMessage(token, sendMessageObject);
    }

    // state 4 Выберите группу
    private boolean state_4(String token, String chatId, Faculty faculty, Integer course) throws Exception {
        //Получаем список групп с заданным факультетом и курсом
        List<Group> groupList = getGroupsByFacultAndCourse(faculty, course);
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
        keyboard.add(lastRow);

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

    /**
     * Получаем список групп с заданным факультетом и курсом
     *
     * @param faculty Faculty факультет
     * @param course  Integer курс
     * @return List<Group> список групп
     */
    private List<Group> getGroupsByFacultAndCourse(Faculty faculty, Integer course) {
        // Получаем все группы по данному факультету ( )
        List<Group> groupList = new ArrayList<>(faculty.getGroupSet());
        // Фильтруем группы по курсу
        groupList = groupList.stream()
                .filter(group -> (LocalDate.now().getYear() - group.getStartYear()) == course)
                .collect(Collectors.toList());
        // Сортируем группы по названию
        groupList = groupList.stream()
                .sorted((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()))
                .collect(Collectors.toList());
        return groupList;
    }


    private boolean checkCommand(String token, Update update, UserTg userTg) throws Exception {
        boolean isBot_command = false;
        final Message message = update.getMessage();
        // проверяем, является ли данное сообщение bot_command
        if(message != null){
            final List<MessageEntity> entities = message.getEntities();
            if(entities != null && entities.size() != 0){
                for (MessageEntity entity : entities) {
                    if (entity.getType().equals("bot_command"))
                        isBot_command = true;
                }
            }
        }
        if( !isBot_command)
            return false;
        final String text = message.getText();
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

    private UserTg getUser(String token, Update update) throws Exception {
        String chatId = null;
        String first_name = null;
        String last_name = null;
        String username = null;
        if (update.getMessage() != null) {
            if (update.getMessage().getChat() != null) {
                chatId = update.getMessage().getChat().getId();
                first_name = update.getMessage().getChat().getFirst_name();
                last_name = update.getMessage().getChat().getLast_name();
                username = update.getMessage().getChat().getUsername();
            } else {
                throw new Exception("'Message' != null but 'Chat' == null");
            }
        } else if (update.getCallback_query() != null) {
            final From from = update.getCallback_query().getFrom();
            if (from != null) {
                chatId = from.getId();
                first_name = from.getFirst_name();
                last_name = from.getLast_name();
                username = from.getUsername();
            } else {
                throw new Exception("'Message' == null, 'Callback_query' != null, but 'From' == null");
            }
        }
        assert chatId != null;
        assert first_name != null;
        assert last_name != null;
        assert username != null;
        UserTg userTg = userTgService.getUserByChatId(chatId).orElse(null);
        // пользователь не существует
        if (userTg == null) {
            userTg = new UserTg();
            userTg.setChatId(chatId);
            userTg.setFirstName(first_name);
            userTg.setLastName(last_name);
            userTg.setUsername(username);

            userTg = userTgService.save(userTg);
            // отправляем пользователю преветственное сообщение
            final boolean result = sendMessage(token, chatId,
                    "Здравствуйте, " + userTg.getUsername() +
                            ", данный бот поможет вам быстро и удобно узнать расписание занятий в вашем учебном заведении!");
        }
        return userTg;
    }
}
