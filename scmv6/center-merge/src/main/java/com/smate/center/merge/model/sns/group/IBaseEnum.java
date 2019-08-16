package com.smate.center.merge.model.sns.group;

/**
 * 基础枚举抽象类
 * 
 * @author houchuanjie
 * @date 2017年7月26日
 * @param <T> value值的类型。value存储枚举值
 */
public interface IBaseEnum<T> {

  /**
   * 获取枚举的值
   * 
   * @author ChuanjieHou
   * @date 2017年9月26日
   * @return
   */
  T getValue();

  /**
   * 获取枚举的描述
   * 
   * @author ChuanjieHou
   * @date 2017年9月26日
   * @return
   */
  String getDescription();

}
