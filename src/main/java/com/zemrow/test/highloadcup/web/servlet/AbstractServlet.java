package com.zemrow.test.highloadcup.web.servlet;

import com.zemrow.test.highloadcup.entity.IJsonRead;
import com.zemrow.test.highloadcup.entity.IJsonWrite;
import com.zemrow.test.highloadcup.web.RestTestContext;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Polyakov Alexandr on 27.08.2017.
 */
public abstract class AbstractServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String pathInfo = request.getPathInfo();
        if ("/new".equals(pathInfo)) {
            try {
                IJsonRead entity = readJson(request);
                // TODO checkAllNotNull
                try (Connection connection = RestTestContext.getConnection()) {
                    final int countUpdate = insert(connection, entity);
                    switch (countUpdate) {
                        case 1:
                            writeAndCloseJson(response, "{}");
                            break;
                        default:
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    }
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }
        } else {
            final Integer id = pathInfoToInt(request);
            if (id != null) {
                try (final BufferedReader reader = request.getReader()) {
                    final JsonObject jsonObject = Json.createReader(reader).readObject();
                    checkAllNotNull(jsonObject);
                    try (Connection connection = RestTestContext.getConnection()) {
                        final int countUpdate = update(connection, id, jsonObject);
                        switch (countUpdate) {
                            case 1:
                                writeAndCloseJson(response, "{}");
                                break;
                            case -1:
                                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not found update field");
                                break;
                            default:
                                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Location not found");
                        }
                    }
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
                    //TODO
//                    e.printStackTrace();
//                    System.out.println("3333333333");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "{entity}/{id}");
                //TODO
//                System.out.println("44444444444444");
            }
        }
    }

    private void checkAllNotNull(JsonObject jsonObject) {
        for (Map.Entry<String, JsonValue> entry : jsonObject.entrySet()) {
            final JsonValue value = entry.getValue();
            if (value == null || JsonValue.NULL == value) {
                throw new IllegalArgumentException();
            }
        }
    }

    protected abstract IJsonRead readJson(HttpServletRequest request) throws IOException;

    protected abstract int insert(Connection connection, IJsonRead entity) throws SQLException;

    protected abstract int update(Connection connection, Integer id, JsonObject jsonObject) throws SQLException;

    /**
     * Получить id из пути
     *
     * @param request
     * @return
     */
    protected Integer pathInfoToInt(HttpServletRequest request) {
        Integer result = null;
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            if (pathInfo.startsWith("/")) {
                pathInfo = pathInfo.substring(1);
            }
            if (pathInfo.isEmpty()) {
                pathInfo = null;
            }
        }
        if (pathInfo != null) {
            result = Integer.parseInt(pathInfo);
        }
        return result;
    }

    protected Integer parameterToInt(HttpServletRequest request, String parameterName) {
        Integer result = null;
        final String valueStr = request.getParameter(parameterName);
        if (valueStr != null) {
            result = Integer.parseInt(valueStr);
        }
        return result;
    }

    protected void selectById(HttpServletResponse response, IJsonWrite entity) throws IOException {
        if (entity == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
        } else {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            try (final PrintWriter writer = response.getWriter()) {
                try (final JsonGenerator generator = Json.createGenerator(writer)) {
                    entity.write(generator);
                }
            }
        }
    }

    protected void writeAndCloseJson(HttpServletResponse response, String jsonStr) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        try (final PrintWriter writer = response.getWriter()) {
            writer.append(jsonStr);
        }
    }


}
