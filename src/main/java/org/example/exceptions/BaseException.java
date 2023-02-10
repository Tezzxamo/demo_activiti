package org.example.exceptions;


import org.example.common.factory.RFactory;
import org.example.enumeration.CodeEnum;
import org.example.model.base.R;

/**
 * 基本异常
 *
 * @author zzx
 */
public abstract class BaseException extends RuntimeException {

    private R<?> r;

    BaseException() {
        super();
    }

    BaseException(int codeInt, String msg) {
        super(msg);
        this.r = RFactory.newResult(CodeEnum.getEnumByCode(codeInt), msg);
    }

    public int getCodeInt() {
        return r.getCode();
    }

    public Object getResult() {
        return r.getResult();
    }

    public String getMsg() {
        return r.getMessage();
    }

    public R<?> getReturnMsg() {
        return r;
    }

}
