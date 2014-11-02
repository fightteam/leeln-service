package org.fightteam.leeln.config;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

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


    @Configuration
    @MapperScan("org.fightteam.leeln.repository")
    public static class DataSourceConfig {

        public static final String MYSQL_DRIVER_NAME = "com.mysql.jdbc.Driver";
        public static final String ORACLE_DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";
        public static final String HSQL_DRIVER_NAME = "org.hsqldb.jdbc.JDBCDriver";
        public static final String H2_DRIVER_NAME = "org.h2.Driver";


        @Value("${jdbc.url}")
        private String jdbcUrl;

        @Value("${jdbc.username}")
        private String jdbcUsername;

        @Value("${jdbc.password}")
        private String jdbcPassword;

        @Autowired
        private Environment env;


        /**
         * 数据源
         *
         * @return 数据源实例
         */
        @Bean
        public DataSource dataSource() {
            PoolProperties poolProperties = new PoolProperties();
            poolProperties.setUrl(jdbcUrl);
            poolProperties.setDriverClassName(isWhatDriverName(jdbcUrl));
            poolProperties.setUsername(jdbcUsername);
            poolProperties.setPassword(jdbcPassword);

            org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
            dataSource.setPoolProperties(poolProperties);

            return dataSource;
        }


        @Bean
        public SqlSessionFactoryBean sqlSessionFactory() throws Exception {
            SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
            sessionFactory.setDataSource(dataSource());
            sessionFactory.setTypeAliasesPackage("org.fightteam.leeln.core");
            return sessionFactory;
        }


        @Bean(name = "transactionManager")
        public DataSourceTransactionManager transactionManager() {
            DataSourceTransactionManager txManager = new DataSourceTransactionManager();
            txManager.setDataSource(dataSource());
            return txManager;
        }


        private String isWhatDriverName(String jdbcUrl) {
            if (jdbcUrl == null) {
                throw new NullPointerException("Database url null");

            }
            // 根据jdbc url判断驱动，注意只有包括了这个驱动包才会正常
            if (jdbcUrl.contains(":h2:")) {
                return H2_DRIVER_NAME;
            } else if (jdbcUrl.contains(":mysql:")) {
                return MYSQL_DRIVER_NAME;
            } else if (jdbcUrl.contains(":oracle:")) {
                return ORACLE_DRIVER_NAME;
            } else if (jdbcUrl.contains(":hsqldb:")) {
                return HSQL_DRIVER_NAME;
            } else {
                throw new IllegalArgumentException("Unknown Database of " + jdbcUrl);
            }
        }
    }

}
