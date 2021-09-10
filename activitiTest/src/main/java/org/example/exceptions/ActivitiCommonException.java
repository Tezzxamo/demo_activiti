package org.example.exceptions;

/**
 * @author Tethamo_zzx
 * @date 2021-9-10  下午 10:12
 */
public class ActivitiCommonException extends CustomException {
    public ActivitiCommonException(ReturnMessageAndTemplateDef.Errors error, Object... params) {
        super(ErrorCode.SERVICE_FAILED, error.getMessage(params));
    }
}
