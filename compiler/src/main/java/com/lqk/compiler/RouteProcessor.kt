package com.lqk.compiler

import com.google.auto.service.AutoService
import com.lqk.annotations.MyGroupRoute
import com.squareup.javapoet.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

/**
 * @author LQK
 * @time 2022/3/4 15:29
 * @remark
 */
@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class RouteProcessor : AbstractProcessor() {

    private var env: Elements? = null
    private var filer: Filer? = null
    private var messager: Messager? = null
    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        env = processingEnv?.elementUtils
        filer = processingEnv?.filer
        messager = processingEnv?.messager
        messager?.printMessage(Diagnostic.Kind.NOTE, "---------------RouteProcessor")
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        val types = LinkedHashSet<String>()
        types.add(MyGroupRoute::class.java.canonicalName)
        return types
    }

    override fun process(
        elementSet: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        // 拿到注解 的 path /group/path
        // 校验注解位置是否正确

        // 生成 group /group/

        // 生成 route /group/path

        val packageName = "com.lqk.routes"
        val className = "_Group"
        val routePathName = "_Route"
        messager?.printMessage(Diagnostic.Kind.WARNING, ">>>>>>>>>>>>>>>>>>开始生成 $routePathName")
        println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        // 拿到所有使用注解的类
        var map = mutableSetOf<String>()
        val environmentSet = roundEnv?.getElementsAnnotatedWith(MyGroupRoute::class.java)
        environmentSet?.forEach {
            var element : Element = it
            var eName = element.simpleName
            var eInterface = (element as TypeElement).interfaces
            var eSize = eInterface.size
//            var e1 = eInterface[0]

            try {
                messager?.printMessage(Diagnostic.Kind.WARNING, ">>>>>>>>>>>>>>>>>>构造方法返回对象")
                // 生成变量 Map<String, String> 作为返回值
                val returnType =
                    ParameterizedTypeName.get(
                        Map::class.java,
                        String::class.java,
                        String::class.java
                    ) //
                // 给 map 添加元素
                messager?.printMessage(Diagnostic.Kind.WARNING, ">>>>>>>>>>>>>>>>>>构造方法体")
                // 方法 构造
                val methodBuilder =
                    MethodSpec.methodBuilder("createGroup")
//                        .addAnnotation(Override::class.java)
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .returns(returnType)
                messager?.printMessage(
                    Diagnostic.Kind.WARNING,
                    ">>>>>>>>>>>>>>>>>>构造方法指令-代码行 Map<String, String> map = new Map<>()"
                )
                // 生成一行代码 Map<String, String> map = new Map<>()
                val codeBlock =
                    CodeBlock.builder()
                        .add(
                            "\$T<\$T,\$T> \$N = new \$T<>()", // 格式话模板
                            ClassName.get(Map::class.java),
                            ClassName.get(String::class.java),
                            ClassName.get(String::class.java),
                            "map",
                            ClassName.get(HashMap::class.java)
                        ).build()
                methodBuilder.addStatement(codeBlock)

                messager?.printMessage(
                    Diagnostic.Kind.WARNING,
                    ">>>>>>>>>>>>>>>>>>构造方法指令 map.put(\"test\",\"123\")"
                )
                // 生成一行代码 map.put("test","123")
                val codeBlock1 = CodeBlock.builder()
                    .add("\$N.put(\$S, \$S)", "map", "test", "123").build()
                methodBuilder.addStatement(codeBlock1)

                messager?.printMessage(
                    Diagnostic.Kind.WARNING,
                    ">>>>>>>>>>>>>>>>>>构造方法指令 return map"
                )
                // 生产一行代码 return map
                val codeBlock2 = CodeBlock.builder()
                    .add("return \$N", "map").build()
                methodBuilder.addStatement(codeBlock2)
                // 方法结束
                val methodSpec = methodBuilder.build()

                messager?.printMessage(Diagnostic.Kind.WARNING, ">>>>>>>>>>>>>>>>>>构造类")
                // 生成类名 it.simpleName + _Group
                val typeSpec = TypeSpec.classBuilder("${it.simpleName}$className")
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(methodSpec)
                    .build()

                messager?.printMessage(Diagnostic.Kind.WARNING, ">>>>>>>>>>>>>>>>>>构造JavaFile")
                // 包名 和 文件创建 一起
                val javaFile = JavaFile.builder(packageName, typeSpec).build()
                javaFile.writeTo(filer)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
//
//        // 方法
//        var modthSpec = MethodSpec.methodBuilder().build()
//
//        // 定义常量
//
//        // 类名 XXX_Group
//        var typeSpec = TypeSpec.classBuilder("Activity_Group").build()
//
//        // Java文件
//        var javaFile = JavaFile.builder(packageName, typeSpec)


        // 方法内执行指令


        return false
    }

    fun createGroup() {}

    fun createRoute() {}
}