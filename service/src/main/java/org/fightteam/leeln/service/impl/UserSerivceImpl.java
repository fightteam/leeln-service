package org.fightteam.leeln.service.impl;

import org.fightteam.leeln.core.domain.User;
import org.fightteam.leeln.service.UserService;
import org.springframework.stereotype.Service;

/**
 * description
 *
 * @author oyach
 * @since 0.0.1
 */
@Service
public class UserSerivceImpl implements UserService {


    @Override
    public User getUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setNickname("欧阳澄泓");
        return user;
    }
}
