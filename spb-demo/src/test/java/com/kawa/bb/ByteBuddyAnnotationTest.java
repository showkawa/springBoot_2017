package com.kawa.bb;


import com.kawa.bb.advice.OnMethodEnterExitAdvice;
import com.kawa.bb.overwrite.OvUserService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.loading.ByteArrayClassLoader;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

@Slf4j
public class ByteBuddyAnnotationTest {

    ClassLoader classLoader;
    Class[] objClasses;

    @Before
    public void setup() {
        objClasses = new Class[]{OvUserService.class};
        classLoader = new ByteArrayClassLoader
                .ChildFirst(getClass().getClassLoader(), ClassFileLocator.ForClassLoader.readToNames(objClasses),
                ByteArrayClassLoader.PersistenceHandler.MANIFEST);

        ByteBuddyAgent.install();
    }

    @Test
    public void Method_Enter_And_Exit_Advice() {
        new AgentBuilder.Default()
                .with(AgentBuilder.PoolStrategy.Default.EXTENDED)
                .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
                // setup match condition
                // todo how to skip con constructor()
                .type(ElementMatchers.is(OvUserService.class), ElementMatchers.is(classLoader))
                .transform((builder, typeDescription, classLoader, module) ->
                        builder.visit(Advice
                                .to(OnMethodEnterExitAdvice.class)
                                .on(ElementMatchers.not(ElementMatchers.isConstructor())
                                        .and(ElementMatchers.any()))))
                .installOnByteBuddyAgent();

        try {
            Class<?> classType = classLoader.loadClass(OvUserService.class.getName());
            Object newInstance = classType.getDeclaredConstructor().newInstance();
            // call method
            classType.getDeclaredMethod("getAge", String.class).invoke(newInstance, "sean");
            // call static method
            classType.getMethod("getIdNumber", String.class).invoke(newInstance, "sean");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


}
