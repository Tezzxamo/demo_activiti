package org.example.Utils.function.handler;

/**
 * @author zzx
 * @date 2021/12/16-15:11
 */
@FunctionalInterface
public interface StringScaleHandler {

    /**
     * 设置位数
     *
     * @param scale    设置位数
     * @param strValue String的值
     * @return string
     */
    String setScale(int scale, String strValue);

}
