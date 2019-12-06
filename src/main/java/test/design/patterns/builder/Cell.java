package test.design.patterns.builder;

/**
 * Created by deweydu
 * Date on 2019/12/6 17:15
 */
public class Cell {
    ComputerBuilder computerBuilder;
    Computer sell(){
        return computerBuilder.buildCPU().buildHardDisk().buildMemory().getComputer();
    }

    public static void main(String[] args) {
        Cell cell = new Cell();
        cell.computerBuilder = new MacBookProBuilder();
        System.out.println(cell.sell());
    }
}
