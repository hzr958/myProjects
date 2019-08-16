package com.smate.center.job.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Created by hcj
 * @date 2018/06/28 15:18
 */
public class ReflectionUtils {

  public static <T> Class<T> determineGenericsType(Type type) {
    return determineGenericsType(type, 0);
  }

  @SuppressWarnings("unchecked")
  public static <T> Class<T> determineGenericsType(Type type, int index) {
    Class<T> result = null;
    Type[] actualTypeArguments = getActualTypeArguments(type);
    if (actualTypeArguments != null) {
      Type genericType = actualTypeArguments[index];
      if (genericType != null) {
        if (genericType instanceof Class) {
          result = (Class<T>) genericType;
        } else if (genericType instanceof ParameterizedType) {
          Type rawType = ((ParameterizedType) genericType).getRawType();
          result = (Class<T>) rawType;
        }
      }
    }
    return result;
  }

  public static Type[] getActualTypeArguments(Type type) {
    if (type != null && ParameterizedType.class.isAssignableFrom(type.getClass())) {
      return ((ParameterizedType) type).getActualTypeArguments();
    }
    return null;
  }

  public static Field getField(Class<?> tClass, String fieldName){
    Field field = null;
    try {
      Field declaredField = tClass.getDeclaredField(fieldName);
    } catch (NoSuchFieldException e) {
    }
    return field;
  }

}
