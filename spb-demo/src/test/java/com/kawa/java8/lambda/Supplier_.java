package com.kawa.java8.lambda;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Supplier不接受参数，返回一个固定的结果
 */
public class Supplier_ {

    @Test
    public void Test_Supplier() {
        Bank b1 = new Bank("ICBC", "china", 50);
        Bank b2 = new Bank("CBC", "china", 50);
        Bank b3 = new Bank("BC", "china", 90);
        Bank b4 = new Bank("ABC", "china", 50);
        Bank b5 = new Bank("OCBC", "sg", 100);
        Bank b6 = new Bank("UOB", "sg", 100);
        Bank b7 = new Bank("DBS", "sg", 100);

        Supplier<List<Bank>> listSupplier = () -> Arrays.asList(b1, b2, b3, b4, b5, b6, b7);
        listSupplier.get().forEach(b -> System.out.println(b.getName()));
    }

}
