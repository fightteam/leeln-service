package org.fightteam.leeln.repository;

import org.fightteam.leeln.core.domain.User;
import org.fightteam.leeln.test.RepositoryTestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;

/**
 * description
 *
 * @author oych
 * @since 0.0.1
 */
public class UserRepositoryTestConfig extends RepositoryTestConfig {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DataSourceInitializer dataSourceInitializer;


    @Before
    public void setUp() throws Exception {


        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        dataSourceInitializer.setDatabasePopulator(databasePopulator);

        databasePopulator.addScript(new ClassPathResource("sql/data.sql"));

        dataSourceInitializer.setDatabasePopulator(databasePopulator);
        dataSourceInitializer.setEnabled(true);
        dataSourceInitializer.afterPropertiesSet();

    }

    @Test
    public void testSave() throws Exception {
        User user = new User();
        user.setUsername("faith");
        user.setNickname("欧阳澄泓");
        long rowNum = userRepository.save(user);
        Assert.assertNotEquals(0L, rowNum);

    }

    @Test
    public void testUpdate() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("oyach2");
        userRepository.update(user);

        User result = userRepository.findByUsername("oyach2");
        Assert.assertNotNull(result);
    }

    @Test
    public void testFindOne() throws Exception {
        User user = userRepository.findByUsername("oyach");
        Assert.assertNotNull(user);
    }

    @Test
    public void testDelete() throws Exception {
        userRepository.delete(1L);
        User user = userRepository.findByUsername("oyach");
        Assert.assertNull(user);
    }


    @Test
    public void testFindByNickname() throws Exception {
        User user = new User();
        user.setUsername("oyach2");
        user.setNickname("欧阳澄泓");
        userRepository.save(user);

        List<User> results = userRepository.findByNickname("欧阳澄泓");

        Assert.assertNotEquals(0, results.size());

    }

    @Test
    public void testFindAll() throws Exception {

        User user = new User();
        user.setUsername("oyach2");
        user.setNickname("欧阳澄泓");
        userRepository.save(user);
        List<User> users = userRepository.findAll();
        Assert.assertNotEquals(0, users.size());
    }
}
