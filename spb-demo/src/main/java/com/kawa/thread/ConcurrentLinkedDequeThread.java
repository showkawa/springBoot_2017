package com.kawa.thread;


import java.util.concurrent.ConcurrentLinkedDeque;

/**
 *ConcurrentLinkedQueue : 是一个适用于高并发场景下的队列，通过无锁的方式，实现
 * 了高并发状态下的高性能，通常ConcurrentLinkedQueue性能好于BlockingQueue.它
 * 是一个基于链接节点的无界线程安全队列。该队列的元素遵循先进先出的原则。头是最先
 * 加入的，尾是最近加入的，该队列不允许null元素。
 * ConcurrentLinkedQueue重要方法:
 * add 和offer() 都是加入元素的方法(在ConcurrentLinkedQueue中这俩个方法没有任何区别)
 * poll() 和peek() 都是取头元素节点，区别在于前者会删除元素，后者不会。
 */
public class ConcurrentLinkedDequeThread {

    public static void main(String[] args) {
        ConcurrentLinkedDeque concurrentLinkedDeque = new ConcurrentLinkedDeque();
        concurrentLinkedDeque.offer("github");
        concurrentLinkedDeque.offer("redhat");
        concurrentLinkedDeque.offer("google");
        concurrentLinkedDeque.offer("alibaba");
        concurrentLinkedDeque.offer("tencent");
        System.out.println("ConcurrentLinkedDeque 长度 "+ concurrentLinkedDeque.size());
        final Object poll = concurrentLinkedDeque.poll();
        System.out.println("ConcurrentLinkedDeque 取出的值 " + poll);
        System.out.println("ConcurrentLinkedDeque 长度 " + concurrentLinkedDeque.size());
        final Object peek = concurrentLinkedDeque.peek();
        System.out.println("ConcurrentLinkedDeque 取出的值 " + peek);
        System.out.println("ConcurrentLinkedDeque  长度"+ concurrentLinkedDeque.size());
        final Object peek2 = concurrentLinkedDeque.peek();
        System.out.println("ConcurrentLinkedDeque 取出的值 " + peek2);
        System.out.println("ConcurrentLinkedDeque  长度"+ concurrentLinkedDeque.size());

    }
}
