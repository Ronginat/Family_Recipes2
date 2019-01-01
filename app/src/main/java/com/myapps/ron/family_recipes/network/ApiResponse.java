package com.myapps.ron.family_recipes.network;

import java.util.List;

/**
 * Created by ronginat on 31/12/2018.
 */
public class ApiResponse<T> {
    private T data;
    private String lastKey;

    ApiResponse() {

    }

    public T getData() {
        return data;
    }

    void setData(T data) {
        this.data = data;
    }

    public String getLastKey() {
        return lastKey;
    }

    void setLastKey(String lastKey) {
        this.lastKey = lastKey;
    }
}
