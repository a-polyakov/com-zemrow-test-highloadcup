package com.zemrow.test.highloadcup.entity;

import com.zemrow.test.highloadcup.entity.constants.GeneralEnum;
import com.zemrow.test.highloadcup.entity.constants.UserConst;

import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;

/**
 * Профиль
 * <p>
 * Created by Polyakov Alexandr on 27.08.2017.
 */
public class User implements IJsonRead, IJsonWrite {
    /**
     * уникальный внешний идентификатор пользователя. Устанавливается тестирующей системой и используется затем, для проверки ответов сервера. 32-разрядное целое число.
     */
    public Integer id;
    /**
     * адрес электронной почты пользователя. Тип - unicode-строка длиной до 100 символов. Гарантируется уникальность.
     */
    public String email;
    /**
     * имя. Тип - unicode-строки длиной до 50 символов.
     */
    public String first_name;
    /**
     * фамилия. Тип - unicode-строки длиной до 50 символов.
     */
    public String last_name;
    /**
     * unicode-строка "m" означает мужской пол, а "f" - женский
     */
    public GeneralEnum gender;
    /**
     * дата рождения, записанная как число секунд от начала UNIX-эпохи по UTC (другими словами - это timestamp). Ограничено снизу 01.01.1930 и сверху 01.01.1999-ым.
     */
    public Integer birth_date;

    @Override
    public void read(JsonParser parser, JsonParser.Event event) {
        if (event == JsonParser.Event.START_OBJECT) {
            while ((event = parser.next()) == JsonParser.Event.KEY_NAME) {
                String name = parser.getString();
                if ((event = parser.next()) != JsonParser.Event.END_OBJECT) {
                    switch (name) {
                        case UserConst.ID:
                            if (event == JsonParser.Event.VALUE_NUMBER) {
                                id = parser.getInt();
                            }
                            break;
                        case UserConst.EMAIL:
                            if (event == JsonParser.Event.VALUE_STRING) {
                                email = parser.getString();
                            }
                            break;
                        case UserConst.FIRST_NAME:
                            if (event == JsonParser.Event.VALUE_STRING) {
                                first_name = parser.getString();
                            }
                            break;
                        case UserConst.LAST_NAME:
                            if (event == JsonParser.Event.VALUE_STRING) {
                                last_name = parser.getString();
                            }
                            break;
                        case UserConst.GENDER:
                            if (event == JsonParser.Event.VALUE_STRING) {
                                gender = GeneralEnum.valueOf(parser.getString());
                            }
                            break;
                        case UserConst.BIRTH_DATE:
                            if (event == JsonParser.Event.VALUE_NUMBER) {
                                birth_date = parser.getInt();
                            }
                            break;
                        default:
                            throw new RuntimeException("unknown field " + name);
                    }
                } else {
                    throw new RuntimeException("incorrect object");
                }
            }
            if (event != JsonParser.Event.END_OBJECT) {
                throw new RuntimeException("incorrect json");
            }
        }
    }

    public void write(final JsonGenerator generator) {
        generator.writeStartObject();
        generator.write(UserConst.ID, id);
        generator.write(UserConst.EMAIL, email);
        generator.write(UserConst.FIRST_NAME, first_name);
        generator.write(UserConst.LAST_NAME, last_name);
        if ((gender != null)) {
            generator.write(UserConst.GENDER, gender.name());
        }
        generator.write(UserConst.BIRTH_DATE, birth_date);
        generator.writeEnd();
    }
}
