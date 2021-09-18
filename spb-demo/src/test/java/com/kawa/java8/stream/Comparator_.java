package com.kawa.java8.stream;

import com.kawa.java8.lambda.Bank;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 比较器
 */
public class Comparator_ {

    @Test
    public void Test_Comparator_() {
        // 按字符串长度升序排序
        List<String> strings = Arrays.asList("brian", "jeo", "alan", "jumper", "cassiel");
        Collections.sort(strings, Comparator.comparingInt(String::length));
        strings.forEach(System.out::println);
        System.out.println("---------------------");

        // 降序
        Collections.sort(strings, Comparator.comparingInt(String::length).reversed());
        strings.forEach(System.out::println);
        System.out.println("---------------------");

        // thenComparing 多个条件排序
        List<Bank> banks = Arrays.asList(new Bank("ICBC", "CN", 50),
                new Bank("CBC", "CN", 70),
                new Bank("BC", "CN", 30),
                new Bank("OCBC", "SG", 100),
                new Bank("ABC", "SG", 90),
                new Bank("BBC", "SG", 100));
        Collections.sort(banks, Comparator.comparingInt(Bank::getAge)
                .thenComparing(Bank::getName));
        banks.forEach(System.out::println);
        System.out.println("---------------------");

        //
        Collections.sort(banks, Comparator.comparingInt(Bank::getAge)
                .thenComparing(Bank::getName,Comparator.reverseOrder()));
        banks.forEach(System.out::println);
    }


}
