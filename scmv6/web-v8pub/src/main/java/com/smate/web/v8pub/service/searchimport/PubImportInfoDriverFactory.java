package com.smate.web.v8pub.service.searchimport;

import java.util.List;

import com.smate.core.base.utils.exception.PubDriverNotFoundException;

/**
 * 生成驱动工厂,查找驱动
 * 
 * @author wsn
 * @date 2018年8月17日
 */
public class PubImportInfoDriverFactory {

  /**
   * 注册使用的驱动.
   */
  private List<PubImportInfoDriver> drivers;

  /**
   * Spring注入的驱动.
   * 
   * @param drivers 驱动列表
   */
  public void setDrivers(List<PubImportInfoDriver> drivers) {
    this.drivers = drivers;
  }

  public PubImportInfoDriver getDriver(int forType) throws PubDriverNotFoundException {
    PubImportInfoDriver result = null;
    for (int i = 0; i < drivers.size(); i++) {
      PubImportInfoDriver item = drivers.get(i);
      if (item.getForType() == forType) {
        result = item;
        break;
      }
    }
    if (result == null) {
      throw new PubDriverNotFoundException(forType);
    }
    return result;
  }
}
