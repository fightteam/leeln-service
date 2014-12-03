package org.fightteam.leeln.service;

import org.junit.Test;

import org.fightteam.leeln.core.domain.User;
import org.fightteam.leeln.test.ServiceTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;


public class UserServiceTest extends ServiceTestConfig {

    @Autowired
    private UserService userService;

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void testGetUserNoAuth() {
        User oyach = userService.getUser("oyach");
        System.out.println(oyach.getNickname());
    }

    @Test(expected = AccessDeniedException.class)
    public void testGetUserAccessDenied() {
        login("admin", "123456");
        User oyach = userService.getUser("oyach");
        System.out.println(oyach.getNickname());

    }

    @Test
    public void testGetUser() throws Exception {
        login("oyach", "123456");
        User oyach = userService.getUser("oyach");
        System.out.println(oyach.getNickname());
    }
}