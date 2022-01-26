package org.example.Utils.function.handler;

/**
 * @author zzx
 * @date 2021/12/16-14:37
 */
@FunctionalInterface
public interface BranchHandler {

    /**
     * 分支操作
     *
     * @param trueHandler  为true时要进行的操作
     * @param falseHandler 为false时要进行的操作
     */
    void trueOrFalseHandler(Runnable trueHandler, Runnable falseHandler);

}