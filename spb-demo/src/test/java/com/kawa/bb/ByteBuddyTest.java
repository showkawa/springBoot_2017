package com.kawa.bb;

import com.kawa.bb.overwrite.OvUserService;
import com.kawa.mock.UserService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.type.TypeDescription;
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
     * overwrite the method return fixed value
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
            assertThat(age, equalTo(999));
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * overwrite the method by subclass
     * if hit below error, need update delegate method to static method
     * None of [] allows for delegation from XXXXXXXX
     */
    @Test
    public void Overwrite_Method() {
        try {
            UserService userService = new ByteBuddy()
                    .subclass(UserService.class)
                    .method(named("getIdNumber").and(returns(String.class)))
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
     * with(new NamingStrategy.AbstractBase()) and name(String) are the same function
     */
    @Test
    public void Output_ByteCode_With_Name_Strategy() {
        try {
            // output the byte code
            new ByteBuddy()
                    .with(new NamingStrategy.AbstractBase() {
                        @Override
                        protected String name(TypeDescription superClass) {
                            return "com.kawa.name.stratgy." + superClass.getSimpleName();
                        }
                    })
                    .subclass(UserService.class)
                    .make()
                    .saveIn(new File("/home/un/code/springBoot_2017/spb-demo/target/classes"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void Add_Field() {
        try {
            new ByteBuddy()
                    .subclass(UserService.class)
                    .name("com.kawa.mock.UserServiceMock")
                    .defineField("address", String.class)
//                    .defineMethod("updateAddress",String.class)
                    .make()
                    .saveIn(new File("/home/un/code/springBoot_2017/spb-demo/target/classes"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

