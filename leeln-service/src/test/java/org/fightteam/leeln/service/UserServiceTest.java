package org.fightteam.leeln.service;

import org.fightteam.leeln.rpc.annotation.Remote;
import org.fightteam.leeln.test.AppConfigTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ResourceLoader;

import java.util.Map;

public class UserServiceTest extends AppConfigTest {


    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    public void testLoadUser() throws Exception {

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
    public void test03() throws Exception {
        String a = this.getClass().getCanonicalName();
        System.out.println(a);
    }
}