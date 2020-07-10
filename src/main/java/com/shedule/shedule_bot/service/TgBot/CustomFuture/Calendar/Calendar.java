package com.shedule.shedule_bot.service.TgBot.CustomFuture.Calendar;

import com.shedule.shedule_bot.service.TgBot.Methods.SendMessageObject;
import com.shedule.shedule_bot.service.TgBot.Objects.InlineKeyboardButton;
import com.shedule.shedule_bot.service.TgBot.Objects.InlineKeyboardMarkup;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

public class Calendar {

    private String separator = "#";




    /**
     * Создаем клавиатуру - календарь
     * @param x дата с месяцем и годом, на которую будет создана клавиатура
     * @return готовая к отправке клавиатура
     */
    public InlineKeyboardMarkup createKeyboard(LocalDate x) {
        List<List<InlineKeyboardButton>> keyboardList = new ArrayList<>();

        final LocalDate localDate = LocalDate.of(x.getYear(), x.getMonth(), 1);
        String mounth_str = localDate.getMonth().getDisplayName(TextStyle.FULL, new Locale("ru"));
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
        for (int i = 0; i < localDate.getDayOfWeek().getValue(); i++) {
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
                            String.valueOf(currentButtonLocalDate.getDayOfWeek().getValue()),
                            createConfirmCallback(currentButtonLocalDate))
            );
            currentButtonLocalDate = currentButtonLocalDate.plusDays(1);
        }
        // пропуски чтобы добить до конца недели
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
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(keyboardList);
        return inlineKeyboardMarkup;
    }

    /**
     * Создаем Сообщение с клавиатурой - календарем
     * @param x дата с месяцем и годом, на которую будет создана клавиатура
     * @param chatId куда будем отправлять сообщение
     * @param messageText текст сообщения
     */
    public void createCalendar(LocalDate x, String chatId, String messageText) {
        InlineKeyboardMarkup inlineKeyboardMarkup = this.createKeyboard(x);
        // создаем объект сообщение
        final SendMessageObject sendMessageObject =
                SendMessageObject.builder(chatId, messageText)
                        .reply_markup(inlineKeyboardMarkup)
                        .build();
    }

    //////////////////////////////////////////////////////////////////////////

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
        return this.createCallback("nothing");
    }

    /**
     * создаем строку callback_data {@link InlineKeyboardButton#setCallback_data(String)},
     * которая подразумевает выполнение команды
     *
     * @param text пояснение к команде
     * @return callback_data
     */
    private String createCommandCallback(String text) {
        return this.createCallback("command", text);
    }

    /**
     * создаем строку callback_data {@link InlineKeyboardButton#setCallback_data(String)},
     * которая должна вернуть дату
     *
     * @param date дата
     * @return callback_data
     */
    private String createConfirmCallback(LocalDate date) {
        return this.createCallback("confirm", date.toString());
    }

}
