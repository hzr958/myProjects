package com.smate.center.batch.dao.sns.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.GrantRule;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 业务类别申请书成果填报规则
 * 
 * @author scy
 * 
 */
@Repository
public class GrantRuleDao extends SnsHibernateDao<GrantRule, Long> {

  /**
   * 根据业务ID和年份查找业务规则
   * 
   * @param codeId
   * @return
   * @throws ServiceException
   */
  public GrantRule getGrantRule(Long codeId, Integer year) throws DaoException {
    String hql = "from GrantRule rule where rule.code = ? and rule.year = ?";
    return this.findUnique(hql, codeId, year);
  }

  /**
   * 根据业务ID查找业务规则
   * 
   * @param codeId
   * @return
   * @throws ServiceException
   */
  public GrantRule getGrantRule(Long codeId) throws DaoException {
    String hql = "from GrantRule rule where rule.code = ?";
    return this.findUnique(hql, codeId);
  }
}
