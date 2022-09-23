/**
 * Top-level build file where you can add configuration options common to all sub-projects/modules.
 *
 * Android 知识总结 //
 * Android 系统架构
 * Application -> 应用层
 * Application Framework -> 应用框架层
 * Libraries (Android Runtime) -> 系统运行库层
 * Linux Kernel -> Linux 内核层
 *
 * 四大组件： 活动 Activity; 服务 Service; 广播 Broadcast; 内容提供者 Content Provider;
 * Activity -> UI 显示
 * Service -> 后台数据处理
 * Broadcast -> ‘变化’通知
 * Content Provider -> 数据存储
 *
 * Fragment 碎片
 * fragment
 *
 * fragment 通信
 * rxbus， eventbus 1.代码非常难维护；2.不能返回消息
 * 广播 处理系统级消息 1.传递数据限制
 * handler 1.内存泄漏；2.存在耦合；3.返回消息困难
 *
 * 接口回调 interface: 1.当 fragment 数量过多时，会导致 activity 臃肿 可读性降低
 *
 *
 * 线程 UI -> 主线程 不能做耗时操作 -> ANT
 * UI 启动子线程开始做耗时操作 -> 完毕后 通知主线程进行更新
 *
 * UI 优化
 * 动画 ->
 * 1.视图动画
 *   a. 补间动画
 *      ①. 平移
 *      ②. 缩放
 *      ③. 旋转
 *      ④. 透明度变化
 *   b. 逐帧动画
 * 2.属性动画
 *  a. 插值器
 *  b. 估值器
 *
 * 数据获取
 *
 * 线程
 *  Handler
 * 进程
 *  AIDL -> Binder机制 -> CS 模型
 * 数据存储
 *
 * 权限获取
 *  Android 6.0 之后 权限动态获取
 *
 * 传感器
 *  陀螺仪
 *  红外感应
 *  指纹
 *  摄像头
 *
 * 音频处理
 *
 * 网络请求
 * Retrofit -> OKHttp
 *
 * 扫码功能
 * ZXing
 * 1. 导入 ZXing.jar
 * 2. 复制扫码所用到的资源到工程中
 * 3. 复制扫码所需要的类到工程中
 *
 * 播放器 MediaPlayer + TextureView
 * 分享 ShareSDK
 * 查看大图功能 github-> PhotoView
 *
 * NDK -> 工具集
 * JNI -> Java Native Interface
 *  基本类型
 *    jstring
 *    jint
 *    jshort
 *    jbool
 *
 *  静态注册
 *  动态注册
 *  Java类型  相应的签名                                       例子
 *  boolean     Z
 *  byte        B
 *  char        C
 *  short       S
 *  int         I
 *  long        L
 *  float       F
 *  double      D
 *  void        V
 *  Object      L用"/"分割的完整类名;                        ex: Ljava/lang/String;
 *  Array       [签名                                      ex: [I [Ljava/lang/String;
 *  Method      (参数1类型签名参数2类型签名...)返回值类型签名
 *
 * todo 查看 java 类中 方法的签名 javap -s -p className
 *
 *  相互调用
 *    Java -> Native:
 *    方法中 有 native 关键字, kotlin 中是 external
 *    Native -> Java:
 *
 *    获取类
 *    FindClass("")
 *
 *    获取方法
 *
 *    GetMethodID(cls, "方法名", "方法签名")
 *    GetStaticMethodID(cls, "方法名", "方法签名")
 *
 *    调用方法
 *
 *    // 修改原生的参数
 *    SetObjectField(cls)
 *    //
 *    CallObjectMethod()
 *    CallStaticObjectMethod()
 *
 * Android应用开发
 * 逆向安全
 * 音视频
 * 车联网
 * 物联网
 * 手机开发
 * SDK开发
 *
 * gradle 依赖包相关
 * compile -> api
 * 使用该方式依赖的库将会参与编译和打包
 * compile -> implementation
 * 只能在内部使用此模块 不会对依赖进行传递
 * provided -> compileOnly
 * 只在编译时有效，不会参与打包
 * apk -> runtimeOnly
 * 只在生成apk的时候参与打包，编译时不会参与，很少用
 * testCompile -> testImplementation
 * 只在单元测试代码的编译以及最终打包测试apk时有效
 * debugCompile -> debugImplementation
 * 只在debug模式的编译和最终的debug apk打包时有效
 * releaseCompile -> releaseImplementation
 * 仅仅针对Release 模式的编译和最终的Release apk打包
 */
