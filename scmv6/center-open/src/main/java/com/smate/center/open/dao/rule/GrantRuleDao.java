package com.smate.center.open.dao.rule;



import org.springframework.stereotype.Repository;

import com.smate.center.open.model.rule.GrantRule;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;



/**
 * 业务类别申请书成果填报规则
 * 
 * @author ajb
 * 
 */
@Repository
public class GrantRuleDao extends HibernateDao<GrantRule, Long> {
  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  public GrantRule getGrantRule(Long codeId) throws Exception {
    String hql = "from GrantRule rule where rule.code = ?";
    return this.findUnique(hql, codeId);
  }
}
