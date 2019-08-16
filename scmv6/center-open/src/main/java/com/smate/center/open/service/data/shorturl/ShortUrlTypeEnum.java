package com.smate.center.open.service.data.shorturl;

/**
 * 短地址类型 枚举
 * 
 * @author tsz
 *
 */
public enum ShortUrlTypeEnum {
  G("/groupweb/grpinfo/outside/main"), // 群组类型真实地址

  P("/psnweb/outside/homepage"); // 人员类型真实地址

  // 定义私有变量
  private String module;


  // 构造函数，枚举类型只能为私有
  private ShortUrlTypeEnum(String module) {
    this.module = module;
  }

  public String toString() { // 定义一个实例成员函数
    return module;
  }

}
