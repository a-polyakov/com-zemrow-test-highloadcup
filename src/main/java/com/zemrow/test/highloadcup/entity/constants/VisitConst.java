package com.zemrow.test.highloadcup.entity.constants;

/**
 * Посещение
 * <p>
 * Created by Polyakov Alexandr on 27.08.2017.
 */
public class VisitConst {
    /**
     * Посещение
     */
    public static final String TABLE = "visit";
    /**
     * уникальный внешний id посещения. Устанавливается тестирующей системой. 32-разрядное целое число.
     */
    public static final String ID = "id";
    /**
     * id достопримечательности. 32-разрядное целое число.
     */
    public static final String LOCATION = "location";
    /**
     * id путешественника. 32-разрядное целое число.
     */
    public static final String USER = "user";
    /**
     * дата посещения, timestamp с ограничениями: снизу 01.01.2000, а сверху 01.01.2015.
     */
    public static final String VISITED_AT = "visited_at";
    /**
     * оценка посещения от 0 до 5 включительно. Целое число.
     */
    public static final String MARK = "mark";
}
