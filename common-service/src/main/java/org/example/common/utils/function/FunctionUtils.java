package org.example.common.utils.function;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.utils.function.handler.ThrowExceptionHandler;
import org.example.enums.CodeEnum;
import org.example.exceptions.BusinessException;

/**
 * @author zzx
 * @date 2021/12/16-14:38
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FunctionUtils {

    /**
     * 如果isError是true，则抛出错误
     *
     * @param isError 是否错误
     * @return ThrowExceptionHandler
     */
    public static ThrowExceptionHandler isWrong(boolean isError) {
        return message -> {
            if (isError) {
                // 打出错误信息日志，并报错
                log.error(message);
                throw new BusinessException(CodeEnum.ERROR, message);
            }
        };
    }

}
