package test.design.patterns.wrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by deweydu
 * Date on 2019/12/5 14:42
 */
@Slf4j
public class SuperMan3 extends SuperMan2 {
    public void action() {
        super.action();
        shoot();
    }
    private void shoot(){
        System.out.println("激光发射!");
    }
}
