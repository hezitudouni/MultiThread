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

    //公共的买票方法
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
