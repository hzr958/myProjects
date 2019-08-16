package com.smate.center.open.service.rule;


import com.smate.center.open.model.rule.GrantRule;



/**
 * 业务类别申请书成果填报规则
 * 
 * @author ajb
 * 
 */

public interface GrantRuleService {


  /**
   * 根据业务ID查找业务规则
   * 
   * @param codeId
   * @return
   * @throws ServiceException
   */
  public GrantRule getGrantRule(Long codeId) throws Exception;


}
