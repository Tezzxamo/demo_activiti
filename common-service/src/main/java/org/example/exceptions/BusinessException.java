package org.example.exceptions;


import org.example.enums.CodeEnum;


/**
 * @author zzx
 */
public class BusinessException extends BaseException {

    public BusinessException() {
    }

    public BusinessException(CodeEnum codeEnum) {
        super(codeEnum.getCode(), codeEnum.description());
    }

    public BusinessException(CodeEnum codeEnum, String msg) {
        super(codeEnum.getCode(), codeEnum.description() + "," + msg);
    }

    public BusinessException(int codeInt, String errorMsg) {
        super(codeInt, errorMsg);
    }
}
