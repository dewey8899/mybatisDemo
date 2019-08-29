package test;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

/**
 * Created by deweydu
 * Date on 2019/8/29 9:33
 */
public class LambdaMap {

    @Test
    public void whenMap_thenOk(){
        User user2 = new User("john@gmail.com","1234");
        String email = Optional.ofNullable(user2).map(user -> user.getEmail()).orElse("default@gmail.com");
        Assert.assertEquals(email,user2.getEmail());
    }
}
