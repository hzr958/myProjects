package com.smate.center.batch.service.emailsimplify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 
 * 邮件发送工厂
 * 
 * @author zk
 * 
 */
@Service("emailFactory")
public class EmailFactory implements BeanFactoryAware, EmailSimplify {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  // bean工厂
  private BeanFactory beanFactory;

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

  @Override
  public void syncEmailInfo(Object... params) {
    Assert.notEmpty(params);
    try {
      // params[0]为想到得到的bean对象
      EmailSimplify targetService = beanFactory.getBean(params[0].toString(), EmailSimplify.class);
      if (targetService != null) {// 调用传进来的bean的具体实现
        targetService.syncEmailInfo(params);
      }
    } catch (Exception e) {
      logger.error("邮件同步工厂,生成bean对象出错,bean=" + params[0], e);
    }
  }
}
