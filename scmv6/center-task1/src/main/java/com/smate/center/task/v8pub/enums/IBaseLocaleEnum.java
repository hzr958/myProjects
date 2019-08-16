package com.smate.center.task.v8pub.enums;

/**
 * 基础枚举抽象类
 * 
 * @author houchuanjie
 * @date 2017年7月26日
 * @param <T> value值的类型。value存储枚举值
 */
public interface IBaseLocaleEnum<T> {

  /**
   * 获取枚举的值
   * 
   * @author ChuanjieHou
   * @date 2017年9月26日
   * @return
   */
  T getValue();

  /**
   * 获取中文枚举的描述
   * 
   * @author ChuanjieHou
   * @date 2017年9月26日
   * @return
   */
  String getZhDescription();

  /**
   * 获取英文枚举的描述
   * 
   * @author ChuanjieHou
   * @date 2017年9月26日
   * @return
   */
  String getEnDescription();

}
