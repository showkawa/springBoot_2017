package com.kawa.bb.advice;

import com.kawa.bb.overwrite.OvUserService;
import net.bytebuddy.asm.Advice;

/**
 * advice enter
 * - skipOn
 * - @Advice.This
 */
public class OnMethodEnterAdvice {

    @Advice.OnMethodEnter(skipOn = Advice.OnDefaultValue.class)
    public static int enter(@Advice.This Object cls) {
        if (cls.getClass().getName().equals(OvUserService.class.getName())) {
            System.out.println(">>>>> enter class: " + cls);
            return 1;
        }
        System.out.println("----- OnMethodEnterExitAdvice ----- enter");
        return 0;
    }


}
