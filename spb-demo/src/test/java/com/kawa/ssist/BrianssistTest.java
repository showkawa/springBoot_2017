package com.kawa.ssist;


import javassist.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


// reference link: https://www.javassist.org/tutorial/tutorial.html
@Slf4j
public class BrianssistTest {

    private ClassPool classPool = ClassPool.getDefault();

    /**
     * ClassPool.get()
     * ClassPool.getOrNull()
     */
    @Test
    public void Javassist_ClassPool_GetClass() {
        try {
            // Get CtClass, will throw NotFoundException when not found
            CtClass cc = classPool.get("com.kawa.ssist.JustRun");
            CtMethod declaredMethod = cc.getDeclaredMethod("returnPublicStr");
            declaredMethod.insertAfter("log.info(\">>>>>>>>>> com.kawa.ssist.JustRun - returnPublicStr - end\");");
            Class<JustRun> cdlt = (Class<JustRun>) cc.toClass();
            JustRun justRun = cdlt.getDeclaredConstructor(null).newInstance();
            justRun.returnPublicStr("kawa");
        } catch (NotFoundException | CannotCompileException | InstantiationException |
                IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        try {
            // Get CtClass, will return null when not found
            CtClass cc = classPool.getOrNull("com.kawa.ssist.JustRunq");
            if (null != cc) {
                CtMethod declaredMethod = cc.getDeclaredMethod("returnPublicStr");
                declaredMethod.insertAfter("log.info(\">>>>>>>>>> com.kawa.ssist.JustRun - returnPublicStr - end\");");
                Class<JustRun> cdlt = (Class<JustRun>) cc.toClass();
                JustRun justRun = cdlt.getDeclaredConstructor(null).newInstance();
                justRun.returnPublicStr("kawa");
            } else {
                log.info(">>>>>>>>>> can not found class: com.kawa.ssist.JustRunq");
            }
        } catch (NotFoundException | CannotCompileException | InstantiationException
                | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * ClassPool.makeClass()
     * ClassPool.getAndRename()
     */
    @Test
    public void Javassist_ClassPool_CreateClass() {
        CtClass ctClass = classPool.makeClass("com.kawa.ssist.JustRuns");
        try {
            CtConstructor nc = new CtConstructor(null, ctClass);
            nc.setBody("{System.out.println(\">>>>>>>>>> CtConstructor \" + this.getClass().getName());}");
            ctClass.addConstructor(nc);
            Class<?> aClass = ctClass.toClass();
            ctClass.writeFile("/home/un/app/test/");
            log.info("----- create and rename class -----");
            CtClass class2 = classPool.getAndRename("com.kawa.ssist.JustRun", "com.kawa.ssist.JustRuna");
            for (CtConstructor constructor : class2.getConstructors()) {
                log.info(">>>>>>>>>>>>>>>>>: {}", constructor);
            }
        } catch (CannotCompileException | IOException | NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * ClassPool.insertClassPath()
     * ClassPool.appendClassPath()
     * 通过 ClassPool.getDefault()获取的ClassPool使用JVM的classpath.
     * 在Tomcat等Web服务器运行时, 服务器会使用多个类加载器作为系统类加载器, 这可能导致ClassPool可能无法找到用户的类.
     * 这时, ClassPool须添加额外的classpath才能搜索到用户的类.
     */
    @Test
    public void Javassist_ClassPool_Classpath() {}

    @Test
    public void Javassist_ClassPool_GetMethod() {
        try {
            CtClass cc = classPool.get("com.kawa.ssist.JustRun");
            log.info(">>>>>>>>>>>>>>>>> getSimpleName(): {}", cc.getSimpleName());
            log.info(">>>>>>>>>>>>>>>>> getSimpleName(): {}", cc.getName());
            log.info(">>>>>>>>>>>>>>>>> getPackageName(): {}", cc.getPackageName());
            log.info(">>>>>>>>>>>>>>>>> getInterfaces(): {}", cc.getInterfaces());
            log.info(">>>>>>>>>>>>>>>>> getSuperclass(): {}", cc.getSuperclass());
            log.info(">>>>>>>>>>>>>>>>> getField(): {}", cc.getField("name"));
            log.info(">>>>>>>>>>>>>>>>> getDeclaredMethod(): {}", cc.getDeclaredMethod("returnPublicStr"));
            log.info(">>>>>>>>>>>>>>>>> isAnnotation(): {}", cc.isAnnotation());
            log.info(">>>>>>>>>>>>>>>>> isInterface(): {}", cc.isInterface());
            log.info(">>>>>>>>>>>>>>>>> isPrimitive(): {}", cc.isPrimitive());
            log.info(">>>>>>>>>>>>>>>>> isModified(): {}", cc.isModified());
            log.info(">>>>>>>>>>>>>>>>> isKotlin(): {}", cc.isKotlin());
            log.info(">>>>>>>>>>>>>>>>> isArray(): {}", cc.isArray());
            log.info(">>>>>>>>>>>>>>>>> isEnum(): {}", cc.isEnum());
            log.info(">>>>>>>>>>>>>>>>> isFrozen(): {}", cc.isFrozen());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }


}
