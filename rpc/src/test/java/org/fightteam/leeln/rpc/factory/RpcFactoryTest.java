package org.fightteam.leeln.rpc.factory;

import com.google.protobuf.Service;
import org.fightteam.leeln.proto.UserServiceProto;
import org.fightteam.leeln.provider.UserSerivceProvider;
import org.fightteam.leeln.test.AppConfigTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;

public class RpcFactoryTest extends AppConfigTest {

    @Autowired
    private GenericApplicationContext genericApplicationContext;

    private ClassPathScanningCandidateComponentProvider scanner;

    @Test
    public void test01() throws Exception {
        Object obj = genericApplicationContext.getBean("userSerivceProviderBlocking");
        System.out.println(obj);

        Class<? extends Service> serviceClass = UserServiceProto.UserService.class;

        String serviceShortName = ClassUtils.getShortName(serviceClass);
        System.out.println(serviceShortName);
        Class<?>[] classes = ClassUtils.getAllInterfaces(obj);
        Class<?> serviceInterface = null;
        Class<?> blockingServiceInterface = null;

        for (Class clazz : classes){
            System.out.println(ClassUtils.getShortName(clazz));
            if (ClassUtils.getShortName(clazz).equals(serviceShortName + ".Interface")){
                serviceInterface = clazz;
            }else if (ClassUtils.getShortName(clazz).equals(serviceShortName + ".BlockingInterface")){
                blockingServiceInterface = clazz;
            }
            System.out.println(clazz);
        }
        Method method = ClassUtils.getStaticMethod(serviceClass, "newReflectiveService", serviceInterface);
        System.out.println(method);
    }

    @Test
    public void test02() throws Exception {

//        UserServiceProto.UserService.newReflectiveBlockingService()
        Class<? extends Service> serviceClass = UserServiceProto.UserService.class;


        Method method = ClassUtils.getStaticMethod(serviceClass, "newReflectiveService", UserServiceProto.UserService.Interface.class);


        Object service = method.invoke(null, new UserSerivceProvider());
        System.out.println(service);
    }
}