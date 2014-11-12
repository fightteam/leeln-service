package org.fightteam.leeln.rpc.service;

import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessage;
import org.fightteam.leeln.test.AppConfigTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class RpcServiceTest extends AppConfigTest {

    @Autowired
    private RpcService rpcService;

    public final static String service_full_name = ".org.fightteam.leeln.pb.UserService";
    public final static String method_full_name = ".org.fightteam.leeln.pb.UserService.getUser";


    @Test
    public void testGetServiceDescriptorByFullName() throws Exception {
        rpcService.getServiceDescriptorByFullName(service_full_name);
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
        Descriptors.MethodDescriptor methodDescriptor = rpcService.getMethodDescriptorByFullName(method_full_name);

        Descriptors.Descriptor descriptor = methodDescriptor.getInputType();
        System.out.println(descriptor.getFullName());
        GeneratedMessage.Builder builder = descriptor.toProto().toBuilder();
        //builder.mergeFrom()

    }
}