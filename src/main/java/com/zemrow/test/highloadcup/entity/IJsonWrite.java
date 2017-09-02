package com.zemrow.test.highloadcup.entity;

import javax.json.stream.JsonGenerator;

/**
 * Created by Polyakov Alexandr on 27.08.2017.
 */
public interface IJsonWrite {
    public void write(final JsonGenerator generator);
}
