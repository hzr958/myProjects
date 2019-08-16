package com.smate.center.open.service.interconnection;

import com.smate.center.open.model.interconnection.AccountInterconnectionForm;


/**
 * 人员信息服务类接口
 * 
 * @author zll
 */
public interface OpenPersonManager {

  /**
   * 通过邮箱、姓名、机构名称匹配科研之友人员
   * 
   * @param form
   */
  void findMatchPerson(AccountInterconnectionForm form);

}
