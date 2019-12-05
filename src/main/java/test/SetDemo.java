package test;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by deweydu
 * Date on 2019/11/29 11:31
 */
public class SetDemo {
    public static void main(String[] args) {
        Set<String> linkedHashSet = new LinkedHashSet();
        linkedHashSet.add("dewey1");
        linkedHashSet.add("dewey2");
        linkedHashSet.add("dewey3");
        linkedHashSet.add("dewey3");
        linkedHashSet.add("dewey4");
        Iterator iterator = linkedHashSet.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }
}
