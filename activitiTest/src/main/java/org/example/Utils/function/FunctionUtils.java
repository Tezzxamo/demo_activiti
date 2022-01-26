package org.example.Utils.function;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Utils.function.handler.*;
import org.example.exceptions.CustomException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * @author zzx
 * @date 2021/12/16-14:38
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FunctionUtils {

    /**
     * 参数为true或false时，分别进行不同的操作
     *
     * @param bool 布尔值
     * @return DirectionHandler
     */
    public static BranchHandler isTrueOrFalse(boolean bool) {
        return (trueHandler, falseHandler) -> {
            if (bool) {
                trueHandler.run();
            } else {
                falseHandler.run();
            }
        };
    }

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
                throw new CustomException(message);
            }
        };
    }

    /**
     * 如果是null，返回默认值，如果不是null，返回原值
     *
     * @param object 传来的值
     * @return ZeroNumberHandler
     */
    public static ZeroNumberHandler isNullNumber(Object object) {
        return value -> {
            if (Objects.isNull(object)) {
                return value;
            }
            try {
                if (object instanceof String) {
                    return new BigDecimal(object.toString());
                } else if (object instanceof BigDecimal) {
                    return (BigDecimal) object;
                }
            } catch (Exception e) {
                FunctionUtils.isWrong(true).throwCustomException("未能转换为BigDecimal的数据类型");
            }
            FunctionUtils.isWrong(true).throwCustomException("未能解析为BigDecimal的数据类型");
            return null;
        };
    }

    /**
     * 设置BigDecimal的位数
     *
     * @return BigDecimalScaleHandler
     */
    public static BigDecimalScaleHandler withBigDecimal() {
        return (scale, bigDecimal) -> {
            bigDecimal = FunctionUtils.isNullNumber(bigDecimal).setDefault(BigDecimal.ZERO);
            return bigDecimal.setScale(scale, RoundingMode.HALF_UP).toPlainString();
        };
    }

    /**
     * 设置string形式的数值的位数
     *
     * @return StringScaleHandler
     */
    public static StringScaleHandler withString() {
        return (scale, string) -> {
            BigDecimal bigDecimal = FunctionUtils.isNullNumber(string).setDefault(BigDecimal.ZERO);
            return bigDecimal.setScale(scale, RoundingMode.HALF_UP).toPlainString();
        };
    }


}
