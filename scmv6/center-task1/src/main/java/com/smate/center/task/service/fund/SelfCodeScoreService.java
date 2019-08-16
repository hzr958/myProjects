package com.smate.center.task.service.fund;

import java.io.Serializable;
import java.util.List;


public interface SelfCodeScoreService extends Serializable {

  /**
   * 个人申请代码与基金代码匹配
   * 
   * @param psnCode
   * @param categoryId
   * @return
   */
  int countPsnAndFundDiscode(List<String> psnCode, Long categoryId);

}
