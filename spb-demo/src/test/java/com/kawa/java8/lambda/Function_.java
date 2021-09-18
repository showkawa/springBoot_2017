package com.kawa.java8.lambda;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Function<T, R>
 * input T and result R 接受一个参数T 返回一个参数R
 */
@Slf4j
public class Function_ {


    private Function_ function;

    /**
     * Function的apply定义了一个描述性的接口，没有具体的实现，我的理解是这里定义的一个抽象的函数，具体实现由使用者去实现
     */
    @Test
    public void Test_Function_Apply() {
        function = new Function_();
        log.info("Test_Function  " + function.convert(3, v -> v + 4));
        log.info("Test_Function  " + function.convert(3, v -> v * v * v));
        log.info("Test_Function  " + function.convert(3, v -> v * 3));

        Function<Integer, Integer> fc = v -> v * 2 + 1;
        log.info("Test_Function  <> " + function.convert(3, fc));
    }

    /**
     * Compose方法是将Function组合起来 this.appl(before.apply(v))
     * <p>
     * AndThen也是将Function组合起来 after.apply(this.apply(t))
     * <p>
     * 两者调用顺序刚好相反
     */
    @Test
    public void Test_Function_Compose_AnAndThen() {
        function = new Function_();
        log.info("Test_Function_Compose_And_AndThen  " +
                function.convert2(2, v -> v * 4, v -> v * v));
        log.info("Test_Function_Compose_And_AndThen  " +
                function.convert3(2, v -> v * 4, v -> v * v));

    }

    /**
     * Function方法是一个输入一个输出，BiFunction是两个输入一个输出
     */
    @Test
    public void Test_BiFunction() {
        function = new Function_();
        log.info("Test_BiFunction  " +
                function.convert4(2, 3, (a, b) -> a + b, v -> v * v));

        Bank b1 = new Bank("ICBC", "china", 50);
        Bank b2 = new Bank("ACBC", "china", 30);
        Bank b3 = new Bank("BCBC", "china", 40);
        Bank b4 = new Bank("CCBC", "china", 50);
        Bank b5 = new Bank("OCBC", "sg", 100);

        Stream<Bank> bankStream = Stream.of(b1, b2, b3, b4, b5);
        List<String> resultList = function.convert5("ICBC", "china", bankStream);
        resultList.forEach(System.out::println);
    }


    public int convert(int input, Function<Integer, Integer> func) {
        return func.apply(input);
    }

    // compose
    public int convert2(int input, Function<Integer, Integer> f1, Function<Integer, Integer> f2) {
        return f1.compose(f2).apply(input);
    }

    // andThen
    public int convert3(int input, Function<Integer, Integer> f1, Function<Integer, Integer> f2) {
        return f1.andThen(f2).apply(input);
    }

    public int convert4(int a, int b, BiFunction<Integer, Integer, Integer> f1, Function<Integer, Integer> f2) {
        return f1.andThen(f2).apply(a, b);
    }

    public List<String> convert5(String name, String nation, Stream<Bank> bankStream) {

        BiFunction<String, String, List<String>> func =
                (n, t) -> bankStream.filter(b -> b.getName().equals(n) && b.getNation().equals(t))
                        .map(b -> b.getNation().concat("-").concat(b.getName()))
                        .collect(Collectors.toList());
        return func.apply(name, nation);
    }
}
