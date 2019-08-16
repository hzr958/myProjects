package com.smate.center.task.v8pub.dao.pdwh;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.pdwh.po.PdwhPubSharePO;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果 分享dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Repository
public class PdwhPubShareDAO extends PdwhHibernateDao<PdwhPubSharePO, Long> {

  public Long getShareCount(Long pdwhPubId) {
    String hql = "select count(t.shareId) from PdwhPubSharePO t where t.pdwhPubId=:pdwhPubId and t.status=0";
    return (Long) this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<PdwhPubSharePO> getShareRecords(Long pdwhPubId) {
    String hql = "from PdwhPubSharePO t where t.pdwhPubId=:pdwhPubId and t.status=0 ";
    return this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
  }

}
