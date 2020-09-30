package com.hbo.multithread;


/**
 * @Classname DeadLock.java
 * @Description TODO  一个死锁的案例
 * @Date 2020/9/30 15:34
 * @Created by hbo
 */
public class DeadLock {

    public static void  main(String[] args){
        //Thread.sleep使当前线程放弃cpu的执行权以保证下一个线程可以获取到cpu的执行权，进入死锁状态
        //调试的工具有 1.jps搭配jstack 2.jvisualvm.exe 3.jconsole
        Object o1 = new Object();
        Object o2 = new Object();

        new Thread(()->{
            synchronized (o1){
                try {
                    System.out.println("Thread1 get o1!");
                   Thread.sleep(100);
                   synchronized (o2){
                       System.out.println("Thread1 get o2!");
                   }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(()->{
            synchronized (o2){
                try {
                    System.out.println("Thread2 get o2!");
                    Thread.sleep(100);
                    synchronized (o1){
                        System.out.println("Thread2 get o1!");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
