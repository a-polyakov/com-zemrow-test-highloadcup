package com.zemrow.test.highloadcup.entity.constants;

/**
 * Профиль
 * <p>
 * Created by Polyakov Alexandr on 27.08.2017.
 */
public class UserConst {
    /**
     * Профиль
     */
    public static final String TABLE = "user";
    /**
     * уникальный внешний идентификатор пользователя. Устанавливается тестирующей системой и используется затем, для проверки ответов сервера. 32-разрядное целое число.
     */
    public static final String ID = "id";
    /**
     * адрес электронной почты пользователя. Тип - unicode-строка длиной до 100 символов. Гарантируется уникальность.
     */
    public static final String EMAIL = "email";
    /**
     * имя. Тип - unicode-строки длиной до 50 символов.
     */
    public static final String FIRST_NAME = "first_name";
    /**
     * фамилия. Тип - unicode-строки длиной до 50 символов.
     */
    public static final String LAST_NAME = "last_name";
    /**
     * unicode-строка "m" означает мужской пол, а "f" - женский
     */
    public static final String GENDER = "gender";
    /**
     * дата рождения, записанная как число секунд от начала UNIX-эпохи по UTC (другими словами - это timestamp). Ограничено снизу 01.01.1930 и сверху 01.01.1999-ым.
     */
    public static final String BIRTH_DATE = "birth_date";
}
