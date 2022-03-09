package com.lqk.compiler;

import com.google.auto.service.AutoService;
import com.lqk.annotations.MyJavaBinderView;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * @author LQK
 * @time 2019/5/2 18:50
 * @remark
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MyJavaBindViewProcessor extends AbstractProcessor {

    Filer filer;
    Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.WARNING, "----------------MyJavaBindViewProcessor");
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
//        return super.getSupportedSourceVersion();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> list = new HashSet<>();
        list.add(MyJavaBinderView.class.getCanonicalName());
        return list;
//        return super.getSupportedAnnotationTypes();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

//        Set<Element> elements = (Set<Element>) roundEnv.getElementsAnnotatedWith(MyJavaBinderView.class);
//        HashMap<String, ArrayList<VariableElement>> map = new HashMap();
//        if (elements != null && !elements.isEmpty()) {
//            for (Element element : elements) {
//                VariableElement variableElement = (VariableElement) element;
//                String activityName = variableElement.getEnclosingElement().getSimpleName().toString();
//                ArrayList<VariableElement> list = map.get(activityName);
//                if (list == null) {
//                    list = new ArrayList<VariableElement>();
//                    map.put(activityName, list);
//                }
//                list.add(variableElement);
//            }
//        }
//        // 生成文件
//        Iterator<String> iterator = map.keySet().iterator();
//        Writer writer = null;
//        while (iterator.hasNext()) {
//            String clsName = iterator.next();
//            ArrayList<VariableElement> l = map.get(clsName);
//            String pckName = processingEnv.getElementUtils().getPackageOf(l.get(0).getEnclosingElement()).toString();
//            try {
//                JavaFileObject javaFileObject = filer.createSourceFile(pckName + "." + clsName + "_Binder");
//                writer = javaFileObject.openWriter();
//                if (writer == null) {
//                    return false;
//                }
//                writer.write("package " + pckName + ";\n");
//                writer.write("public class " + clsName + "_Binder {\n");
//                writer.write("public static final void add(){\n");
//                writer.write("\n");
//                writer.write("}\n}");
//                writer.flush();
//                writer.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
        // Router  /group/path



        return true;
    }
}
