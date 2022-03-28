package com.kawa.bb.advice;

import com.kawa.bb.overwrite.BrianService;
import net.bytebuddy.asm.Advice;


/**
 * advice enter and exit annotation
 *
 * @Advice.FieldValue can get the filed in target class
 */
public class OnMethodEnterFieldAdvice {

    @Advice.OnMethodEnter
    public static void enter(@Advice.FieldValue(value = "type", declaringType = BrianService.class) String type) {
        System.out.println("----- OnMethodEnterFieldAdvice ----- enter");
        if (type.equals("1FA")) {
            System.out.println(">>>>> " + type);
        }
    }

}
