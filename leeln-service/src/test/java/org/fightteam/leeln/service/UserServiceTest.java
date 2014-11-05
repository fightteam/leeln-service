package org.fightteam.leeln.service;

import org.fightteam.leeln.test.AppConfigTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends AppConfigTest {

    @Autowired
    private UserService userService;

    @Test
    public void testLoadUser() throws Exception {
        System.out.println(userService.loadUserByUsername("faith"));
    }
}