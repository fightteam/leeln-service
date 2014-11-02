package org.fightteam.leeln.core;

/**
 * 用户基础类
 *
 * @author oyach
 * @since 0.0.1
 */
public class User {
    /**
     * 主键
     */
    private Long id;

    private String username;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
