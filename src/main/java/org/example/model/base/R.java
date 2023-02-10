package org.example.model.base;

import lombok.Data;

import java.io.Serializable;

@Data
public class R<T> implements Serializable {

    private int code;
    private T result;
    private String message;

    public R(int code, T result, String message) {
        this.code = code;
        this.result = result;
        this.message = message;
    }

}
