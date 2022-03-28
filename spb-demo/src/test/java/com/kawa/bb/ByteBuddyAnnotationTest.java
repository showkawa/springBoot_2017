package com.kawa.bb;


import com.kawa.bb.advice.*;
import com.kawa.bb.overwrite.BrianService;
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Slf4j
public class ByteBuddyAnnotationTest {

    ClassLoader classLoader;

    @Before
    public void setup() {
        classLoader = new ByteArrayClassLoader
                .ChildFirst(getClass().getClassLoader(), ClassFileLocator.ForClassLoader.readToNames(BrianService.class),
                ByteArrayClassLoader.PersistenceHandler.MANIFEST);

        ByteBuddyAgent.install();
    }

    @Test
    public void Method_Enter_And_Exit_Advice() {
        new AgentBuilder.Default()
                .with(AgentBuilder.PoolStrategy.Default.EXTENDED)
                .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
                // setup match condition
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
            classType.getMethod("getIdNumber", String.class).invoke(newInstance, "sean");
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException
                | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void Method_Enter_Advice_With_Condition() {
        new AgentBuilder.Default()
                .with(AgentBuilder.PoolStrategy.Default.EXTENDED)
                .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
                // setup match condition
                .type(ElementMatchers.is(OvUserService.class), ElementMatchers.is(classLoader))
                .transform((builder, typeDescription, classLoader, module) ->
                        builder.visit(Advice
                                .to(OnMethodEnterAdvice.class)
                                .on(ElementMatchers.isMethod()
                                        .and(ElementMatchers.not(ElementMatchers.isStatic())))))
                .installOnByteBuddyAgent();

        try {
            Class<?> classType = classLoader.loadClass(OvUserService.class.getName());
            Object newInstance = classType.getDeclaredConstructor().newInstance();
            // call method
            classType.getDeclaredMethod("getAge", String.class).invoke(newInstance, "sean");
            classType.getMethod("getIdNumber", String.class).invoke(newInstance, "sean");
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException
                | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void Method_Enter_Advice_With_AllArguments() {
        new AgentBuilder.Default()
                .with(AgentBuilder.PoolStrategy.Default.EXTENDED)
                .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
                // setup match condition
                .type(ElementMatchers.is(OvUserService.class), ElementMatchers.is(classLoader))
                .transform((builder, typeDescription, classLoader, module) ->
                        builder.visit(Advice
                                .to(OnMethodEnterAdviceWithArgs.class)
                                .on(ElementMatchers.isMethod().and(ElementMatchers.isPublic()))))
//                                .on(ElementMatchers.nameStartsWith(""))))
//                                .on(ElementMatchers.named("getAge"))))
                .installOnByteBuddyAgent();

        try {
            Class<?> classType = classLoader.loadClass(OvUserService.class.getName());
            Object newInstance = classType.getDeclaredConstructor().newInstance();
            // call method
            classType.getDeclaredMethod("getAge", String.class).invoke(newInstance, "sean age");
            classType.getMethod("getIdNumber", String.class).invoke(newInstance, "sean id");
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException
                | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void Method_Enter_Advice_With_Return() {
        new AgentBuilder.Default()
                .with(AgentBuilder.PoolStrategy.Default.EXTENDED)
                .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
                // setup match condition
                .type(ElementMatchers.is(OvUserService.class), ElementMatchers.is(classLoader))
                .transform((builder, typeDescription, classLoader, module) ->
                        builder.visit(Advice
                                .to(OnMethodExitReturnAdvice.class)
                                .on(ElementMatchers.isMethod().and(ElementMatchers.isPublic()))))
                .installOnByteBuddyAgent();

        try {
            Class<?> classType = classLoader.loadClass(OvUserService.class.getName());
            Object newInstance = classType.getDeclaredConstructor().newInstance();
            // call method
            classType.getDeclaredMethod("getAge", String.class).invoke(newInstance, "sean age");
            classType.getMethod("getIdNumber", String.class).invoke(newInstance, "sean id");
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException
                | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void Method_Enter_Advice_With_FiledValue() {
        new AgentBuilder.Default()
                .with(AgentBuilder.PoolStrategy.Default.EXTENDED)
                .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
                // setup match condition
                .type(ElementMatchers.is(BrianService.class), ElementMatchers.is(classLoader))
                .transform((builder, typeDescription, classLoader, module) ->
                        builder.visit(Advice
                                .to(OnMethodEnterFieldAdvice.class)
                                .on(ElementMatchers.isMethod().and(ElementMatchers.isPublic()))))
                .installOnByteBuddyAgent();

        try {
            Class<?> classType = classLoader.loadClass(BrianService.class.getName());
//            Object newInstance = classType.getDeclaredConstructor(OvUserService.class).setAccessible(true)
//            Constructor<?> declaredConstructor = classType.getDeclaredConstructor(OvUserService.class);
            Constructor<?> declaredConstructor = classType.getDeclaredConstructor();
            Object newInstance = declaredConstructor.newInstance();
            // call method
            classType.getDeclaredMethod("testUs", boolean.class).invoke(newInstance, false);
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException
                | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void Method_Enter_Advice_With_Origin() {
        new AgentBuilder.Default()
                .with(AgentBuilder.PoolStrategy.Default.EXTENDED)
                .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
                // setup match condition
                .type(ElementMatchers.is(BrianService.class), ElementMatchers.is(classLoader))
                .transform((builder, typeDescription, classLoader, module) ->
                        builder.visit(Advice
                                .to(OnMethodEnterOriginAdvice.class)
                                .on(ElementMatchers.isMethod().and(ElementMatchers.isPublic()))))
                .installOnByteBuddyAgent();

        try {
            Class<?> classType = classLoader.loadClass(BrianService.class.getName());
            Constructor<?> declaredConstructor = classType.getDeclaredConstructor();
            Object newInstance = declaredConstructor.newInstance();
            // call method
            classType.getDeclaredMethod("testUs", boolean.class).invoke(newInstance, false);
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException
                | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


}
