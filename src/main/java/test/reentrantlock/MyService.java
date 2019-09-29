package test.reentrantlock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by deweydu
 * Date on 2019/9/27 10:08
 */
public class MyService {
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private boolean hasValue = false;


    public void produce() {
        lock.lock();
        try {
            /*只有list为空时才会去进行生产操作*/
            while (hasValue == true) {
                System.out.println("生产者" + Thread.currentThread().getName() + " waiting");
                condition.await();
            }
            hasValue = true;
            System.out.println("生产者" + Thread.currentThread().getName() + " Runnable");
            condition.signalAll();//然后去唤醒因object调用wait方法处于阻塞状态的线程
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    public void consmer() {
        lock.lock();
        try {
            /*只有list为空时才会去进行生产操作*/
            while(hasValue == false){
                System.out.println("消费者"+Thread.currentThread().getName()+" waiting");
                condition.await();
            }
            hasValue = false;
            System.out.println("消费者"+Thread.currentThread().getName()+" Runnable");
            condition.signalAll();//然后去唤醒因object调用wait方法处于阻塞状态的线程
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            lock.unlock();
        }

    }
}
