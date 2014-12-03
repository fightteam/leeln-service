package org.fightteam.leeln.security;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 采用aop方式来处理异常
 *
 * @author oyach
 * @since 0.0.1
 */
@Component
@Aspect
public class SecurityExceptionHandler {


    @Pointcut("execution(* org.fightteam.leeln.provider.*.*(..))")
    public void pointCutMethod() {
    }


    @AfterThrowing(pointcut = "pointCutMethod()", throwing = "e")
    public void doAfterThrowing(Exception e) {
        System.out.println("出现异常了！");
        System.out.println(e.getMessage());
    }
}
