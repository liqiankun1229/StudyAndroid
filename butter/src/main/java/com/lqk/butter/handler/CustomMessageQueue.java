package com.lqk.butter.handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author LQK
 * @time 2019/2/20 11:02
 * @remark
 */
public class CustomMessageQueue {

    public static final int MAX_COUNT = 50;
    Lock lock;
    Condition notEmpty;
    Condition notFull;

    CustomMessage[] items;
    int putIntex = 0;
    int takeIndex = 0;
    int count;
    public final BlockingQueue<CustomMessage> mQueue;


    public CustomMessageQueue() {
        this.mQueue = new ArrayBlockingQueue<CustomMessage>(MAX_COUNT);
        this.items = new CustomMessage[50];
        this.lock = new ReentrantLock();

        this.notEmpty = lock.newCondition();
        this.notFull = lock.newCondition();

    }

    /**
     * 入队
     *
     * @param customMessage 消息
     */
    public void enqueueMessage(CustomMessage customMessage) {
        try {
            lock.lock();

            while (count == items.length) {
                //
                try {

                    notEmpty.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            items[putIntex] = customMessage;
            putIntex = (++putIntex == items.length) ? 0 : putIntex;
            count++;
            // 唤醒
            notFull.signalAll();
        } finally {

            lock.unlock();
        }
    }

    /**
     * 出队
     *
     * @return 消息
     */
    public CustomMessage next() {
        CustomMessage customMessage = null;
        try {
            lock.lock();
            while (count == 0) {
                try {
                    notFull.await();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            customMessage = items[takeIndex];
            takeIndex = (++takeIndex == items.length) ? 0 : takeIndex;
            count--;
            // 唤醒
            notEmpty.signalAll();
        } finally {

            lock.unlock();
        }
        return customMessage;
    }
}
