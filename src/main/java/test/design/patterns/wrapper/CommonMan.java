package test.design.patterns.wrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by deweydu
 * Date on 2019/12/5 14:41
 */
@Slf4j
public class CommonMan implements Man {
    @Override
    public void action() {
        log.info("I can walk..run.");
    }
}
