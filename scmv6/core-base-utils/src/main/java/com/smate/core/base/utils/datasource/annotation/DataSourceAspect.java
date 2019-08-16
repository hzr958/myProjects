package com.smate.core.base.utils.datasource.annotation;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import com.smate.core.base.utils.dynamicds.CustomerContextHolder;
import com.smate.core.base.utils.dynamicds.DataSourceEnum;

/**
 * 切换数据源 切点类
 * 
 * @author tsz
 *
 */

@Aspect
@Component
public class DataSourceAspect {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PlatformTransactionManager transactionManager;

  public DataSourceAspect() {
    logger.warn("数据源注解启动成功");
  }

  /**
   * 自定义注解类
   */
  @Pointcut("@annotation(com.smate.core.base.utils.datasource.annotation.NextDataSourceAnnotation)")
  public void dataSourceAspect() {}

  /**
   * 前置操作 在操作前 切换数据源 切点
   * 
   * @param joinPoint
   * @throws Exception
   */

  @Before("dataSourceAspect()")
  public void doBefore(JoinPoint joinPoint) throws Exception {
    String dataSource = null;
    try {
      dataSource = getControllerMethodDataSource(joinPoint);
      logger.debug("切换数据源到" + dataSource);
      // 切换数据源
      CustomerContextHolder.setDataSource(dataSource);
      logger.debug("切换数据源到" + dataSource + "成功");
    } catch (Exception e) {
      logger.error("切换数据源失败" + dataSource, e);
      throw new Exception(e);
    }

  }

  /**
   * 数据源使用完后 需要切换回原来的数据源 不然 同一个线程 会出现数据源混乱
   * 
   * @param joinPoint
   * @throws Exception
   */
  @After("dataSourceAspect()")
  public void doAfter(JoinPoint joinPoint) throws Exception {
    String dataSource = DataSourceEnum.DB_SNS.toString();
    try {
      logger.debug("数据源回复到初始状态" + dataSource);
      // 切换数据源
      CustomerContextHolder.setDataSource(dataSource);
      logger.debug("数据源回复到初始状态" + dataSource + "成功");
    } catch (Exception e) {
      logger.error("数据源回复到初始状态" + dataSource, e);
      throw new Exception(e);
    }
  }

  @AfterThrowing("dataSourceAspect()")
  public void doException(JoinPoint joinPoint) throws Exception {
    String dataSource = DataSourceEnum.DB_SNS.toString();
    try {
      logger.debug("出现异常 切换数据源到" + dataSource);
      // 切换数据源
      CustomerContextHolder.setDataSource(dataSource);
      logger.debug("出现异常  切换数据源到" + dataSource + "成功");
    } catch (Exception e) {
      logger.error("出现异常  切换数据源失败" + dataSource, e);
      throw new Exception(e);
    }
  }


  /**
   * 获取注解中 传入的参数信息 主要是区分需要切换的数据源
   * 
   * @param joinPoint 切点
   * @throws Exception
   */
  @SuppressWarnings("rawtypes")
  public static String getControllerMethodDataSource(JoinPoint joinPoint) throws Exception {
    String targetName = joinPoint.getTarget().getClass().getName();
    String methodName = joinPoint.getSignature().getName();
    Object[] arguments = joinPoint.getArgs();
    Class targetClass = Class.forName(targetName);
    Method[] methods = targetClass.getMethods();
    String dataSource = "";
    for (Method method : methods) {
      if (method.getName().equals(methodName)) {
        Class[] clazzs = method.getParameterTypes();
        if (clazzs.length == arguments.length) {
          dataSource = method.getAnnotation(NextDataSourceAnnotation.class).dataSource().toString();
          break;
        }
      }
    }
    return dataSource;
  }

}
