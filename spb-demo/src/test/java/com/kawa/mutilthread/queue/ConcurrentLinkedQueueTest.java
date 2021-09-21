package com.kawa.mutilthread.queue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 一个基于链接节点的无界线程安全队列。此队列按照 FIFO（先进先出）原则对元素进行排序。队列的头部 是队列中时间最长的元素。
 * 队列的尾部 是队列中时间最短的元素。
    新的元素插入到队列的尾部，队列获取操作从队列头部获得元素。当多个线程共享访问一个公共 collection 时
    ，ConcurrentLinkedQueue 是一个恰当的选择。此队列不允许使用 null 元素。
 */
@Slf4j
@SuppressWarnings("unchecked")
public class ConcurrentLinkedQueueTest {

    public static void main(String[] args) {
        
        ConcurrentLinkedQueue clq = new ConcurrentLinkedQueue<>();
        // offer 插入到队列尾部
        clq.offer("东方航空");
        clq.offer("国泰航空");
        clq.offer("港龙航空");
        clq.add("南方航空");
        clq.add("海南航空");
        log.info("ConcurrentLinkedQueue队列是否为空: {}",clq.isEmpty());
        // peek 获取但不移除此队列的头；如果此队列为空，则返回 null
        clq.peek();
        log.info("ConcurrentLinkedQueue队列是否为空: {}",clq.isEmpty());
        // remove一个已存在元素，会返回true，remove不存在元素，返回false
        clq.remove("港龙航空");
        for(;;){
            // poll 获取并移除此队列的头，如果此队列为空，则返回 null
            String flightName = (String) clq.poll();
            if(StringUtils.isEmpty(flightName)){
                break;
            }
            log.info("ConcurrentLinkedQueue队列 ===poll===: {}", flightName);
            log.info("ConcurrentLinkedQueue队列是否为空: {}",clq.isEmpty());
        }
        /********************************************************************************************** */
        int passenger = 100000; //乘客
        int lounge = 20; // 休息室
        CountDownLatch cdl = new CountDownLatch(lounge);
        ConcurrentLinkedQueue<String> clq2 = new ConcurrentLinkedQueue<>();

        ExecutorService es=  Executors.newFixedThreadPool(lounge);
        for(int i =1;i<passenger;i++){
            clq2.offer("乘客_" + i);
        }
        long start = System.currentTimeMillis();
        for(int i=0;i<lounge;i++){
            es.submit(new TakeBus(cdl,clq2,"休息区_"+i));
        }
        try{
            cdl.await();
            System.out.println("全部耗时时间：" +(System.currentTimeMillis()-start));
        }catch(Exception e) {}

        // 停止线程
        es.shutdown();



    }
}

class TakeBus implements Runnable {

    private CountDownLatch cdl;

    private ConcurrentLinkedQueue<String> clq;

    private String name;

    public TakeBus(CountDownLatch cdl, ConcurrentLinkedQueue<String> clq, String name){
        this.cdl = cdl;
        this.clq =clq;
        this.name =name;
    }

    @Override
    public void run() {
        while(!clq.isEmpty()){
        // while(clq.size() > 0){
            System.out.println(Thread.currentThread().getName() +" <> 乘客 【"+clq.poll() +"】 进入机场休息室 【"+name +"】 等候飞机");
        }
        cdl.countDown();
    }
    
}