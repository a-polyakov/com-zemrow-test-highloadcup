package com.zemrow.test.highloadcup.json;

import com.zemrow.test.highloadcup.db.LocationDB;
import com.zemrow.test.highloadcup.entity.Location;

import javax.json.stream.JsonParser;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Polyakov Alexandr on 27.08.2017.
 */
public class LocationToDB extends AbstractEntityToDB<Location> {
    @Override
    public void insert(Connection connection, Location location) throws SQLException {
        LocationDB.insert(connection, location);
    }

    protected Location jsonToEntity(JsonParser parser, JsonParser.Event event) {
        if (event == JsonParser.Event.START_OBJECT) {
            final Location location = new Location();
            location.read(parser, event);
            return location;
        }
        return null;
    }
}
