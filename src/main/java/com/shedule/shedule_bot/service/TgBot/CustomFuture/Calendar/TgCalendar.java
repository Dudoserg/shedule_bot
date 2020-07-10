package com.shedule.shedule_bot.service.TgBot.CustomFuture.Calendar;

import com.shedule.shedule_bot.service.TgBot.Entity.Update.CallbackQuery;
import com.shedule.shedule_bot.service.TgBot.Methods.SendMessage_Method;
import com.shedule.shedule_bot.service.TgBot.Objects.InlineKeyboardButton;
import com.shedule.shedule_bot.service.TgBot.Objects.InlineKeyboardMarkup;

import java.time.LocalDate;
import java.util.*;

public class TgCalendar {

    public enum TgCalendarCommandType {
        COMMAND("COMMAND"),
        NOTHING("NOTHING"),
        CONFIRM("CONFIRM");

        private String str;

        public String getStr() {
            return str;
        }

        TgCalendarCommandType(String str) {
            this.str = str;
        }

        public static TgCalendarCommandType getTypeByStr(String s) {
            for (TgCalendarCommandType value : TgCalendarCommandType.values()) {
                if (value.getStr().equals(s))
                    return value;
            }
            return null;
        }
    }

    private String separator = "#";

    private List<String> mounthTitleList = new ArrayList<>(
            Arrays.asList("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь")
    );

    public void changeSeparator(String str) {
        this.separator = str;
    }


    /**
     * Создаем клавиатуру - календарь
     *
     * @param x дата с месяцем и годом, на которую будет создана клавиатура
     * @return готовая к отправке клавиатура
     */
    public InlineKeyboardMarkup createKeyboard(LocalDate x) {
        List<List<InlineKeyboardButton>> keyboardList = new ArrayList<>();

        final LocalDate localDate = LocalDate.of(x.getYear(), x.getMonth(), 1);
        String mounth_str = this.mounthTitleList.get(localDate.getMonth().getValue() - 1);
        String year_str = String.valueOf(localDate.getYear());

        keyboardList.add(
                Collections.singletonList(
                        InlineKeyboardButton.createWithCallback_data(
                                mounth_str + " " + year_str,
                                createNothingCallback(mounth_str + " " + year_str)
                        )
                )
        );

        keyboardList.add(
                Arrays.asList(
                        InlineKeyboardButton.createWithCallback_data("Пн", createNothingCallback("Пн")),
                        InlineKeyboardButton.createWithCallback_data("Вт", createNothingCallback("Вт")),
                        InlineKeyboardButton.createWithCallback_data("Ср", createNothingCallback("Ср")),
                        InlineKeyboardButton.createWithCallback_data("Чт", createNothingCallback("Чт")),
                        InlineKeyboardButton.createWithCallback_data("Пт", createNothingCallback("Пт")),
                        InlineKeyboardButton.createWithCallback_data("Сб", createNothingCallback("Сб")),
                        InlineKeyboardButton.createWithCallback_data("Вс", createNothingCallback("Вс"))
                )
        );

        // пропуски ( которые являются днями прошлого месяца )
        keyboardList.add(new ArrayList<>());
        for (int i = 0; i < localDate.getDayOfWeek().getValue() - 1; i++) {
            keyboardList.get(keyboardList.size() - 1).add(
                    InlineKeyboardButton.createWithCallback_data("-", createNothingCallback("-"))
            );
        }
        // числа
        LocalDate currentButtonLocalDate = localDate.plusDays(0);
        for (int i = 0; i < localDate.lengthOfMonth(); i++) {
            List<InlineKeyboardButton> lastRow = keyboardList.get(keyboardList.size() - 1);
            if (lastRow.size() == 7) {
                keyboardList.add(new ArrayList<>());
                lastRow = keyboardList.get(keyboardList.size() - 1);
            }
            lastRow.add(
                    InlineKeyboardButton.createWithCallback_data(
                            String.valueOf(currentButtonLocalDate.getDayOfMonth()),
                            createConfirmCallback(currentButtonLocalDate))
            );
            currentButtonLocalDate = currentButtonLocalDate.plusDays(1);
        }
        // пропуски чтобы добить до конца последней недели
        while (keyboardList.get(keyboardList.size() - 1).size() != 7) {
            keyboardList.get(
                    keyboardList.size() - 1).add(InlineKeyboardButton.createWithCallback_data("-", createNothingCallback("-"))
            );
        }
        // пагинация месяцев
        keyboardList.add(
                Arrays.asList(
                        InlineKeyboardButton.createWithCallback_data("<", createCommandCallback("<" + localDate.toString())),
                        InlineKeyboardButton.createWithCallback_data(">", createCommandCallback(">" + localDate.toString()))
                )
        );
        return new InlineKeyboardMarkup(keyboardList);
    }

