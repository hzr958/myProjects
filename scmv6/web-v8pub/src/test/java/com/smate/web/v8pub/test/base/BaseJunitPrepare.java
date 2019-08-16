package com.smate.web.v8pub.test.base;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class BaseJunitPrepare {

  @BeforeAll
  public static void loadProperties() throws IOException {
    System.out.println("开始加载properties文件");
    JunitPropertiesUtils.loadProperties();
  }

  @Test
  public void testPropertiesLoad() {
    System.out.println("开始测试");
    System.out.println("------------" + JunitPropertiesUtils.JUNIT_PROPERTIES.get("junit_domain") + "-----------");
    System.out.println("------------" + JunitPropertiesUtils.JUNIT_PROPERTIES.get("junit_domainMobile") + "-----------");
  }
}
