package test.reentrantlock;

/**
 * Created by deweydu
 * Date on 2019/9/27 10:09
 */
public class ProduceThread extends Thread{
    private MyService p;
    public ProduceThread(MyService p){
        this.p = p;
    }
    @Override
    public void run() {
        while (true) {
            p.produce();
        }
    }
}
