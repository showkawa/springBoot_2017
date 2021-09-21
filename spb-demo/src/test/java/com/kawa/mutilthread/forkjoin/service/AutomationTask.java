package com.kawa.mutilthread.forkjoin.service;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.RecursiveTask;


@Slf4j
public class AutomationTask extends RecursiveTask<List<Map<String, String>>> {

    static List<Map<String, String>> resultList;
    private List<String> list;
    private int start;
    private int end;

    public AutomationTask(List<String> list, int start, int end) {
        this.list = list;
        this.start = start;
        this.end = end;
        log.info(">>>>>>>> :{}", list.size());
    }

    @Override
    protected List<Map<String, String>> compute() {
        if ((end - start) < 1) {
            log.info("=== {} === {}-{}", Thread.currentThread().getName(), start, list.get(start));
            if (null == resultList) {
                resultList = new ArrayList<>(list.size());
            }
            Map<String, String> result = new HashMap<>();
            result.put("region", list.get(start));
            try {
                ServiceTask serviceTask = uuid -> {
                    int rad = (int) (Math.random() * 100);
                    if (rad > 80) {
                        throw new Exception("getTransitions exception");
                    }
                    Thread.sleep(rad);
                    return UUID.randomUUID().toString().replace("-", "");
                };
                // step 1
                String parameter = serviceTask.getParameter(list.get(start));
                // step 2
                String task = serviceTask.createTask(parameter);
                // step 3
                String transitions = serviceTask.getTransitions(task);
                // step 4
                String s = serviceTask.transferStatus(transitions);
                result.put("status", s);
            } catch (Exception e) {
                result.put("error", e.toString());
            }
            synchronized (resultList) {
                resultList.add(result);
            }
            log.info("<><><><><>: paramList:{}, resultList:{}", list.size(), resultList.size());
            if (resultList.size() - list.size() > 0) {
                int removeCount = resultList.size() - list.size();
                while (removeCount-- > 0) {
                    resultList.remove(0);
                }
            }

        } else {
            int middle = (start + end) / 2;
            AutomationTask leftTask = new AutomationTask(list, start, middle);
            log.info("=== {} === fork left {}-{}", Thread.currentThread().getName(), start, middle);
            AutomationTask rightTask = new AutomationTask(list, middle + 1, end);
            log.info("=== {} === fork right {}-{}", Thread.currentThread().getName(), middle + 1, end);
            leftTask.fork();
            rightTask.compute();
            List<Map<String, String>> leftList = leftTask.join();
            log.info("=== {} === leftList {}", Thread.currentThread().getName(), leftList.toArray());
        }
        return resultList;
    }
}
