package org.example.enumeration.base;

/**
 * 枚举基类
 * @author zzx
 */
public interface BaseEnum {

    /**
     * name
     *
     * @return name
     */
    String name();

    /**
     * 描述
     *
     * @return 描述
     */
    String description();

    /**
     * 获取code值
     *
     * @return codeInt
     */
    default int getCode() {
        return -1;
    }
}
