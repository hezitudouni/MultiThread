package com.hbo.multithread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassNameSellTickets
 * @Description TODO
 * @Author hbo
 * @Date 2020/12/7 22:39
 */
public class SellTickets { //资源类
    //三十张票
    private int total = 30;

    //公共的买票方法  操作
    //1.老的写法 synchronized
    public synchronized void sellTicket(){
        if(total>0){
            System.out.println(Thread.currentThread().getName() +"买到第"+ total-- +"张票 "+ "剩余票数 ："+ total);
        }
    }


    //2.新写法 synchronized 使用lock方法
   private Lock lock = new ReentrantLock();

    public synchronized void sellTicket1(){
        lock.lock();
        try{
            if(total>0){
                System.out.println(Thread.currentThread().getName() +"买到第"+ total-- +"张票 "+ "剩余票数 ："+ total);
            }
        }finally {
            lock.unlock();
        }

    }
    //      线程的运行状态   Thread.State枚举类
    //         1.NEW  new Thread()
    //         2.RUNNABLE 就绪 new Thread().satrt;
    //         3.BLOCKED  阻塞 Thread.sleep() sleep不释放锁 ,object.wait() wait释放所;
    //         4.WAITING  等待
    //         5.TIMED_WAITING  带时间等候
    //         6.TERMINATED 终止
    //
    //
    public static void main(String[] args) {
        //多线程操作  线程操作资源类
        SellTickets tickets = new SellTickets();
//        new Thread(()->{for(int i = 0;i<30;i++){tickets.sellTicket();}},"A").start();
//        new Thread(()->{for(int i = 0;i<30;i++){tickets.sellTicket();}},"B").start();
//        new Thread(()->{for(int i = 0;i<30;i++){tickets.sellTicket();}},"C").start();

        new Thread(()->{for(int i = 0;i<30;i++){tickets.sellTicket1();}},"E").start();
        new Thread(()->{for(int i = 0;i<30;i++){tickets.sellTicket1();}},"F").start();
        new Thread(()->{for(int i = 0;i<30;i++){tickets.sellTicket1();}},"G").start();


    }

}
