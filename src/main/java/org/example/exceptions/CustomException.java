package org.example.exceptions;

/**
 * @author Tethamo
 */
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(String message) {
        super(message);
        this.errorCode = ErrorCode.UNKNOWN;
    }

    public CustomException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}