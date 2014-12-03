package org.fightteam.leeln.core.domain;

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
    private long id;

    private String username;

    private String nickname;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
