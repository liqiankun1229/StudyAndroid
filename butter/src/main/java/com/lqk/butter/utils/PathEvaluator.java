package com.lqk.butter.utils;

import android.animation.TypeEvaluator;

/**
 * @author LQK
 * @time 2019/2/17 11:03
 * @remark TypeEvaluator 估值器
 */
public class PathEvaluator implements TypeEvaluator<PathPoint> {


    @Override
    public PathPoint evaluate(float fraction, PathPoint startValue, PathPoint endValue) {

        float x, y;
        // fraction : 执行的百分比 0~1
        // 按照不同的路径类型运动，计算方式不同
        if (endValue.mOperstion == PathPoint.OPERATION_LINE) {
            // 按照直线运动
            // x，y = 起点 + fraction * (起点到终点的距离)
            x = startValue.mX + fraction * (endValue.mX - startValue.mX);
            y = startValue.mY + fraction * (endValue.mY - startValue.mY);

        } else if (endValue.mOperstion == PathPoint.OPERATION_CUBIC) {
            // 贝塞尔曲线运动 3阶
            float oneMinusT = 1 - fraction;
            x = oneMinusT * oneMinusT * oneMinusT * startValue.mX +
                    3 * oneMinusT * oneMinusT * fraction * endValue.mControl0X +
                    3 * oneMinusT * fraction * fraction * endValue.mControl1X +
                    fraction * fraction * fraction * endValue.mX;
            y = oneMinusT * oneMinusT * oneMinusT * startValue.mY +
                    3 * oneMinusT * oneMinusT * fraction * endValue.mControl0Y +
                    3 * oneMinusT * fraction * fraction * endValue.mControl1Y +
                    fraction * fraction * fraction * endValue.mY;
        } else {
//            (endValue.mOperstion == PathPoint.OPERATION_MOVE)
            x = endValue.mX;
            y = endValue.mY;
        }
        return PathPoint.moveTo(x, y);
    }
}
