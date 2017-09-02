package com.zemrow.test.highloadcup.db;

import com.zemrow.test.highloadcup.entity.Location;
import com.zemrow.test.highloadcup.entity.constants.GeneralEnum;
import com.zemrow.test.highloadcup.entity.constants.LocationConst;
import com.zemrow.test.highloadcup.entity.constants.UserConst;
import com.zemrow.test.highloadcup.entity.constants.VisitConst;

import javax.json.JsonObject;
import java.sql.*;


/**
 * Created by Polyakov Alexandr on 27.08.2017.
 */
public class LocationDB {
    public static void createTable(Connection connection) throws SQLException {
        try (final Statement statement = connection.createStatement()) {
            statement.execute("CREATE table " + LocationConst.TABLE + " (" +
                    LocationConst.ID + " INT PRIMARY KEY," +
                    LocationConst.PLACE + " TEXT NOT NULL," +
                    LocationConst.COUNTRY + " VARCHAR (50) NOT NULL," +
                    LocationConst.CITY + " VARCHAR (50) NOT NULL," +
                    LocationConst.DISTANCE + " INT NOT NULL" +
                    ")");
        }
    }

    public static int insert(Connection connection, Location location) throws SQLException {
        try (final PreparedStatement statement = connection.prepareStatement("" +
                "insert into " + LocationConst.TABLE + "(" +
                LocationConst.ID + " ," +
                LocationConst.PLACE + " ," +
                LocationConst.COUNTRY + " ," +
                LocationConst.CITY + " ," +
                LocationConst.DISTANCE + " " +
                ") VALUES (?,?,?,?,?)")) {
            statement.setObject(1, location.id);
            statement.setObject(2, location.place);
            statement.setObject(3, location.country);
            statement.setObject(4, location.city);
            statement.setObject(5, location.distance);
            return statement.executeUpdate();
        }
    }

    public static Location select(Connection connection, Integer id) throws SQLException {
        try (final PreparedStatement statement = connection.prepareStatement(
                "select " +
                        LocationConst.ID + " ," +
                        LocationConst.PLACE + " ," +
                        LocationConst.COUNTRY + " ," +
                        LocationConst.CITY + " ," +
                        LocationConst.DISTANCE +
                        " from " + LocationConst.TABLE +
                        " where " + LocationConst.ID + " = ?")) {
            Location result = null;
            statement.setObject(1, id);
            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = new Location();
                result.id = resultSet.getInt(1);
                result.place = resultSet.getString(2);
                result.country = resultSet.getString(3);
                result.city = resultSet.getString(4);
                result.distance = resultSet.getInt(5);
            }
            return result;
        }
    }

    public static Double avg(Connection connection, Integer locationId, Integer fromDate, Integer toDate, Integer fromAge, Integer toAge, GeneralEnum gender) throws SQLException {
        String sql = "select " +
                "avg(v." + VisitConst.MARK + ")" +
                " from " + LocationConst.TABLE + " l" +
                " join " + VisitConst.TABLE + " v on v." + VisitConst.LOCATION + "=l." + LocationConst.ID;
        if (fromAge != null || toAge != null || gender != null) {
            sql += " join " + UserConst.TABLE + " u on u." + UserConst.ID + "=v." + VisitConst.USER;
        }
        sql += " where l." + LocationConst.ID + " = ?";
        if (fromDate != null) {
            sql += " and v." + VisitConst.VISITED_AT + ">?";
        }
        if (toDate != null) {
            sql += " and v." + VisitConst.VISITED_AT + "<?";
        }
        if (fromAge != null) {
            sql += " and v." + UserConst.BIRTH_DATE + "<?";
        }
        if (toAge != null) {
            sql += " and v." + UserConst.BIRTH_DATE + ">?";
        }
        if (gender != null) {
            sql += " and v." + UserConst.GENDER + "=?";
        }

        try (final PreparedStatement statement = connection.prepareStatement(sql)) {
            Double result = null;
            int i = 1;
            statement.setInt(i++, locationId);
            if (fromDate != null) {
                statement.setInt(i++, fromDate);
            }
            if (toDate != null) {
                statement.setInt(i++, toDate);
            }
            if (fromAge != null) {
                statement.setObject(i++, System.currentTimeMillis() / 1000 - fromAge);
            }
            if (toAge != null) {
                statement.setObject(i++, System.currentTimeMillis() / 1000 - toDate);
            }
            if (gender != null) {
                statement.setString(i++, gender.name());
            }
            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getDouble(1);
            }
            return result;
        }
    }

    public static int update(Connection connection, Integer id, JsonObject jsonObject) throws SQLException {
        int result;
        boolean findUpdateField = false;
        final StringBuilder sql = new StringBuilder("UPDATE " + LocationConst.TABLE + " set ");
        if (jsonObject.containsKey(LocationConst.PLACE)) {
            sql.append(" " + LocationConst.PLACE + "=?");
            findUpdateField = true;
        }
        if (jsonObject.containsKey(LocationConst.COUNTRY)) {
            if (findUpdateField) {
                sql.append(',');
            }
            sql.append(" " + LocationConst.COUNTRY + "=?");
            findUpdateField = true;
        }
        if (jsonObject.containsKey(LocationConst.CITY)) {
            if (findUpdateField) {
                sql.append(',');
            }
            sql.append(" " + LocationConst.CITY + "=?");
            findUpdateField = true;
        }
        if (jsonObject.containsKey(LocationConst.DISTANCE)) {
            if (findUpdateField) {
                sql.append(',');
            }
            sql.append(" " + LocationConst.DISTANCE + "=?");
            findUpdateField = true;
        }
        sql.append(" where " + LocationConst.ID + "=?");
        if (findUpdateField) {
            try (final PreparedStatement statement = connection.prepareStatement(sql.toString())) {
                int i = 1;
                if (jsonObject.containsKey(LocationConst.PLACE)) {
                    statement.setString(i++, jsonObject.getString(LocationConst.PLACE));
                }
                if (jsonObject.containsKey(LocationConst.COUNTRY)) {
                    statement.setString(i++, jsonObject.getString(LocationConst.COUNTRY));
                }
                if (jsonObject.containsKey(LocationConst.CITY)) {
                    statement.setString(i++, jsonObject.getString(LocationConst.CITY));
                }
                if (jsonObject.containsKey(LocationConst.DISTANCE)) {
                    statement.setInt(i++, jsonObject.getInt(LocationConst.DISTANCE));
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
