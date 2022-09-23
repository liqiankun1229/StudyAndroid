package com.lqk.activity;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

    }

    public class TaskRunnable implements Runnable {

        public class TaskBean {
            private int num;

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }


            public String toString() {
                return "Num:" + num;
            }
        }

        public TaskBean bean = new TaskBean();

        @Override
        public void run() {

        }
    }
}