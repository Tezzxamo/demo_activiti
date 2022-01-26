package org.example.Utils.function.handler;

import java.math.BigDecimal;

/**
 * @author zzx
 * @date 2021/12/16-15:11
 */
@FunctionalInterface
public interface BigDecimalScaleHandler {

    /**
     * 设置位数
     *
     * @param scale       设置位数
     * @param numberValue BigDecimal的值
     * @return string
     */
    String setScale(int scale, BigDecimal numberValue);

}
