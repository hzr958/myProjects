package com.smate.center.job.framework.util;

import java.beans.PropertyEditor;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.BeansException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;

/**
 * 类属性转换器，用于给定一个目标对象或者目标类，提供将原始值（字符串或者其他任意类型）转换为目标类（对象）某属性类型值的能力。 此类依赖于Spring容器，必须在Spring容器启动后才可使用。
 *
 * @author Created by hcj
 * @date 2018/06/29 11:47
 */
public class BeanPropertyParser implements ApplicationContextAware {

  private static final BeanWrapperBuilder BEAN_WRAPPER_FACTORY = new BeanWrapperBuilder();
  private static ConversionService conversionService;
  private static AbstractBeanFactory beanFactory;
  private static Map<Class<?>, Class<? extends PropertyEditor>> customEditors;
  private DirectFieldAccessFallbackBeanWrapper beanWrapper;
  private Class<?> targetClass;

  public BeanPropertyParser(Class<?> targetClass) {
    this.beanWrapper = BEAN_WRAPPER_FACTORY.build(targetClass);
    this.targetClass = beanWrapper.getWrappedClass();
  }

  public BeanPropertyParser(Object targetObject) {
    this.beanWrapper = BEAN_WRAPPER_FACTORY.build(targetObject);
    this.targetClass = beanWrapper.getWrappedClass();
  }

  public static ConversionService getConversionService() {
    return conversionService;
  }

  public static <T> PropertyEditor getPropertyEditor(Class<T> requiredType) {
    PropertyEditor editor = null;
    for (Entry<Class<?>, Class<? extends PropertyEditor>> entry : customEditors.entrySet()) {
      if (entry.getKey().equals(requiredType)) {
        editor = BeanUtil.instantiate(entry.getValue());
        break;
      }
    }
    if (editor == null) {
      DirectFieldAccessFallbackBeanWrapper beanWrapper = BEAN_WRAPPER_FACTORY.build(Object.class);
      editor = beanWrapper.getDefaultEditor(requiredType);
    }
    return editor;
  }

  public static <T> T convert2Type(Object value, Class<T> requiredType) {
    boolean canConvert = Optional.ofNullable(getConversionService())
        .map(cnvs -> cnvs.canConvert(value.getClass(), requiredType)).orElse(false);
    if (canConvert) {
      return getConversionService().convert(value, requiredType);
    }
    PropertyEditor propertyEditor = getPropertyEditor(requiredType);
    if (Objects.nonNull(propertyEditor)) {
      String source = null;
      if (Objects.nonNull(value)) {
        if (value instanceof String) {
          source = (String) value;
        } else {
          source = convert2Type(value, String.class);
        }
        if (Objects.nonNull(source)) {
          propertyEditor.setAsText(source);
          return (T) propertyEditor.getValue();
        }
      }
    }
    return null;
  }

  /**
   * Convert the given value for the specified property to the latter's type.
   * <p>
   * This method is only intended for optimizations in a BeanFactory. Use the {@code
   * convertIfNecessary} methods for programmatic conversion.
   * </p>
   * <p>
   * Note: this method is use the {@link org.springframework.beans.BeanWrapperImpl#getPropertyValue(String)}
   * method to get the result.
   * </p>
   *
   * @param value the value to convert
   * @param propertyName the target property (note that nested or indexed properties are not
   * supported here)
   * @return the new value, possibly the result of type conversion
   * @throws TypeMismatchException if type conversion failed
   * @see org.springframework.beans.BeanWrapperImpl#getPropertyValue(String)
   */
  public Object convertForProperty(Object value, String propertyName) {
    return beanWrapper.convertForProperty(value, propertyName);
  }

  /**
   * Determine the property type for the specified property, either checking * the property
   * descriptor or checking the value in case of an indexed or mapped element.
   * <p>
   * Note: this method is use the {@link org.springframework.beans.BeanWrapperImpl#getPropertyType(String)}
   * method to get the result.
   * </p>
   *
   * @param propertyName the property to check (may be a nested path and/or an indexed/mapped
   * property)
   * @return the property type for the particular property, or {@code null} if not determinable
   * @throws BeansException
   * @see org.springframework.beans.PropertyAccessor#getPropertyType(String)
   */
  public Class<?> getPropertyType(String propertyName) {
    return beanWrapper.getPropertyType(propertyName);
  }

  public DirectFieldAccessFallbackBeanWrapper getBeanWrapper() {
    return beanWrapper;
  }

  @Override
  public void setApplicationContext(ApplicationContext ctxt) throws BeansException {
    beanFactory = (AbstractBeanFactory) ctxt.getAutowireCapableBeanFactory();
    customEditors = beanFactory.getCustomEditors();
    BEAN_WRAPPER_FACTORY.setBeanFactory(beanFactory);
    conversionService = beanFactory.getConversionService();
  }
}
