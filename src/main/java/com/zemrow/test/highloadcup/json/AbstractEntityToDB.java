package com.zemrow.test.highloadcup.json;

import javax.json.stream.JsonParser;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Polyakov Alexandr on 27.08.2017.
 */
public abstract class AbstractEntityToDB<T> {
    public long readAndInsert(Connection connection, JsonParser parser) throws SQLException {
        long size = 0;
        JsonParser.Event event;
        while ((event = parser.next()) == JsonParser.Event.START_OBJECT) {
            final T entity = jsonToEntity(parser, event);
            size++;
            insert(connection, entity);
        }
        return size;
    }

    protected abstract T jsonToEntity(JsonParser parser, JsonParser.Event event);

    protected abstract void insert(Connection connection, T entity) throws SQLException;
}
