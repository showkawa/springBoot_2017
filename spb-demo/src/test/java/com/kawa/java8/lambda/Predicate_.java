package com.kawa.java8.lambda;

import org.junit.Test;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Predicate给定一个入参 返回一个boolean值
 */
public class Predicate_ {

    /**
     * 函数接口传递的是行为，面向对象的开发中，具体的实现是写在方法体内，而函数接口仅仅负责定义行为，具体实现当作参数
     */
    @Test
    public void Test_Predicate() {
        Bank b1 = new Bank("ICBC", "china", 50);
        Bank b2 = new Bank("CBC", "china", 50);
        Bank b3 = new Bank("BC", "china", 90);
        Bank b4 = new Bank("ABC", "china", 50);
        Bank b5 = new Bank("OCBC", "sg", 100);
        Bank b6 = new Bank("UOB", "sg", 100);
        Bank b7 = new Bank("DBS", "sg", 100);

        Bank bankFilter = new Bank("ICBC", "", 0);
        Stream<Bank> bankStream2 = Stream.of(b1, b2, b3, b4, b5, b6, b7);
        List<Bank> bankList2 = bankStream2.filter(b ->
                StringUtils.isEmpty(bankFilter.getName())
                        ? true : b.getName().equals(bankFilter.getName()))
                .collect(Collectors.toList());
        bankList2.forEach(b -> System.out.println(b.getName()));
    }

    @Test
    public void Test_Predicate_And() {
        Bank b1 = new Bank("ICBC", "china", 50);
        Bank b2 = new Bank("CBC", "china", 50);
        Bank b3 = new Bank("BC", "china", 90);
        Bank b4 = new Bank("ABC", "china", 50);
        Bank b5 = new Bank("OCBC", "sg", 100);
        Bank b6 = new Bank("UOB", "sg", 100);
        Bank b7 = new Bank("DBS", "sg", 100);
        Stream<Bank> bankStream = Stream.of(b1, b2, b3, b4, b5, b6, b7);
        Bank bankFilter = new Bank("", "sg", 0);
        List<Bank> bankList1 = bankStream
                .filter(bank -> matchAnd(bank,
                        b -> StringUtils.isEmpty(bankFilter.getName()) ? true : b.getName().equals(bankFilter.getName()),
                        b -> StringUtils.isEmpty(bankFilter.getNation()) ? true : b.getNation().equals(bankFilter.getNation())))
                .collect(Collectors.toList());
        bankList1.forEach(b -> System.out.println(b.getName()));
    }

    // and
    public boolean matchAnd(Bank bank, Predicate<Bank> namePredicate, Predicate<Bank> nationPredicate) {
        return namePredicate.and(nationPredicate).test(bank);
    }
}
