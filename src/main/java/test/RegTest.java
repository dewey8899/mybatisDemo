package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by deweydu
 * Date on 2019/6/28
 * function：正则表达式
 */
public class RegTest {
    public static void main(String[] args) {
        String pattern = "^(0|[1-9][0-9]*)$";//大于等于0的数
        String dot = "^0\\.\\d*[1-9]\\d*$";//正浮点数
        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher("10234");
        Pattern compiledot = Pattern.compile(dot);
        Matcher matcherdot = compiledot.matcher("1.00");
        System.out.println(matcherdot.matches());
    }
}
