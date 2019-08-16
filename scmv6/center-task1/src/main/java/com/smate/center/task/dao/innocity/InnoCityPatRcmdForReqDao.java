package com.smate.center.task.dao.innocity;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.innocity.InnoCityPatRcmdForReq;
import com.smate.core.base.utils.data.InnoCityHibernateDao;



/**
 * innocity为需求书推荐专利发起表dao
 * 
 * @author liqinghua
 * 
 */
@Repository
public class InnoCityPatRcmdForReqDao extends InnoCityHibernateDao<InnoCityPatRcmdForReq, Long> {

  @SuppressWarnings("unchecked")
  public List<InnoCityPatRcmdForReq> getListByStatus(Integer status) {
    String hql = "from InnoCityPatRcmdForReq t where t.status =:status";
    List<InnoCityPatRcmdForReq> rsList =
        super.createQuery(hql).setParameter("status", status).setMaxResults(2000).list();
    return rsList;
  }
}
