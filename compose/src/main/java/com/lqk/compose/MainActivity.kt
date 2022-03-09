package com.lqk.compose

import android.app.Service
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.MainThread
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import com.google.accompanist.imageloading.rememberDrawablePainter
import com.lqk.compose.helper.info.AppData
import kotlinx.coroutines.launch

const val TAG = "MainActivity"

class CustomService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}

class MainActivity : ComponentActivity(), LifecycleEventObserver {


    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> {
                loadData()
            }
            else -> {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 遍历手机的 apk 文件
//        loadAllApp(this)
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val packageManager = this.packageManager
        val appInfo = packageManager.queryIntentActivities(intent, 0)
        val list = mutableListOf<AppData>()
        appInfo.forEachIndexed { index, resolveInfo ->
            run {
                val appData = AppData(
                    resolveInfo.activityInfo.packageName,
                    resolveInfo.loadLabel(packageManager) as String,
                    resolveInfo.loadIcon(packageManager),
                    resolveInfo.activityInfo.name,
                    resolveInfo.activityInfo.flags == ApplicationInfo.FLAG_SYSTEM
                )
                list.add(appData)
            }
        }
        listData.addAll(list)
        // 检查无障碍服务
        // 多选安装
        // 单选安装
        setContent {
            ApkInfo(apks = listData)
        }
    }
}

var appList = object : LiveData<MutableList<AppData>>() {
    override fun postValue(value: MutableList<AppData>?) {
        super.postValue(value)

    }

    @MainThread
    override fun setValue(value: MutableList<AppData>?) {
        super.setValue(value)

    }
}

var listData = mutableListOf<AppData>()


fun loadData() {
    // 更新数据
}


/**
 * App 信息 Item
 */
@Composable
fun ApkInfo(apks: MutableList<AppData>) {
    LazyColumn(content = {
        apks.forEach {
            item {
                Row(
                    modifier = Modifier.padding(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = rememberDrawablePainter(drawable = it.appIcon),
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp),
                        contentDescription = ""
                    )
                    Text(text = it.appName, modifier = Modifier.fillMaxHeight())
                }
            }
        }
    })
}

data class ApkInfoBean(val name: String, var isInstalled: Boolean = false)

@Composable
fun load() {
//    GlobalScope.launch {
//
//    }

    remember {
        return@remember 1
    }
}

@Composable
fun loadMore() {
    LaunchedEffect(key1 = 2) {

    }
}

class NewsRepository() {
    fun loadSucceed(string: String) {}
}


class NewsViewModel(private val loadNewsRepository: NewsRepository) : ViewModel() {
    fun load() {
        viewModelScope.launch {
            loadNewsRepository.loadSucceed("{}")
        }
    }
}
