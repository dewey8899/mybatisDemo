package test.reentrantlock;

/**
 * Created by deweydu
 * Date on 2019/9/27 10:10
 */
public class Main {
    public static void main(String[] args) {

        MyService service = new MyService();

        ProduceThread[] pt = new ProduceThread[1];
        ConsumeThread[] ct = new ConsumeThread[1];

        for(int i=0;i<1;i++){
            pt[i] = new ProduceThread(service);
            pt[i].setName("Condition 生产者 "+(i+1));
            ct[i] = new ConsumeThread(service);
            ct[i].setName("Condition 消费者"+(i+1));
            pt[i].start();
            ct[i].start();
        }
    }
}
