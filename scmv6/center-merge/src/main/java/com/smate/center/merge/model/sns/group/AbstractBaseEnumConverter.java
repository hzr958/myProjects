package com.smate.center.merge.model.sns.group;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import javax.persistence.AttributeConverter;


/**
 * 基础枚举类型属性转换器
 * 
 * @author houchuanjie
 * @date 2017年7月26日
 * @param <X> 需要转换的枚举类型
 * @param <Y> 转换后的目标类型，该类型与枚举类型中value属性类型保持一致
 */
public abstract class AbstractBaseEnumConverter<X extends IBaseEnum<Y>, Y>
    implements AttributeConverter<IBaseEnum<Y>, Y> {
  /**
   * 枚举类class
   */
  private Class<X> xclazz;
  /**
   * 目标类class
   */
  private Class<Y> yclazz;
  /**
   * 反射获得枚举类的values()方法
   */
  private Method valuesMethod;

  @SuppressWarnings("unchecked")
  public AbstractBaseEnumConverter() {
    this.xclazz = (Class<X>) (((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments())[0];

    this.yclazz = (Class<Y>) (((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments())[1];
    try {
      valuesMethod = xclazz.getMethod("values");
    } catch (Exception e) {
      throw new RuntimeException("Can't get the method values() from " + xclazz);
    }
  }

  /**
   * 将枚举类型转换为要映射到数据库的目标java类型
   */
  @Override
  public Y convertToDatabaseColumn(IBaseEnum<Y> attribute) {
    return attribute == null ? null : attribute.getValue();
  }

  /**
   * 将映射到数据库的目标java类型转换为枚举类型
   */
  @SuppressWarnings("unchecked")
  @Override
  public X convertToEntityAttribute(Y dbData) {
    try {
      X[] values = (X[]) valuesMethod.invoke(null);
      for (X x : values) {
        if (x.getValue().equals(dbData)) {
          return x;
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(
          "Can't convertToEntityAttribute from " + yclazz + " to " + xclazz + "." + e.getMessage());
    }
    throw new RuntimeException("Unknown database value: " + dbData);
  }
}
