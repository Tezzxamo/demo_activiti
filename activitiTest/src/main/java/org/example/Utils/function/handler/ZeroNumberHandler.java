package org.example.Utils.function.handler;

import java.math.BigDecimal;

/**
 * @author zzx
 * @date 2021/12/16-15:26
 */
@FunctionalInterface
public interface ZeroNumberHandler {

    /**
     * 设置默认值
     *
     * @param defaultValue 默认值
     * @return 默认值
     */
    BigDecimal setDefault(BigDecimal defaultValue);

}
