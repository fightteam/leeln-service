package org.fightteam.leeln.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;


/**
 * 程序入口
 *
 * @author oych
 * @since 0.0.1
 */
public class Main {

    public static void main(String[] args) {
        AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(
                AppConfig.class);
        ctx.registerShutdownHook();


    }
}
