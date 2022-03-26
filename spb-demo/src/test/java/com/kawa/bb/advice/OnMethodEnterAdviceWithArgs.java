package com.kawa.bb.advice;


import net.bytebuddy.asm.Advice;

/**
 * advice enter
 * - @AllArguments
 */
public class OnMethodEnterAdviceWithArgs {
    @Advice.OnMethodEnter(skipOn = Advice.OnDefaultValue.class)
    public static int enter(@Advice.AllArguments Object[] args) {
        System.out.println(">>>>> enter args length: " + args.length);
        for (int i = 0; i < args.length; i++) {
            System.out.println(">>>>> arg: " + args[0].getClass().getName() + " - " + args[0]);
        }
        if (args.length > 0) {
            System.out.println("----- OnMethodEnterExitAdvice ----- enter");
            return 1;
        }
        return 0;
    }


}
