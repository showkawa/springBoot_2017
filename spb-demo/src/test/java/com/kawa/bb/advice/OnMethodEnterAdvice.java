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
    public static int enter(@Advice.This Class<?> cls) {
//    public static int enter() {
        System.out.println("cls");
        if (cls.getName().equals(OvUserService.class.getName())) {
            System.out.println("1111111111");
            return 0;
        }
        System.out.println("----- OnMethodEnterExitAdvice ----- enter");
        return 0;
    }


}
