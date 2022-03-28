package com.kawa.bb.advice;


import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;

/**
 * @Advice.Origin can get the target class and method
 * @Advice.Enter and @Advice.Exit and only can use in advice method, and pass the value in the advice chain
 */
public class OnMethodEnterOriginAdvice {
    @Advice.OnMethodEnter
    public static boolean enter(@Advice.Origin Method m, @Advice.Origin Class<?> origin) {
        System.out.println("----- OnMethodEnterOriginAdvice ----- enter");
        System.out.println(">>>>> target method " + m.getName());
        System.out.println(">>>>> target class " + origin.getName());
        return m.getName().equals("testUs");
    }

    @Advice.OnMethodExit
    public static void exit(@Advice.Enter Object value) { // todo type need update to boolean
        System.out.println("----- OnMethodEnterOriginAdvice ----- exit");
        System.out.println(">>>>> target value " + value);
    }
}
