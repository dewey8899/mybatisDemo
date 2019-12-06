package test.design.patterns.builder;

/**
 * Created by deweydu
 * Date on 2019/12/6 16:58
 */
public abstract class ComputerBuilder {
    protected Computer comp = new Computer();
    abstract ComputerBuilder buildMemory();
    abstract ComputerBuilder buildCPU();
    abstract ComputerBuilder buildHardDisk();
    protected Computer getComputer(){
        return comp;
    }
}
