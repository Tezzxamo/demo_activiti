package org.example.exceptions;


public class ActivitiCommonException  extends CustomException {
    public ActivitiCommonException(ReturnMessageAndTemplateDef.Errors error, Object... params) {
        super(ErrorCode.SERVICE_FAILED, error.getMessage(params));
    }
}
