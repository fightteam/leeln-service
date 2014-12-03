package org.fightteam.leeln.provider;

import org.fightteam.leeln.proto.UserServiceProto;
import org.fightteam.leeln.rpc.client.ClientBlockingRpcChannel;
import org.fightteam.leeln.rpc.client.ClientRpcChannel;
import org.junit.Test;

public class UserSerivceProviderTest {



    @Test
    public void testFindByUsername() throws Exception {
        UserServiceProto.UserService.Interface service = UserServiceProto.UserService.newStub(new ClientRpcChannel());

    }

    @Test
    public void testFindByUsernameBlocking() throws Exception {
        UserServiceProto.UserService.BlockingInterface blockingInterface = UserServiceProto.UserService.newBlockingStub(new ClientBlockingRpcChannel());

    }

    @Test
    public void testFindByUsername1() throws Exception {

    }
}