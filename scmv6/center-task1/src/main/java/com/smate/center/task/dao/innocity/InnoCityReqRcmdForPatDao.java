package com.smate.center.task.dao.innocity;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.innocity.InnoCityReqRcmdForPat;
import com.smate.core.base.utils.data.InnoCityHibernateDao;



/**
 * innocity为专利推荐需求书发起表dao
 * 
 * @author liqinghua
 * 
 */
@Repository
public class InnoCityReqRcmdForPatDao extends InnoCityHibernateDao<InnoCityReqRcmdForPat, Long> {

  @SuppressWarnings("unchecked")
  public List<InnoCityReqRcmdForPat> getListByStatus(Integer status) {
    String hql = "from InnoCityReqRcmdForPat t where t.status =:status";
    List<InnoCityReqRcmdForPat> rsList =
        super.createQuery(hql).setParameter("status", status).setMaxResults(2000).list();
    return rsList;
  }

}
