package com.zemrow.test.highloadcup.web.servlet;

import com.zemrow.test.highloadcup.db.LocationDB;
import com.zemrow.test.highloadcup.entity.IJsonRead;
import com.zemrow.test.highloadcup.entity.Location;
import com.zemrow.test.highloadcup.entity.constants.GeneralEnum;
import com.zemrow.test.highloadcup.web.RestTestContext;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonParser;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;

/**
 * Created by Polyakov Alexandr on 27.08.2017.
 */
@WebServlet("/locations/*")
public class LocationServlet extends AbstractServlet {

    private static final ThreadLocal<DecimalFormat> FORMATTER =
            new ThreadLocal<DecimalFormat>() {
                @Override
                protected DecimalFormat initialValue() {
                    return new DecimalFormat("#.#####");
                }
            };

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean getAvg = false;
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            if (pathInfo.startsWith("/")) {
                if (pathInfo.endsWith("/avg")) {
                    getAvg = true;
                    pathInfo = pathInfo.substring(1, pathInfo.length() - 4);
                } else {
                    pathInfo = pathInfo.substring(1);
                }
            }
        }
        if (pathInfo != null || !pathInfo.isEmpty()) {
            try {
                final Integer id = Integer.parseInt(pathInfo);
                try (Connection connection = RestTestContext.getConnection()) {
                    if (getAvg) {
                        final Integer fromDate = parameterToInt(request, "fromDate");
                        final Integer toDate = parameterToInt(request, "toDate");
                        final Integer fromAge = parameterToInt(request, "fromAge");
                        final Integer toAge = parameterToInt(request, "toAge");
                        final String genderStr = request.getParameter("gender");
                        final GeneralEnum gender = (genderStr != null) ? GeneralEnum.valueOf(genderStr) : null;
                        final Double avg = LocationDB.avg(connection, id, fromDate, toDate, fromAge, toAge, gender);
                        writeAndCloseJson(response, "{\"avg\":" + FORMATTER.get().format(avg) + "}");
                    } else {
                        final Location entity = LocationDB.select(connection, id);
                        selectById(response, entity);
                    }
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "locations/{id} or locations/{id}/avg");
            }
        }
    }

    @Override
    protected IJsonRead readJson(HttpServletRequest request) throws IOException {
        try (final BufferedReader reader = request.getReader()) {
            try (final JsonParser parser = Json.createParser(reader)) {
                final JsonParser.Event event = parser.next();
                if (event == JsonParser.Event.START_OBJECT) {
                    final Location location = new Location();
                    location.read(parser, event);
                    return location;
                }
            }
        }
        return null;
    }

    @Override
    protected int insert(Connection connection, IJsonRead entity) throws SQLException {
        return LocationDB.insert(connection, (Location) entity);
    }

    @Override
    protected int update(Connection connection, Integer id, JsonObject jsonObject) throws SQLException {
        return LocationDB.update(connection, id, jsonObject);
    }


}
