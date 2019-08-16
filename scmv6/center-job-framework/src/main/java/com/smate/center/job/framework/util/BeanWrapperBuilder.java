package com.smate.center.job.framework.util;

import java.beans.PropertyEditor;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.PropertyEditorRegistrySupport;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;

/**
 * This class is imitate the {@link AbstractBeanFactory} to instantiate and init {@link
 * BeanWrapper}, use default property editors and custom property editors, and use a Spring 3.0
 * ConversionService for converting property values, as an alternative to JavaBeans
 * PropertyEditors.
 *
 * @author Created by hcj
 * @date 2018/06/29 10:03
 */
public class BeanWrapperBuilder extends DefaultSingletonBeanRegistry {

  /**
   * Spring ConversionService to use instead of PropertyEditors
   */
  @Autowired
  private ConversionService conversionService;
  /**
   * Custom PropertyEditorRegistrars to apply to the beans of this factory
   */
  private Set<PropertyEditorRegistrar> propertyEditorRegistrars;

  /**
   * Custom PropertyEditors to apply to the beans of this factory
   */
  private Map<Class<?>, Class<? extends PropertyEditor>> customEditors;

  /**
   * A custom TypeConverter to use, overriding the default PropertyEditor mechanism
   */
  private TypeConverter typeConverter;

  /**
   * Create a new DirectFieldAccessFallbackBeanWrapper, wrapping a new instance of the specified
   * class.
   *
   * @param clazz class to instantiate and wrap
   * @return
   */
  public DirectFieldAccessFallbackBeanWrapper build(Class<?> clazz) {

    DirectFieldAccessFallbackBeanWrapper beanWrapper = new DirectFieldAccessFallbackBeanWrapper(
        clazz);
    return beanWrapper;
  }

  /**
   * Create a new DirectFieldAccessFallbackBeanWrapper for the given object.
   *
   * @param instance object wrapped by DirectFieldAccessFallbackBeanWrapper
   */
  public DirectFieldAccessFallbackBeanWrapper build(Object instance) {
    DirectFieldAccessFallbackBeanWrapper beanWrapper = new DirectFieldAccessFallbackBeanWrapper(
        instance);
    return beanWrapper;
  }

  /**
   * Initialize the given BeanWrapper with the custom editors registered with this factory. To be
   * called for BeanWrappers that will create and populate bean instances.
   * <p>The default implementation delegates to {@link #registerCustomEditors}.
   * Can be overridden in subclasses.
   *
   * @param bw the BeanWrapper to initialize
   * @see AbstractBeanFactory#initBeanWrapper(BeanWrapper)
   */
  protected void initBeanWrapper(BeanWrapper bw) {
    bw.setConversionService(this.conversionService);
    registerCustomEditors(bw);
  }

  /**
   * Initialize the given PropertyEditorRegistry with the custom editors that have been registered
   * with this BeanFactory.
   * <p>To be called for BeanWrappers that will create and populate bean
   * instances, and for SimpleTypeConverter used for constructor argument and factory method type
   * conversion.
   *
   * @param registry the PropertyEditorRegistry to initialize
   * @see AbstractBeanFactory#registerCustomEditors(PropertyEditorRegistry)
   */
  private void registerCustomEditors(PropertyEditorRegistry registry) {
    PropertyEditorRegistrySupport registrySupport =
        (registry instanceof PropertyEditorRegistrySupport
            ? (PropertyEditorRegistrySupport) registry : null);
    if (registrySupport != null) {
      registrySupport.useConfigValueEditors();
    }
    if (!this.propertyEditorRegistrars.isEmpty()) {
      for (PropertyEditorRegistrar registrar : this.propertyEditorRegistrars) {
        try {
          registrar.registerCustomEditors(registry);
        } catch (BeanCreationException ex) {
          Throwable rootCause = ex.getMostSpecificCause();
          if (rootCause instanceof BeanCurrentlyInCreationException) {
            BeanCreationException bce = (BeanCreationException) rootCause;
            if (isCurrentlyInCreation(bce.getBeanName())) {
              if (logger.isDebugEnabled()) {
                logger.debug("PropertyEditorRegistrar [" + registrar.getClass().getName() +
                    "] failed because it tried to obtain currently created bean '" +
                    ex.getBeanName() + "': " + ex.getMessage());
              }
              onSuppressedException(ex);
              continue;
            }
          }
          throw ex;
        }
      }
    }
    if (!this.customEditors.isEmpty()) {
      for (Map.Entry<Class<?>, Class<? extends PropertyEditor>> entry : this.customEditors
          .entrySet()) {
        Class<?> requiredType = entry.getKey();
        Class<? extends PropertyEditor> editorClass = entry.getValue();
        registry.registerCustomEditor(requiredType, BeanUtils.instantiateClass(editorClass));
      }
    }
  }

  public void setBeanFactory(AbstractBeanFactory beanFactory) {
    this.propertyEditorRegistrars = beanFactory.getPropertyEditorRegistrars();
    this.customEditors = beanFactory.getCustomEditors();
    this.conversionService = beanFactory.getConversionService();
    this.typeConverter = beanFactory.getTypeConverter();
  }

}
