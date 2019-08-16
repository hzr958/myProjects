package com.smate.center.job.web.support.spring.convert;

import com.smate.center.job.framework.util.BeanPropertyParser;
import com.smate.center.job.framework.util.ReflectionUtils;
import com.smate.core.base.enums.IBaseEnum;
import com.sun.istack.internal.NotNull;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * 自定义枚举类型（枚举类型需实现接口{@link IBaseEnum}）的属性转换器，此类实现了{@link Converter}接口，
 * 提供Spring解析bean时对自定义枚举类型的属性转换的能力
 *
 * @param <S> 原始值类型
 * @param <T> 目标值类型
 * @author Created by hcj
 * @date 2018/06/29 20:01
 */
public class AbstractCustomEnumPropertyConverter<S, T extends IBaseEnum> implements
    Converter<S, T> {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private Class<S> sourceClass;
  private Class<T> enumClass;

  public AbstractCustomEnumPropertyConverter() {
    Type type = getClass().getGenericSuperclass();
    sourceClass = ReflectionUtils.determineGenericsType(type, 0);
    enumClass = ReflectionUtils.determineGenericsType(type, 1);
  }

  @Override
  public T convert(@NotNull S source) {
    Method method = BeanUtils.findMethod(enumClass, "getValue");
    if (method == null) {
      throw new RuntimeException(
          "无法将" + source.getClass() + "类型转换为" + enumClass + "类型，枚举类型" + enumClass + "必须实现接口"
              + IBaseEnum.class);
    }
    Type returnType = method.getReturnType();
    Function<S, Object> parser = null;
    try {
      Class<?> enumValueType = Class.forName(returnType.getTypeName());
      if (source.getClass() != enumValueType && enumValueType != Object.class) {
        parser = s -> BeanPropertyParser.convert2Type(s, enumValueType);
      }
    } catch (ClassNotFoundException e) {
      logger.error("无法加载枚举{}中定义的方法getValue()的返回类型{}", enumClass, returnType);
    }
    return IBaseEnum.parse(source, enumClass, parser);
  }
}
