package com.lqk.butter.handler;

/**
 * @author LQK
 * @time 2019/2/20 11:02
 * @remark
 */
public class CustomLooper {

    static ThreadLocal<CustomLooper> sThreadLocal = new ThreadLocal<>();

    CustomMessageQueue customMessageQueue;

    public CustomLooper() {
        customMessageQueue = new CustomMessageQueue();
    }

    public static void prepare() {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("");
        }
        sThreadLocal.set(new CustomLooper());
    }

    public static CustomLooper myLooper() {

        return sThreadLocal.get();
    }

    /**
     * 循环处理消息
     */
    public static void loop() {
        CustomLooper customLooper = myLooper();
        CustomMessageQueue queue = customLooper.customMessageQueue;
        for (; ; ) {
            CustomMessage customMessage = queue.next();
            if (customMessage == null) {
                continue;
            }

            customMessage.target.dispatchMessage(customMessage);
        }
    }
}
