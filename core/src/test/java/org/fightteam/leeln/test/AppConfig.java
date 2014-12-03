package org.fightteam.leeln.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 程序配置
 *
 * @author oych
 * @since 0.0.1
 */
@Configuration
@PropertySource(value = "classpath:properties/app.properties")
//@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
@ComponentScan(basePackages = "org.fightteam.leeln")
public class AppConfig {
    private final static Logger log = LoggerFactory.getLogger(AppConfig.class);


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
