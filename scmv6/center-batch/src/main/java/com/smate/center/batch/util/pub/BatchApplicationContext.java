package com.smate.center.batch.util.pub;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class BatchApplicationContext implements ApplicationContextAware {
  private static ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    BatchApplicationContext.applicationContext = applicationContext;
  }

  public static ApplicationContext getApplicationContext() {
    return BatchApplicationContext.applicationContext;
  }

}
