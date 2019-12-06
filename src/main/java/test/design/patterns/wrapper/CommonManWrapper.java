package test.design.patterns.wrapper;

/**
 * Created by deweydu
 * Date on 2019/12/5 14:52
 * function: 我们需要先提供一个抽象装饰者类，这个类封装需要增强的原始对象，然后，根据不同的功能点提供其子类。
 *
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