    /**
     * Создаем Сообщение с клавиатурой - календарем
     *
     * @param x           дата с месяцем и годом, на которую будет создана клавиатура
     * @param chatId      куда будем отправлять сообщение
     * @param messageText текст сообщения
     */
    public void createCalendar(LocalDate x, String chatId, String messageText) {
        InlineKeyboardMarkup inlineKeyboardMarkup = this.createKeyboard(x);
        // создаем объект сообщение
        final SendMessage_Method sendMessageMethod =
                SendMessage_Method.builder(chatId, messageText)
                        .reply_markup(inlineKeyboardMarkup)
                        .build();
    }

    /**
     * Получить результат в виде строки
     *
     * @param callbackQuery CallbackQuery
     * @return результат в виде строки
     */
    public String getResult(CallbackQuery callbackQuery) {
        return callbackQuery.getData();
    }

    /**
     * Получаем тип нажатой кнопки (Команда, Ничего, выбрана дата)
     *
     * @param callbackQuery CallbackQuery
     * @return TgCalendarCommandType
     */
    public TgCalendarCommandType getType(CallbackQuery callbackQuery) {
        final String result = getResult(callbackQuery);
        final String[] split = result.split("#");
        final String type_str = split[2].toUpperCase();
        return TgCalendarCommandType.getTypeByStr(type_str);
    }

    public InlineKeyboardMarkup executeCommand(CallbackQuery callbackQuery) throws Exception {
        final TgCalendarCommandType type = getType(callbackQuery);
        final String command = getResult(callbackQuery);

        if (type != TgCalendarCommandType.COMMAND)
            throw new Exception(command + " this is not command!");

        final String body = this.getCommandBody(command);

        if (body.charAt(0) == '>') {
            // на месяц в перед
            final String dateStr = body.substring(1, body.length());
            LocalDate localDate = LocalDate.parse(dateStr);
            localDate = localDate.plusMonths(1);
            return this.createKeyboard(localDate);
        } else if (body.charAt(0) == '<') {
            // на месяц в перед
            final String dateStr = body.substring(1, body.length());
            LocalDate localDate = LocalDate.parse(dateStr);
            localDate = localDate.minusMonths(1);
            return this.createKeyboard(localDate);
        } else
            throw new Exception("unknown command!");
    }

    public LocalDate getConfirmDate(CallbackQuery callbackQuery) throws Exception {
        final TgCalendarCommandType type = getType(callbackQuery);
        final String command = getResult(callbackQuery);

        if (type != TgCalendarCommandType.CONFIRM)
            throw new Exception(command + " this is not CONFIRM!");

        final String body = this.getConfirmBody(command);
        LocalDate localDate = LocalDate.parse(body);
        return localDate;
    }

    public String buetifyLocalDate(LocalDate localDate) {
        final String mounth_str = this.getTitleOfMounth(localDate);
        return localDate.getDayOfMonth() + " " + mounth_str + " " + localDate.getYear() + " " + "года";
    }

    //////////////////////////////////////////////////////////////////////////
    private String getTitleOfMounth(LocalDate localDate) {
        return this.mounthTitleList.get(localDate.getMonthValue() - 1);
    }

    // тело команды
    private String getCommandBody(String command) {
        final String[] split = command.split("#");
        final String body = split[3];
        return body;
    }

    // тело подтверждения
    private String getConfirmBody(String command) {
        final String[] split = command.split("#");
        final String body = split[3];
        return body;
    }

    private String createCallback(String str, String text) {
        return separator + "calendar" + separator + str + separator + text;
    }

    private String createCallback(String str) {
        return separator + "calendar" + separator + str + separator;

    }

    /**
     * создаем строку callback_data {@link InlineKeyboardButton#setCallback_data(String)}, на которую не нужно выполнять никаких действий
     *
     * @param text пояснение к команде( не обязательно )
     * @return callback_data
     */
    private String createNothingCallback(String text) {
        return this.createCallback("nothing", text);
    }

    /**
     * создаем строку callback_data {@link InlineKeyboardButton#setCallback_data(String)}, на которую не нужно выполнять никаких действий
     *
     * @return callback_data
     */
    private String createNothingCallback() {
        return this.createCallback(TgCalendarCommandType.NOTHING.toString());
    }

    /**
     * создаем строку callback_data {@link InlineKeyboardButton#setCallback_data(String)},
     * которая подразумевает выполнение команды
     *
     * @param text пояснение к команде
     * @return callback_data
     */
    private String createCommandCallback(String text) {
        return this.createCallback(TgCalendarCommandType.COMMAND.toString(), text);
    }

    /**
     * создаем строку callback_data {@link InlineKeyboardButton#setCallback_data(String)},
     * которая должна вернуть дату
     *
     * @param date дата
     * @return callback_data
     */
    private String createConfirmCallback(LocalDate date) {
        return this.createCallback(TgCalendarCommandType.CONFIRM.toString(), date.toString());
    }

}
