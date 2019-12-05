package test.design.patterns.wrapper;

/**
 * Created by deweydu
 * Date on 2019/12/5 14:52
 */
public abstract class CommonManWrapper implements Man{
    private Man man;
    public CommonManWrapper(Man man) {
        this.man = man;
    }
    public void action() {
        man.action();
    }
}
