package com.smate.sie.center.task.pdwh.brief;

import java.util.List;

import org.springframework.util.Assert;

import com.smate.core.base.utils.exception.BriefDriverNotFoundException;

/** @author yamingd Brief生成驱动工厂,查找驱动 */
public class BriefDriverFactory {

  /** 注册使用的驱动. */
  private List<IBriefDriver> drivers;

  /**
   * Spring注入的Brief驱动.
   * 
   * @param drivers Brief驱动列表
   */
  public void setDrivers(List<IBriefDriver> drivers) {
    Assert.notNull(drivers);
    Assert.notEmpty(drivers);
    this.drivers = drivers;
  }

  /**
   * 查找Brief生成驱动.
   * 
   * @param forTmplForm 成果录入模板名称
   * @param forType 成果类型
   * @return IBriefDriver
   * @throws BriefDriverNotFoundException BriefDriverNotFoundException
   */
  public IBriefDriver getDriver(String forTmplForm, int forType) throws BriefDriverNotFoundException {
    Assert.notNull(forTmplForm);
    IBriefDriver result = null;
    for (int i = 0; i < drivers.size(); i++) {
      IBriefDriver item = drivers.get(i);
      if (item.getForType() == forType && item.getForTmplForm().equalsIgnoreCase(forTmplForm)) {
        result = item;
        break;
      }
    }
    if (result == null) {
      throw new BriefDriverNotFoundException(forTmplForm, forType);
    }

    return result;
  }
}
