package test.design.patterns.wrapper;

/**
 * Created by deweydu
 * Date on 2019/12/5 14:53
 */
public class CommonManFlyWrapper extends CommonManWrapper {
    public CommonManFlyWrapper(Man man) {
        super(man);
    }

    @Override
    public void action() {
        super.action();
        fly();
    }
    private void fly(){
        System.out.println("I can fly...");
    }

    public static void main(String[] args) {
        new CommonManFlyWrapper(new CommonMan()).action();
    }
}
