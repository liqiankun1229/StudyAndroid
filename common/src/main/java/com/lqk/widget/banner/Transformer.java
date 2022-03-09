package com.lqk.widget.banner;


import com.lqk.widget.banner.transformer.AccordionTransformer;
import com.lqk.widget.banner.transformer.BackgroundToForegroundTransformer;
import com.lqk.widget.banner.transformer.CubeInTransformer;
import com.lqk.widget.banner.transformer.CubeOutTransformer;
import com.lqk.widget.banner.transformer.DefaultTransformer;
import com.lqk.widget.banner.transformer.DepthPageTransformer;
import com.lqk.widget.banner.transformer.FlipHorizontalTransformer;
import com.lqk.widget.banner.transformer.FlipVerticalTransformer;
import com.lqk.widget.banner.transformer.ForegroundToBackgroundTransformer;
import com.lqk.widget.banner.transformer.RotateDownTransformer;
import com.lqk.widget.banner.transformer.RotateUpTransformer;
import com.lqk.widget.banner.transformer.ScaleInOutTransformer;
import com.lqk.widget.banner.transformer.StackTransformer;
import com.lqk.widget.banner.transformer.TabletTransformer;
import com.lqk.widget.banner.transformer.ZoomInTransformer;
import com.lqk.widget.banner.transformer.ZoomOutSlideTransformer;
import com.lqk.widget.banner.transformer.ZoomOutTransformer;

import androidx.viewpager.widget.ViewPager;


public class Transformer {
    public static Class<? extends ViewPager.PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ZoomOut = ZoomOutTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
