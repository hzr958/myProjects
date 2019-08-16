package com.smate.center.job.framework.util;

import com.smate.center.job.common.po.OnlineJobPO;
import com.smate.core.base.utils.string.StringUtils;
import com.sun.istack.internal.NotNull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import org.springframework.util.ReflectionUtils;

public class HibernateUtil {

  /**
   * 获取表名
   *
   * @param clazz
   * @return
   */
  public static String getTableName(Class<?> clazz) {
    Table table = (Table) clazz.getAnnotation(Table.class);
    return table.name();
  }

  /**
   * 获取@Id注解的属性名
   *
   * @param clazz
   * @return
   */
  public static String findIdFieldName(Class<?> clazz) {
    List<String> names = findFiledNamesByAnnoation(clazz, Id.class);
    return names.size() > 0 ? names.get(0) : null;
  }

  /**
   * 获取@Id注解的属性的get方法
   *
   * @param clazz
   * @return
   */
  public static Method findIdFieldGetter(Class<?> clazz) {
    Method idGetter = Optional.ofNullable(findIdFieldName(clazz)).map(name -> {
      String getter = "get" + StringUtils.capitalize(name);
      return ReflectionUtils.findMethod(clazz, getter);
    }).orElse(null);
    return idGetter;
  }

  public static <T extends Annotation> List<String> findFiledNamesByAnnoation(Class<?> clazz,
      Class<T> annotationClass) {
    List<Field> fileds = findFiledsByAnnoation(clazz, annotationClass);
    return fileds.stream().map(Field::getName).collect(Collectors.toList());
  }

  public static <T extends Annotation> List<Field> findFiledsByAnnoation(Class<?> clazz,
      Class<T> annotationClass) {
    List<Field> declaredFields = getDeclaredFields(clazz);
    Method[] methods = clazz.getMethods();
    List<Field> fieldList = new ArrayList<>(declaredFields.size());
    for (Field field : declaredFields) {
      T annotation = Optional.ofNullable(field.getAnnotation(annotationClass)).orElseGet(() -> {
        String methodName = "get" + StringUtils.capitalize(field.getName());
        return findMethodAnnotation(methods, methodName, annotationClass);
      });
      if (annotation != null) {
        fieldList.add(field);
      }
    }
    return fieldList;
  }

  /**
   * 获取Hibernate注解的实体类属性映射，返回Map，其中key是表字段名称，value是属性名称
   *
   * @param clazz 持久化注解的实体类
   * @return Map<String                                                               ,
    * String>
   */
  public static Map<String, String> getColumnMappings(Class<?> clazz) {
    Map<String, String> map = new HashMap<String, String>();
    Method[] methods = clazz.getMethods();
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      Column c = field.getAnnotation(Column.class);
      if (c != null) {
        map.put(c.name(), field.getName());
        continue;
      }

      String methodName = "get" + StringUtils.capitalize(field.getName());
      c = findMethodAnnotation(methods, methodName, Column.class);

      if (c != null) {
        map.put(c.name(), field.getName());
        continue;
      }

      JoinColumn jc = field.getAnnotation(JoinColumn.class); // 获取外键的字段名称
      if (jc != null) {
        map.put(jc.name(), jc.name());
        continue;
      }

      jc = findMethodAnnotation(methods, methodName, JoinColumn.class);
      if (jc != null) {
        map.put(c.name(), field.getName());
        continue;
      }
      String underscoreName = StringUtils.underscoreName(field.getName());
      map.put(underscoreName, field.getName());
    }
    return map;
  }

  public static <T extends Annotation> T findFieldAnnotation(@NotNull Class<?> clazz,
      @NotNull String fieldName, @NotNull Class<T> annotationClass) {
    Field[] fields = clazz.getDeclaredFields();
    return findFieldAnnotation(fields, fieldName, annotationClass);
  }

  public static <T extends Annotation> T findFieldAnnotation(@NotNull Field[] fields,
      @NotNull String fieldName, @NotNull Class<T> annotationClass) {
    for (Field field : fields) {
      if (field.getName().equals(fieldName)) {
        T annotation = field.getAnnotation(annotationClass);
        if (annotation != null) {
          return annotation;
        }
      }
    }
    return null;
  }

  public static <T extends Annotation> T findMethodAnnotation(@NotNull Class<?> clazz,
      @NotNull String methodName, @NotNull Class<T> annotationClass) {
    Method[] methods = clazz.getMethods();
    return findMethodAnnotation(methods, methodName, annotationClass);
  }

  public static <T extends Annotation> T findMethodAnnotation(@NotNull Method[] methods,
      @NotNull String methodName, @NotNull Class<T> annotationClass) {
    for (Method method : methods) {
      if (method.getName().equals(methodName)) {
        T annotation = method.getAnnotation(annotationClass);
        if (annotation != null) {
          return annotation;
        }
      }
    }
    return null;
  }

  public static List<Field> getDeclaredFields(@NotNull Class<?> clazz) {
    List<Field> fields = new ArrayList<>();
    while (clazz != Object.class) {
      Field[] declaredFields = clazz.getDeclaredFields();
      fields.addAll(Arrays.asList(declaredFields));
      clazz = clazz.getSuperclass();
    }
    return fields;
  }

  public static void main(String[] args) {
    OnlineJobPO onlineJobPO = new OnlineJobPO();
    onlineJobPO.setId("abcdef");
    Method idFieldGetter = findIdFieldGetter(OnlineJobPO.class);
    Object o = ReflectionUtils.invokeMethod(idFieldGetter, onlineJobPO);
    System.out.println(o);
  }
}
