package com.kawa.mutilthread.queue;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.PriorityQueue;

/**
 * PriorityQueue是基于优先级堆的无界优先级队列。他们的元素可按自然排序，也可在创建ProorityQueue实例时指定比较器。
    不能添加null对象，也不能添加不可比对象，这样会抛出ClassCastException异常。
 */
@SuppressWarnings("unchecked")
public class PriorityQueueTest {

    public static void main(String[] args) {
        PriorityQueue pq = new PriorityQueue<>();
        pq.add(new Flight(4, "F", "AAA"));
        pq.add(new Flight(5, "J", "BBBB"));
        pq.add(new Flight(64, "K", "CCCC"));
        pq.add(new Flight(72, "L", "DDDD"));
        pq.add(new Flight(8, "M", "RRRR"));
        pq.add(new Flight(9, "F", "UUUU"));
        pq.add(new Flight(140, "F", "PPOI"));
        pq.add(new Flight(11, "F", "ADDX"));

        while(true){
            Flight fl = (Flight) pq.poll();
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
class Flight implements Comparable<Flight> {
    private int no;
    private String type;
    private  String acRegn;

    @Override
    public int compareTo(Flight f) {
        // 升序
        // return this.no - f.getNo();
        // 降序
        return f.getNo() - this.no;
    }
}