// class文件
/**
 * 类型             名称                   数量
 * u4              magic                  1
 * u2              minor_version          1
 * u2              major_version          1
 * u2              constant_pool_count    1
 * cp_info         constant_pool          constant_pool_count - 1
 * u2              access_flags           1
 * u2              this_class             1
 * u2              super_class            1
 * u2              interfaces_count       1
 * u2              interfaces             interfaces_count
 * u2              fields_count           1
 * field_info      fields                 fields_count
 * u2              methods_count          1
 * method_info     methods                methods_count
 * u2              attributes_count       1
 * attribute_info  attributes             attributes_count
 */

/**
 * ClassLoader
 */

/**
 * AndFix:
 * 导入包依赖
 * compile 'com.alipay.euler:andfix:0.5.0@aar'
 *
 * 必须包含的权限
 * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 *
 * 初始化
 * patchManager = new PatchManager(context) -> 尽量在 application 中操作
 * initPatch(appVersionName)
 *
 * 加载 补丁文件 -> 在 Application 的 onCreate() 方法中进行加载
 * patchManager.loadPatch() -> 加载全部补丁文件
 * patchManager.loadPatch(path) -> 加载指定补丁文件
 *
 * 添加补丁文件 进行修复
 * patchManager.addPatch(path)
 *
 * 生成补丁文件
 * apkpatch -f <new.apk> -t <old.apk> -o <output/dir/name.apatch> -k <keystore> -p <***> -a <alias> -e <***>
 *
 * Tinker:
 * 命令行集成方式:
 *
 * 工程module build.gradle.kts ：
 * implementation 'com.android.support:multidex:1.0.3'
 * // tinker 热修复
 * //tinker's main Android lib
 * implementation("com.tencent.tinker:tinker-android-lib:$TINKER_VERSION") { changing = true }-
 * implementation("com.tencent.tinker:tinker-android-loader:$TINKER_VERSION) { changing = true }-
 * // 若使用annotation需要单独引用,对于tinker的其他库都无需再引用
 * // optional, help to generate the final application
 * compileOnly("com.tencent.tinker:tinker-android-anno:$TINKER_VERSION") { changing = true }-
 * kotlin ->
 * kapt("com.tencent.tinker:tinker-android-anno:$TINKER_VERSION") { changing = true }-
 * java ->
 * annotationProcessor("com.tencent.tinker:tinker-android-anno:$TINKER_VERSION") { changing = true }*
 * 工程根目录 build.gradle.kts ：
 * classpath("com.tencent.tinker:tinker-patch-gradle-plugin:$TINKER_VERSION")
 *
 * 工程根目录 配置文件 gradle.properties：
 * TINKER_ID=1.0
 * TINKER_VERSION=1.9.9
 * TINKER_ENABLE=true
 *
 * 建立类
 * @DefaultLifeCycle (
 * application = ".tinker.CustomTinkerApplication " ,
 * flags =ShareConstants.TINKER_ENABLE_ALL,
 * loadVerifyFlag =false)
 * CustomTinkerLike 继承 ApplicationLike
 * // 复写方法 onBaseContextAttached 中-> 初始化Tinker
 * override fun onBaseContextAttached(base: Context?) {*      super.onBaseContextAttached(base)
 *      // 需要时分包
 *      MultiDex.install(base)
 *      // 初始化 Tinker
 *      TinkerManager.installTinker(this) -> TinkerInstaller.install(mAppLike)
 *}--
 *
 * // 加载补丁文件
 * // path -> 补丁文件路径
 * fun loadPatch(path: String) {*     if (Tinker.isTinkerInstalled()) {*         TinkerInstaller.onReceiveUpgradePatch(appLike.application.application.applicationContext, path)
 *}}--
 *
 * 权限
 * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 *
 * AndroidManifest.xml application ：
 * <meta-data
 *      android:name="TINKER_ID"
 *      android:value="tinker_id_052320"/>
 * 应用 name=".tinker.CustomTinkerApplication"
 *
 * 打包工具 Github 下载源码 idea 或 as 打开
 * 执行 tinker-patch-cli module 的 build.gradle.kts buildTinkerSdk 任务 生成 jar 文件
 * 如果jar 没有包含 test.dex ->
 * 使用 WinRAR 打开jar 将 tinker-patch-lib -> src/main/resources 下的文件 复制进 jar 包中
 *
 * 使用 java -jar tinker-patch-cli-1.9.12.jar -old old.apk -new new.apk -config tinker_config.xml -out output 生成差异包
 * 将生成的差异包 推送到指定目录
 *
 * 差异包加载过后 会自动删除
 * 1.9 版本之后可以新增非export的Activity 生成patch需要 忽略警告信息 ignoreWarning = true
 *
 * gradle 集成方式
 * // 基本的文件夹 会输出到 app/build/bakApk
 * def bakPath = file("${buildDir}/bakApk/")
 * //D:\Project\Android\MyApplication\app\build\bakApk\app-release-0404-14-44-21.apk
 * ext {*     // 是否开启 tinker
 *     tinkerEnabled = true
 *     // 老的 APK 安装包路径
 *     oldApkPath = "${bakPath}/app-release-0404-14-44-21.apk"
 *     // 混淆文件
 *     tinkerApplyMappingPath = "${bakPath}/app-release-0404-14-44-21-mapping.txt"
 *     // 资源文件清单
 *     tinkerApplyResourcePath = "${bakPath}/app-release-0404-14-44-21-R.txt"
 *     // tinker 版本
 *     tinkerID = "1.0.0"
 *     // 多渠道
 *     tinkerBuildFlavorDirectory = "${bakPath}/app"
 *}-//
 * 插件化 Small
 * 不能使用 kotlin 插件添加的 kotlin 版本已经废弃 会失败
 * 完整的 Small 需要使用 2.3.0 版本的 plugin gradle 版本
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 * Cordova 学习
 * 安装 : Node.js -> npm 安装 Cordova
 * npm -> npm install -g cordova
 * 测试安装 -> cordova -v
 *
 * 开始
 * 创建项目:
 * cordova create [projectName] [packageName] [appName]
 * 生成项目目录
 * project-> hooks ->
 *        -> node_modules ->
 *        -> platforms -> android
 *                     -> ios
 *                     -> ...
 *        -> plugins -> plugin ->
 *                   -> plugin ->
 *        -> www -> css ->
 *               -> js ->
 *               -> img ->
 *               -> index.html
 *        -> config.xml
 *        -> package.json
 *        -> package-lock.json
 * 添加平台 (在工程目录下) ->
 * cordova platform add [android/ios/...]
 *
 * Cordova 插件开发 ->
 *
 *
 *
 *
 *
 * 开源库 源码阅读
 * Glide
 * 1. 使用
 * Glide.with(activity/fragment/context/view).load(url/uri/@Res-int).into(ImageView)
 * -占位图 placeholder
 *
 *
 *
 *
 *
 *
 */


