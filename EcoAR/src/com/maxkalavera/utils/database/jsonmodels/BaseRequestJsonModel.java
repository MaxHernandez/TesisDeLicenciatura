package com.maxkalavera.utils.database.jsonmodels;

import com.google.gson.Gson;


public interface BaseRequestJsonModel {

    public String serialize();

};


/*
public abstract class BaseRequestJsonModel {

    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

};
*/