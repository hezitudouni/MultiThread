package com.hbo.multithread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassNameThreadOrderAccess  现场顺序执行
 * @Description TODO
 * @Author hbo  分别操作三个线程执 A线程打印5次->B线程打印10次->C线程打印15次
 * @Date 2020/12/8 23:06
 */
public class ThreadOrderAccess {

    public static void main(String[] args) {
        ShareResource resource = new ShareResource();
//        new Thread(()->{for(int i = 0;i<10;i++){resource.print5();}},"A").start();
//        new Thread(()->{for(int i = 0;i<10;i++){resource.print10();}},"B").start();
//        new Thread(()->{for(int i = 0;i<10;i++){resource.print15();}},"C").start();

        new Thread(()->{for(int i = 0;i<10;i++){resource.print(5);}},"A").start();
        new Thread(()->{for(int i = 0;i<10;i++){resource.print(10);}},"B").start();
        new Thread(()->{for(int i = 0;i<10;i++){resource.print(15);}},"C").start();

    }

}

class ShareResource{

    private int flag = 1;
    private Lock lock = new ReentrantLock();

    private Condition condition1 = lock.newCondition();
    private Condition condition2 = lock.newCondition();
    private Condition condition3 = lock.newCondition();


    public void print5(){
        lock.lock();
        try{
            //判断
            while(flag!=1){
                condition1.await();
            }
            //干活
            for(int i = 0;i<5;i++){
                System.out.println(Thread.currentThread().getName()+":"+i);
            }
            //通知
            flag = 2;
            condition2.signal();

        }catch (Exception e){

        }finally {
            lock.unlock();
        }

    }

    public void print10(){
        lock.lock();
        try{
            //判断
            while(flag!=2){
                condition2.await();
            }
            //干活
            for(int i = 0;i<10;i++){
                System.out.println(Thread.currentThread().getName()+":"+i);
            }
            //通知
            flag = 3;
            condition3.signal();

        }catch (Exception e){

        }finally {
            lock.unlock();
        }

    }
    public void print15(){
        lock.lock();
        try{
            //判断
            while(flag!=3){
                condition3.await();
            }
            //干活
            for(int i = 0;i<15;i++){
                System.out.println(Thread.currentThread().getName()+":"+i);
            }
            //通知
            flag = 1;
            condition1.signal();

        }catch (Exception e){

        }finally {
            lock.unlock();
        }

    }


    public void print(int count){
        lock.lock();
        try{
            String threadName = Thread.currentThread().getName();
            if("A".equals(threadName)){
                //判断
                while(flag!=1){
                    condition1.await();
                }
                //干活
                for(int i = 0;i<count;i++){
                    System.out.println(Thread.currentThread().getName()+":"+i);
                }
                //通知
                flag = 2;
                condition2.signal();
            }
            if("B".equals(threadName)){
                //判断
                while(flag!=2){
                    condition2.await();
                }
                //干活
                for(int i = 0;i<count;i++){
                    System.out.println(Thread.currentThread().getName()+":"+i);
                }
                //通知
                flag = 3;
                condition3.signal();
            }
            if("C".equals(threadName)){
                //判断
                while(flag!=3){
                    condition3.await();
                }
                //干活
                for(int i = 0;i<count;i++){
                    System.out.println(Thread.currentThread().getName()+":"+i);
                }
                //通知
                flag = 1;
                condition1.signal();
            }

        }catch (Exception e){

        }finally {
            lock.unlock();
        }

    }

}
