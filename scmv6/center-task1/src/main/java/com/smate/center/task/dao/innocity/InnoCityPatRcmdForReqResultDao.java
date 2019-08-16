package com.smate.center.task.dao.innocity;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.innocity.InnoCityPatRcmdForReqResult;
import com.smate.core.base.utils.data.InnoCityHibernateDao;



/**
 * innocity为需求书推荐专利结果表dao
 * 
 * @author liqinghua
 * 
 */
@Repository
public class InnoCityPatRcmdForReqResultDao extends InnoCityHibernateDao<InnoCityPatRcmdForReqResult, Long> {

  public void deleteAutoRcmdResult(Long reqId) {
    String hql = "delete from InnoCityPatRcmdForReqResult t where t.reqId =:reqId and t.status = 1";
    super.createQuery(hql).setParameter("reqId", reqId).executeUpdate();
  }
}
