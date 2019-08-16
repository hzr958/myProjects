package com.smate.center.job.framework.util;

import com.smate.center.job.framework.exception.DuplicateBeanDefinitionException;
import com.sun.istack.internal.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring Bean工厂的工具类，方便动态获取bean和创建bean
 *
 * @author houchuanjie
 * @date 2018/04/11 09:22
 */
public class SpringUtil implements ApplicationContextAware {

  private static final Logger LOGGER = LoggerFactory.getLogger(SpringUtil.class);
  /**
   * spring上下文
   */
  private static ApplicationContext ctx;
  /**
   * spring bean工厂
   */
  private static DefaultListableBeanFactory beanFactory;

  /**
   * 通过Spring创建bean
   *
   * @param beanClass 要创建的bean类的class
   * @param <T> bean的类型
   * @return Class<T>类型的实例
   * @see DefaultListableBeanFactory#createBean(Class)
   */
  public static <T> T createBean(@NotNull Class<T> beanClass) {
    return beanFactory.createBean(beanClass);
  }

  public static <T> T createBeanWithAutowire(@NotNull Class<T> beanClass) {
    T bean = (T) beanFactory
        .createBean(beanClass, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, true);
    return bean;
  }

  /**
   * @param beanClass
   * @param autowireMode
   * @param dependencyCheck
   * @param <T>
   * @return
   * @throws BeansException
   * @see AbstractAutowireCapableBeanFactory#createBean(String, RootBeanDefinition, Object[])
   */
  public static <T> T createBean(@NotNull Class<T> beanClass, int autowireMode,
      boolean dependencyCheck) throws BeansException {
    T bean = (T) beanFactory.createBean(beanClass, autowireMode, dependencyCheck);
    return bean;
  }

  public static <T> T createBean(@NotNull Class<T> beanClass, String beanId, String beanName,
      Map<String, Object> propertiesMap, Map<String, String> referenceMap,
      List<Object> constructorArgValues, List<String> constructorArgReferences,
      String initMethodName, String destroyMethodName) {
    final String id = beanId, name = beanName;
    //id为空则取name，name为空则取beanClass全类名
    beanId = Optional.ofNullable(id)
        .orElseGet(() -> Optional.ofNullable(name).orElse(beanClass.getName()));
    //name为空则取beanId名
    beanName = Optional.ofNullable(name).orElse(beanId);
    if (beanFactory.containsBeanDefinition(beanName)) {
      throw new DuplicateBeanDefinitionException();
    }
    BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
        .rootBeanDefinition(beanClass);

    //设置bean属性
    if (MapUtils.isNotEmpty(propertiesMap)) {
      propertiesMap.forEach((k, v) -> beanDefinitionBuilder.addPropertyValue(k, v));
    }

    //设置bean参数引用
    if (MapUtils.isNotEmpty(referenceMap)) {
      referenceMap.forEach((k, v) -> beanDefinitionBuilder.addPropertyReference(k, v));
    }

    //设置bean构造参数
    if (CollectionUtils.isNotEmpty(constructorArgValues)) {
      constructorArgValues.forEach(value -> beanDefinitionBuilder.addConstructorArgValue(value));
    }

    //设置bean构造参数引用
    if (CollectionUtils.isNotEmpty(constructorArgReferences)) {
      constructorArgReferences
          .forEach(reference -> beanDefinitionBuilder.addConstructorArgReference(reference));
    }

    //设置初始化方法
    if (StringUtils.isNotBlank(initMethodName)) {
      beanDefinitionBuilder.setInitMethodName(initMethodName);
    }
    //设置销毁方法
    if (StringUtils.isNotBlank(destroyMethodName)) {
      beanDefinitionBuilder.setDestroyMethodName(destroyMethodName);
    }
    AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
    beanDefinition.setAttribute("id", beanId);
    beanDefinition.setAttribute("name", beanName);
    beanFactory.registerBeanDefinition(beanName, beanDefinition);
    return getBean(beanName, beanClass);
  }

  /**
   * 如果有的话，返回唯一匹配给定对象类型的spring管理的bean实例。
   *
   * @param requiredType 输入bean必须匹配;可以是一个接口或超类。不允许为null。
   * @return 与所需类型匹配的单例实例
   * @throws NoSuchBeanDefinitionException 如果给定对象类型的实例不存在的话
   * @throws NoUniqueBeanDefinitionException 如果给定对象类型的实例发现超过1个的话
   * @throws BeansException 如果bean无法被创建的话
   * @see ApplicationContext#getBean(Class)
   */
  public static <T> T getBean(Class<T> requiredType) throws BeansException {
    return ctx.getBean(requiredType);
  }

  /**
   * 返回一个实例，该实例可以共享或独立于指定bean。
   * <p>
   * 允许指定显式的构造函数参数/工厂方法参数，在bean定义中覆盖指定的默认参数（如果有的话）。
   * </p>
   *
   * @param beanName 要检索的bean的名称
   * @param requiredType bean必须匹配的类型。可以是实际类的接口或超类，也可以是任何匹配的{@code null}。 例如，如果值是{@code
   * Object.class}，这个方法将会成功，不管返回的实例的类是什么。
   * @return 一个对象的实例
   * @throws NoSuchBeanDefinitionException 如果给定对象类型的实例不存在的话
   * @throws NoUniqueBeanDefinitionException 如果给定对象类型的实例发现超过1个的话
   * @throws BeansException 如果bean无法被创建的话
   * @see ApplicationContext#getBean(String, Class)
   */
  public static <T> T getBean(String beanName, Class<T> requiredType) throws BeansException {
    return ctx.getBean(beanName, requiredType);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.ctx = applicationContext;
    this.beanFactory = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
  }
}
