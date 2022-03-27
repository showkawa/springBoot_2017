package com.kawa.bb.advice;

import net.bytebuddy.asm.Advice;

/**
 * advice exit annotation
 *
 * @Advice.Return can get the method return data
 */
public class OnMethodExitReturnAdvice {

    @Advice.OnMethodExit
    public static void exit(@Advice.Return Object result) {
        System.out.println("----- OnMethodExitReturnAdvice ----- exit" + result);
    }
}
