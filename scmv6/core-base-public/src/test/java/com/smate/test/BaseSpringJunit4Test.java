package com.smate.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Spring Junit4 测试用基类，需要依赖Spring进行测试的单元测试类都需要继承此类。<br>
 * 如果要使用事务控制，请给子类加上以下两个注解:
 * 
 * <pre>
 * &#64;Transactional 
 * &#64;TransactionConfiguration(transactionManager = "transactionManager-xxx")
 * </pre>
 * 
 * transactionManager指定要使用的事务管理器，事务管理器的配置在applicationContext-sessionFactory-xxx.xml文件中
 * 
 * @author houchuanjie
 * @date 2017年11月28日 上午9:38:49
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring/applicationContext*.xml" })
public class BaseSpringJunit4Test {
}
