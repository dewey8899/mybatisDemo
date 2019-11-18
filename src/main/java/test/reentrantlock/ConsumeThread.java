package test.reentrantlock;

/**
 * Created by deweydu
 * Date on 2019/9/27 10:09
 */
public class ConsumeThread extends Thread {
    private MyService c;
    public ConsumeThread(MyService c){
        this.c = c;
    }
    @Override
    public void run() {
        while (true) {
            c.consmer();
        }
    }
}