buildscript {
    val projectTargetSDK by extra(33)
    val projectMinSDK by extra(21)
    val kotlinVersion by extra("1.7.10")
    val compose_version by extra("1.2.0")
    repositories {
        maven("https://jitpack.io")
        maven("file:///D:/Maven")
        jcenter()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.2.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
        // NOTE: Do not place your application dependencies here;
        // they belong in the individual module build.gradle.kts files
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.1")
        classpath("com.jakewharton:butterknife-gradle-plugin:10.2.3")
        classpath("com.alibaba:arouter-register:1.0.2")


        // 自定义插件
//        classpath("com.example.lib:plugin:0.0.1")
    }
}

class BuildTimeListener : TaskExecutionListener, BuildListener {
    private var taskTimeMap = linkedMapOf<String, TaskTimeInfo>()

    inner class TaskTimeInfo {

        var total: Long = 0L
        var path: String = ""
        var start: Long = 0L
        var end: Long = 0L
    }

    /**
     * 开始执行任务之前
     */
    override fun beforeExecute(task: Task) {
        val taskTimeInfo = TaskTimeInfo()
        taskTimeInfo.start = System.currentTimeMillis()
        taskTimeInfo.path = task.path
        taskTimeMap[task.path] = taskTimeInfo
    }

    /**
     * 任务执行完成之后
     */
    override fun afterExecute(task: Task, taskState: TaskState) {
        val taskTimeInfo = taskTimeMap[task.path]
        taskTimeInfo?.end = System.currentTimeMillis()
        taskTimeInfo?.let {
            it.total = it.end - it.start
        }
    }

    override fun settingsEvaluated(settings: Settings) {

    }

    override fun projectsLoaded(gradle: Gradle) {

    }

    override fun projectsEvaluated(gradle: Gradle) {

    }

    override fun buildFinished(buildResult: BuildResult) {
        println("==============================")
        println("")
        for (mutableEntry in taskTimeMap) {
            println("${mutableEntry.value.path} : ${mutableEntry.value.total}")
        }
        println("")
        println("==============================")
    }
}

subprojects {
    repositories {
        maven("https://jitpack.io")
        maven("file:///D:/Maven")
        jcenter()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        mavenCentral()
        google()
    }

    afterEvaluate {
        println("=========== ${this.name} Build Over ==============")
    }
    this.gradle.addBuildListener(BuildTimeListener())
}

tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
}