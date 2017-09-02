package com.zemrow.test.highloadcup.entity;

import com.zemrow.test.highloadcup.entity.constants.VisitConst;

import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;

/**
 * Посещение
 * <p>
 * Created by Polyakov Alexandr on 27.08.2017.
 */
public class Visit implements IJsonRead, IJsonWrite {
    /**
     * уникальный внешний id посещения. Устанавливается тестирующей системой. 32-разрядное целое число.
     */
    public Integer id;
    /**
     * id достопримечательности. 32-разрядное целое число.
     */
    public Integer location;
    /**
     * id путешественника. 32-разрядное целое число.
     */
    public Integer user;
    /**
     * дата посещения, timestamp с ограничениями: снизу 01.01.2000, а сверху 01.01.2015.
     */
    public Integer visited_at;
    /**
     * оценка посещения от 0 до 5 включительно. Целое число.
     */
    public Integer mark;

    @Override
    public void read(JsonParser parser, JsonParser.Event event) {
        if (event == JsonParser.Event.START_OBJECT) {
            while ((event = parser.next()) == JsonParser.Event.KEY_NAME) {
                String name = parser.getString();
                if ((event = parser.next()) != JsonParser.Event.END_OBJECT) {
                    switch (name) {
                        case VisitConst.ID:
                            if (event == JsonParser.Event.VALUE_NUMBER) {
                                id = parser.getInt();
                            }
                            break;
                        case VisitConst.LOCATION:
                            if (event == JsonParser.Event.VALUE_NUMBER) {
                                location = parser.getInt();
                            }
                            break;
                        case VisitConst.USER:
                            if (event == JsonParser.Event.VALUE_NUMBER) {
                                user = parser.getInt();
                            }
                            break;
                        case VisitConst.VISITED_AT:
                            if (event == JsonParser.Event.VALUE_NUMBER) {
                                visited_at = parser.getInt();
                            }
                            break;
                        case VisitConst.MARK:
                            if (event == JsonParser.Event.VALUE_NUMBER) {
                                mark = parser.getInt();
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

    @Override
    public void write(final JsonGenerator generator) {
        generator.writeStartObject();
        generator.write(VisitConst.ID, id);
        generator.write(VisitConst.LOCATION, location);
        generator.write(VisitConst.USER, user);
        generator.write(VisitConst.VISITED_AT, visited_at);
        generator.write(VisitConst.MARK, mark);
        generator.writeEnd();
    }
}
