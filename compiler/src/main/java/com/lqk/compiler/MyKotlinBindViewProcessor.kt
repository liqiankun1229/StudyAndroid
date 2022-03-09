package com.lqk.compiler

import com.google.auto.service.AutoService
import com.lqk.annotations.MyKotlinBindView
import java.io.Writer
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.tools.Diagnostic

/**
 * 注册 自己的注解处理器 kapt
 */
@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class MyKotlinBindViewProcessor : AbstractProcessor() {

    var filer: Filer? = null
    var messager: Messager? = null

    @Synchronized
    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        filer = processingEnv?.filer

        messager = processingEnv?.messager
        messager?.printMessage(Diagnostic.Kind.WARNING, "----------------MyKotlinBindViewProcessor");
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        val types = LinkedHashSet<String>()
        types.add(MyKotlinBindView::class.java.canonicalName)
        // ...
        return types
    }

    override fun getSupportedSourceVersion(): SourceVersion {
//        return super.getSupportedSourceVersion()
//        return SourceVersion.latestSupported()
        return processingEnv.sourceVersion
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
//        val elements = roundEnv?.getElementsAnnotatedWith(MyBindView::class.java)!!
//        for (element in elements) {
//            var typeElement = element as TypeElement
//            var members = processingEnv!!.elementUtils.getAllMembers(typeElement)
//            val bindFunBuilder = FunSpec.builder("bindView").addParameter("activity", typeElement.asClassName())
//                    .addAnnotation(JvmStatic::class.java)
//            for (m in members) {
//                val find = m.getAnnotation(MyBindView::class.java)
//                if (find!=null){
//                    bindFunBuilder.addStatement("activity.${m.simpleName}=activity.findViewById(${find.myId})")
//                }
//            }
//            val bindFun = bindFunBuilder.build()
//            val file = FileSpec.builder(
//                    processingEnv.elementUtils.getPackageOf(typeElement).toString(),
//                    "${element.simpleName}_BindView")
//                    .addType(TypeSpec.classBuilder("${element.simpleName}_BindView").build())
//                    .addType(TypeSpec.companionObjectBuilder().addFunction(bindFun).build())
//                    .build()
//            file.writeTo(filer!!)
//        }


        // 拿到 程序中所有使用了 自己注解的节点
//        roundEnv?.getElementsAnnotatedWith(MyKotlinBindView::class.java)?.let {
//            if (it.isNotEmpty()){
//
//            }
//        }
        val elements: Set<Element> =
            roundEnv?.getElementsAnnotatedWith(MyKotlinBindView::class.java)!!
        // 把所有数据结构化 key activityName， value 成员变量的集合
        val map = HashMap<String, MutableList<VariableElement>>()
        // 遍历我们的成员变量节点的集合
        for (element in elements) {
            val variableElement = element as VariableElement
            // 获取类名
            val activityName = variableElement.enclosingElement.simpleName.toString()
            var list = map[activityName]
            if (list == null) {
                list = mutableListOf()
                map[activityName] = list
            }
            list.add(variableElement)
        }
        // 写文件
        val iterator = map.keys.iterator()
        var writer: Writer? = null
        while (iterator.hasNext()) {
            // 拿到类名
            val activityName = iterator.next()
            // 拿到相对应的成员变量
            val list = map[activityName]
            // 拿到包名
            val packageName =
                processingEnv.elementUtils.getPackageOf(list!![0].enclosingElement).toString()
            // 创建一个 java 文件
            try {
                // 核心方法
                val javaFileObject =
                    filer?.createSourceFile("$packageName.${activityName}_ViewBinder")
                writer = javaFileObject?.openWriter() ?: return false
                // 1.写包名
                writer.write("package $packageName;\n")
                // 2.写引入接口名
                writer.write("import $packageName.ViewBinder;\n")
                // 3.定义类语句
                writer.write("public class ${activityName}_ViewBinder implements ViewBinder<$packageName.$activityName>{\n")
                // 4.写 bind 方法
                writer.write("public void bind($packageName.$activityName target){\n")
                //
                for (variableElement in list) {
                    // 获取成员变量的名字
                    val name = variableElement.simpleName.toString()
                    // 成员变脸的类型
                    val typeMirror = variableElement.asType()
                    // 控件的ID
                    val annotation = variableElement.getAnnotation(MyKotlinBindView::class.java)
                    val id = annotation.myId
                    writer.write("target.$name=($typeMirror)target.findViewById($id);\n")
                }
                writer.write("}\n}")
                messager?.printMessage(Diagnostic.Kind.WARNING,">>>>>>>>>>>>>生成文件")
//            var resultPath = processingEnv.options.get()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                writer?.flush()
                writer?.close()
            }
        }
        return true
    }
}
