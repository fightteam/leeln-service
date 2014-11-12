package org.fightteam.leeln.rpc.annotation;

import java.lang.annotation.*;

/**
 * 远程调用注解
 *
 * 只有当接口，有标识的时候，才能被调用
 *
 * @author oych
 * @since 0.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Remote {
    /**
     * 定义远程调用的名字，方便调用
     *
     * 注意:该名字必须唯一，一般采用取类名方式。
     *
     * @return 调用名字
     */
    String name() default "";

}
