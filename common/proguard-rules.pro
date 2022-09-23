# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 关闭压缩
-dontshrink
# 关闭代码优化
-dontoptimize
# 关闭混淆
-dontobfuscate
# 指定代码优化等级 0-7
-optimizationpasses 5
# 混淆时不使用大小写混合类名
-dontusemixedcaseclassnames
# 不忽略库中的 非 public 类
-dontskipnonpubliclibraryclasses
# 不忽略库中 非 public 的类成员
-dontskipnonpubliclibraryclassmembers
# 输出详细信息
-verbose
# 不做预校验
-dontpreverify