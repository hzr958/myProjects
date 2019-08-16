package com.smate.web.dyn.dao.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.pub.PubAssignLog;

@Repository
public class PubAssignLogDao extends SnsHibernateDao<PubAssignLog, Long> {

  public Long getconfirmCount(Long psnId) {
    String countHql =
        "select count( t.pdwhPubId) from PubAssignLog t where t.confirmResult=0 and t.psnId =:psnId and t.status=0 and nvl(t.score,0) > 0";
    return (Long) super.createQuery(countHql).setParameter("psnId", psnId).uniqueResult();
  }

  public List<Long> queryPubConfirmCount(Long psnId) {
    String hql =
        "select  t.pdwhPubId from PubAssignLog t where t.confirmResult=0 and t.psnId = :psnId and t.status=0 and nvl(t.score,0) > 0";
    return this.createQuery(hql).setParameter("psnId", psnId).list();
  }
}
