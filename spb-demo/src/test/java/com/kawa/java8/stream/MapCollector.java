package com.kawa.java8.stream;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;


/**
 * 自定义Map 收集器
 *
 * @param <T>
 */
public class MapCollector<T> implements Collector<T, Set<T>, Map<T, T>> {


    @Override
    public Supplier<Set<T>> supplier() {
        System.out.println("----supplier----");
        return HashSet::new;
    }

    @Override
    public BiConsumer<Set<T>, T> accumulator() {
        System.out.println("----accumulator----");
        return Set::add;
    }

    @Override
    public BinaryOperator<Set<T>> combiner() {
        System.out.println("----combiner----");
        return (l, r) -> {
            l.addAll(r);
            return l;
        };
    }

    @Override
    public Function<Set<T>, Map<T, T>> finisher() {
        System.out.println("----finisher----");
        return set -> {
            Map<T, T> map = new HashMap<>();
            set.stream().forEach(s -> {
                map.put(s, s);
            });
            return map;
        };

    }

    @Override
    public Set<Characteristics> characteristics() {
        System.out.println("----characteristics----");
        return Collections.unmodifiableSet(EnumSet.of(Characteristics.UNORDERED));
    }
}
