package test.design.patterns.builder;

/**
 * Created by deweydu
 * Date on 2019/12/6 17:00
 */
public class MacBookProBuilder extends ComputerBuilder {
    @Override
    MacBookProBuilder buildMemory() {
        comp.setMemory("16g");
        return this;
    }

    @Override
    MacBookProBuilder buildCPU() {
        comp.setCPU("i7");
        return this;
    }

    @Override
    MacBookProBuilder buildHardDisk() {
        comp.setHardDisk("1t");
        return this;
    }
}
