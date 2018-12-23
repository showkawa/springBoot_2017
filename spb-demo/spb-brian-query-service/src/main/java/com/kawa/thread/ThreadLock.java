package com.kawa.thread;

import com.kawa.pojo.Worker;

import java.util.concurrent.locks.Condition;

/**
 * JDk1.5 Lock锁
 *   lock()到unlock()之间，相当于synchronize大括号代码块之间区域
 *   不过unLock()一定要放在try catch 后面的finally里面，不用程序在ock()到unlock()之间异常了，锁不会被释放
 *    condition.await() ==> wait()
 *    condition.signal() ==> notify()
 */
public class ThreadLock {

        public static void main(String[] args) {
            Worker u = new Worker();
            Condition condition = u.getLock().newCondition();
            InputUserL iu =  new InputUserL(u,condition);
            OutputUserL ou =  new OutputUserL(u,condition);
            iu.start();
            ou.start();
        }

    }

class InputUserL extends  Thread {


    Worker worker;
    Condition condition;
    public InputUserL(Worker worker,Condition condition) {
        this.worker = worker;
        this.condition = condition;
    }

    @Override
    public void run() {
        int count = 0;
        while (true){
          //  synchronized (worker){
            worker.getLock().lock();
            try {
                if (worker.getwFlag()) {
                    try {
                       // worker.wait();
                        condition.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (count == 1) {
                    worker.setwName("AAA");
                    worker.setwNumber("222222");
                } else {
                    worker.setwName("UUU");
                    worker.setwNumber("555555");
                }
                count = (count + 1) % 2;
                worker.setwFlag(true);
                //worker.notify();
                condition.signal();
            } catch (Exception e){
                e.printStackTrace();
            }finally {
                worker.getLock().unlock();
            }
            //}
        }
    }
}

    class OutputUserL extends  Thread {

        Worker worker;
        Condition condition;
        public OutputUserL(Worker worker,Condition condition) {
            this.worker = worker;
            this.condition = condition;
        }

        @Override
        public void run() {
            while(true){
              //  synchronized (worker) {
                worker.getLock().lock();
                try {
                    if(!worker.getwFlag()){
                        try {
                            //worker.wait();
                            condition.await();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    System.out.println("取值：{ " + worker.getwName() + "  " + worker.getwNumber()+ " }");
                    worker.setwFlag(false);
                   // worker.notify();
                    condition.signal();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    worker.getLock().unlock();
                }
               // }
            }

        }



}
