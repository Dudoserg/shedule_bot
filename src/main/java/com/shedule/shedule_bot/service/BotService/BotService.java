package com.shedule.shedule_bot.service.BotService;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shedule.shedule_bot.entity.*;
import com.shedule.shedule_bot.entity.Db.Shedule.Faculty;
import com.shedule.shedule_bot.entity.Db.Shedule.Group;
import com.shedule.shedule_bot.entity.Db.Shedule.TimeSubject;
import com.shedule.shedule_bot.entity.Db.UserTg;
import com.shedule.shedule_bot.service.RepoService.*;
import com.shedule.shedule_bot.service.TgBot.CustomFuture.Calendar.TgCalendar;
import com.shedule.shedule_bot.service.TgBot.Db.Entity.UpdateDb;
import com.shedule.shedule_bot.service.TgBot.Db.Service.DbService;
import com.shedule.shedule_bot.service.TgBot.Db.Service.UpdateDbService;
import com.shedule.shedule_bot.service.TgBot.Entity.Update.*;
import com.shedule.shedule_bot.service.TgBot.Methods.EditMessageText_Method;
import com.shedule.shedule_bot.service.TgBot.Methods.SendMessage_Method;
import com.shedule.shedule_bot.service.TgBot.Objects.InlineKeyboardMarkup;
import com.shedule.shedule_bot.service.TgBot.Objects.KeyboardButton;
import com.shedule.shedule_bot.service.TgBot.Objects.ReplyKeyboardMarkup;
import com.shedule.shedule_bot.service.TgBot.Objects.SendMessageResult;
import com.shedule.shedule_bot.service.TgBot.TgBot;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class BotService {
    private static final Logger logger = Logger.getLogger(BotService.class);


    @Autowired
    DbService dbService;

    final
    UserTgServiceImpl userTgService;

    final
    SheduleServiceImpl sheduleService;

    final
    FacultyServiceImpl facultyService;

    final
    GroupServiceImpl groupService;

    private final String BASE_URL = "api.telegram.org";

    public BotService(UserTgServiceImpl userTgService, SheduleServiceImpl sheduleService, FacultyServiceImpl facultyService, GroupServiceImpl groupService, TimeSubjectServiceImpl timeSubjectService) {
        this.userTgService = userTgService;
        this.sheduleService = sheduleService;
        this.facultyService = facultyService;
        this.groupService = groupService;
        this.timeSubjectService = timeSubjectService;
    }


    @Transactional(rollbackFor = Exception.class)
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

//        logger.info(" get message "  + jacksonObjectMapper.writeValueAsString(update));

        Integer state = userTg.getState();
        switch (state) {
            case -1: {
                if (update.getMessage() == null || update.getMessage().getChat() == null || update.getMessage().getChat().getId() == null) {
                    return false;
                }
                final Message message = update.getMessage();
                final Chat chat = message.getChat();
                final String chatId = chat.getId();

                final SendMessageResult result = state_0(token, chatId);
                userTg.updateState(0);
                break;
            }
            case 0: {
                SendMessageResult result = null;
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
                SendMessageResult result = null;
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
                SendMessageResult result = null;
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
                SendMessageResult result = null;
                // в этом состойнии мы должны получить Message, а ни что-то другое
                if (update.getMessage() == null || update.getMessage().getChat() == null || update.getMessage().getChat().getId() == null) {
                    throw new Exception("Данный случай пока что не обработан (state 3)");
                }
                final Message message = update.getMessage();
                final Chat chat = message.getChat();
                final String chatId = chat.getId();
                if (message.getText().equals("Назад")) {
                    state_2(token, chatId);
                    userTg.updateState(2);
                    break;
                }

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
                SendMessageResult result = null;
                // в этом состойнии мы должны получить Message, а ни что-то другое
                if (update.getMessage() == null || update.getMessage().getChat() == null || update.getMessage().getChat().getId() == null) {
                    throw new Exception("Данный случай пока что не обработан (state 4)");
                }
                final Message message = update.getMessage();
                final Chat chat = message.getChat();
                final String chatId = chat.getId();
                if (message.getText().equals("Назад")) {
                    state_3(token, chatId, userTg.getSaveFaculty_1());
                    userTg.updateState(3);
                    break;
                }

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
                        result = state_5(token, chatId);
                        userTg.updateState(5);
                        userTg.setSavedGroup_1(selectedGroup);
                    } else if (userTg.getFlagAnotherGroup()) {
                        // переходим в состояние 8, если flag_anotherGroup == true
                        result = state_8(token, chatId);
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
                SendMessageResult result = null;

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
                        result = state_8(token, chatId);
                        userTg.updateState(8);
                        break;
                    }
                    case "Время пар": {
                        // выводим время пар
                        String str = this.getSchoolTime();
                        sendMessage(token, chatId, str);
                        result = state_5(token, chatId);
                        break;
                    }
//                    case "Расписание другой группы": {
//                        result = state_0(token, chatId);
//                        userTg.setFlagAnotherGroup(true);
//                        userTg.setFlagCurrentGroup(false);
//                        userTg.updateState(0);
//                        break;
//                    }
                    case "Настройки": {
                        result = state_6(token, chatId);
                        userTg.updateState(6);
                        break;
                    }
                    default: {
                        // Говорим что ошибка, заново выводим 5е состояние
                        sendMessage(token, chatId, "Возможно произошла ошибка, пожалуйста, выберите один из пунктов меню:");
                        result = state_5(token, chatId);
                        userTg.updateState(5);
                    }
                }
                break;
            }
            case 6: {
                SendMessageResult result = null;

                // в этом состойнии мы должны получить Message, а ни что-то другое
                if (update.getMessage() == null || update.getMessage().getChat() == null || update.getMessage().getChat().getId() == null) {
                    throw new Exception("Данный случай пока что не обработан (state 5)");
                }
                final Message message = update.getMessage();
                final Chat chat = message.getChat();
                final String chatId = chat.getId();

                final boolean isVerify = this.verifyAnswerByKeyboard(keyboard_state_6, message.getText());
                switch (message.getText()) {
                    case "Назад": {
                        result = state_5(token, chatId);
                        userTg.updateState(5);
                        break;
                    }
                    case "Поменять группу": {
                        result = state_0(token, chatId);
                        userTg.updateState(0);
                        break;
                    }
                    case "Обратная связь": {
                        result = state_7(token, chatId);
                        userTg.updateState(7);
                        break;
                    }
                    case "Автор бота": {
                        sendMessage(token, chatId, " ● @DudoSerg");
                        result = state_6(token, chatId);
                        userTg.updateState(6);
                        break;
                    }
                    default: {
                        sendMessage(token, chatId, "Возможно произошла ошибка, пожалуйста, выберите один из пунктов меню:");
                        result = state_6(token, chatId);
                        break;
                    }
                }
                break;
            }
            case 7: {
                SendMessageResult result = null;
                // в этом состойнии мы должны получить Message, а ни что-то другое
                if (update.getMessage() == null || update.getMessage().getChat() == null || update.getMessage().getChat().getId() == null) {
                    throw new Exception("Данный случай пока что не обработан (state 7)");
                }
                final Message message = update.getMessage();
                final Chat chat = message.getChat();
                final String chatId = chat.getId();
                final boolean isVerified = verifyAnswerByKeyboard(keyboard_state_7, message.getText());
                switch (message.getText()) {
                    case "Назад": {
                        result = state_6(token, chatId);
                        userTg.updateState(6);
                        break;
                    }
                    default: {
                        userTg.getFeedbackList().add(message.getText());
                        sendMessage(token, chatId, "Спасибо! Ваш отзыв поможет улучшить бота.");
                        result = state_6(token, chatId);
                        userTg.updateState(6);
                        break;
                    }
                }
                break;
            }
            case 8: {
                SendMessageResult result = null;

                // в этом состойнии мы должны получить Message, а ни что-то другое
                if (update.getMessage() == null || update.getMessage().getChat() == null || update.getMessage().getChat().getId() == null) {
                    throw new Exception("Данный случай пока что не обработан (state 8)");
                }
                final Message message = update.getMessage();
                final Chat chat = message.getChat();
                final String chatId = chat.getId();

                // валидация ответа
                State_8 state_8_object = new State_8();
                boolean isValidate = state_8_object.verifyPossibleButtons(message.getText());
                if (isValidate) {
                    if (message.getText().equals("Назад")) {
                        result = state_5(token, chatId);
                        userTg.updateState(5);
                        break;
                    }
                    TgBot tgBot = new TgBot();

                    Group groupSheduleShow = null;
                    if (userTg.getFlagCurrentGroup() && !userTg.getFlagAnotherGroup()) {
                        groupSheduleShow = userTg.getSavedGroup_1();
                    } else if (!userTg.getFlagCurrentGroup() && userTg.getFlagAnotherGroup()) {
                        groupSheduleShow = userTg.getSavedAnotherGroup_1();
                    }
                    boolean isExecuted = false;

                    switch (message.getText()) {
                        case "На сегодня": {
                            final SheduleDay sheduleByDate = sheduleService.getSheduleToday(groupSheduleShow);
                            sendShedule(token, userTg, chatId, sheduleByDate);
                            break;
                        }
                        case "На завтра": {
                            final SheduleDay sheduleByDate = sheduleService.getSheduleTomorrow(groupSheduleShow);
                            sendShedule(token, userTg, chatId, sheduleByDate);
                            break;
                        }
                        case "Первая неделя": {
                            final List<SheduleDay> sheduleByDate = sheduleService.getSheduleFirstWeek(groupSheduleShow);
                            sendShedule(token, userTg, chatId, sheduleByDate);
                            break;
                        }
                        case "Вторая неделя": {
                            final List<SheduleDay> sheduleByDate = sheduleService.getSheduleSecondWeek(groupSheduleShow);
                            sendShedule(token, userTg, chatId, sheduleByDate);
                            break;
                        }
                        case "Расписание на конкретную дату": {
                            result = state_9(token, chatId, LocalDate.now());
                            userTg.updateState(9);          // переход в состояние 9
                            userTg.setSaveIdOfMessageWithCalendar_1(result.getResult().getMessage_id());
                            break;
                        }
                    }

                } else {
                    sendMessage(token, chatId, "Возможно произошла ошибка, пожалуйста, повторите ввод.");
                    result = state_8(token, chatId);    // заново отправляем вопрос про день
                }
                break;
            }
            case 9: {
                SendMessageResult result = null;
                // в этом состойнии мы должны получить Callback_query, а ни что-то другое
                if (update.getCallback_query() == null || update.getCallback_query().getFrom() == null
                        || update.getCallback_query().getFrom().getId() == null) {
                    throw new Exception("Данный случай пока что не обработан (state 9)");
                }
                final CallbackQuery callbackQuery = update.getCallback_query();
                final From from = callbackQuery.getFrom();
                final String chatId = from.getId();

                TgCalendar tgCalendar = new TgCalendar();
                final TgCalendar.TgCalendarCommandType type = tgCalendar.getType(callbackQuery);

                switch (type) {
                    case COMMAND: {
                        final InlineKeyboardMarkup inlineKeyboardMarkup = tgCalendar.executeCommand(callbackQuery);
                        result = state_9(token, chatId, inlineKeyboardMarkup, userTg.getSaveIdOfMessageWithCalendar_1());
                        break;
                    }
                    case CONFIRM: {
                        final LocalDate confirmDate = tgCalendar.getConfirmDate(callbackQuery);
                        //sendMessage(token, chatId, "Вы выбрали : " + tgCalendar.buetifyLocalDate(confirmDate));

                        TgBot tgBot = new TgBot();
                        EditMessageText_Method editMessageText_method =
                                EditMessageText_Method.builder(
                                        chatId,
                                        callbackQuery.getMessage().getMessage_id(),
                                        "Вы выбрали : " + tgCalendar.buetifyLocalDate(confirmDate)
                                ).build();
                        final SendMessageResult sendMessageResult = tgBot.editMessageText(token, editMessageText_method);
                        // Выводим расписание по подписке
                        Group groupGorShow = null;
                        if (userTg.getFlagCurrentGroup() == true && userTg.getFlagAnotherGroup() == false) {
                            groupGorShow = userTg.getSavedGroup_1();
                        } else if (userTg.getFlagCurrentGroup() == false && userTg.getFlagAnotherGroup() == true) {
                            groupGorShow = userTg.getSavedAnotherGroup_1();
                        }
                        final SheduleDay sheduleByDate = sheduleService.getSheduleByDate(groupGorShow, confirmDate);

                        final String shedule_str = sheduleByDate.getSheduleDayString();

                        // создаем объект сообщение
                        final SendMessage_Method sendMessageMethod =
                                SendMessage_Method.builder(chatId, shedule_str)
                                        .parse_mode("HTML")
                                        .build();
                        SendMessageResult sendMessageResult_2 = tgBot.sendMessage(token, sendMessageMethod);
                        // переводим опять на главнео меню
                        userTg.updateState(5);
                        result = state_5(token, chatId);


                        break;
                    }
                    case NOTHING: {
                        break;
                    }
                    default: {
                        throw new Exception("Данный случай пока что не обработан (state 9)");
                    }
                }

                break;
            }
            default: {
                //sendMessage(token, chatId, "Я таки не понимаю что ты отправляешь");
                throw new Exception("Данный случай пока что не обработан (default)");
            }
        }
        userTg = userTgService.save(userTg);


        {
            ObjectMapper jacksonObjectMapper = new ObjectMapper();
            jacksonObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            jacksonObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            final String json = jacksonObjectMapper.writeValueAsString(update);
            UpdateDb updateDb = jacksonObjectMapper.readValue(json, UpdateDb.class);
            updateDb = dbService.save(updateDb);
            System.out.println();
        }

        return true;
    }

    List<List<KeyboardButton>> keyboard_state_7 = new ArrayList<>(Collections.singletonList(
            Collections.singletonList(new KeyboardButton("Назад"))
    ));

    private SendMessageResult state_7(String token, String chatId) throws Exception {
        final ReplyKeyboardMarkup replyKeyboardMarkup =
                ReplyKeyboardMarkup.builder(keyboard_state_7)
                        .resize_keyboard(true)
                        .build();
        // создаем объект сообщение
        final SendMessage_Method sendMessageMethod =
                SendMessage_Method.builder(chatId, "Внесите ваше предложение по улучшению бота:")
                        .reply_markup(replyKeyboardMarkup)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        return tgBot.sendMessage(token, sendMessageMethod);
    }


    private SendMessageResult state_9(String token, String chatId, InlineKeyboardMarkup
            inlineKeyboardMarkup, Integer messageId) throws Exception {
        EditMessageText_Method editMessageText_method =
                EditMessageText_Method.builder(chatId, messageId, "Выберите дату:")
                        .reply_markup(inlineKeyboardMarkup)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        final SendMessageResult sendMessageResult = tgBot.editMessageText(token, editMessageText_method);
        return sendMessageResult;
    }

    /**
     * state_9 отправляем календарь
     * // нужно запомнить айдишник сообщения, в котором отправили календарь
     * // нужно запомнить отправленный месяц
     *
     * @param token
     * @param chatId
     * @param l
     * @return
     * @throws Exception
     */
    private SendMessageResult state_9(String token, String chatId, LocalDate l) throws Exception {
        TgCalendar tgCalendar = new TgCalendar();
        InlineKeyboardMarkup inlineKeyboardMarkup = tgCalendar.createKeyboard(l);
        // создаем объект сообщение
        final SendMessage_Method sendMessageMethod =
                SendMessage_Method.builder(chatId, "Выберите дату:")
                        .reply_markup(inlineKeyboardMarkup)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        final SendMessageResult sendMessageResult = tgBot.sendMessage(token, sendMessageMethod);
        return sendMessageResult;
    }

    // state_8 "На какой день вывести расписание?"
    // (в зависимости от флагов выведем расписание той или иной группы)
    private SendMessageResult state_8(String token, String chatId) throws Exception {
        // Создаем кнопки клавы
        List<List<KeyboardButton>> keyboard = new ArrayList<>(Arrays.asList(
                Collections.singletonList(new KeyboardButton("Назад")),
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
        final SendMessage_Method sendMessageMethod =
                SendMessage_Method.builder(chatId, "На какой день вывести расписание?")
                        .reply_markup(replyKeyboardMarkup)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        final SendMessageResult sendMessageResult = tgBot.sendMessage(token, sendMessageMethod);
        return sendMessageResult;
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

    List<List<KeyboardButton>> keyboard_state_6 = new ArrayList<>(Arrays.asList(
            Collections.singletonList(new KeyboardButton("Назад")),
            Collections.singletonList(new KeyboardButton("Поменять группу")),
            Arrays.asList(new KeyboardButton("Обратная связь"), new KeyboardButton("Автор бота"))
    ));

    private SendMessageResult state_6(String token, String chatId) throws Exception {
        // создаем саму клаву
        ReplyKeyboardMarkup replyKeyboardMarkup
                = ReplyKeyboardMarkup.builder(keyboard_state_6)
                .resize_keyboard(true)
                .one_time_keyboard(false)
                .build();
        // создаем объект сообщение
        final SendMessage_Method sendMessageMethod =
                SendMessage_Method.builder(chatId, "Выберите пункт меню:")
                        .reply_markup(replyKeyboardMarkup)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        final SendMessageResult sendMessageResult = tgBot.sendMessage(token, sendMessageMethod);
        return sendMessageResult;
    }


    List<List<KeyboardButton>> keyboard_state_5 = new ArrayList<>(Arrays.asList(
            Collections.singletonList(new KeyboardButton("Расписание моей группы")),
            Collections.singletonList(new KeyboardButton("Время пар")),
//            Collections.singletonList(new KeyboardButton("Расписание другой группы")),
            Collections.singletonList(new KeyboardButton("Настройки"))
    ));

    // state 5 Выберите пункт меню:
    private SendMessageResult state_5(String token, String chatId) throws Exception {
        // создаем саму клаву
        ReplyKeyboardMarkup replyKeyboardMarkup
                = ReplyKeyboardMarkup.builder(keyboard_state_5)
                .resize_keyboard(true)
                .one_time_keyboard(false)
                .build();
        // создаем объект сообщение
        final SendMessage_Method sendMessageMethod =
                SendMessage_Method.builder(chatId, "Выберите пункт меню:")
                        .reply_markup(replyKeyboardMarkup)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        final SendMessageResult sendMessageResult = tgBot.sendMessage(token, sendMessageMethod);
        return sendMessageResult;
    }

    // state 4 Выберите группу
    private SendMessageResult state_4(String token, String chatId, Faculty faculty, Integer course) throws
            Exception {
        //Получаем список групп с заданным факультетом и курсом
        List<Group> groupList = getGroupsByFacultAndCourse(faculty, course);
        // Создаем кнопки клавы
        List<List<KeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Collections.singletonList(new KeyboardButton("Назад")));
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
        final SendMessage_Method sendMessageMethod =
                SendMessage_Method.builder(chatId, "Выберите группу")
                        .reply_markup(replyKeyboardMarkup)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        final SendMessageResult sendMessageResult = tgBot.sendMessage(token, sendMessageMethod);
        return sendMessageResult;
    }


    // state 3 Выберите курс
    private SendMessageResult state_3(String token, String chatId, Faculty faculty) throws Exception {
        // находим самый старый год обучения текущего факультета
        final Integer minStartYearByFaculty = groupService.findMinStartYearByFaculty(faculty);
        final int countCoursesInThisFacult = (LocalDate.now().getYear() - minStartYearByFaculty);

        // Создаем кнопки клавы
        List<List<KeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Collections.singletonList(new KeyboardButton("Назад")));
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
        final SendMessage_Method sendMessageMethod =
                SendMessage_Method.builder(chatId, "Выберите курс")
                        .reply_markup(replyKeyboardMarkup)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        final SendMessageResult sendMessageResult = tgBot.sendMessage(token, sendMessageMethod);
        return sendMessageResult;
    }

    //Выберите Факультет
    private SendMessageResult state_2(String token, String chatId) throws Exception {
        final List<Faculty> allFaculty = facultyService.getAllFaculty();
        allFaculty.sort((o1, o2) -> o1.getFacultyName().compareToIgnoreCase(o2.getFacultyName()));
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
        final SendMessage_Method sendMessageMethod =
                SendMessage_Method.builder(chatId, "Выберите Факультет")
                        .reply_markup(replyKeyboardMarkup)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        final SendMessageResult sendMessageResult = tgBot.sendMessage(token, sendMessageMethod);
        return sendMessageResult;
    }

    // Выберите учебное заведение
    private SendMessageResult state_1(String token, String chatId) throws Exception {
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
        final SendMessage_Method sendMessageMethod =
                SendMessage_Method.builder(chatId, "Выберите учебное заведение")
                        .reply_markup(replyKeyboardMarkup)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        final SendMessageResult sendMessageResult = tgBot.sendMessage(token, sendMessageMethod);
        return sendMessageResult;
    }

    private SendMessageResult state_0(String token, String chatId) throws Exception {
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
        final SendMessage_Method sendMessageMethod =
                SendMessage_Method.builder(chatId, "Выберите субъект РФ в котором расположено учебное заведение")
                        .reply_markup(replyKeyboardMarkup)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        final SendMessageResult sendMessageResult = tgBot.sendMessage(token, sendMessageMethod);
        return sendMessageResult;
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
        if (message != null) {
            final List<MessageEntity> entities = message.getEntities();
            if (entities != null && entities.size() != 0) {
                for (MessageEntity entity : entities) {
                    if (entity.getType().equals("bot_command"))
                        isBot_command = true;
                }
            }
        }
        if (!isBot_command)
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
    public boolean setWebhook(String token, String MY_HOST) throws
            JsonProcessingException, UnsupportedEncodingException {
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
    public SendMessageResult sendMessage(String token, String chat_id, String text) throws Exception {
        // создаем объект сообщение
        final SendMessage_Method sendMessageMethod =
                SendMessage_Method.builder(chat_id, text)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        final SendMessageResult sendMessageResult = tgBot.sendMessage(token, sendMessageMethod);
        return sendMessageResult;
    }


    /**
     * Отправляет в чат сообщение (настраеваемый объект SendMessage)
     *
     * @param token токен бота
     * @return статус выполнения
     * @throws Exception Exception
     */
    public SendMessageResult sendKeyboard(String token)
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
        final SendMessage_Method sendMessageMethod =
                SendMessage_Method.builder("346755292", "text")
                        .reply_markup(replyKeyboardMarkup)
                        .build();
        // посылаем сообщение пользователю
        TgBot tgBot = new TgBot();
        final SendMessageResult sendMessageResult = tgBot.sendMessage(token, sendMessageMethod);
        return sendMessageResult;
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
            final SendMessageResult result = sendMessage(token, chatId,
                    "Здравствуйте, " + userTg.getFirstName() +
                            ", данный бот поможет вам быстро и удобно узнать расписание занятий в вашем учебном заведении!");
        }
        return userTg;
    }

    /**
     * Отправляем расписание пользователю
     *
     * @param token         токен
     * @param userTg        пользователь, которому отправляем сообщение
     * @param chatId        чат
     * @param sheduleByDate расписание
     * @throws Exception ошибка отправки
     */
    private SendMessageResult sendShedule(String token, UserTg userTg, String chatId, SheduleDay sheduleByDate) throws Exception {
        TgBot tgBot = new TgBot();
        SendMessageResult result;// создаем объект сообщение
        final SendMessage_Method sendMessageMethod =
                SendMessage_Method.builder(chatId, sheduleByDate.getSheduleDayString())
                        .parse_mode("HTML")
                        .build();
        SendMessageResult sendMessageResult_2 = tgBot.sendMessage(token, sendMessageMethod);
        // переводим опять на главнео меню
        userTg.updateState(8);
        result = state_8(token, chatId);
        return result;
    }

    /**
     * Отправляем расписание пользователю
     *
     * @param token       токен
     * @param userTg      пользователь, которому отправляем сообщение
     * @param chatId      айдишник чата
     * @param sheduleList расписание на несколько дней
     * @return SendMessageResult
     * @throws Exception Exception
     */
    private SendMessageResult sendShedule(String token, UserTg userTg, String chatId, List<SheduleDay> sheduleList) throws Exception {
        TgBot tgBot = new TgBot();
        SendMessageResult result;// создаем объект сообщение
        final SendMessage_Method sendMessageMethod =
                SendMessage_Method.builder(chatId, SheduleDay.getSheduleWeekString(sheduleList))
                        .parse_mode("HTML")
                        .build();
        SendMessageResult sendMessageResult_2 = tgBot.sendMessage(token, sendMessageMethod);
        // переводим опять на главнео меню
        userTg.updateState(5);
        result = state_5(token, chatId);
        return result;
    }


    final
    TimeSubjectServiceImpl timeSubjectService;

    private String getSchoolTime() {
        final List<TimeSubject> schoolTime = timeSubjectService.getAll();
        schoolTime.sort(Comparator.comparing(TimeSubject::getTimeStart));
        AtomicInteger counter = new AtomicInteger(1);

        final String result = schoolTime.stream()
                .map(timeSubject -> counter.getAndIncrement() + " пара    "
                        + timeSubject.getTimeStartStr() + " - " + timeSubject.getTimeEndStr())
                .collect(Collectors.joining("\n"));
        return result;
    }
}
