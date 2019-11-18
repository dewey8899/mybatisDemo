package test;

import com.system.exception.BusinessException;
import com.system.utils.BigDecimalUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Created by deweydu
 * Date on 2019/8/28 13:55
 */
@Slf4j
public class Lambda {
    public static void main(String[] args) {

        String user = null;
        String user2 = "dewey";
        //如果user是空，则不执行lambda表达式
        Optional.ofNullable(user).ifPresent(na -> System.out.println(na + "ifPresent"));
        //如果user2不是空，则执行lambda表达式
        Optional.ofNullable(user2).ifPresent(na -> {
            System.out.println(na + " - > user2");
        });
        BigDecimal multiply = BigDecimalUtils.multiply(new BigDecimal("100"), new BigDecimal("501.81"));
        BigDecimal deliveryPriceRatio = BigDecimalUtils.divide(multiply, new BigDecimal("526.21"), 2, BigDecimal.ROUND_HALF_UP);
        System.out.println(deliveryPriceRatio);
        System.out.println(deliveryPriceRatio.toString() + "%");
        System.out.println(new BigDecimal("0.00").compareTo(new BigDecimal("0.00000")) == 0);
    }

    @Test
    public void whenEmptyValue_thenReturnDefault() {
        User user = null;
        User user2 = new User("anna@gmail.com", "1234");
        User result = Optional.ofNullable(user).orElse(user2);
        User result2 = Optional.ofNullable(user).orElseGet(() -> user2);
        assertEquals(user2.getEmail(), result.getEmail());
        System.out.println(true);
        System.out.println(result2);
    }

    @Test
    public void orElse() {
        User user = null;
        User user2 = new User("john@gmail.com", "1234");
        log.info("Using orElse");
        User result = Optional.ofNullable(user).orElse(new User("dewey", "dewey"));
        User result2 = Optional.ofNullable(user2).orElse(new User("dewey", "dewey"));
        User result3 = Optional.ofNullable(user2).orElse(createNewUser());
        System.out.println(result);
        System.out.println(result2);
        log.info("Using orElseGet");
        User result4 = Optional.ofNullable(user2).orElseGet(() -> createNewUser());

    }

    @Test
    public void orElseThrowble() {
        User user2 = null;
        User result = Optional.ofNullable(user2).orElseThrow(() -> new BusinessException("数据为空"));
        System.out.println(result);
    }

    private User createNewUser() {
        log.info("creating  New  User..");
        return new User();
    }

    @Test
    public void orMethod() {
        Integer value1 = null;
        Integer value2 = 10;
        Optional<Integer> a = Optional.ofNullable(value1);
        Optional<Integer> b = Optional.of(value2);
        System.out.println(b.orElse(getDefaultValue())); // 不调用getDefaultValue
        System.out.println(a.orElse(getDefaultValue())); // 调用getDefaultValue
        System.out.println(b.orElseGet(() -> getDefaultValue())); // 不调用getDefaultValue
        System.out.println(a.orElseGet(() -> getDefaultValue())); // 调用getDefaultValue
    }

    @Test
    public void lambda() {
//        List car = new ArrayList();
//        car.add("a");
//        car.add("b");
//        car.forEach(s -> {
//            System.out.println(s);
//        });
//
//        Map<String, String> map = null;
//        map.forEach((k, v) -> {
//            System.out.println(k);
//        });
        List<User> userList = new ArrayList<>();
        userList.add(new User("123@qq.com", "dewey"));
        userList.add(new User("123456@qq.com", "456dewey"));
        Set<User> collect = userList.stream().filter(p -> p.getEmail().equals("1238@qq.com")).collect(Collectors.toSet());
        System.out.println(collect.size());
    }

    public static Integer getDefaultValue() {
        return new Integer(0);
    }
}

@Data
class User {
    private String email;
    private String username;

    public User() {

    }

    public User(String email, String username) {
        this.email = email;
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
