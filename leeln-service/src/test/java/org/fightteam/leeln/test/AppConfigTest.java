package org.fightteam.leeln.test;

import org.fightteam.leeln.config.AppConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 测试主体配置类
 *
 * 如果要对业务逻辑，或者依赖于业务逻辑进行单元测试，那么需要继承该类。
 *
 * @author oych
 * @since 0.0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public abstract class AppConfigTest {

}
