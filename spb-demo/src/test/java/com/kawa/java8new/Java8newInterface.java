package com.kawa.java8new;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * java8新特性接口添加default和static方法
 */
public class Java8newInterface {
    Logger logger = LoggerFactory.getLogger(Java8newInterface.class);


    public static void main(String[] args) {
        BrianInterface2.brianStatic();
        BrainClass bs = new BrainClass();
        bs.brianDefault();
        System.out.println(BrainClass.kawa);

    }


     static class  BrainClass implements  BrianInterface,BrianInterface2 {

    }

    interface BrianInterface {
        default void brianDefault (){
            System.out.println("默认方法。。。。");
        }
    }

    interface BrianInterface2 {
        final static  String kawa = "NN";
        static void brianStatic (){
            System.out.println("静态方法。。。。");
        }
    }

}
