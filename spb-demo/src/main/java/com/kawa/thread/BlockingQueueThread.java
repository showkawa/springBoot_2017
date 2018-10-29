package com.kawa.thread;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class BlockingQueueThread {


    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10);
        ProducerThread producerThread1 = new ProducerThread(queue);
        ProducerThread producerThread2 = new ProducerThread(queue);
        ConsumerThread consumerThread1 = new ConsumerThread(queue);
        Thread t1 = new Thread(producerThread1);
        Thread t2 = new Thread(producerThread2);
        Thread c1 = new Thread(consumerThread1);
        t1.start();
        t2.start();
        c1.start();

        // 执行10s
        Thread.sleep(10 * 1000);
        producerThread1.stop();
        producerThread2.stop();

    }


    static class ProducerThread implements Runnable{

        private BlockingQueue blockingQueue;
        private volatile boolean flag = true;
        private static AtomicInteger atomicInteger = new AtomicInteger();

        public ProducerThread(BlockingQueue blockingQueue) {
            this.blockingQueue = blockingQueue;
        }

        public void stop(){
            this.flag = false;
        }

        @Override
        public void run() {
            try {
                System.out.println("生产者启动线程");
                while(flag){
                   String data = atomicInteger.incrementAndGet() + "";
                    boolean offer = blockingQueue.offer(data, 2, TimeUnit.SECONDS);
                    if(offer) {
                        System.out.println("生产者,存入" + data + "到队列中 success.");
                    }else {
                        System.out.println("生产者,存入" + data + "到队列中 failed.");
                    }
                    Thread.sleep(1000);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                System.out.println("生产者推出线程");
            }
        }
    }

    static class ConsumerThread  implements Runnable{

        private BlockingQueue<String> blockingQueue;
        private volatile boolean flag = true;

        public ConsumerThread(BlockingQueue<String> blockingQueue) {
            this.blockingQueue = blockingQueue;
        }


        @Override
        public void run() {
            try {
                System.out.println("消费者启动线程");
                while(flag){
                    String data = blockingQueue.poll( 2, TimeUnit.SECONDS);
                    if(data != null) {
                        System.out.println("消费者,拿到队列中的数据data:" + data);
                        Thread.sleep(1000);
                    }else {
                        System.out.println("消费者,超过2秒未获取到数据..");
                        flag = false;
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                System.out.println("消费者推出线程");
            }
        }
    }

}
