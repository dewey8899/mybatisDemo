package test.design.patterns.proxy;

/**
 * Created by deweydu
 * Date on 2019/12/5 16:51
 */
public class Landlord implements Person{
    public void rent() {
        System.out.println("客官请进，我家的房子又大又便宜，来租我的吧...");
    }
}
