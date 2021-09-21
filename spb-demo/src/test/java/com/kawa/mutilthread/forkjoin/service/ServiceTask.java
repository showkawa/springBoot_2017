package com.kawa.mutilthread.forkjoin.service;


import java.util.UUID;

@FunctionalInterface
public interface ServiceTask {

    default String getParameter(String region) throws Exception {
        int rad = (int) (Math.random() * 100);
        if (rad > 80) {
            throw new Exception("getParameter exception");
        }
        Thread.sleep(rad);
        return UUID.randomUUID().toString().replace("-", "");
    }

    String createTask(String uuid) throws Exception;

    default String getTransitions(String jid) throws Exception {
        int rad = (int) (Math.random() * 100);
        if (rad > 80) {
            throw new Exception("getTransitions exception");
        }
        Thread.sleep(rad);
        return UUID.randomUUID().toString().replace("-", "");
    }

    default String transferStatus(String tid) throws Exception {
        int rad = (int) (Math.random() * 100);
        if (rad > 80) {
            throw new Exception("transferStatus exception");
        }
        Thread.sleep(rad);
        return UUID.randomUUID().toString().replace("-", "");
    }
}
