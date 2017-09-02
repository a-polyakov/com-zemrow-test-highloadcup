package com.zemrow.test.highloadcup.entity.constants;

/**
 * Достопримечательность
 * <p>
 * Created by Polyakov Alexandr on 27.08.2017.
 */
public class LocationConst {
    /**
     * Достопримечательность
     */
    public static final String TABLE = "location";
    /**
     * уникальный внешний id достопримечательности. Устанавливается тестирующей системой. 32-разрядное целое число.
     */
    public static final String ID = "id";
    /**
     * описание достопримечательности. Текстовое поле неограниченной длины.
     */
    public static final String PLACE = "place";
    /**
     * название страны расположения. unicode-строка длиной до 50 символов.
     */
    public static final String COUNTRY = "country";
    /**
     * название города расположения. unicode-строка длиной до 50 символов.
     */
    public static final String CITY = "city";
    /**
     * расстояние от города по прямой в километрах. 32-разрядное целое число.
     */
    public static final String DISTANCE = "distance";
}
