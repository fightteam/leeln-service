package org.fightteam.leeln.rpc.annotation;

import com.google.protobuf.Service;

import java.lang.annotation.*;

/**
 * description
 *
 * @author oyach
 * @since 0.0.1
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcServiceProvider {

    String serviceName() default "";

    String blockingServiceName() default "";

    ServiceType[] serviceType() default {ServiceType.ASYN, ServiceType.SYNC};

    Class<? extends Service> service();

    public enum ServiceType {
         ASYN, SYNC
    }
}
