package com.kawa.ssist;


import javassist.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

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
            JustRun justRun = cdlt.newInstance();
            justRun.returnPublicStr("kawa");
        } catch (NotFoundException | CannotCompileException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            // Get CtClass, will return null when not found
            CtClass cc = classPool.getOrNull("com.kawa.ssist.JustRunq");
            if (null != cc) {
                CtMethod declaredMethod = cc.getDeclaredMethod("returnPublicStr");
                declaredMethod.insertAfter("log.info(\">>>>>>>>>> com.kawa.ssist.JustRun - returnPublicStr - end\");");
                Class<JustRun> cdlt = (Class<JustRun>) cc.toClass();
                JustRun justRun = cdlt.newInstance();
                justRun.returnPublicStr("kawa");
            } else {
                log.info(">>>>>>>>>> can not found class: com.kawa.ssist.JustRunq");
            }
        } catch (NotFoundException | CannotCompileException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     *
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
                log.info(">>>>>>>>>>>>>>>>>: {}",constructor);
            }
        } catch (CannotCompileException | IOException | NotFoundException e) {
            e.printStackTrace();
        }
    }



}
