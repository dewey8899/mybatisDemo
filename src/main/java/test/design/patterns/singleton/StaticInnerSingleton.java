package test.design.patterns.singleton;

/**
 * Created by deweydu
 * Date on 2019/12/5 16:18
 */
public class StaticInnerSingleton {
    private StaticInnerSingleton() {
    }
    private static class StaticInnerSingletonInstance {
        private static final StaticInnerSingleton instance = new StaticInnerSingleton();
    }
    public static StaticInnerSingleton getInstance() {
        return StaticInnerSingletonInstance.instance;
    }
}
