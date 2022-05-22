package com.kawa.mutilthread.threadlocal;

import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

public class ThreadLocalTest {


    @Test
    public void test() {
        InheritableThreadLocal<ConcurrentHashMap<String, String>> var = new InheritableThreadLocal<>();
        var param = new ConcurrentHashMap<String, String>();
        param.put("thread1", "have a nice day");
        var.set(param);
        // new logic in another method, when use SpringBoot can init InheritableThreadLocal as a Bean
        new Thread(() -> {
            ConcurrentHashMap<String, String> result = var.get();
            String val = result.get("thread1");
            System.out.println("---> " + val);

        }).run();
    }
}
