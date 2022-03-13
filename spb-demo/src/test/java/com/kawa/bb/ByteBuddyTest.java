package com.kawa.bb;

import com.kawa.mock.UserService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static net.bytebuddy.matcher.ElementMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
public class ByteBuddyTest {

    /**
     * overwrite the method com.kawa.mock.UserService#getAge(java.lang.String)
     */
    @Test
    public void Overwrite_Method() {
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
}
