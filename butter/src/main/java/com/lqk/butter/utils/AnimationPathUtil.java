package com.lqk.butter.utils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author LQK
 * @time 2019/2/13 22:24
 * @remark 动画路径工具类
 */
public class AnimationPathUtil {

    // 保存 动画路径上的点
    ArrayList<PathPoint> pathPofloats = new ArrayList<>();

    public void moveTo(float x, float y) {
        pathPofloats.add(PathPoint.moveTo(x, y));
    }

    public void cubicTo(float x, float y, float x1, float y1, float x2, float y2) {
        pathPofloats.add(PathPoint.cubicTo(x, y, x1, y1, x2, y2));
    }

    public void lineTo(float x, float y) {
        pathPofloats.add(PathPoint.lineTo(x, y));
    }

    public ArrayList<PathPoint> getPathPofloats(){
        return pathPofloats;
    }

}

