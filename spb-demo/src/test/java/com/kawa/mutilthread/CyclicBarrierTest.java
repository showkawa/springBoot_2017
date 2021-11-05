package com.kawa.mutilthread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CyclicBarrier;

/**
 * 
 * CyclicBarrier初始化时规定一个数目，然后计算调用了CyclicBarrier.await()进入等待的线程数。
 * 当线程数达到了这个数目时，所有进入等待状态的线程被唤醒并继续。 CyclicBarrier就象它名字的意思一样，可看成是个障碍，
 * 所有的线程必须到齐后才能一起通过这个障碍。 CyclicBarrier初始时还可带一个Runnable的参数，
 * 此Runnable任务在CyclicBarrier的数目达到后， 所有其它线程被唤醒前被执行。
 */
@Slf4j
public class CyclicBarrierTest {

   public static void main(final String[] args) {

      final CyclicBarrier cb = new CyclicBarrier(10, () -> {
         log.info(Thread.currentThread().getName() + " <> 聚齐10件货物开始装箱");
      });
    

      try {
        int i = 1;
         while( i < 101){
            new HealthCheck(cb,i).start();
            i++;
         }
      }catch(final Exception e){} finally{
      }
   }
}

@Slf4j
class HealthCheck extends Thread {
   private final CyclicBarrier  cyclicBarrier;
   private final int  no;

   public HealthCheck(final CyclicBarrier  cyclicBarrier,final int no){
         this.cyclicBarrier = cyclicBarrier;
         this.no = no;
   }

   @Override
   public void run() {
      log.info(String.format("%s <> 行李箱[%d]扫描准备装箱", Thread.currentThread().getName(),no));
      try{
         cyclicBarrier.await();
      }catch(final Exception e){}         
      log.info(String.format("%s <> 行李箱[%d]已经装箱", Thread.currentThread().getName(),no));
   }
}