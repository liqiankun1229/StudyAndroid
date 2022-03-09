package com.lqk.butter.handler;

/**
 * @author LQK
 * @time 2019/2/20 11:01
 * @remark
 */
public class CustomHandler {
    // 消息队列
    CustomMessageQueue customMessageQueue;
    // 循环处理消息
    CustomLooper customLooper;

    public CustomHandler() {
        customLooper = CustomLooper.myLooper();
        customMessageQueue = customLooper.customMessageQueue;
    }

    /**
     * 发送消息 -> 加入消息队列
     *
     * @param customMessage 消息
     */
    public void sendMessage(CustomMessage customMessage) {
        customMessage.target = this;
        customMessageQueue.enqueueMessage(customMessage);
    }

    public void dispatchMessage(CustomMessage customMessage) {
        handlerMessage(customMessage);
    }

    /**
     * 消息处理 需要 用户自己复写处理
     */
    public void handlerMessage(CustomMessage customMessage) {
    }

}
