package com.lqk.butter.handler;

import java.util.UUID;

/**
 * @author LQK
 * @time 2019/2/20 11:41
 * @remark
 */
public class CustomHandlerTest {
    public static void main(String[] args) {
        System.out.println("1234");
        CustomLooper.prepare();

        CustomHandler customHandler = new CustomHandler() {
            @Override
            public void handlerMessage(CustomMessage message) {
                if (message.what == 2) {
                    System.out.println("线程：" + Thread.currentThread() + "---- message:" + message.object.toString());
                }
                super.handlerMessage(message);
            }
        };

        new Thread() {
            @Override
            public void run() {
                CustomMessage customMessage = new CustomMessage();
                customMessage.what = 2;
                customMessage.object = UUID.randomUUID();
                System.out.println("线程：" + Thread.currentThread() + "---- message:" + customMessage.object.toString());
                customHandler.sendMessage(customMessage);
                super.run();
            }
        }.start();

        CustomLooper.loop();
    }
}
