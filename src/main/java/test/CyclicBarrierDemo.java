package test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by deweydu
 * Date on 2019/9/26 13:14
 */
public class CyclicBarrierDemo {
    public static void main(String[] args) {
        //栅栏动作，在计数器为0的时候执行
        CyclicBarrier barrier = new CyclicBarrier(2, () -> System.out.println("我们都准备好了."));

        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < 2; i++) {
            es.execute(new Roommate(barrier));
        }
    }

}
class Roommate implements Runnable {
    private CyclicBarrier barrier;
    private static int Count = 1;
    private int id;

    public Roommate(CyclicBarrier barrier) {
        this.barrier = barrier;
        this.id = Count++;
    }

    @Override
    public void run() {
        System.out.print(id + " : 我到了--> ");
        try {
            //通知barrier，已经完成动作，在等待
            barrier.await();
            System.out.print("Id " + id + " : 点菜吧!-- >");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
