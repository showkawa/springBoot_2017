package com.kawa.thread;

import com.kawa.pojo.Worker;

/**
 *   synchronized同步代码块实现所机制
 *      wait() notify() 方法只能放在synchronized同步代码块里面
 */
public class ThreadSync {

    public static void main(String[] args) {
        Worker u = new Worker();
        InputUser iu =  new InputUser(u);
        OutputUser ou =  new OutputUser(u);
        iu.start();
        ou.start();
    }


}

class InputUser extends  Thread {

    Worker worker;
    public InputUser(Worker worker) {
        this.worker = worker;
    }

    @Override
    public void run() {
        int count = 0;
            while (true){
                synchronized (worker){
                    if(worker.getwFlag()){
                        try{
                            worker.wait();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                if(count == 1) {
                    worker.setwName("AAA");
                    worker.setwNumber("222222");
                } else {
                    worker.setwName("UUU");
                    worker.setwNumber("555555");
                }
                count = (count + 1)%2;
                worker.setwFlag(true);
                worker.notify();
            }
        }
    }
}

class OutputUser extends  Thread {

    Worker worker;
    public OutputUser(Worker worker) {
        this.worker = worker;
    }

    @Override
    public void run() {
        while(true){
         synchronized (worker) {
             if(!worker.getwFlag()){
                 try {
                     worker.wait();
                 }
                 catch (Exception e){
                     e.printStackTrace();
                 }
             }
             System.out.println("取值：{ " + worker.getwName() + "  " + worker.getwNumber()+ " }");
             worker.setwFlag(false);
             worker.notify();
            }
        }
    }
}


