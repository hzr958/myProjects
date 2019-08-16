package com.smate.center.task.v8pub.strategy;

import java.util.Map;

/**
 * 成果类别对象构造器
 * 
 * @author YJ
 *
 *         2018年8月15日
 */
public class PubTypeInfoConstructor {

  // 存放
  private Map<Integer, PubTypeInfoDriver> drivers;

  public PubTypeInfoDriver getPubTypeInfoDriver(Integer pubType) {
    return drivers.get(pubType);
  }

  public Map<Integer, PubTypeInfoDriver> getDrivers() {
    return drivers;
  }

  public void setDrivers(Map<Integer, PubTypeInfoDriver> drivers) {
    this.drivers = drivers;
  }
}
