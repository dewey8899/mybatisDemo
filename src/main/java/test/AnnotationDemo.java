package test;

import com.system.Application;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import test.design.patterns.proxy.Person;

/**
 * Created by deweydu
 * Date on 2019/12/9 13:49
 */
@RunWith(SpringRunner.class)
@SpringBootTest (classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ContextHierarchy(@ContextConfiguration())
public class AnnotationDemo {
    @Autowired
    Person person;
    @Test
    public void person(){
        System.out.println(person.toString());
    }
    @Before
    public void init() {
        System.out.println("开始测试-----------------");
    }

    @After
    public void after() {
        System.out.println("测试结束-----------------");
    }
}
