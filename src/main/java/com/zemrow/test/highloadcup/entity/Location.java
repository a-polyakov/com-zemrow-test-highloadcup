package com.zemrow.test.highloadcup.entity;

import com.zemrow.test.highloadcup.entity.constants.LocationConst;

import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;

/**
 * Достопримечательность
 * <p>
 * Created by Polyakov Alexandr on 27.08.2017.
 */
public class Location implements IJsonRead, IJsonWrite {
    /**
     * уникальный внешний id достопримечательности. Устанавливается тестирующей системой. 32-разрядное целое число.
     */
    public Integer id;
    /**
     * описание достопримечательности. Текстовое поле неограниченной длины.
     */
    public String place;
    /**
     * название страны расположения. unicode-строка длиной до 50 символов.
     */
    public String country;
    /**
     * название города расположения. unicode-строка длиной до 50 символов.
     */
    public String city;
    /**
     * расстояние от города по прямой в километрах. 32-разрядное целое число.
     */
    public Integer distance;

    @Override
    public void read(JsonParser parser, JsonParser.Event event) {
        if (event == JsonParser.Event.START_OBJECT) {
            while ((event = parser.next()) == JsonParser.Event.KEY_NAME) {
                String name = parser.getString();
                if ((event = parser.next()) != JsonParser.Event.END_OBJECT) {
                    switch (name) {
                        case LocationConst.ID:
                            if (event == JsonParser.Event.VALUE_NUMBER) {
                                id = parser.getInt();
                            }
                            break;
                        case LocationConst.PLACE:
                            if (event == JsonParser.Event.VALUE_STRING) {
                                place = parser.getString();
                            }
                            break;
                        case LocationConst.COUNTRY:
                            if (event == JsonParser.Event.VALUE_STRING) {
                                country = parser.getString();
                            }
                            break;
                        case LocationConst.CITY:
                            if (event == JsonParser.Event.VALUE_STRING) {
                                city = parser.getString();
                            }
                            break;
                        case LocationConst.DISTANCE:
                            if (event == JsonParser.Event.VALUE_NUMBER) {
                                distance = parser.getInt();
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
        generator.write(LocationConst.ID, id);
        generator.write(LocationConst.PLACE, place);
        generator.write(LocationConst.COUNTRY, country);
        generator.write(LocationConst.CITY, city);
        generator.write(LocationConst.DISTANCE, distance);
        generator.writeEnd();
    }
}
