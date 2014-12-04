package org.fightteam.leeln.rpc.factory;

import com.google.protobuf.BlockingService;
import com.google.protobuf.Service;
import org.fightteam.leeln.core.exception.InterfaceNotFoundException;
import org.fightteam.leeln.proto.UserServiceProto;
import org.fightteam.leeln.provider.UserSerivceProvider;
import org.fightteam.leeln.rpc.annotation.RpcServiceProvider;
import org.fightteam.leeln.rpc.context.InMemoryRpcContext;
import org.fightteam.leeln.rpc.context.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * RPC 创建工厂 单例
 * <p/>
 * 动态注册提供RPC的类，提供扩展接口方便监测等等使用。
 *
 * @author oyach
 * @since 0.0.1
 */
public class RpcFactory {
    private final static Logger logger = LoggerFactory.getLogger(RpcFactory.class);

    private static RpcFactory ourInstance = null;

    private RpcContext rpcContext;

    private DefaultListableBeanFactory factory;

    private String basePackage;

    private ClassPathScanningCandidateComponentProvider scanner;

    public static RpcFactory getInstance(DefaultListableBeanFactory factory, String basePackage)throws Exception{
        if (ourInstance != null) {
            return ourInstance;
        }
        ourInstance = new RpcFactory(factory, basePackage);
        return ourInstance;
    }

    private RpcFactory(DefaultListableBeanFactory factory, String basePackage) throws Exception {

        this.factory = factory;
        this.basePackage = basePackage;
        rpcContext = new InMemoryRpcContext();
        // 最后吧rpccontext 注册入spring
        factory.registerSingleton(StringUtils.uncapitalize(ClassUtils.getShortName(rpcContext.getClass())), rpcContext);


        // 使用spring的注解扫描类   让其不扫描spring的默认注解
        scanner = new ClassPathScanningCandidateComponentProvider(false);

        scanner.addIncludeFilter(new AnnotationTypeFilter(RpcServiceProvider.class));

        for (BeanDefinition beanDefinition : scanner.findCandidateComponents(basePackage)){

            BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(beanDefinition.getBeanClassName());
            AbstractBeanDefinition abstractBeanDefinition = builder.getBeanDefinition();
            Class<?> clazz = null;
            try {
                clazz = abstractBeanDefinition.resolveBeanClass(ClassUtils.getDefaultClassLoader());
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage());
            }
            RpcServiceProvider provider = AnnotationUtils.findAnnotation(clazz, RpcServiceProvider.class);

            RpcServiceProvider.ServiceType[] serviceTypes = provider.serviceType();

            Class<? extends Service> serviceClass = provider.service();

            String shortName = StringUtils.uncapitalize(ClassUtils.getShortName(clazz));



            for (RpcServiceProvider.ServiceType serviceType : serviceTypes){
                String beanName = null;
                if(serviceType == RpcServiceProvider.ServiceType.ASYN){
                    beanName = provider.serviceName();
                    if (StringUtils.isEmpty(beanName)){
                        beanName = shortName;
                    }

                    factory.registerBeanDefinition(beanName, builder.getBeanDefinition());
                    Object bean = factory.getBean(beanName);

                    Service service = buildSerivce(bean, serviceClass);

                    rpcContext.registerService(service);
                }else if (serviceType == RpcServiceProvider.ServiceType.SYNC){
                    beanName = provider.blockingServiceName();
                    if (StringUtils.isEmpty(beanName)){
                        beanName = shortName + "Blocking";
                    }

                    factory.registerBeanDefinition(beanName, builder.getBeanDefinition());
                    Object bean = factory.getBean(beanName);

                    BlockingService blockingService = buildBlockingSerivce(bean, serviceClass);

                    rpcContext.registerBlockingService(blockingService);
                }


            }


        }



    }


    private Service buildSerivce(Object obj, Class<? extends Service> serviceClass) throws InvocationTargetException,
            IllegalAccessException, InterfaceNotFoundException {
        String serviceShortName = ClassUtils.getShortName(serviceClass);
        Class<?>[] classes = ClassUtils.getAllInterfaces(obj);
        Class<?> serviceInterface = null;

        for (Class clazz : classes){
            if (ClassUtils.getShortName(clazz).equals(serviceShortName + ".Interface")){
                serviceInterface = clazz;
            }
        }

        if (serviceInterface == null){
            throw new InterfaceNotFoundException("Service provider should implement interface");
        }


        Method method = ClassUtils.getStaticMethod(serviceClass, "newReflectiveService", serviceInterface);

        return (Service) method.invoke(null, obj);
    }

    private BlockingService buildBlockingSerivce(Object obj, Class<? extends Service> serviceClass) throws InvocationTargetException,
            IllegalAccessException, InterfaceNotFoundException {
        String serviceShortName = ClassUtils.getShortName(serviceClass);
        Class<?>[] classes = ClassUtils.getAllInterfaces(obj);

        Class<?> blockingServiceInterface = null;

        for (Class clazz : classes){
            if (ClassUtils.getShortName(clazz).equals(serviceShortName + ".BlockingInterface")){
                blockingServiceInterface = clazz;
            }
        }

        if (blockingServiceInterface == null){
            throw new InterfaceNotFoundException("Service provider should implement interface");
        }


        Method method = ClassUtils.getStaticMethod(serviceClass, "newReflectiveBlockingService", blockingServiceInterface);

        return (BlockingService) method.invoke(null, obj);
    }
}
