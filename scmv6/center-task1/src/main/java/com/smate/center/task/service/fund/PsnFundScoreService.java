package com.smate.center.task.service.fund;

import java.io.Serializable;
import java.util.List;

import com.smate.center.task.model.fund.rcmd.ConstFundCategory;
import com.smate.center.task.model.sns.psn.PsnPrjGrant;

public interface PsnFundScoreService extends Serializable {

  final static int PSN_PRJ_ROLE_GET = 1;// 人员获得基金的角色.
  final static int PSN_PRJ_ROLE_PART = 0;// 人员参与基金的角色.

  /**
   * 
   * 个人获得基金与基金类别匹配
   * 
   * @param psnGrantList
   * @param fund
   * @return
   */
  int countPsnAndFundGrant(List<PsnPrjGrant> psnGrantList, ConstFundCategory fund);
}
