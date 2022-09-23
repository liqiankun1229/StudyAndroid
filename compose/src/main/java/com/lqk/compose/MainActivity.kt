package com.lqk.compose

import android.app.Service
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.MainThread
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.google.accompanist.imageloading.rememberDrawablePainter
import com.lqk.compose.helper.info.AppData
import com.lqk.compose.ui.theme.MyApplicationTheme
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
        listData.clear()
        listData.addAll(list)
        // 检查无障碍服务
        // 多选安装
        // 单选安装
        setContent {
//            ApkInfo(apks = listData)
            CustomRootView()
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

@Composable
fun WhoBirthday(name: String, form: String) {
    val context = LocalContext.current
    val offset = Offset(5.0f, 10f)
    val annotatedText = buildAnnotatedString {
        append("click")
    }
    var outlinedText by remember {
        mutableStateOf("123")
    }
    val bg = rememberDrawablePainter(ContextCompat.getDrawable(context, R.mipmap.bg))
//    painterResource(id = )
    Box {
        Image(
            painter = bg,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier.padding(start = 0.dp, top = 100.dp, end = 0.dp, bottom = 0.dp)
        ) {
            Text(
                text = "Happy Birthday $name !!!",
                fontSize = 36.sp
            )
            ClickableText(

                text = AnnotatedString(
                    text = "Click",
                    spanStyle = SpanStyle(
                        fontSize = 24.sp
                    )
                ),
                modifier = Modifier
                    .width(100.dp)
                    .padding(all = 5.dp)
                    .border(1.dp, Color.Black)
                    .padding(all = 5.dp),
                onClick = {
                    context.startActivity(Intent(context, SecondActivity::class.java))

                    Toast.makeText(context, "Offset: ", Toast.LENGTH_SHORT).show()
                }
            )
            OutlinedTextField(
                value = outlinedText,
                label = {
                    Text(text = "name")
                },
                onValueChange = {
                    outlinedText = it
                })
            Text(
                // 内容
//            text = "-- form $form",
                // 花式 内容 有颜色
                buildAnnotatedString {
                    append("--form ")
                    // 文字内容
                    withStyle(style = SpanStyle(color = Color.Blue)) {
                        append("L")
                    }
                    // Paragraph 设置行高, 回自动换行
                    withStyle(style = ParagraphStyle(lineHeight = 30.sp)) {
                        withStyle(style = SpanStyle(color = Color.Black)) {
                            append(form)
                        }
                    }
                },
                // 宽度占满父布局
                modifier = Modifier.fillMaxWidth(),
                // 字体大小
                fontSize = 24.sp,
                // 字重
                fontWeight = FontWeight.Bold,
                // 内部位置
                textAlign = TextAlign.End,
                // 字体
                fontFamily = FontFamily.Serif,
                // 字体样式
                style = TextStyle(
                    fontSize = 24.sp,
                    shadow = Shadow(
                        color = Color.Red,
                        offset = offset,
                        blurRadius = 3f
                    )
                ),
            )
        }
    }

}

@Composable
fun CustomRootView() {
    MyApplicationTheme {
        WhoBirthday(name = "Sam", "ni")
    }
}

/**
 * App 信息 Item
 */
@Composable
fun ApkInfo(apks: MutableList<AppData>) {
//    Text(
//        text = "${apks.size}",
//        color = Color.Red
//    )

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
