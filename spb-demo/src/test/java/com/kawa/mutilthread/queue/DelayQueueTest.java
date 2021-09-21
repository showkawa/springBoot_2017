package com.kawa.mutilthread.queue;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * DelayQueue 是一个无界阻塞队列，要添加进去的元素必须实现Delayed接口，只有在延迟期满时才能从中提取元素。 该队列的头部
 * 是延迟期满后保存时间最长的 Delayed 元素。 如果延迟都还没有期满，则队列没有头部，并且 poll 将返回 null。 当一个元素的
 * getDelay(TimeUnit.NANOSECONDS) 方法返回一个小于等于 0 的值时，表示该元素到期了。 无法使用 take 或 poll
 * 移除未到期的元素，也不会将这些元素作为正常元素对待。例如，size 方法同时返回到期和未到期元素的计数。 此队列不允许使用 null 元素。
 * 
 */
@SuppressWarnings("unchecked")
public class DelayQueueTest {

    public static void main(String[] args) {
        DelayQueue dq = new DelayQueue<>();
        dq.add(new FlightJob("housekeepJob-01", 1000));
        dq.add(new FlightJob("housekeepJob-02", 5000));
        dq.add(new FlightJob("housekeepJob-03", 6000));
        dq.add(new FlightJob("housekeepJob-04", 3000));
        dq.add(new FlightJob("housekeepJob-05", 4000));
        dq.add(new FlightJob("housekeepJob-06", 12000));
        dq.add(new FlightJob("housekeepJob-07", 3000));
        dq.add(new FlightJob("housekeepJob-08", 3000));

        while (true) {
            try {
                 FlightJob fj = (FlightJob) dq.take();
                System.out.println("JOB执行信息: " + fj);
                FlightJob checkNull = (FlightJob) dq.peek();
                if(null == checkNull){
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

@Data
@AllArgsConstructor
class FlightJob implements Delayed {
    private String jobName;
    private long start = System.currentTimeMillis();
    private long handletTime;

    public FlightJob(String jobName,long handletTime) {
        this.jobName = jobName;
        this.handletTime = handletTime;
    }

    /**
     * 用于延迟队列内部比较排序   当前时间的延迟时间 - 比较对象的延迟时间
     */
    @Override
    public int compareTo(Delayed delayed) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - delayed.getDelay(TimeUnit.MILLISECONDS));
    }

    /**
     * 获得延迟时间   用过期时间-当前时间
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert((start+handletTime) - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
    }

   
}