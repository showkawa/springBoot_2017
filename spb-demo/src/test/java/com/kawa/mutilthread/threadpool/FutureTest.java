package com.kawa.mutilthread.threadpool;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.*;

/**
 *  Future
    V get() ：获取异步执行的结果，如果没有结果可用，此方法会阻塞直到异步计算完成。
    V get(Long timeout , TimeUnit unit) ：获取异步执行结果，如果没有结果可用，此方法会阻塞，但是会有时间限制，如果阻塞时间超过设定的timeout时间，该方法将抛出异常。
    boolean isDone() ：如果任务执行结束，无论是正常结束或是中途取消还是发生异常，都返回true。
    boolean isCanceller() ：如果任务完成前被取消，则返回true。
    boolean cancel(boolean mayInterruptRunning) ：如果任务还没开始，执行cancel(...)方法将返回false；
    如果任务已经启动，执行cancel(true)方法将以中断执行此任务线程的方式来试图停止任务，如果停止成功，返回true；
    当任务已经启动，执行cancel(false)方法将不会对正在执行的任务线程产生影响(让线程正常执行到完成)，此时返回false；
    当任务已经完成，执行cancel(...)方法将返回false。mayInterruptRunning参数表示是否中断执行中的线程。
    通过方法分析我们也知道实际上Future提供了3种功能：（1）能够中断执行中的任务（2）判断任务是否执行完成（3）获取任务执行完成后额结果。
 * 
 */
public class FutureTest {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
     ExecutorService  pool =
        new ThreadPoolExecutor(1, 1, 2000, TimeUnit.MICROSECONDS, new ArrayBlockingQueue<>(1));
            Future<Flight> futureFlight =
             pool.submit(new CodeTransferTask(new Flight(1, null)));

            System.out.println(Thread.currentThread().getName() + " <> 线程执行其他任务");
            System.out.println(Thread.currentThread().getName() + " <> 接受到返回值: " + futureFlight.get().toString());

        if(null != pool) {
           pool.shutdown();
        }
    }
}

class CodeTransferTask  implements Callable<Flight> {

    private Flight flight;
    public CodeTransferTask(Flight flight){
        this.flight = flight;
    }

    @Override
    public Flight call() throws Exception {
        this.flight.setType("J");
        if(this.flight.getNo() % 3 == 0){
            this.flight.setType("F");
        }
        TimeUnit.SECONDS.sleep(2);
        return this.flight;
    }
}

@Data
@AllArgsConstructor
class Flight  {
    private int no;
    private String type;
}