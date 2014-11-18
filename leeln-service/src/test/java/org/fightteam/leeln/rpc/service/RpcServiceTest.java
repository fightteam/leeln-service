package org.fightteam.leeln.rpc.service;

import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessage;
import org.fightteam.leeln.proto.RpcProto;
import org.fightteam.leeln.proto.UserServiceProto;
import org.fightteam.leeln.test.AppConfigTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class RpcServiceTest extends AppConfigTest {



    public final static String service_full_name = ".org.fightteam.leeln.pb.UserService";
    public final static String method_full_name = ".org.fightteam.leeln.pb.UserService.getUser";


    @Test
    public void testGetServiceDescriptorByFullName() throws Exception {
        System.out.println(RpcProto.RpcRequest.getDescriptor().getFullName());
        System.out.println(UserServiceProto.UserService.getDescriptor().getFullName());
    }

    @Test
    public void testCallMethod() throws Exception {

    }

    @Test
    public void testGetRequestPrototype() throws Exception {

    }

    @Test
    public void testGetResponsePrototype() throws Exception {

    }

    @Test
    public void testGetMethodDescriptorByFullName() throws Exception {

    }
}