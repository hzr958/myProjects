package com.smate.center.task.dao.group;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.group.PrjGrpTmp;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PrjGrpTmpDao extends SnsHibernateDao<PrjGrpTmp, Long> {

  @SuppressWarnings("unchecked")
  public List<PrjGrpTmp> getPrjGrpInfo(int size) {
    String hql =
        "select new PrjGrpTmp(t.grpId,t.grpName,t.ownerPsnId) from PrjGrpTmp t where t.status=0 order by t.grpId";
    return super.createQuery(hql).setMaxResults(size).list();
  }

  public void saveOptResult(Long grpId, int result) {
    String hql = "update PrjGrpTmp t set t.status = :result where t.grpId = :grpId";
    super.createQuery(hql).setParameter("result", result).setParameter("grpId", grpId).executeUpdate();
  }

}
