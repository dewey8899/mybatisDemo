package test;

import com.system.exception.BusinessException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;

import java.util.Optional;

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
        Optional.ofNullable(user).ifPresent(na->System.out.println(na+"ifPresent"));
        //如果user2不是空，则执行lambda表达式
        Optional.ofNullable(user2).ifPresent(na->{
            System.out.println(na+" - > user2");
        });


    }

    @Test
    public void whenEmptyValue_thenReturnDefault(){
        User user = null;
        User user2=new User("anna@gmail.com","1234");
        User result = Optional.ofNullable(user).orElse(user2);
        User result2 = Optional.ofNullable(user).orElseGet(()->user2);
        assertEquals(user2.getEmail(),result.getEmail());
        System.out.println(true);
        System.out.println(result2);
    }

    @Test
    public void givenPresentValue_whenCompare_thenOk(){
        User user2=new User("john@gmail.com","1234");
        log.info("Using orElse");
        User result = Optional.ofNullable(user2).orElse(createNewUser());
        log.info("Using orElseGet");
        User result2 = Optional.ofNullable(user2).orElseGet(()->createNewUser());
    }

    @Test
    public void orElseThrowble(){
        User user2 = null;
        User result = Optional.ofNullable(user2).orElseThrow(()->new BusinessException("数据为空"));
    }
    private User createNewUser(){
        log.info("creating  New  User..");
        return new User();
    }
}
@Data
class User{
    private String email;
    private String username;
    public User(){

    }
    public User(String email,String username){
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
