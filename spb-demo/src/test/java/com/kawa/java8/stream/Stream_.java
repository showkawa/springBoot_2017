package com.kawa.java8.stream;

import com.kawa.java8.lambda.Account;
import com.kawa.java8.lambda.Bank;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * 流由3部分构成：1.源   2.零个或多个中间操作   3.终止操作
 * <p>
 * 流操作的分类： 1.惰性求值   2.及早求值
 */
public class Stream_ {

    @Test
    public void Test_Stream_() {
        Stream<Integer> numberStream = Stream.of(1, 2, 3, 4, 5);
        System.out.println(numberStream.map(n -> n * 2).reduce(0, Integer::sum));
    }

    /**
     * <R> R collect(Supplier<R> supplier,BiConsumer<R, ? super T> accumulator,BiConsumer<R, R> combiner);
     * 三个入参：Supplier，BiConsumer，BiConsumer
     */
    @Test
    public void Test_Stream_Collect() {
        Stream<Integer> numberStream = Stream.of(1, 2, 3, 4, 5);

//        List<Integer> list = numberStream.filter(n -> n > 2).collect(Collectors.toList());

//        List<Integer> list = numberStream.filter(n -> n > 2).collect(() -> new ArrayList<>(),
//                (l, r) -> l.add(r), (left, right) -> left.addAll(right));

        ArrayList<Object> list = numberStream.filter(n -> n > 2).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        System.out.println(list.toString());
    }


    /**
     * ap 映射 {a,a,a}-> {A,A,A}
     * <p>
     * FlatMap 扁平化映射 {{a,a,a},{b,b,b},{c,c,c}}-> {A,A,A,B,B,B,C,C,C}
     */
    @Test
    public void Test_Stream_Map_And_FlatMap() {
        Stream<Integer> numberStream = Stream.of(1, 2, 3, 4, 5);
        numberStream.map(n -> n * n).forEach(System.out::println);
        System.out.println("-------------------------");

        List<List<String>> lists =
                Arrays.asList(Arrays.asList("a", "b"), Arrays.asList("c", "d"), Arrays.asList("e", "f"));
        lists.stream().flatMap(ls -> ls.stream()).map(String::toUpperCase).forEach(System.out::println);
        System.out.println("-------------------------");

        List<String> list1 = Arrays.asList("Hi", "This is");
        List<String> list2 = Arrays.asList("Brian", "Jumper", "Alan", "Tony");
        list1.stream()
                .flatMap(item -> list2.stream().map(item2 -> item.concat(" ").concat(item2)))
                .forEach(System.out::println);

    }

    /**
     * 分组 group by
     * 分区 partition By （分区是分组的一类特殊情况只有两个组）
     */
    @Test
    public void Test_Stream_Group() {
        List<Account> accounts = Arrays.asList(new Account("brian", "ICBC", "2019", "VIP1"),
                new Account("brian", "CBC", "2020", "VIP2"),
                new Account("cassiel", "BC", "2019", "VIP1"),
                new Account("tony", "ICBC", "2019", "VIP1"),
                new Account("jumper", "ICBC", "2019", "VIP1"));
        accounts.stream().collect(Collectors.groupingBy(Account::getYear))
                .forEach((l, r) -> System.out.println(l + " <> " + r));
        System.out.println("-------------------------");

        accounts.stream().collect(Collectors.groupingBy(Account::getBankName, Collectors.counting()))
                .forEach((k, v) -> System.out.println(k + " & " + v));
        System.out.println("-------------------------");
        List<Bank> banks = Arrays.asList(new Bank("ICBC", "CN", 50),
                new Bank("CBC", "CN", 70),
                new Bank("BC", "CN", 30),
                new Bank("OCBC", "SG", 100),
                new Bank("ABC", "SG", 90),
                new Bank("BBC", "SG", 100));

        banks.stream().collect(Collectors.groupingBy(Bank::getNation, Collectors.averagingDouble(Bank::getAge)))
                .forEach((k, v) -> {
                    System.out.println(k + " @ " + v);
                });
        System.out.println("-------------------------");

        banks.stream().collect(Collectors.partitioningBy(b -> b.getAge() > 50))
                .forEach((k, v) -> System.out.println(k + " ^ " + v.size()));
    }

    /**
     * Iterate 会产生一个无限流，需要和短路操作
     */
    @Test
    public void Test_Stream_Iterate() {
        int sum = Stream.iterate(1, l -> l + 2).limit(6)
                .filter(n -> n > 2)
                .mapToInt(n -> n * 2)
                .skip(2)
                .limit(2)
                .peek(a -> {
                    System.out.println(a + " ------------------------");
                })
                .sum();
        System.out.println("-------------------------" + sum);
    }

    /**
     * IntSummaryStatistics [count,sum,min,max,average]
     */
    @Test
    public void Test_Stream_IntSummaryStatistics() {
        IntSummaryStatistics intSummaryStatistics = Stream.iterate(1, l -> l + 2).limit(6)
                .mapToInt(n -> n + 2)
                .summaryStatistics();
        System.out.println(intSummaryStatistics.toString());
    }

    /**
     * Distinct去重
     */
    @Test
    public void Test_Stream_Distinct() {
        List<String> strings = Arrays.asList("brian is man", "brian is 18 age", "brian in shenzhen");
        strings.stream().map(s -> s.split(" ")).flatMap(s -> Arrays.stream(s)).distinct().forEach(System.out::println);
    }

    /**
     * Collector<T, A, R>
     * T 输入流中每一个元素的类型
     * A 汇聚操作时的可变结果类型
     * R  返回结果类型
     */
    @Test
    public void Test_Stream_Collector() {
        List<Bank> banks = Arrays.asList(new Bank("ICBC", "CN", 50),
                new Bank("CBC", "CN", 70),
                new Bank("BC", "CN", 30),
                new Bank("OCBC", "SG", 100),
                new Bank("ABC", "SG", 90),
                new Bank("BBC", "SG", 100));

        // min
        banks.stream().collect(minBy(Comparator.comparingInt(Bank::getAge))).ifPresent(System.out::println);
        // max
        banks.stream().collect(maxBy(Comparator.comparingInt(Bank::getAge))).ifPresent(System.out::println);
        // average
        System.out.println(banks.stream().collect(averagingInt(Bank::getAge)));
        // sum
        System.out.println(banks.stream().collect(summingInt(Bank::getAge)));
        // summary
        System.out.println(banks.stream().collect(summarizingInt(Bank::getAge)));
        // joining
        System.out.println(banks.stream().map(Bank::getName).collect(joining(",")));
        // joining
        System.out.println(banks.stream().map(Bank::getName).collect(joining(",", "[", "]")));
        // group by + group by
        banks.stream().collect(groupingBy(Bank::getNation, groupingBy(Bank::getAge)))
                .forEach((k, v) -> System.out.println(k + " <> " + v));
        //partition by
        banks.stream().collect(partitioningBy(b -> b.getAge() > 50, partitioningBy(b -> b.getAge() > 70)))
                .forEach((k, v) -> System.out.println(k + " @ " + v));
        // collectingAndThen
        banks.stream()
                .collect(groupingBy(Bank::getNation,
                        collectingAndThen(minBy(Comparator.comparingInt(Bank::getAge)), a -> a.get())))
                .forEach((k, v) -> System.out.println(k + " （） " + v));
    }


}
