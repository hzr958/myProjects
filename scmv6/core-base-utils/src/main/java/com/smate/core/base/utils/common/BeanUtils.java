package com.smate.core.base.utils.common;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

public class BeanUtils {
  private static Log log = LogFactory.getLog(BeanUtils.class);
  public final static String ADD = "add";
  public final static String DEL = "del";
  public final static String UPDATE = "update";

  /**
   * 排除值为NUll的属性
   */
  public static void copyProperties(Object dest, Object orig) throws IllegalAccessException, InvocationTargetException {
    ConvertUtils.register(new DateConverter(), java.util.Date.class);
    BeanUtilsBean beanUtil = new BeanUtilsBean();
    // Validate existence of the specified beans
    if (dest == null) {
      throw new IllegalArgumentException("No destination bean specified");
    }
    if (orig == null) {
      throw new IllegalArgumentException("No origin bean specified");
    }
    if (log.isDebugEnabled()) {
      log.debug("BeanUtils.copyProperties(" + dest + ", " + orig + ")");
    }

    // Copy the properties, converting as necessary
    if (orig instanceof DynaBean) {
      DynaProperty origDescriptors[] = ((DynaBean) orig).getDynaClass().getDynaProperties();
      for (int i = 0; i < origDescriptors.length; i++) {
        String name = origDescriptors[i].getName();
        if (beanUtil.getPropertyUtils().isWriteable(dest, name)) {
          Object value = ((DynaBean) orig).get(name);
          copyProperty(dest, name, value);
        }
      }
    } else if (orig instanceof Map) {
      Iterator names = ((Map) orig).keySet().iterator();
      while (names.hasNext()) {
        String name = (String) names.next();
        if (beanUtil.getPropertyUtils().isWriteable(dest, name)) {
          Object value = ((Map) orig).get(name);
          copyProperty(dest, name, value);
        }
      }
    } else /* if (orig is a standard JavaBean) */ {
      PropertyDescriptor origDescriptors[] = beanUtil.getPropertyUtils().getPropertyDescriptors(orig);
      for (int i = 0; i < origDescriptors.length; i++) {
        String name = origDescriptors[i].getName();
        if ("class".equals(name)) {
          continue; // No point in trying to set an object's class
        }
        if (beanUtil.getPropertyUtils().isReadable(orig, name) && beanUtil.getPropertyUtils().isWriteable(dest, name)) {
          try {
            Object value = beanUtil.getPropertyUtils().getSimpleProperty(orig, name);
            if (value == null) {
              continue;
            }
            copyProperty(dest, name, value);
          } catch (NoSuchMethodException e) {
            log.error(e);
          }
        }
      }
    }
  }

  /**
   * 不改变源bean的对象属性 FIXME 未完成 COPY BEAN
   */
  public static void copyPropertiesForObject(Object dest, Object orig)
      throws IllegalAccessException, InvocationTargetException {
    BeanUtilsBean beanUtil = new BeanUtilsBean();
    // Validate existence of the specified beans
    if (dest == null) {
      throw new IllegalArgumentException("No destination bean specified");
    }
    if (orig == null) {
      throw new IllegalArgumentException("No origin bean specified");
    }
    if (log.isDebugEnabled()) {
      log.debug("BeanUtils.copyProperties(" + dest + ", " + orig + ")");
    }

    // Copy the properties, converting as necessary
    if (orig instanceof DynaBean) {
      DynaProperty origDescriptors[] = ((DynaBean) orig).getDynaClass().getDynaProperties();
      for (int i = 0; i < origDescriptors.length; i++) {
        String name = origDescriptors[i].getName();
        if (beanUtil.getPropertyUtils().isWriteable(dest, name)) {
          Object value = ((DynaBean) orig).get(name);
          copyProperty(dest, name, value);
        }
      }
    } else if (orig instanceof Map) {
      Iterator names = ((Map) orig).keySet().iterator();
      while (names.hasNext()) {
        String name = (String) names.next();
        if (beanUtil.getPropertyUtils().isWriteable(dest, name)) {
          Object value = ((Map) orig).get(name);
          copyProperty(dest, name, value);
        }
      }
    } else /* if (orig is a standard JavaBean) */ {
      PropertyDescriptor origDescriptors[] = beanUtil.getPropertyUtils().getPropertyDescriptors(orig);
      for (int i = 0; i < origDescriptors.length; i++) {
        String name = origDescriptors[i].getName();
        if ("class".equals(name)) {
          continue; // No point in trying to set an object's class
        }
        if (beanUtil.getPropertyUtils().isReadable(orig, name) && beanUtil.getPropertyUtils().isWriteable(dest, name)) {
          try {
            Object value = beanUtil.getPropertyUtils().getSimpleProperty(orig, name);
            copyProperty(dest, name, value);
          } catch (NoSuchMethodException e) {
            log.error(e);
          }
        }
      }
    }
  }

