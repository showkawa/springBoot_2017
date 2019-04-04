package com.kawa.javaJVM;

import org.junit.Ignore;

public class Test {

    @org.junit.Test
    @Ignore
    public void testGC(){
            Test t = new Test();
            t=null;
            System.gc();
    }

    @org.junit.Test
    public void testMemory(){
        byte[] b = null;
        for (int i = 0; i < 15; i++) {
            b = new byte[1 * 1024 * 1024];
        }
    }

    /*@Override
    protected void finalize() throws Throwable {
        System.out.println("垃圾回收机制要启动了！！");
        System.out.print("最大内存");
        System.out.println(Runtime.getRuntime().maxMemory() / 1024.0 / 1024 + "M");
        System.out.print("可用内存");
        System.out.println(Runtime.getRuntime().freeMemory() / 1024.0 / 1024 + "M");
        System.out.print("已经使用内存");
        System.out.println(Runtime.getRuntime().totalMemory() / 1024.0 / 1024 + "M");

    }*/
}
