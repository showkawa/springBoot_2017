package com.kawa.java8.test;

import org.junit.Test;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MyTest {


    @Test
    public void test() {
        List<Req> reqs = Arrays.asList(new Req("CN", "IN", "V1"),
                new Req("CN", "IN", "V1"),
                new Req("MY", "TR", "V2"),
                new Req("MY", "TR", "V2"),
                new Req("MY", "TR", "V2"),
                new Req("SG", "OUT", "V2"),
                new Req("CN", "OUT", "V3"),
                new Req("CN", "OUT", "V3"),
                new Req("CN", "OUT", "V2"),
                new Req("CN", "OUT", "V1"),
                new Req("CN", "OUT", "V1"),
                new Req("CN", "OUT", "V3"),
                new Req("CN", "OUT", "V3"),
                new Req("SG", "TR", "V3"),
                new Req("SG", "TR", "V3"),
                new Req("SG", "TR", "V3"),
                new Req("MY", "TR", "V4"));
        Req reqFilter = new Req("", "", "");

        reqs.stream()
                .filter(req -> matchAnd(req,
                        r -> StringUtils.isEmpty(reqFilter.getDomain()) ? true : r.getDomain().equals(reqFilter.getDomain()),
                        r -> StringUtils.isEmpty(reqFilter.getTrxType()) ? true : r.getTrxType().equals(reqFilter.getTrxType()))
                )
                .collect(Collectors.groupingBy(req -> mapToGroup(req, r -> {
                    String group = new StringJoiner("&").add(r.getDomain()).add(r.getTrxType()).add(r.getStateName()).toString();
                    return group;
                }), Collectors.counting()))
                .forEach((k, v) -> System.out.println(k + " <> " + v));


    }

    public boolean matchAnd(Req req, Predicate<Req> domainPredicate, Predicate<Req> trxTypePredicate) {
        return domainPredicate.and(trxTypePredicate).test(req);
    }

    public StateGroup mapToStateGroup(Req req, Function<Req, StateGroup> function) {
        return function.apply(req);
    }

    public String mapToGroup(Req req, Function<Req, String> function) {
        return function.apply(req);
    }
}
