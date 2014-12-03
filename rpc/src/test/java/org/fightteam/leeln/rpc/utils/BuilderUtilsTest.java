package org.fightteam.leeln.rpc.utils;

import org.fightteam.leeln.core.domain.User;
import org.fightteam.leeln.proto.UserServiceProto;
import org.junit.Test;

public class BuilderUtilsTest {

    @Test
    public void testBuilder() throws Exception {
        User user = new User();
        user.setId(3L);
        user.setUsername("oyach");

        UserServiceProto.UserMsg userMsg = BuilderUtils.builder(UserServiceProto.UserMsg.getDefaultInstance(), user);

        System.out.println(userMsg.getId());
        System.out.println(userMsg.getUsername());
        System.out.println(userMsg.getNickname());
        System.out.println(userMsg.isInitialized());
    }

    @Test
    public void test02() throws Exception {

        UserServiceProto.UserMsg userMsg = UserServiceProto.UserMsg.getDefaultInstance();

        System.out.println(userMsg.getId());
        System.out.println(userMsg.getUsername());
        System.out.println(userMsg.getNickname());
        System.out.println(userMsg.isInitialized());
        UserServiceProto.UserMsg.Builder builder = userMsg.toBuilder();

        builder.setId(3L);

        builder.setUsername("oyach");

        userMsg = builder.build();

        System.out.println("========");

        System.out.println(userMsg.getId());
        System.out.println(userMsg.getUsername());
        System.out.println(userMsg.getNickname());
        System.out.println(userMsg.isInitialized());
    }
}