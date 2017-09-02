package com.zemrow.test.highloadcup.web;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.junit.*;

import javax.servlet.ServletException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class TestWeb {
    //    private static final String host = "http://localhost:8080";
    private static final String host = "http://localhost:80";

    private static Tomcat tomcat;

    @BeforeClass
    public static void beforeClass() throws ServletException, LifecycleException, InterruptedException {
        tomcat = new Tomcat();
        tomcat.setPort(80);
        String webappDirLocation = "src/main/webapp/";
        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
        System.out.println("configuring app with basedir: " + new File("./" + webappDirLocation).getAbsolutePath());

        WebResourceRoot resources = new StandardRoot(ctx);
        File additionWebInfClasses = new File("target/classes");
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);

        tomcat.start();

        Thread.sleep(3000);
    }

    @AfterClass
    public static void afterClass() throws LifecycleException {
        tomcat.stop();
    }

    @Test
    public void test() {
        try {

            Assert.assertEquals(sendGet(host + "/user").getResponseCode(), 404);
            Assert.assertEquals(sendGet(host + "/users/string").getResponseCode(), 404);
            Assert.assertEquals(sendGet(host + "/users/string/somethingbad").getResponseCode(), 404);
            Assert.assertEquals(sendGet(host + "/users/").getResponseCode(), 404);


            Assert.assertEquals(sendGet(host + "/users/100000").getResponseCode(), 404);

            HttpURLConnection connection = sendGet(host + "/users/1");
            Assert.assertEquals(connection.getResponseCode(), 200);
            String json = inputStreamToString(connection.getInputStream());
            System.out.println(json);
            Assert.assertEquals(json, "{\"id\":1,\"email\":\"wibylcudestiwuk@icloud.com\",\"first_name\":\"Пётр\",\"last_name\":\"Фетатосян\",\"gender\":\"m\",\"birth_date\":-1720915200}");

            connection = sendGet(host + "/locations/1");
            Assert.assertEquals(connection.getResponseCode(), 200);
            json = inputStreamToString(connection.getInputStream());
            System.out.println(json);
            Assert.assertEquals(json, "{\"id\":1,\"place\":\"Набережная\",\"country\":\"Аргентина\",\"city\":\"Москва\",\"distance\":6}");

            connection = sendGet(host + "/visits/1");
            Assert.assertEquals(connection.getResponseCode(), 200);
            json = inputStreamToString(connection.getInputStream());
            System.out.println(json);
            Assert.assertEquals(json, "{\"id\":1,\"location\":32,\"user\":44,\"visited_at\":1103485742,\"mark\":4}");

            //TODO
//            Assert.assertEquals(sendGet(host+"/users/100000/visits").getResponseCode(), 404);


            connection = sendGet(host + "/users/1/visits");
            Assert.assertEquals(connection.getResponseCode(), 200);
            json = inputStreamToString(connection.getInputStream());
            System.out.println(json);
            Assert.assertEquals(json, "{\"visits\":[{\"visited_at\":965970299,\"mark\":3,\"place\":\"Фонарь\"},{\"visited_at\":969951712,\"mark\":4,\"place\":\"Гараж\"},{\"visited_at\":972265952,\"mark\":1,\"place\":\"Ратуша\"},{\"visited_at\":987892019,\"mark\":3,\"place\":\"Парк\"},{\"visited_at\":1001990372,\"mark\":1,\"place\":\"Аэропорт\"},{\"visited_at\":1036735014,\"mark\":2,\"place\":\"Замок\"},{\"visited_at\":1050873905,\"mark\":2,\"place\":\"Парк\"},{\"visited_at\":1055440494,\"mark\":2,\"place\":\"Ресторан\"},{\"visited_at\":1062936415,\"mark\":1,\"place\":\"Пристань\"},{\"visited_at\":1096509570,\"mark\":1,\"place\":\"Поместье\"},{\"visited_at\":1101117457,\"mark\":2,\"place\":\"Дорожка\"},{\"visited_at\":1116879743,\"mark\":3,\"place\":\"Пруд\"},{\"visited_at\":1179873480,\"mark\":4,\"place\":\"Речка\"},{\"visited_at\":1198196917,\"mark\":1,\"place\":\"Скала\"},{\"visited_at\":1198897703,\"mark\":3,\"place\":\"Дом\"},{\"visited_at\":1216825459,\"mark\":3,\"place\":\"Речка\"},{\"visited_at\":1219145269,\"mark\":2,\"place\":\"Серпантин\"},{\"visited_at\":1225382162,\"mark\":2,\"place\":\"Аэропорт\"},{\"visited_at\":1232392264,\"mark\":4,\"place\":\"Лес\"},{\"visited_at\":1244446970,\"mark\":1,\"place\":\"Гараж\"},{\"visited_at\":1250935360,\"mark\":2,\"place\":\"Набережная\"},{\"visited_at\":1263635162,\"mark\":4,\"place\":\"Дача\"},{\"visited_at\":1266920118,\"mark\":2,\"place\":\"Улица\"},{\"visited_at\":1292237548,\"mark\":3,\"place\":\"Замок\"},{\"visited_at\":1319418446,\"mark\":4,\"place\":\"Магазин\"},{\"visited_at\":1345935489,\"mark\":2,\"place\":\"Статуя\"},{\"visited_at\":1349466871,\"mark\":3,\"place\":\"Гора\"},{\"visited_at\":1350842886,\"mark\":1,\"place\":\"Парк\"},{\"visited_at\":1353386973,\"mark\":4,\"place\":\"Уступ\"},{\"visited_at\":1355050593,\"mark\":1,\"place\":\"Улочка\"},{\"visited_at\":1355721993,\"mark\":4,\"place\":\"Фонтан\"},{\"visited_at\":1360590852,\"mark\":3,\"place\":\"Серпантин\"},{\"visited_at\":1367448196,\"mark\":4,\"place\":\"Парк\"},{\"visited_at\":1374117339,\"mark\":1,\"place\":\"Пруд\"},{\"visited_at\":1376660354,\"mark\":1,\"place\":\"Скала\"},{\"visited_at\":1377486488,\"mark\":3,\"place\":\"Фонтан\"},{\"visited_at\":1381426459,\"mark\":2,\"place\":\"Дом\"},{\"visited_at\":1386348679,\"mark\":1,\"place\":\"Серпантин\"},{\"visited_at\":1400729994,\"mark\":4,\"place\":\"Дорожка\"},{\"visited_at\":1418372744,\"mark\":4,\"place\":\"Забор\"}]}");

            Assert.assertEquals(sendGet(host + "/users/1/visit").getResponseCode(), 404);
            Assert.assertEquals(sendGet(host + "/users/1/visits?fromDate=").getResponseCode(), 400);
            Assert.assertEquals(sendGet(host + "/users/1/visits?fromDate=abracadbra").getResponseCode(), 400);
            Assert.assertEquals(sendGet(host + "/users/somethingstringhere/visits?fromDate=1").getResponseCode(), 404);
            Assert.assertEquals(sendGet(host + "/users/1/visit?fromDate=915148800&toDate=915148800").getResponseCode(), 404);

            connection = sendGet(host + "/locations/1/avg");
            Assert.assertEquals(connection.getResponseCode(), 200);
            json = inputStreamToString(connection.getInputStream());
            System.out.println(json);
            Assert.assertEquals(json, "{\"avg\":2}");

            Assert.assertEquals(sendGet(host + "/locations/somethingsomething/avg").getResponseCode(), 404);

//            URL url = new URL(host+"/users/1/visits?fromDate toDate country toDistance ");
            testUpdateUser();
            testUpdateLocation();
            testUpdateVisit();

            testNewUser();
            testNewLocation();
            testNewVisit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    @Test
    public void testUpdateUser() throws IOException {
        HttpURLConnection connection = sendPost(host + "/users/214", "{\n" +
                "    \"email\": \"johndoe@gmail.com\",\n" +
                "    \"first_name\": \"Jessie\",\n" +
                "    \"last_name\": \"Pinkman\",\n" +
                "    \"birth_date\": 616550400\n" +
                "}");
        Assert.assertEquals(connection.getResponseCode(), 200);
        String json = inputStreamToString(connection.getInputStream());
        System.out.println(json);
        Assert.assertEquals(json, "{}");

        connection = sendPost(host + "/users/214", "{\n" +
                "    \"email\": null,\n" +
                "    \"first_name\": \"Jessie\",\n" +
                "    \"last_name\": \"Pinkman\",\n" +
                "    \"birth_date\": 616550400\n" +
                "}");
        Assert.assertEquals(connection.getResponseCode(), 400);

        // пользователь не существует
        connection = sendPost(host + "/users/100000", "{\n" +
                "    \"email\": \"johndoe@gmail.com\",\n" +
                "    \"first_name\": \"Jessie\",\n" +
                "    \"last_name\": \"Pinkman\",\n" +
                "    \"birth_date\": 616550400\n" +
                "}");
        Assert.assertEquals(connection.getResponseCode(), 404);
    }

//    @Test
    public void testUpdateLocation() throws IOException {
        final HttpURLConnection connection = sendPost(host + "/locations/1", "{\"place\":\"Набережная2\",\"country\":\"Аргентина2\",\"city\":\"Москва2\",\"distance\":62}");
        Assert.assertEquals(connection.getResponseCode(), 200);
        final String json = inputStreamToString(connection.getInputStream());
        System.out.println(json);
        Assert.assertEquals(json, "{}");
    }

    public void testUpdateVisit() throws IOException {
        HttpURLConnection connection = sendPost(host + "/visits/1", "{\"visited_at\":1103485744,\"mark\":2}");
        Assert.assertEquals(connection.getResponseCode(), 200);
        String json = inputStreamToString(connection.getInputStream());
        System.out.println(json);
        Assert.assertEquals(json, "{}");

        connection = sendGet(host + "/visits/1");
        Assert.assertEquals(connection.getResponseCode(), 200);
        json = inputStreamToString(connection.getInputStream());
        System.out.println(json);
        Assert.assertEquals(json, "{\"id\":1,\"location\":32,\"user\":44,\"visited_at\":1103485744,\"mark\":2}");
    }

    public void testNewUser() throws IOException {
        final String jsonSource = "{\"id\":100000,\"email\":\"test@test.com\",\"first_name\":\"first_name1\",\"last_name\":\"last_name1\",\"gender\":\"m\",\"birth_date\":-1720915200}";
        HttpURLConnection connection = sendPost(host + "/users/new", jsonSource);
        Assert.assertEquals(connection.getResponseCode(), 200);
        String json = inputStreamToString(connection.getInputStream());
        System.out.println(json);
        Assert.assertEquals(json, "{}");

        connection = sendGet(host + "/users/100000");
        Assert.assertEquals(connection.getResponseCode(), 200);
        json = inputStreamToString(connection.getInputStream());
        System.out.println(json);
        Assert.assertEquals(json, jsonSource);

        connection = sendPost(host + "/users/new", "{\"id\":100001,\"email\":null,\"first_name\":\"first_name1\",\"last_name\":\"last_name1\",\"gender\":\"m\",\"birth_date\":-1720915200}");
        Assert.assertEquals(connection.getResponseCode(), 400);
    }

    public void testNewLocation() throws IOException {
        HttpURLConnection connection = sendPost(host + "/locations/new", "{\"id\":100000,\"place\":\"place1\",\"country\":\"country1\",\"city\":\"city1\",\"distance\":1000}");
        Assert.assertEquals(connection.getResponseCode(), 200);
        String json = inputStreamToString(connection.getInputStream());
        System.out.println(json);
        Assert.assertEquals(json, "{}");
    }

    public void testNewVisit() throws IOException {
        final String jsonSource = "{\"id\":10000000,\"location\":32,\"user\":44,\"visited_at\":1103485746,\"mark\":2}";
        HttpURLConnection connection = sendPost(host + "/visits/new", jsonSource);
        Assert.assertEquals(connection.getResponseCode(), 200);
        String json = inputStreamToString(connection.getInputStream());
        System.out.println(json);
        Assert.assertEquals(json, "{}");

        connection = sendGet(host + "/visits/10000000");
        Assert.assertEquals(connection.getResponseCode(), 200);
        json = inputStreamToString(connection.getInputStream());
        System.out.println(json);
        Assert.assertEquals(json, jsonSource);
    }

    private HttpURLConnection sendGet(String urlStr) throws IOException {
        final URL url = new URL(urlStr);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        //conn.setRequestProperty("Content-Type", "application/json");
        return conn;
    }

    private HttpURLConnection sendPost(String urlStr, String body) throws IOException {
        final URL url = new URL(urlStr);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        // Send response
        try (final OutputStream os = conn.getOutputStream()) {
            try (final OutputStreamWriter pw = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
                pw.append(body);
            }
        }
        return conn;
    }

    private String inputStreamToString(final InputStream is) throws IOException {
        final StringBuffer result = new StringBuffer();
        try (BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            boolean first = true;
            while ((line = rd.readLine()) != null) {
                if (first) {
                    first = false;
                } else {
                    result.append('\n');
                }
                result.append(line);
            }
        }
        return result.toString();
    }
}