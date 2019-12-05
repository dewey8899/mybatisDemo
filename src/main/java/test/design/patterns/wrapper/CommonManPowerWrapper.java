package test.design.patterns.wrapper;

/**
 * Created by deweydu
 * Date on 2019/12/5 15:04
 */
public class CommonManPowerWrapper extends CommonManWrapper {
    public CommonManPowerWrapper(Man man) {
        super(man);
    }

    @Override
    public void action() {
        super.action();
        power();

    }
    private void power(){
        System.out.println("I have infinite power...");
    }

    public static void main(String[] args) {

        new CommonManPowerWrapper(new CommonMan()).action();

        new CommonManPowerWrapper(new CommonManFlyWrapper(new CommonMan())).action();

    }
}
