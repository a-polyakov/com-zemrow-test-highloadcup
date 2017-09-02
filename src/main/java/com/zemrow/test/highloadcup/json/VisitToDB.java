package com.zemrow.test.highloadcup.json;

import com.zemrow.test.highloadcup.db.VisitDB;
import com.zemrow.test.highloadcup.entity.Visit;

import javax.json.stream.JsonParser;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Polyakov Alexandr on 27.08.2017.
 */
public class VisitToDB extends AbstractEntityToDB<Visit> {
    protected Visit jsonToEntity(JsonParser parser, JsonParser.Event event) {
        if (event == JsonParser.Event.START_OBJECT) {
            final Visit location = new Visit();
            location.read(parser, event);
            return location;
        }
        return null;
    }

    @Override
    protected void insert(Connection connection, Visit entity) throws SQLException {
        VisitDB.insert(connection, entity);
    }
}
