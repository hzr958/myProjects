package com.smate.center.open.service.rule;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.rule.GrantRuleDao;
import com.smate.center.open.model.rule.GrantRule;



/**
 * 业务类别申请书成果填报规则
 * 
 * @author scy
 * 
 */
@Transactional(rollbackFor = Exception.class)
@Service("grantRuleService")
public class GrantRuleServiceImpl implements GrantRuleService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GrantRuleDao grantRuleDao;


  /**
   * 根据业务ID查找业务规则
   * 
   * @param codeId
   * @return
   * @throws ServiceException
   */
  @Override
  public GrantRule getGrantRule(Long codeId) throws Exception {
    try {



      return grantRuleDao.getGrantRule(codeId);
    } catch (Exception e) {
      logger.error("根据业务ID查找业务规则出现问题", e);
      throw new Exception("根据业务ID查找业务规则出现问题", e);
    }
  }


}
