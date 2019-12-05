package test.design.patterns.singleton;

/**
 * Created by deweydu
 * Date on 2019/12/5 16:21
 */
public class HungrySingleton {
    private HungrySingleton() {
        if (instance != null) {
            try {
                throw new Exception("只能创建一个对象！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static int flag = 1;
    private static HungrySingleton instance = new HungrySingleton();

    public static HungrySingleton getInstance() {
        return instance;
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        System.out.println(HungrySingleton.getInstance());
        System.out.println(HungrySingleton.getInstance());
        System.out.println("反射破解单例...");
        HungrySingleton instance1 = HungrySingleton.class.newInstance();
        HungrySingleton instance2 = HungrySingleton.class.newInstance();
        System.out.println(instance1);
        System.out.println(instance2);
    }
}
