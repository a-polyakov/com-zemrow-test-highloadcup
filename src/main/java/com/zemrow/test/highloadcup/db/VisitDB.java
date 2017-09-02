package com.zemrow.test.highloadcup.db;

import com.zemrow.test.highloadcup.entity.Visit;
import com.zemrow.test.highloadcup.entity.VisitDto;
import com.zemrow.test.highloadcup.entity.constants.LocationConst;
import com.zemrow.test.highloadcup.entity.constants.VisitConst;

import javax.json.JsonObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Polyakov Alexandr on 27.08.2017.
 */
public class VisitDB {
    public static void createTable(Connection connection) throws SQLException {
        try (final Statement statement = connection.createStatement()) {
            statement.execute("CREATE table " + VisitConst.TABLE + " (" +
                    VisitConst.ID + " INT PRIMARY KEY," +
                    VisitConst.LOCATION + " INT NOT NULL REFERENCES location(id) ," +
                    VisitConst.USER + " INT NOT NULL REFERENCES USER (id) ," +
                    VisitConst.VISITED_AT + " INT NOT NULL," +
                    VisitConst.MARK + " INT NOT NULL" +
                    ")");
        }
    }

    public static int insert(Connection connection, Visit visit) throws SQLException {
        try (final PreparedStatement statement = connection.prepareStatement("" +
                "insert into " + VisitConst.TABLE + "(" +
                VisitConst.ID + " ," +
                VisitConst.LOCATION + " ," +
                VisitConst.USER + " ," +
                VisitConst.VISITED_AT + " ," +
                VisitConst.MARK + " " +
                ") VALUES (?,?,?,?,?)")) {
            statement.setInt(1, visit.id);
            statement.setInt(2, visit.location);
            statement.setInt(3, visit.user);
            statement.setInt(4, visit.visited_at);
            statement.setInt(5, visit.mark);
            return statement.executeUpdate();
        }
    }

    private static final String SQL_SELECT_FROM = "select " +
            "v." + VisitConst.ID + " ," +
            "v." + VisitConst.LOCATION + " ," +
            "v." + VisitConst.USER + " ," +
            "v." + VisitConst.VISITED_AT + " ," +
            "v." + VisitConst.MARK +
            " from " + VisitConst.TABLE + " v";

    public static Visit select(Connection connection, Integer id) throws SQLException {
        try (final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_FROM +
                " where v." + VisitConst.ID + " = ?")) {
            Visit result = null;
            statement.setObject(1, id);
            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = createVisit(resultSet);
            }
            return result;
        }
    }

    public static List<VisitDto> select(Connection connection, Integer userId, Integer fromDate, Integer toDate, String country, Integer toDistance) throws SQLException {
        String sql = "select " +
                "v." + VisitConst.MARK + " ," +
                "v." + VisitConst.VISITED_AT + " ," +
                "l." + LocationConst.PLACE +
                " from " + VisitConst.TABLE + " v" +
                " join " + LocationConst.TABLE + " l on l." + LocationConst.ID + "=v." + VisitConst.LOCATION;
        sql += " where v." + VisitConst.USER + " = ?";
        if (fromDate != null) {
            sql += " and v." + VisitConst.VISITED_AT + ">?";
        }
        if (toDate != null) {
            sql += " and v." + VisitConst.VISITED_AT + "<?";
        }
        if (country != null) {
            sql += " and l." + LocationConst.COUNTRY + "=?";
        }
        if (toDistance != null) {
            sql += " and l." + LocationConst.DISTANCE + "<?";
        }
        sql += " order by " + VisitConst.VISITED_AT;
        try (final PreparedStatement statement = connection.prepareStatement(sql)) {
            List<VisitDto> result = new ArrayList<>();
            int i = 1;
            statement.setObject(i++, userId);
            if (fromDate != null) {
                statement.setObject(i++, fromDate);
            }
            if (toDate != null) {
                statement.setObject(i++, toDate);
            }
            if (country != null) {
                statement.setObject(i++, country);
            }
            if (toDistance != null) {
                statement.setObject(i++, toDistance);
            }
            try (final ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(createVisitDto(resultSet));
                }
            }
            return result;
        }
    }

    private static Visit createVisit(ResultSet resultSet) throws SQLException {
        Visit result;
        result = new Visit();
        result.id = resultSet.getInt(1);
        result.location = resultSet.getInt(2);
        result.user = resultSet.getInt(3);
        result.visited_at = resultSet.getInt(4);
        result.mark = resultSet.getInt(5);
        return result;
    }

    private static VisitDto createVisitDto(ResultSet resultSet) throws SQLException {
        VisitDto result;
        result = new VisitDto();
        result.mark = resultSet.getInt(1);
        result.visited_at = resultSet.getInt(2);
        result.place = resultSet.getString(3);
        return result;
    }

    public static int update(Connection connection, Integer id, JsonObject jsonObject) throws SQLException {
        int result;
        boolean findUpdateField = false;
        final StringBuilder sql = new StringBuilder("UPDATE " + VisitConst.TABLE + " set ");
        if (jsonObject.containsKey(VisitConst.LOCATION)) {
            sql.append(" " + VisitConst.LOCATION + "=?");
            findUpdateField = true;
        }
        if (jsonObject.containsKey(VisitConst.USER)) {
            if (findUpdateField) {
                sql.append(',');
            }
            sql.append(" " + VisitConst.USER + "=?");
            findUpdateField = true;
        }
        if (jsonObject.containsKey(VisitConst.VISITED_AT)) {
            if (findUpdateField) {
                sql.append(',');
            }
            sql.append(" " + VisitConst.VISITED_AT + "=?");
            findUpdateField = true;
        }
        if (jsonObject.containsKey(VisitConst.MARK)) {
            if (findUpdateField) {
                sql.append(',');
            }
            sql.append(" " + VisitConst.MARK + "=?");
            findUpdateField = true;
        }
        sql.append(" where " + VisitConst.ID + "=?");
        if (findUpdateField) {
            try (final PreparedStatement statement = connection.prepareStatement(sql.toString())) {
                int i = 1;
                if (jsonObject.containsKey(VisitConst.LOCATION)) {
                    statement.setInt(i++, jsonObject.getInt(VisitConst.LOCATION));
                }
                if (jsonObject.containsKey(VisitConst.USER)) {
                    statement.setInt(i++, jsonObject.getInt(VisitConst.USER));
                }
                if (jsonObject.containsKey(VisitConst.VISITED_AT)) {
                    statement.setInt(i++, jsonObject.getInt(VisitConst.VISITED_AT));
                }
                if (jsonObject.containsKey(VisitConst.MARK)) {
                    statement.setInt(i++, jsonObject.getInt(VisitConst.MARK));
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
