package com.smate.center.job.framework.support;

import com.smate.center.job.framework.util.BeanPropertyParser;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 动态注册bean
 *
 * @author houchuanjie
 * @date 2018/05/18 17:31
 */
@Configuration
public class BeanConfig {

  @Value("${zookeeper.url}")
  private String zookeeperUrl;
  @Value("${RUN_ENV}")
  private String runEnv;
  @Value("${zookeeper.namespace}")
  private String zookeeperNamespace;

  @Bean("curatorFramework")
  public CuratorFramework getCuratorFramework() {
    String envNamesapce = zookeeperNamespace + "/" + runEnv.toLowerCase();
    CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString(zookeeperUrl)
        .namespace(envNamesapce)
        .connectionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(3000, 3)).build();
    injectCuratorFramework(curatorFramework);
    return curatorFramework;
  }

  private void injectCuratorFramework(CuratorFramework curatorFramework) {
    ZKFactoryBean.setZKClient(curatorFramework);
  }


  /**
   * 注册一个属性转换器，注册此bean只是为了能够加载Spring容器中定义的 PropertyEditor、ConversionService等
   * 转换器工具，在实际使用时，请重新new此对象，不要使用这个bean，以防止出 现并发引起的线程安全问题。
   *
   * @return
   */
  @Bean
  public BeanPropertyParser getBeanPropertyParser() {
    return new BeanPropertyParser(Object.class);
  }
}
