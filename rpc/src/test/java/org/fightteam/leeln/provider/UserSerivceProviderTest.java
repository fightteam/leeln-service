package org.fightteam.leeln.provider;

import com.google.protobuf.BlockingService;
import com.google.protobuf.Message;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import org.fightteam.leeln.proto.UserServiceProto;
import org.fightteam.leeln.rpc.client.ClientBlockingRpcChannel;
import org.fightteam.leeln.rpc.client.ClientRpcChannel;
import org.fightteam.leeln.rpc.client.ClientServer;
import org.fightteam.leeln.rpc.controller.NettyRpcController;
import org.fightteam.leeln.test.AppConfigTest;
import org.junit.Test;

public class UserSerivceProviderTest extends AppConfigTest {



    @Test
    public void testFindByUsername() throws Exception {
//        UserServiceProto.UserService.Interface service = UserServiceProto.UserService.newStub(new ClientRpcChannel(new ClientServer(8080)));

    }

    @Test
    public void testFindByUsernameBlocking() throws Exception {
//        UserServiceProto.UserService.BlockingInterface blockingInterface = UserServiceProto.UserService.newBlockingStub(new ClientBlockingRpcChannel(new ClientServer(8080)));
//
//        UserServiceProto.UserService.newReflectiveBlockingService(blockingInterface);
//
//        UserServiceProto.UsernameMsg usernameMsg = UserServiceProto.UsernameMsg.getDefaultInstance().toBuilder().setUsername("oyach").build();
//        blockingInterface.findByUsername(new NettyRpcController(), usernameMsg);
    }

    @Test
    public void testFindByUsername1() throws Exception {

        ClientServer clientServer = new ClientServer("127.0.0.1", 8088);
        UserServiceProto.UserService.BlockingInterface blockingInterface = UserServiceProto.UserService.newBlockingStub(new ClientBlockingRpcChannel(clientServer));


        UserServiceProto.UsernameMsg usernameMsg = UserServiceProto.UsernameMsg.getDefaultInstance().toBuilder().setUsername("oyach").build();

        UserServiceProto.UserMsg userMsg = blockingInterface.findByUsername(new NettyRpcController(), usernameMsg);

        System.out.println(userMsg.isInitialized());
    }

    @Test
    public void testFindByUsername2() throws Exception {

        ClientServer clientServer = new ClientServer("127.0.0.1", 8088);
        UserServiceProto.UserService.Stub stub = UserServiceProto.UserService.newStub(new ClientRpcChannel(clientServer));


        UserServiceProto.UsernameMsg usernameMsg = UserServiceProto.UsernameMsg.getDefaultInstance().toBuilder().setUsername("oyach").build();

        final RpcController rpcController = new NettyRpcController();
        stub.findByUsername(rpcController, usernameMsg, new RpcCallback<UserServiceProto.UserMsg>(){

            @Override
            public void run(UserServiceProto.UserMsg userMsg) {
                System.out.println(rpcController.errorText());
                System.out.println(userMsg.isInitialized());
            }
        });

    }
}