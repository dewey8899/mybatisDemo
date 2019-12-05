package test.design.patterns.wrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by deweydu
 * Date on 2019/12/5 14:42
 */
@Slf4j
public class SuperMan2 extends SuperMan {
    public void action() {
        super.action();
        power();
    }
    private void power(){
        log.info("无穷力量!");
    }
}
