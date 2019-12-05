package test.design.patterns.proxy;

/**
 * Created by deweydu
 * Date on 2019/12/5 16:51
 */
public class Agent implements Person {
    Person landlord;

    public Agent(Person landlord) {
        this.landlord = landlord;
    }
    @Override
    public void rent() {
        //前置处理
        System.out.println("经过前期调研，西湖边的房子环境挺好的...");
        //委托真实角色处理
        landlord.rent();
        //后置处理
        System.out.println("房子漏水，帮你联系维修人员...");
    }
}
