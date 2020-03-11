package test.ifelse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by deweydu
 * Date on 2020/3/11 15:23
 */
public class IfFunction<K> {

    private Map<K, Function> map;

    /**
     * the IfFunction need a map to save keys and functions
     *
     * @param map a map
     */
    public IfFunction(Map<K, Function> map) {
        this.map = map;
    }

    /**
     * add key and function to the map
     *
     * @param key      the key need to verify
     * @param function it will be executed when the key exists
     * @return this.
     */
    public IfFunction<K> add(K key, Function function) {
        this.map.put(key, function);
        return this;
    }

    /**
     * Determine whether the key exists, and if there is, the corresponding method is executed.
     *
     * @param key the key need to verify
     */
    public void doIf(K key) {
        if (this.map.containsKey(key)) {
            map.get(key).invoke();
        }
    }

    /**
     * Determine whether the key exists, and if there is, the corresponding method is executed.
     * otherwise the defaultFunction is executed.
     *
     * @param key             the key need to verify
     * @param defaultFunction it will be executed when the key is not exists.
     */
    public void doIfWithDefault(K key, Function defaultFunction) {
        if (this.map.containsKey(key)) {
            map.get(key).invoke();
        } else {
            defaultFunction.invoke();
        }
    }

    public static void main(String[] args) {
        IfFunction<String> ifFunction = new IfFunction<>(new HashMap<>(5));
        ifFunction.add("hello", () -> System.out.println("你好"))
                .add("helloWorld", () -> System.out.println("你好，世界"))
                .doIfWithDefault("helloWorld1", () -> System.out.println("没有找到对应的key!"));
    }
}
