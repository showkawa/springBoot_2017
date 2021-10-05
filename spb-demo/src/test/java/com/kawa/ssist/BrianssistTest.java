package com.kawa.ssist;


import javassist.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class BrianssistTest {

    /**
     * ClassPool.get()
     * ClassPool.getOrNull()
     */
    @Test
    public void Javassist_ClassPool_GetClass() {
        ClassPool classPool = ClassPool.getDefault();
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

}
