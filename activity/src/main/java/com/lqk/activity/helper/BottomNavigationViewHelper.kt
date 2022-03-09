package com.lqk.activity.helper

import android.annotation.SuppressLint
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * @date 2018/9/15
 * @time 18:37
 * @remarks
 */
object BottomNavigationViewHelper {

    @SuppressLint("RestrictedApi")
    fun disableShiftMode(view: BottomNavigationView) {
        val menuView = view.getChildAt(0) as BottomNavigationMenuView
        try {
            val shiftingMode = (menuView::class.java).getDeclaredField("mShiftingMode")
            shiftingMode.isAccessible = true
            shiftingMode.setBoolean(menuView, false)
            shiftingMode.isAccessible = false
            for (i in 0..menuView.childCount) {
                var item = menuView.getChildAt(i) as BottomNavigationItemView
                //noinspection RestrictedApi
//                item.setShifting(false)
                item.setChecked(item.itemData?.isChecked ?: false)
            }
        } catch (e: NoSuchFieldException) {
            Log.e("", "", e)
        } catch (e: IllegalAccessException) {
            Log.e("", "", e)
        }
    }
}