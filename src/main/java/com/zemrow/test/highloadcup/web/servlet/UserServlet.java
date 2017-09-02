package com.zemrow.test.highloadcup.web.servlet;

import com.zemrow.test.highloadcup.db.UserDB;
import com.zemrow.test.highloadcup.db.VisitDB;
import com.zemrow.test.highloadcup.entity.IJsonRead;
import com.zemrow.test.highloadcup.entity.User;
import com.zemrow.test.highloadcup.entity.VisitDto;
import com.zemrow.test.highloadcup.web.RestTestContext;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Polyakov Alexandr on 27.08.2017.
 */
@WebServlet("/users/*")
public class UserServlet extends AbstractServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean getVisits = false;
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            if (pathInfo.startsWith("/")) {
                if (pathInfo.endsWith("/visits")) {
                    getVisits = true;
                    pathInfo = pathInfo.substring(1, pathInfo.length() - 7);
                } else {
                    pathInfo = pathInfo.substring(1);
                }
            }
        }
        if (pathInfo != null && !pathInfo.isEmpty()) {
            try {
                final Integer id = Integer.parseInt(pathInfo);
                try (Connection connection = RestTestContext.getConnection()) {
                    if (getVisits) {
                        Integer fromDate = parameterToInt(request, "fromDate");
                        Integer toDate = parameterToInt(request, "toDate");
                        String country = request.getParameter("country");
                        Integer toDistance = parameterToInt(request, "toDistance");
                        final List<VisitDto> list = VisitDB.select(connection, id, fromDate, toDate, country, toDistance);
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/json");
                        try (final JsonGenerator generator = Json.createGenerator(response.getWriter())) {
                            generator.writeStartObject();
                            generator.writeStartArray("visits");
                            for (VisitDto visit : list) {
                                visit.write(generator);
                            }
                            generator.writeEnd();
                            generator.writeEnd();
                        }
                    } else {
                        final User user = UserDB.select(connection, id);
                        selectById(response, user);
                    }
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "users/{id} or users/{id}/visits");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "users/{id} or users/{id}/visits");
        }
    }

    @Override
    protected IJsonRead readJson(HttpServletRequest request) throws IOException {
        try (final JsonParser parser = Json.createParser(request.getReader())) {
            final JsonParser.Event event = parser.next();
            if (event == JsonParser.Event.START_OBJECT) {
                final User user = new User();
                user.read(parser, event);
                return user;
            }
        }
        return null;
    }

    @Override
    protected int insert(Connection connection, IJsonRead entity) throws SQLException {
        return UserDB.insert(connection, (User) entity);
    }

    @Override
    protected int update(Connection connection, Integer id, JsonObject jsonObject) throws SQLException {
        return UserDB.update(connection, id, jsonObject);
    }
}
