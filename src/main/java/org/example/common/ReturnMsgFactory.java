package org.example.common;


import org.example.enumeration.CodeEnum;

/**
 * Created by superlee on 2017/11/7.
 * baseResult工程方法
 */
public final class ReturnMsgFactory {

    /**
     * 根据Code创建有返回消息的result
     *
     * @param codeEnum 状态Code体
     * @return ReturnMsg
     */
    public static ReturnMsg newResult(CodeEnum codeEnum) {
        return new ReturnMsg(codeEnum.getCode(), codeEnum.description());
    }

    /**
     * 创建无返回消息的成功的result
     *
     * @return ReturnMsg
     */
    public static ReturnMsg newSuccess() {
        return new ReturnMsg(CodeEnum.SUCCESS.getCode());
    }

    /**
     * 创建成功有返回的result
     *
     * @param returnMessage 返回信息
     * @return ReturnMsg
     */
    public static ReturnMsg newSuccess(Object returnMessage) {
        return new ReturnMsg(CodeEnum.SUCCESS.getCode(), returnMessage);
    }

}
