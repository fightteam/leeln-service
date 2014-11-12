package org.fightteam.leeln.service;

import org.fightteam.leeln.core.User;
import org.fightteam.leeln.rpc.annotation.Remote;

/**
 * 用户业务逻辑类
 *
 * @author oych
 * @since 0.0.1
 */
@Remote
public interface UserService {

    /**
     * 根据username查找对象
     *
     * @param username 用户账号
     * @return 有返回对象 否则为null
     */
    User loadUserByUsername(String username);


}
