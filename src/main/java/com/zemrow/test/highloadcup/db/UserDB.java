package com.zemrow.test.highloadcup.db;

import com.zemrow.test.highloadcup.entity.User;
import com.zemrow.test.highloadcup.entity.constants.GeneralEnum;
import com.zemrow.test.highloadcup.entity.constants.UserConst;

import javax.json.JsonObject;
import java.sql.*;


/**
 * Created by Polyakov Alexandr on 27.08.2017.
 */
public class UserDB {
    public static void createTable(Connection connection) throws SQLException {
        try (final Statement statement = connection.createStatement()) {
            statement.execute("CREATE table " + UserConst.TABLE + " (" +
                    UserConst.ID + " INT PRIMARY KEY," +
                    UserConst.EMAIL + " VARCHAR (100) NOT NULL UNIQUE," +
                    UserConst.FIRST_NAME + "  VARCHAR (50) NOT NULL," +
                    UserConst.LAST_NAME + " VARCHAR (50) NOT NULL," +
                    UserConst.GENDER + " CHAR (1) NOT NULL," +
                    UserConst.BIRTH_DATE + " INT NOT NULL" +
                    ")");
        }
    }

    public static int insert(Connection connection, User user) throws SQLException {
        try (final PreparedStatement statement = connection.prepareStatement("" +
                "insert into " + UserConst.TABLE + "(" +
                UserConst.ID + " ," +
                UserConst.EMAIL + " ," +
                UserConst.FIRST_NAME + " ," +
                UserConst.LAST_NAME + " ," +
                UserConst.GENDER + " ," +
                UserConst.BIRTH_DATE + " " +
                ") VALUES (?,?,?,?,?,?)")) {
            statement.setObject(1, user.id);
            statement.setObject(2, user.email);
            statement.setObject(3, user.first_name);
            statement.setObject(4, user.last_name);
            statement.setObject(5, (user.gender != null) ? user.gender.name() : null);
            statement.setObject(6, user.birth_date);
            return statement.executeUpdate();
        }
    }

    public static User select(Connection connection, Integer id) throws SQLException {
        try (final PreparedStatement statement = connection.prepareStatement(
                "select " +
                        UserConst.ID + " ," +
                        UserConst.EMAIL + " ," +
                        UserConst.FIRST_NAME + " ," +
                        UserConst.LAST_NAME + " ," +
                        UserConst.GENDER + " ," +
                        UserConst.BIRTH_DATE +
                        " from " + UserConst.TABLE +
                        " where " + UserConst.ID + " = ?")) {
            User result = null;
            statement.setObject(1, id);
            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = new User();
                result.id = resultSet.getInt(1);
                result.email = resultSet.getString(2);
                result.first_name = resultSet.getString(3);
                result.last_name = resultSet.getString(4);
                result.gender = GeneralEnum.valueOf(resultSet.getString(5));
                result.birth_date = resultSet.getInt(6);

            }
            return result;
        }
    }

    public static int update(Connection connection, Integer id, JsonObject jsonObject) throws SQLException {
        int result;
        boolean findUpdateField = false;
        final StringBuilder sql = new StringBuilder("UPDATE " + UserConst.TABLE + " set ");
        if (jsonObject.containsKey(UserConst.EMAIL)) {
            sql.append(" " + UserConst.EMAIL + "=?");
            findUpdateField = true;
        }
        if (jsonObject.containsKey(UserConst.FIRST_NAME)) {
            if (findUpdateField) {
                sql.append(',');
            }
            sql.append(" " + UserConst.FIRST_NAME + "=?");
            findUpdateField = true;
        }
        if (jsonObject.containsKey(UserConst.LAST_NAME)) {
            if (findUpdateField) {
                sql.append(',');
            }
            sql.append(" " + UserConst.LAST_NAME + "=?");
            findUpdateField = true;
        }
        if (jsonObject.containsKey(UserConst.GENDER)) {
            if (findUpdateField) {
                sql.append(',');
            }
            sql.append(" " + UserConst.GENDER + "=?");
            findUpdateField = true;
        }
        if (jsonObject.containsKey(UserConst.BIRTH_DATE)) {
            if (findUpdateField) {
                sql.append(',');
            }
            sql.append(" " + UserConst.BIRTH_DATE + "=?");
            findUpdateField = true;
        }
        sql.append(" where " + UserConst.ID + "=?");
        if (findUpdateField) {
            try (final PreparedStatement statement = connection.prepareStatement(sql.toString())) {
                int i = 1;
                if (jsonObject.containsKey(UserConst.EMAIL)) {
                    statement.setString(i++, jsonObject.getString(UserConst.EMAIL));
                }
                if (jsonObject.containsKey(UserConst.FIRST_NAME)) {
                    statement.setString(i++, jsonObject.getString(UserConst.FIRST_NAME));
                }
                if (jsonObject.containsKey(UserConst.LAST_NAME)) {
                    statement.setString(i++, jsonObject.getString(UserConst.LAST_NAME));
                }
                if (jsonObject.containsKey(UserConst.GENDER)) {
                    statement.setString(i++, jsonObject.getString(UserConst.GENDER));
                }
                if (jsonObject.containsKey(UserConst.BIRTH_DATE)) {
                    statement.setInt(i++, jsonObject.getInt(UserConst.BIRTH_DATE));
                }
                statement.setInt(i++, id);
                result = statement.executeUpdate();
            }
        } else {
            result = -1;
        }
        return result;
    }
}
