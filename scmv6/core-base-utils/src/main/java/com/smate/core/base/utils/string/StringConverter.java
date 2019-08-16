package com.smate.core.base.utils.string;

/**
 * 字符串类型转换器接口，支持java8 lamda表达式的接口
 * 
 * @author houchuanjie
 * @date 2018年3月2日 下午4:27:03
 */
@FunctionalInterface
public interface StringConverter<T> {
  T convert(String str);
}
