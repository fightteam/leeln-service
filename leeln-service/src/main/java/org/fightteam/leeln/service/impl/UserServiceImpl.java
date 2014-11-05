package org.fightteam.leeln.service.impl;

import org.fightteam.leeln.core.User;
import org.fightteam.leeln.repository.UserRepository;
import org.fightteam.leeln.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户业务逻辑实现类
 *
 * 默认不开启事务
 *
 * @author oych
 * @since 0.0.1
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;


    @Override
    public User loadUserByUsername(String username) {
        return userRepository.findOne(username);
    }

    private void pb(){
        //UserRequ
    }
}
