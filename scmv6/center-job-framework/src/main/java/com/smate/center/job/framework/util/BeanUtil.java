package com.smate.center.job.framework.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import org.apache.commons.beanutils.PropertyUtils;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;

/**
 * Bean工具类，继承自{@link BeanUtils}，扩展提供对象映射（利用Dozer，依赖于Spring容器注入相关mapper），提供对象间的属性合并
 */
public class BeanUtil extends BeanUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtil.class);

  /**
   * 合并源对象属性值到目标对象属性，只合并属性名称相同的，属性值相同不做改变，属性值不同时， 优先取属性值不为null的，如果二者都不为null，则取源对象的属性值复制到目标属性上
   *
   * @param source 源对象
   * @param target 目标对象
   */
  public static final void mergeProperties(Object source, Object target) throws BeansException {

    Map<String, Object> describe = null;
    try {
      describe = PropertyUtils.describe(source);
    } catch (Exception e) {
      throw new FatalBeanException("无法读取source的属性描述！", e);
    }
    String[] ignoreProperties = new String[describe.size()];
    int i = 0;
    for (Entry<String, Object> entry : describe.entrySet()) {
      if (Objects.isNull(entry.getValue())) {
        ignoreProperties[i++] = entry.getKey();
      }
    }
    copyProperties(source, target, ignoreProperties);
  }

  /**
   * Performs mapping between source and destination objects
   *
   * @param source object to convert from
   * @param destination object to convert to
   * @throws MappingException mapping failure
   */
  public static final void map(Object source, Object destination) {
    try {
      getMapper().map(source, destination);
    } catch (MappingException e) {
      LOGGER.error("{}类型对象映射到目标类型{}对象时发生错误！源对象：{}，目标对象：{}", source.getClass().getTypeName(),
          destination.getClass().getTypeName(), source, destination, e);
      throw e;
    }
  }

  /**
   * Constructs new instance of destinationClass and performs mapping between from source
   *
   * @param source object to convert from
   * @param destinationClass type to convert to
   * @param <T> type to convert to
   * @return mapped object
   * @throws MappingException mapping failure
   */
  public static final <T> T map(Object source, Class<T> destinationClass) throws MappingException {
    try {
      return getMapper().map(source, destinationClass);
    } catch (MappingException e) {
      LOGGER.error("{}类型对象映射到目标类型{}创建新对象时发生错误！源对象：{}", source.getClass().getTypeName(),
          destinationClass.getTypeName(), source, e);
      throw e;
    }
  }

  /**
   * 获取org.dozer.mapper
   *
   * @return
   */
  public static final Mapper getMapper() {
    try {
      Mapper mapper = SpringUtil.getBean(Mapper.class);
      return mapper;
    } catch (BeansException e) {
      LOGGER.error("在Spring容器中找不到{}类型的对象！", Mapper.class.getTypeName(), e);
      throw e;
    }
  }
}
