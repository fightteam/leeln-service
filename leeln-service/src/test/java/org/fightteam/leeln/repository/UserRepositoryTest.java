package org.fightteam.leeln.repository;

import org.fightteam.leeln.core.User;
import org.fightteam.leeln.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * description
 *
 * @author oych
 * @since 0.0.1
 */
public class UserRepositoryTest extends BaseTest{


    @Autowired
    private UserRepository userRepository;


    @Test
    public void test01() throws Exception {

        User user = userRepository.getUser("faith");
        System.out.println(user.getId());
    }

    @Test
    public void test02() throws Exception {
        User user = userRepository.getUser("aaa");
        System.out.println(user);

    }
}
