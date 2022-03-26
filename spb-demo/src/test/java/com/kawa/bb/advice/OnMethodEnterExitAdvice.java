package com.kawa.bb.advice;

import net.bytebuddy.asm.Advice;

public class OnMethodEnterExitAdvice {

    @Advice.OnMethodEnter
    public static void enter() {
        System.out.println("----- OnMethodEnterExitAdvice ----- enter");
    }

    @Advice.OnMethodExit
    public static void exit() {
        System.out.println("----- OnMethodEnterExitAdvice ----- exit");
    }

}
