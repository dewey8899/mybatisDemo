package test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by deweydu
 * Date on 2019/8/29 9:33
 */
@Slf4j
public class LambdaMap {

    @Test
    public void whenMap_thenOk() {
        User user2 = new User("john@gmail.com", "1234");
        String email = Optional.ofNullable(user2).map(user -> user.getEmail()).orElse("default@gmail.com");
        Assert.assertEquals(email, user2.getEmail());
        createNewUser();

    }

    private User createNewUser() {
        log.info("creating  New  User..");
        return new User();
    }
    @Test
    public void test3() {
        //这个e就代表所实现的接口的方法的参数，
        Consumer<String> consumer = e->System.out.println("ghijhkhi"+e);
        consumer.accept("woojopj");
    }
    @Test
    public void test6() {
        Supplier<String> supplier = ()->"532323".substring(0, 2);
        System.out.println(supplier.get());
    }
    @Test
    public void test7() {
        Function<String, String> function = (x)->x.substring(0, 2);
        System.out.println(function.apply("我是中国人"));
    }
    @Test
    public void test8() {
        Predicate<String> predicate = (x)->x.length()>5;
        System.out.println(predicate.test("12345678"));
        System.out.println(predicate.test("123"));
    }


}
