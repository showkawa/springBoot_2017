package com.kawa.mutilthread.queue;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * PriorityBlockingQueue是一个没有边界的队列，它的排序规则和 java.util.PriorityQueue一样。
 * 需要注意，PriorityBlockingQueue中允许插入null对象。所有插入PriorityBlockingQueue的对象必须实现 java.lang.Comparable接口，
 * 队列优先级的排序规则就是按照我们对这个接口的实现来定义的。另外，我们可以从PriorityBlockingQueue获得一个迭代器Iterator，
 * 但这个迭代器并不保证按照优先级顺序进行迭代。
 */
@SuppressWarnings("unchecked")
public class PriorityBlockingQueueTest {

    public static void main(String[] args) {
        PriorityBlockingQueue pbq = new PriorityBlockingQueue<>();
        pbq.add(new Bus(1, "F"));
        pbq.add(new Bus(2, "J"));
        pbq.add(new Bus(4, "K"));
        pbq.add(new Bus(7, "L"));
        pbq.add(new Bus(3, "M"));
        pbq.add(new Bus(5, "F"));
        pbq.add(new Bus(6, "F"));
        pbq.add(new Bus(8, "F"));

        while(true){
            Bus fl = (Bus) pbq.poll();
            if(null != fl){
                System.out.println("处理的航班：" + fl);
                continue;
            }
            break;
        }
    }


}

@Data
@AllArgsConstructor
class Bus implements Comparable<Bus> {
    private int no;
    private String type;

    @Override
    public int compareTo(Bus f) {
        // 升序
        // return this.no - f.getNo();
        // 降序
        return f.getNo() - this.no;
    }
}