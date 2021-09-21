package com.kawa.mutilthread.queue;


import java.util.ArrayDeque;

/**
 *  ArrayDeque是JDK容器中的一个双端队列实现，内部使用数组进行元素存储，不允许存储null值，可以高效的进行元素查找和尾部插入取出，
 *  是用作队列、双端队列、栈的绝佳选择，性能比LinkedList还要好
 * 
 * 
 */
public class ArrayDequeTest {
    public static void main(String[] args) {
        ArrayDeque<String> arrayDeque = new ArrayDeque<>();

                //添加元素
                arrayDeque.add("A");
                arrayDeque.add("B");
                arrayDeque.add("C");
                arrayDeque.add("D");
                arrayDeque.add("E");
                arrayDeque.add("F");
                arrayDeque.add("G");
                arrayDeque.add("H");
                arrayDeque.add("I");
                System.out.println(arrayDeque);

                // 获取元素
                String a = arrayDeque.getFirst();
                String a1 = arrayDeque.pop();
                String b = arrayDeque.element();
                String b1 = arrayDeque.removeFirst();
                String c = arrayDeque.peek();
                String c1 = arrayDeque.poll();
                String d = arrayDeque.pollFirst();
                String i = arrayDeque.pollLast();
                String e = arrayDeque.peekFirst();
                String h = arrayDeque.peekLast();
                String h1 = arrayDeque.removeLast();
                System.out.printf("a = %s, a1 = %s, b = %s, b1 = %s, c = %s, c1 = %s, d = %s, i = %s, e = %s, h = %s, h1 = %s", a,a1,b,b1,c,c1,d,i,e,h,h1);
                System.out.println();
                System.out.println(arrayDeque);

                // 添加元素
                arrayDeque.push(e);
                arrayDeque.add(h);
                arrayDeque.offer(d);
                arrayDeque.offerFirst(i);
                arrayDeque.offerLast(c);
                arrayDeque.offerLast(h);
                arrayDeque.offerLast(c);
                arrayDeque.offerLast(h);
                arrayDeque.offerLast(i);
                arrayDeque.offerLast(c);
                System.out.println(arrayDeque);

                // 移除第一次出现的C
                arrayDeque.removeFirstOccurrence(c);
                System.out.println(arrayDeque);

                // 移除最后一次出现的C
                arrayDeque.removeLastOccurrence(c);
                System.out.println(arrayDeque);

    }

}
