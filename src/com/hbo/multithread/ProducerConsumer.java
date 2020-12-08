package com.hbo.multithread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**  //1.高内聚低偶合情况下线程操作资源类
 *     2.判断->干活->通知
 *     3.多线程交互中，必须防止线程的虚假唤醒(多线程的判断不能使用if 必须使用while)
 *
 * @ClassNameProducerConsumer
 * @Description TODO
 * @Author hbo  生产面包和消费面包 生产一个消费一个  最后初始值始终为0
 *          分析:当只有一个生产者和一个消费者的时候  使用notifyAll不会出问题,当产生了多个生产者和消费者的时候，
 *              会出现线程虚假唤醒,使用notifyAll会唤醒所有等待的线程,抢到执行权的可能不是我们想要的线程。
 *
 *            **tips: wait和notify notifyAll要使用在同步代码块中(synchronized) 否则会报java.lang.IllegalMonitorStateException
 *                    condition.await()和signal(),signalAll()必须使用在lock.lock()和lock.unlock()之间 否则会报java.lang.IllegalMonitorStateException
 * @Date 2020/12/8 21:55
 */
public class ProducerConsumer {

    public static void main(String[] args) {

        Bread bread = new Bread();
        new Thread(()->{for(int i =0;i<10;i++) bread.produce();},"A").start();
        new Thread(()->{for(int i =0;i<10;i++) bread.consume();},"B").start();
        new Thread(()->{for(int i =0;i<10;i++) bread.produce();},"C").start();
        new Thread(()->{for(int i =0;i<10;i++) bread.consume();},"D").start();
//
//        new Thread(()->{for(int i =0;i<10;i++) bread.produceC();},"E").start();
//        new Thread(()->{for(int i =0;i<10;i++) bread.consumeC();},"F").start();
//        new Thread(()->{for(int i =0;i<10;i++) bread.produceC();},"G").start();
//        new Thread(()->{for(int i =0;i<10;i++) bread.consumeC();},"H").start();
    }
}

class Bread{

    private int num  = 0;


    //使用condition

    //3.新写法  使用condition
    private Lock mylock = new ReentrantLock();
    private Condition condition = mylock.newCondition();

    public synchronized void produce() {
        try {
        //判断
        while(num!=0){
                this.wait();
            }
        //干活
        num++;
        System.out.println(Thread.currentThread().getName()+"生产了:"+num);
        //通知
        this.notifyAll();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void consume() {
        try {
             while(num==0){
                this.wait();
            }
            num--;
            System.out.println(Thread.currentThread().getName()+"消费了:"+ num);
            this.notifyAll();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public  void produceC() {
        mylock.lock();
        try {
                while(num!=0){
                    condition.await();
                }
                num++;
                System.out.println(Thread.currentThread().getName()+"生产了:"+num);
                condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            mylock.unlock();
        }

    }

    public  void consumeC() {
        mylock.lock();
        try {
             while(num==0){
                condition.await();
             }
            num--;
            System.out.println(Thread.currentThread().getName()+"消费了:"+ num);
            condition.signalAll();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally{
                mylock.unlock();
        }

    }
}
