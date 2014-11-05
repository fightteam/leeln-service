package org.fightteam.leeln.config;

import org.fightteam.leeln.config.DataSourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.*;
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
@Import(DataSourceConfig.class)
public class AppConfig {
    private final static Logger log = LoggerFactory.getLogger(AppConfig.class);


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
