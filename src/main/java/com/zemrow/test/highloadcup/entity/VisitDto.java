package com.zemrow.test.highloadcup.entity;

import com.zemrow.test.highloadcup.entity.constants.LocationConst;
import com.zemrow.test.highloadcup.entity.constants.VisitConst;

import javax.json.stream.JsonGenerator;

/**
 * Посещение
 * <p>
 * Created by Polyakov Alexandr on 27.08.2017.
 */
public class VisitDto implements IJsonWrite {
    /**
     * оценка посещения от 0 до 5 включительно. Целое число.
     */
    public Integer mark;
    /**
     * дата посещения, timestamp с ограничениями: снизу 01.01.2000, а сверху 01.01.2015.
     */
    public Integer visited_at;
    /**
     * описание достопримечательности. Текстовое поле неограниченной длины.
     */
    public String place;

    public void write(final JsonGenerator generator) {
        generator.writeStartObject();
        generator.write(VisitConst.VISITED_AT, visited_at);
        generator.write(VisitConst.MARK, mark);
        generator.write(LocationConst.PLACE, place);
        generator.writeEnd();
    }
}
