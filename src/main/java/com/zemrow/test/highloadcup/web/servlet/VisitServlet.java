package com.zemrow.test.highloadcup.web.servlet;

import com.zemrow.test.highloadcup.db.VisitDB;
import com.zemrow.test.highloadcup.entity.IJsonRead;
import com.zemrow.test.highloadcup.entity.Visit;
import com.zemrow.test.highloadcup.web.RestTestContext;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonParser;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Polyakov Alexandr on 27.08.2017.
 */
@WebServlet("/visits/*")
public class VisitServlet extends AbstractServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        final Integer id = pathInfoToInt(req);
        try (Connection connection = RestTestContext.getConnection()) {
            final Visit visit = VisitDB.select(connection, id);
            selectById(response, visit);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected IJsonRead readJson(HttpServletRequest request) throws IOException {
        try (final JsonParser parser = Json.createParser(request.getReader())) {
            final JsonParser.Event event = parser.next();
            if (event == JsonParser.Event.START_OBJECT) {
                final Visit visit = new Visit();
                visit.read(parser, event);
                return visit;
            }
        }
        return null;
    }

    @Override
    protected int insert(Connection connection, IJsonRead entity) throws SQLException {
        return VisitDB.insert(connection, (Visit) entity);
    }

    @Override
    protected int update(Connection connection, Integer id, JsonObject jsonObject) throws SQLException {
        return VisitDB.update(connection, id, jsonObject);
    }
}
