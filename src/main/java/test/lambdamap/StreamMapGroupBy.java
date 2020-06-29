package test.lambdamap;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by deweydu
 * Date on 2019/9/29 14:25
 */
public class StreamMapGroupBy {
    public static void main(String[] args) {
        List<Item> items = Arrays.asList(
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 20, new BigDecimal("19.99")),
                new Item("orang", 10, new BigDecimal("29.99")),
                new Item("watermelon", 10, new BigDecimal("29.99")),
                new Item("papaya", 20, new BigDecimal("9.99")),
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 10, new BigDecimal("19.99")),
                new Item("apple", 10, new BigDecimal("9.99"))
        );

        //根据名字分组计数
        Map<String, Long> counting = items.stream().collect(Collectors.groupingBy(Item::getName, Collectors.counting()));
        System.out.println(counting);
        //统计累加
        Map<String, Integer> sum = items.stream().collect(Collectors.groupingBy(Item::getName, Collectors.summingInt(Item::getQty)));
        System.out.println(sum);
//        Map<String, Double> priceSum = items.stream().collect(Collectors.groupingBy(Item::getName, Collectors.summingDouble(Item::getPrice)));
//        System.out.println(priceSum);
        Map<String, BigDecimal> associationSum = items.stream()
                .collect(Collectors.groupingBy(p->p.getName()+"|"+p.getQty()+"|"+p.getPrice(),
                        CollectorsUtils.summingBigDecimal(Item::getPrice)));
        System.out.println(associationSum);
        Short i = 1;
        System.out.println(i.equals((short)1));
    }
}
