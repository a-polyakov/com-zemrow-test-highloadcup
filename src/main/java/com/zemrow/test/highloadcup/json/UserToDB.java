package com.zemrow.test.highloadcup.json;

import com.zemrow.test.highloadcup.db.UserDB;
import com.zemrow.test.highloadcup.entity.User;

import javax.json.stream.JsonParser;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Polyakov Alexandr on 27.08.2017.
 */
public class UserToDB extends AbstractEntityToDB<User> {

    protected User jsonToEntity(JsonParser parser, JsonParser.Event event) {
        if (event == JsonParser.Event.START_OBJECT) {
            final User user = new User();
            user.read(parser, event);
            return user;
        }
        return null;
    }

    @Override
    protected void insert(Connection connection, User entity) throws SQLException {
        UserDB.insert(connection, entity);
    }
}
