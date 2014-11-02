package org.fightteam.leeln.repository;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.fightteam.leeln.core.User;

/**
 * 用户数据对外接口
 *
 * @author oyach
 * @since 0.0.1
 */
public interface UserRepository {

    @Select("select * from user where username = #{username}")
    User getUser(@Param("username") String username);

}
