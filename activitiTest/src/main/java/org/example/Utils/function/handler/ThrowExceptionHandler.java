package org.example.Utils.function.handler;

/**
 * @author zzx
 * @date 2021/12/16-14:56
 */
@FunctionalInterface
public interface ThrowExceptionHandler {

    /**
     * 抛出异常信息
     *
     * @param message 异常信息
     */
    void throwCustomException(String message);
}
