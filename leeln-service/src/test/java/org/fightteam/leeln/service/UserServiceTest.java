package org.fightteam.leeln.service;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Service;
import org.fightteam.leeln.pb.MethodFactory;
import org.fightteam.leeln.pb.UserServiceFactory;
import org.fightteam.leeln.rpc.MyRpcService;
import org.fightteam.leeln.rpc.annotation.Remote;
import org.fightteam.leeln.test.AppConfigTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ResourceLoader;

import java.util.List;
import java.util.Map;

public class UserServiceTest extends AppConfigTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    public void testLoadUser() throws Exception {
        System.out.println(userService.loadUserByUsername("faith"));
    }


    @Test
    public void server() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(Remote.class);

        System.out.println(beans.size());
        for (String bean : beans.keySet()) {
            System.out.println(bean);
            System.out.println(beans.get(bean));
        }
        System.out.println(resourceLoader);
    }

    @Test
    public void test01() throws Exception {
        //AnnotationUtils.
//        RpcUtil;
//        RpcController;
//
//
//        RpcChannel channel = rpcImpl.newChannel("remotehost.example.com:1234");
//        RpcController controller = rpcImpl.newController();
//        MyService service = MyService.newStub(channel);
//        service.myMethod(controller, request, callback);

        String methodName = ".org.fightteam.leeln.pb.UserService.getUser";

        Descriptors.FileDescriptor fileDescriptor = UserServiceFactory.getDescriptor();
        System.out.println(fileDescriptor.getName());
        System.out.println(fileDescriptor.getFullName());

        Descriptors.ServiceDescriptor serviceDescriptor2 = fileDescriptor.findServiceByName("UserService");
        System.out.println(serviceDescriptor2.getFullName());
        System.out.println(serviceDescriptor2.getName());

        //fileDescriptor.f
        List<Descriptors.ServiceDescriptor> serviceDescriptors = fileDescriptor.getServices();
        for (Descriptors.ServiceDescriptor serviceDescriptor : serviceDescriptors) {
            System.out.println(serviceDescriptor.getName());

            System.out.println(serviceDescriptor.getFullName());
            System.out.println(serviceDescriptor.getMethods());
            System.out.println("================");

            List<Descriptors.MethodDescriptor> methodDescriptors = serviceDescriptor.getMethods();
            for (Descriptors.MethodDescriptor methodDescriptor : methodDescriptors) {
                System.out.println(methodDescriptor.getFullName());
                System.out.println(methodDescriptor.getFile());
            }
        }


    }


    @Test
    public void test02() throws Exception {
        Descriptors.FileDescriptor fileDescriptor = MethodFactory.getDescriptor();
        List<Descriptors.FileDescriptor> fileDescriptors = fileDescriptor.getPublicDependencies();
        System.out.println("----------------");
        for (Descriptors.FileDescriptor f : fileDescriptors){
            System.out.println("++++");
            System.out.println(f.getFullName());
        }
    }

    @Test
    public void test03() throws Exception {
        String a = this.getClass().getCanonicalName();
        System.out.println(a);
    }
}