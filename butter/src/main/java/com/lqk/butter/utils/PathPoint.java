package com.lqk.butter.utils;

/**
 * @author LQK
 * @time 2019/2/16 20:24
 * @remark 存储路径 的点
 */
public class PathPoint {

    public static final float OPERATION_MOVE = 0;
    public static final float OPERATION_LINE = 1;
    public static final float OPERATION_CUBIC = 2;


    // 操作指令
    float mOperstion;

    float mX;
    float mY;
    float mControl0X;
    float mControl0Y;
    float mControl1X;
    float mControl1Y;


    private PathPoint(float operstion, float c0x, float c0y, float c1x, float c1y, float x, float y) {
        this.mOperstion = operstion;
        this.mX = x;
        this.mY = y;
        this.mControl0X = c0x;
        this.mControl0Y = c0y;
        this.mControl1X = c1x;
        this.mControl1Y = c1y;
    }

    private PathPoint(float operstion, float x, float y) {
        this.mOperstion = operstion;
        this.mX = x;
        this.mY = y;
    }

    public float getmOperstion() {
        return mOperstion;
    }

    public void setmOperstion(float mOperstion) {
        this.mOperstion = mOperstion;
    }

    public float getmX() {
        return mX;
    }

    public void setmX(float mX) {
        this.mX = mX;
    }

    public float getmY() {
        return mY;
    }

    public void setmY(float mY) {
        this.mY = mY;
    }

    public float getmControl0X() {
        return mControl0X;
    }

    public void setmControl0X(float mControl0X) {
        this.mControl0X = mControl0X;
    }

    public float getmControl0Y() {
        return mControl0Y;
    }

    public void setmControl0Y(float mControl0Y) {
        this.mControl0Y = mControl0Y;
    }

    public float getmControl1X() {
        return mControl1X;
    }

    public void setmControl1X(float mControl1X) {
        this.mControl1X = mControl1X;
    }

    public float getmControl1Y() {
        return mControl1Y;
    }

    public void setmControl1Y(float mControl1Y) {
        this.mControl1Y = mControl1Y;
    }

    public static PathPoint moveTo(float x, float y) {
        return new PathPoint(PathPoint.OPERATION_MOVE, x, y);
    }

    public static PathPoint lineTo(float x, float y) {
        return new PathPoint(PathPoint.OPERATION_LINE, x, y);
    }

    public static PathPoint cubicTo(float c0x, float c0y, float c1x, float c1y, float x, float y) {
        return new PathPoint(PathPoint.OPERATION_CUBIC, c0x, c0y, c1x, c1y, x, y);
    }

    public static class Builder {
        public Builder() {
        }

        public Builder(float operstion) {

        }

        public PathPoint moveTo(float x, float y) {
            return new PathPoint(PathPoint.OPERATION_MOVE, x, y);
        }

        public PathPoint lineTo(float x, float y) {
            return new PathPoint(PathPoint.OPERATION_LINE, x, y);
        }

        public PathPoint cubicTo(float c0x, float c0y, float c1x, float c1y, float x, float y) {
            return new PathPoint(PathPoint.OPERATION_CUBIC, c0x, c0y, c1x, c1y, x, y);
        }
    }
}
