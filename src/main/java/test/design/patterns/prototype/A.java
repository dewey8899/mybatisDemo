package test.design.patterns.prototype;

import lombok.Data;

/**
 * Created by deweydu
 * Date on 2019/12/6 16:21
 */
@Data
public class A {
    private int a;
}
@Data
class B implements Cloneable{
    private int b;
    private A a;
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    public static void main(String[] args) throws CloneNotSupportedException {
        A a = new A();        a.setA(1);
        B b = new B();        b.setA(a);        b.setB(2);
        B b2 = (B)b.clone();
        System.out.println("b-->" + b);
        System.out.println("b2-->" + b2);
        b2.setB(3);
        System.out.println("b-->" + b);
        System.out.println("b2-->" + b2);
        b2.getA().setA(10);
        System.out.println("b-->" + b);
        System.out.println("b2-->" + b2);
    }
}
