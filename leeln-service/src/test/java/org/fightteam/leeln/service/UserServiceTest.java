package org.fightteam.leeln.service;

import com.google.protobuf.*;
import org.fightteam.leeln.proto.UserServiceProto;
import org.fightteam.leeln.rpc.controller.NettyRpcController;
import org.fightteam.leeln.service.impl.UserServiceImpl;
import org.fightteam.leeln.test.AppConfigTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends AppConfigTest {

//    @Autowired
//    private ApplicationContext applicationContext;

    // private Service userService;

    @Autowired
    private UserServiceImpl userBlockingService;

    @Autowired
    private UserServiceImpl userService;


    private Service service;

    private BlockingService blockingService;

    @Before
    public void setUp() throws Exception {
        service = UserServiceProto.UserService.newReflectiveService(userService);
        blockingService = UserServiceProto.UserService.newReflectiveBlockingService(userBlockingService);
    }


    @Test
    public void testFindByUsername() throws Exception {
        Descriptors.MethodDescriptor methodDescriptor = blockingService.getDescriptorForType().findMethodByName("findByUsername");
        RpcController controller = new NettyRpcController();

        Message methodRequest = null;

        methodRequest = blockingService.getRequestPrototype(methodDescriptor);
        methodRequest.newBuilderForType().setField(methodDescriptor.getInputType().findFieldByNumber(1), "oyach").build();


        Message response = blockingService.callBlockingMethod(methodDescriptor, controller, methodRequest);

        System.out.println(response.isInitialized());
        System.out.println(response);
    }

    @Test
    public void testFindByNickname() throws Exception {

    }

    @Test
    public void testFindAll() throws Exception {

    }

    @Test
    public void testFindById() throws Exception {

    }

    @Test
    public void testInsert() throws Exception {
        UserServiceProto.EmptyMsg emptyMsg = UserServiceProto.EmptyMsg.getDefaultInstance();
        System.out.println(emptyMsg.isInitialized());
    }

    @Test
    public void testUpdate() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {

    }
}