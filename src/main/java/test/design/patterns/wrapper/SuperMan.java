package test.design.patterns.wrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by deweydu
 * Date on 2019/12/5 14:42
 */
@Slf4j
public class SuperMan extends CommonMan {
    public void action() {
        super.action();
        fly();
    }
    private void fly(){
        log.info("我会飞...");
    }
}
