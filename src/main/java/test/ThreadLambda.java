package test;

/**
 * Created by deweydu
 * Date on 2019/8/29 17:35
 */
public class ThreadLambda {
    public static void main(String[] args) {
        Runnable runnable = () -> System.out.println("hello world");
        runnable.run();
        new Thread(()->{
            System.out.println("线程启动了");
        }).start();
    }
}
