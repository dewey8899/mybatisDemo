package test;

import java.util.*;

/**
 * Created by deweydu
 * Date on 2019/11/29 11:31
 */
public class SetDemo {
    public static void main(String[] args) {
//        Set<String> linkedHashSet = new LinkedHashSet();
//        linkedHashSet.add("dewey1");
//        linkedHashSet.add("dewey2");
//        linkedHashSet.add("dewey3");
//        linkedHashSet.add("dewey3");
//        linkedHashSet.add("dewey4");
//        Iterator iterator = linkedHashSet.iterator();
//        while (iterator.hasNext()){
//            System.out.println(iterator.next());
//        }

        Map<List<String>, List<String>> map = new HashMap<>();
        List<String> keys = new ArrayList<>();
        keys.add("k_dewey0");
//        keys.add("k_dewey1");
        List<String> values = new ArrayList<>();
        values.add("v_dewey0");
        values.add("v_dewey1");
        for (String key :keys) {
            map.put(Arrays.asList(key.split(",")), new ArrayList<>());
            Set<List<String>> lists = map.keySet();
            for (List<String> k:lists) {
                System.out.println(k+"-->" + map.get(k));
                List<String> vals = map.get(k);
                vals.add(values.get(0));
                System.out.println(k+"--->" + map.get(k));
            }

        }

        byte a = 127;
        byte b = 127;
        b += a;//+=操作符会进行隐式自动类型转换，此处a+=b隐式的将加操作的结果类型强制转换为持有结果的类型，而a=a+b则不会自动进行类型转换。
//        b = b+a;//会报错，

        Short c = 1;
//        c = c+1; //会报错
        int d = c+1;
    }
}
