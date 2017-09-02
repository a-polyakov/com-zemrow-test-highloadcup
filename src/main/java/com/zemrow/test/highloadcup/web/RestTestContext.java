package com.zemrow.test.highloadcup.web;

import com.zemrow.test.highloadcup.db.LocationDB;
import com.zemrow.test.highloadcup.db.UserDB;
import com.zemrow.test.highloadcup.db.VisitDB;
import com.zemrow.test.highloadcup.entity.constants.LocationConst;
import com.zemrow.test.highloadcup.entity.constants.UserConst;
import com.zemrow.test.highloadcup.entity.constants.VisitConst;
import com.zemrow.test.highloadcup.json.AbstractEntityToDB;
import com.zemrow.test.highloadcup.json.LocationToDB;
import com.zemrow.test.highloadcup.json.UserToDB;
import com.zemrow.test.highloadcup.json.VisitToDB;
import org.h2.jdbcx.JdbcConnectionPool;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by Polyakov Alexandr on 27.08.2017.
 */
@WebListener
public class RestTestContext implements ServletContextListener {
    private static JdbcConnectionPool cp;

    /**
     * Получение соединения с базой данных
     *
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        final Connection connection = cp.getConnection();
        return connection;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        cp = JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "sa");

        try (final Connection conn = cp.getConnection()) {
            long time = System.currentTimeMillis();
            UserDB.createTable(conn);
            LocationDB.createTable(conn);
            VisitDB.createTable(conn);
            time = System.currentTimeMillis() - time;
            System.out.println("Создание схемы БД " + time + "ms");

            final UserToDB userToDB = new UserToDB();
            final LocationToDB locationToDB = new LocationToDB();
            final VisitToDB visitToDB = new VisitToDB();

            final File file=new File("/tmp/data/data.zip");
            System.out.println(file.getAbsolutePath());
            final ZipFile zip = new ZipFile(file);
            final Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                time = System.currentTimeMillis();
                long size = 0;
                final ZipEntry zipEntry = entries.nextElement();

                // TODO порядок сначала пользователей потом достопримечательности и только после посещения

                final AbstractEntityToDB entityToDB;
                if (zipEntry.getName().startsWith(LocationConst.TABLE)) {
                    entityToDB = locationToDB;
                } else if (zipEntry.getName().startsWith(UserConst.TABLE)) {
                    entityToDB = userToDB;
                } else if (zipEntry.getName().startsWith(VisitConst.TABLE)) {
                    entityToDB = visitToDB;
                } else {
                    throw new RuntimeException("Unknow file " + zipEntry.getName());
                }

                try (final JsonParser parser = Json.createParser(zip.getInputStream(zipEntry))) {
                    while (parser.hasNext()) {
                        JsonParser.Event event = parser.next();
                        if (event == JsonParser.Event.START_OBJECT) {
                            event = parser.next();
                            if (event == JsonParser.Event.KEY_NAME) {
                                event = parser.next();
                                if (event == JsonParser.Event.START_ARRAY) {
                                    size += entityToDB.readAndInsert(conn, parser);
                                }
                            }
                        }
                    }
                }
                time = System.currentTimeMillis() - time;
                System.out.println("Запись в БД данных из файла " + zipEntry.getName() + " " + size + " за " + time + "ms");
            }
//            conn.commit();
//            System.out.println(a1(conn, UserConst.TABLE));
//            System.out.println(a1(conn, LocationConst.TABLE));
//            System.out.println(a1(conn, VisitConst.TABLE));
        } catch (Exception e) {
            throw new RuntimeException("Ошибка создания БД", e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        cp.dispose();
    }
}
