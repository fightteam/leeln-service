package org.fightteam.leeln.repository;

import org.apache.ibatis.annotations.*;
import org.fightteam.leeln.core.domain.User;


import java.util.List;

/**
 * 用户数据对外接口
 *
 * @author oyach
 * @since 0.0.1
 */
public interface UserRepository {

    /**
     * 根据username查询一个用户对象
     *
     * @param username 用户的账号
     * @return 用户对象
     */
    @Select("select * from user where username = #{username}")
    @Options(useCache = true, flushCache = true)
    User findByUsername(@Param("username") String username);

    /**
     * 获取全部用户
     *
     * @return 全部用户
     */
    @Select("select * from user")
    @Options(useCache = true, flushCache = true)
    List<User> findAll();

    /**
     * 根据用户昵称获取对象
     *
     * @param nickname 用户昵称
     * @return 该用户昵称的对象
     */
    @Select("select * from user where nickname = #{nickname}")
    @Options(useCache = true, flushCache = true)
    List<User> findByNickname(@Param("nickname") String nickname);

    /**
     * 根据id 获取用户对象
     *
     * @param id 用户id
     * @return 用户对象
     */
    @Select("select * from user where id = #{id}")
    @Options(useCache = true, flushCache = true)
    User findById(@Param("id") long id);

    /**
     * 增加一个用户，不用包括id  自动增长
     *
     * @param user 用户信息
     * @return 返回影响的行数
     */
    @Insert("insert into user (username, nickname) values(#{username},#{nickname})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    long save(User user);

    /**
     * 更新用户操作
     *
     * @param user 用户信息，必须包含id
     * @return 返回影响的行数
     */
    @Update("update user set nickname = #{nickname}, username = #{username}")
    long update(User user);

    /**
     * 根据id删除一个对象
     * @param id 用户主键
     * @return 删除影响的行数
     */
    @Delete("delete from user where id = #{id}")
    long delete(long id);

    /**
     * 统计该表数据条数
     *
     * @return 数据条数
     */
    @Select("select count(*) from user")
    long count();

    /**
     * 删除所有数据 危险操作 注意
     *
     * @return 删除的行数
     */
    @Delete("delete from user")
    long deleteAll();
}
