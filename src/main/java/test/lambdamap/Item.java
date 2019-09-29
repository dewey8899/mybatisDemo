package test.lambdamap;

import lombok.Data;

/**
 * Created by deweydu
 * Date on 2019/9/29 14:25
 */
@Data
public class Item {
    private String name;
    private int qty;
    private double price;
    public Item(String name,int qty,double price){
        this.name = name;
        this.qty = qty;
        this.price = price;
    }
}
