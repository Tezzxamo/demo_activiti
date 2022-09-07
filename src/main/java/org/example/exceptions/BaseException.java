package org.example.exceptions;


import org.example.common.ReturnMsg;

/**
 * 基本异常
 *
 * @author zzx
 */
public abstract class BaseException extends RuntimeException {

    private ReturnMsg returnMsg;

    BaseException() {
        super();
    }

    BaseException(int codeInt, String msg) {
        super(msg);
        this.returnMsg = new ReturnMsg(codeInt, msg);
    }

    public int getCodeInt() {
        return returnMsg.getReturnCode();
    }

    public Object getMsg() {
        return returnMsg.getReturnMessage();
    }


    public ReturnMsg getReturnMsg() {
        return returnMsg;
    }

}