  /**
   * <p>
   * Copy the specified property value to the specified destination bean, performing any type
   * conversion that is required. If the specified bean does not have a property of the specified
   * name, return without doing anything. If you have custom destination property types, register
   * {@link Converter}s for them by calling the <code>register()</code> method of
   * {@link ConvertUtils}.
   * </p>
   * 
   * <p>
   * <strong>FIXME</strong> - Indexed and mapped properties that do not have getter and setter methods
   * for the underlying array or Map are not copied by this method.
   * </p>
   * 
   * @param bean Bean on which setting is to be performed
   * @param name Simple property name of the property to be set
   * @param value Property value to be set
   * 
   * @exception IllegalAccessException if the caller does not have access to the property accessor
   *            method
   * @exception InvocationTargetException if the property accessor method throws an exception
   */
  public static void copyProperty(Object bean, String name, Object value)
      throws IllegalAccessException, InvocationTargetException {

    if (log.isTraceEnabled()) {
      StringBuffer sb = new StringBuffer("  copyProperty(");
      sb.append(bean);
      sb.append(", ");
      sb.append(name);
      sb.append(", ");
      if (value == null) {
        sb.append("<NULL>");
      } else if (value instanceof String) {
        sb.append((String) value);
      } else if (value instanceof String[]) {
        String values[] = (String[]) value;
        sb.append('[');
        for (int i = 0; i < values.length; i++) {
          if (i > 0) {
            sb.append(',');
          }
          sb.append(values[i]);
        }
        sb.append(']');
      } else {
        sb.append(value.toString());
      }
      sb.append(')');
      log.trace(sb.toString());
    }

    if (bean instanceof DynaBean) {
      DynaProperty propDescriptor = ((DynaBean) bean).getDynaClass().getDynaProperty(name);
      if (propDescriptor != null) {
        Converter converter = ConvertUtils.lookup(propDescriptor.getType());
        if (converter != null) {
          value = converter.convert(propDescriptor.getType(), value);
        }
        try {
          PropertyUtils.setSimpleProperty(bean, name, value);
        } catch (NoSuchMethodException e) {
          log.error("-->Should not have happened", e);; // Silently ignored
        }
      } else {
        if (log.isTraceEnabled()) {
          log.trace("-->No setter on 'to' DynaBean, skipping");
        }
      }
    } else /* if (!(bean instanceof DynaBean)) */ {
      PropertyDescriptor propDescriptor = null;
      try {
        propDescriptor = PropertyUtils.getPropertyDescriptor(bean, name);
      } catch (NoSuchMethodException e) {
        propDescriptor = null;
      }
      if ((propDescriptor != null) && (propDescriptor.getWriteMethod() == null)) {
        propDescriptor = null;
      }
      if (propDescriptor != null) {
        try {
          PropertyUtils.setSimpleProperty(bean, name, value);
        } catch (NoSuchMethodException e) {
          log.error("-->Should not have happened", e);; // Silently ignored
        }
      } else {
        if (log.isTraceEnabled()) {
          log.trace("-->No setter on JavaBean, skipping");
        }
      }
    }

  }

  /**
   * 注册一个通用bean.
   * 
   * @param beanName
   * @param beanClass
   * @param attribute
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T> T registryBean(ConfigurableListableBeanFactory factory, ApplicationContext appContext,
      String beanName, Class<T> clazz, Map<String, Object> attribute) {

    BeanDefinitionRegistry registry = ((BeanDefinitionRegistry) factory);
    GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
    beanDefinition.setBeanClass(clazz);
    beanDefinition.setLazyInit(false);
    beanDefinition.setAbstract(false);
    beanDefinition.setAutowireCandidate(true);
    beanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
    if (attribute != null) {
      beanDefinition.setPropertyValues(new MutablePropertyValues(attribute));
    }
    registry.registerBeanDefinition(beanName, beanDefinition);
    return (T) appContext.getBean(beanName);
  }

  /**
   * <strong>比较的对象Object要重写equals方法</strong> 可参考PsnPubSyncState.java
   * <p>
   * 需要保存的数据集合与数据库集合作比较,返回三个Key(ADD,DEL,UPDATE)的Map集合
   * </p>
   * 
   * <p>
   * 通过BeanUtils.get(BeanUtils.ADD\DEL\UPDATE)返回需要添加,删除或更新的集合
   * </p>
   * 
   * @param saveObject需要保存的集合
   * @param dbObject数据库中已经有集合
   * @return
   */
  public static Map<String, List<? extends Object>> compareListObject(List<? extends Object> saveObject,
      List<? extends Object> dbObject) {
    Assert.notEmpty(saveObject);
    Map<String, List<? extends Object>> map = new HashMap<String, List<? extends Object>>();
    if (CollectionUtils.isEmpty(dbObject)) {
      map.put(ADD, saveObject);
    } else {
      List<Object> add = new ArrayList<Object>();// 添加集合
      List<Object> update = new ArrayList<Object>();// 更新集合
      List<Object> del = new ArrayList<Object>();// 删除集合
      for (Object o : saveObject) {
        // 如果需要保存的集合在数据库中已经存在,则更新,否则添加
        if (dbObject.contains(o)) {
          update.add(o);
        } else {
          add.add(o);
        }
      }
      for (Object o : dbObject) {
        // 如果数据库已有的,但没有出现在更新集合中,则放到要删除集合中
        if (!update.contains(o)) {
          del.add(o);
        }
      }
      map.put(ADD, add);
      map.put(DEL, del);
      map.put(UPDATE, update);
    }
    return map;
  }
}


class DateConvert implements Converter {
  public DateConvert() {

  }

  @Override
  public Object convert(Class arg0, Object arg1) {
    String p = (String) arg1;
    if (p == null || p.trim().length() == 0) {
      return null;
    }
    try {
      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
      return df.parse(p.trim());
    } catch (Exception e) {
      return null;
    }
  }
}
