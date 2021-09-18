package com.kawa.java8.stream;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Characteristics 决定收集器的特性
 * (
 * CONCURRENT 多线程并行操作同一个结果容器
 * ,UNORDERED 无序
 * ,IDENTITY_FINISH 强制类型转换
 * )
 * <p>
 * Collectors源码
 */
public class Collector_ {

    @Test
    public void Test_Collector_() {
        List<String> strings = Arrays.asList("brian", "jeo", "alan", "jumper", "cassiel", "brian");
        strings.stream().collect(new SetCollector<>()).forEach(System.out::println);
        System.out.println("---------------------");

        Set<String> sets = Set.of("a", "b", "c", "", "d");
        sets.stream().collect(new MapCollector<>()).forEach((k, v) -> {
            System.out.println(k + " <> " + v);
        });

    }

}
