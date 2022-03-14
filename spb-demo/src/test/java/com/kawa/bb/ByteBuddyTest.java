package com.kawa.bb;

import com.kawa.bb.overwrite.OvUserService;
import com.kawa.mock.UserService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static net.bytebuddy.implementation.MethodDelegation.to;
import static net.bytebuddy.matcher.ElementMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
public class ByteBuddyTest {

    /**
     * overwrite the method return value
     * for com.kawa.mock.UserService#getAge(java.lang.String)
     */
    @Test
    public void Overwrite_Method_Return_Value() {
        try {
            UserService bbUserService = new ByteBuddy()
                    .subclass(UserService.class)
                    .name("com.kawa.bb.BbUserService")
                    .method(named("getAge").and(returns(int.class).and(takesArguments(1))))
                    .intercept(FixedValue.value(999))
                    .make()
                    .load(this.getClass().getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor().newInstance();

            int age = bbUserService.getAge("");
            log.info(">>>>age:{}", age);
            assertThat(age, equalTo(18));
        } catch (InstantiationException | IllegalAccessException |
                InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void Output_ByteCode() {
        try {
            // output the byte code
            new ByteBuddy()
                    .subclass(UserService.class)
                    .name("com.kawa.mock.BcUserService")
                    .make()
                    .saveIn(new File("/home/un/code/springBoot_2017/spb-demo/target/classes"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * overwrite the method
     */
    @Test
    public void Overwrite_Method() {
        try {
            UserService userService = new ByteBuddy()
                    .redefine(UserService.class)
                    .method(named("getIdNumber").and(takesArguments(1)).and(returns(String.class)))
                    .intercept(to(OvUserService.class))
                    .make().load(getClass().getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor()
                    .newInstance();

            String idNum = userService.getIdNumber("sean");
            log.info("idNum:{}", idNum);
        } catch (InstantiationException | IllegalAccessException |
                InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
