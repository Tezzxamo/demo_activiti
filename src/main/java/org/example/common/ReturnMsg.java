package org.example.common;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReturnMsg {
    private int returnCode;
    private Object returnMessage;

    public ReturnMsg() {
        this.returnMessage = new JSONObject();
    }

    public ReturnMsg(int returnCode) {
        this.returnCode = returnCode;
        this.returnMessage = new JSONObject();
    }

    public ReturnMsg(int returnCode, Object returnMessage) {
        this.returnCode = returnCode;
        this.returnMessage = returnMessage;
    }

}
