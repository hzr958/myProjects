package com.smate.center.task.dao.innocity;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.innocity.InnoCityReqRcmdForPatResult;
import com.smate.core.base.utils.data.InnoCityHibernateDao;



/**
 * innocity为专利推荐需求书结果表dao
 * 
 * @author liqinghua
 * 
 */
@Repository
public class InnoCityReqRcmdForPatResultDao extends InnoCityHibernateDao<InnoCityReqRcmdForPatResult, Long> {

  public void deleteAutoRcmdResult(Long patentId) {
    String hql = "delete from InnoCityReqRcmdForPatResult t where t.patentId =:patentId and t.status = 1";
    super.createQuery(hql).setParameter("patentId", patentId).executeUpdate();
  }

}
