package com.lqk.butter.ui.activity

import android.app.Activity
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import com.lqk.annotations.MyGroupRoute
import com.lqk.butter.R
import com.lqk.butter.base.BaseActivity
import com.lqk.butter.common.Constant

/**
 * 修改 启动图标
 */
@MyGroupRoute("/group/change", "/group/")
class ChangeIconActivity : BaseActivity() {


    companion object {
        const val KEY_ICON = "KEY_ICON"
        const val KEY_ICON_NAME = "KEY_ICON_NAME"
    }

    override fun layoutId(): Int {
        return R.layout.activity_change_icon
    }

    override fun initView() {
        disableIcon()
        checkIcon("Test1")
    }

    private fun disableIcon() {
        val mPm = applicationContext.packageManager
        for (entry in Constant.mapIcon) {
            if (entry.value.isEmpty()) {
                mPm.setComponentEnabledSetting(
                        componentName,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP)
            } else {
                val packName = packageName
                mPm.setComponentEnabledSetting(
                        ComponentName(baseContext, "$packName.${entry.value}"),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP)
            }
        }
    }

    private fun checkIcon(icon: String) {
        val mPm = applicationContext.packageManager
        var checkSuccess = false
        for (entry in Constant.mapIcon) {
            if (entry.value == icon) {
                mPm.setComponentEnabledSetting(
                        ComponentName(baseContext, "$packageName.${entry.value}"),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        0)
//                        PackageManager.DONT_KILL_APP)
                checkSuccess = true
                Toast.makeText(this, "更新 Icon 成功-重启APP", Toast.LENGTH_SHORT).show()
            }
        }
        if (!checkSuccess) {
            mPm.setComponentEnabledSetting(
                    componentName,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP)
            Toast.makeText(this, "更新 Icon 失败", Toast.LENGTH_SHORT).show()
        }
    }

    private fun rebootNow() {
        val mPm = applicationContext.packageManager
        val am = applicationContext.getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
        val reIntent = Intent(Intent.ACTION_MAIN)
        reIntent.addCategory(Intent.CATEGORY_HOME)
        reIntent.addCategory(Intent.CATEGORY_DEFAULT)
        val resolves = mPm.queryIntentServices(reIntent, 0)
        for (resolve in resolves) {
            if (resolve.activityInfo != null) {
                am.killBackgroundProcesses(resolve.activityInfo.packageName)
            }
        }
    }
}
