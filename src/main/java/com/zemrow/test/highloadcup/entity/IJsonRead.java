package com.zemrow.test.highloadcup.entity;

import javax.json.stream.JsonParser;

/**
 * Created by Polyakov Alexandr on 27.08.2017.
 */
public interface IJsonRead {
    public void read(JsonParser parser, JsonParser.Event event);
}
