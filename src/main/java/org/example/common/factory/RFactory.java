package org.example.common.factory;


import com.alibaba.fastjson.JSONObject;
import org.example.enumeration.CodeEnum;
import org.example.model.base.R;

public final class RFactory {

    /**
     * 根据Code创建有返回消息的result
     *
     * @param codeEnum 状态Code体
     * @return ReturnMsg
     */
    public static R<JSONObject> newResult(CodeEnum codeEnum) {
        return new R<>(codeEnum.getCode(), new JSONObject(), codeEnum.description());
    }

    /**
     * 根据Code创建有返回消息的result
     *
     * @param codeEnum 状态Code体
     * @return ReturnMsg
     */
    public static R<JSONObject> newResult(CodeEnum codeEnum, String msg) {
        return new R<>(codeEnum.getCode(), new JSONObject(), codeEnum.description() + msg);
    }

    /**
     * 创建无返回消息的成功的result
     *
     * @return ReturnMsg
     */
    public static R<JSONObject> newSuccess() {
        return new R<>(CodeEnum.SUCCESS.getCode(), new JSONObject(), "成功");
    }

    /**
     * 创建成功有返回的result
     *
     * @param result 返回信息
     * @return ReturnMsg
     */
    public static R<?> newSuccess(Object result) {
        return new R<>(CodeEnum.SUCCESS.getCode(), result, "成功");
    }

    /**
     * 创建无返回消息的错误的result
     *
     * @param msg 错误消息
     * @return {@link R}
     */
    public static R<JSONObject> newError(String msg) {
        return new R<>(CodeEnum.ERROR.getCode(), new JSONObject(), CodeEnum.ERROR.description() + "," + msg);
    }

    /**
     * 创建错误有返回的result
     *
     * @param result 返回信息
     * @return {@link R}
     */
    public static R<?> newError(Object result) {
        return new R<>(CodeEnum.ERROR.getCode(), result, CodeEnum.ERROR.description());
    }

    /**
     * 创建错误有返回的result
     *
     * @param result 返回信息
     * @param msg    错误消息
     * @return {@link R}
     */
    public static R<?> newError(Object result, String msg) {
        return new R<>(CodeEnum.ERROR.getCode(), result, CodeEnum.ERROR.description() + "," + msg);
    }

}
