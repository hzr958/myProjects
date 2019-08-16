package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.smate.center.task.model.pdwh.pub.PdwhPubShare;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库成果分享记录dao
 * 
 * @author lhd
 *
 */
@Repository
public class PdwhPubShareDao extends PdwhHibernateDao<PdwhPubShare, Long> {

  @SuppressWarnings("unchecked")
  public List<PdwhPubShare> listByPdwhPubId(Long pdwhPubId) {
    String hql = "from PdwhPubShare t where t.resId =:pdwhPubId and t.resType = 1";
    return this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
  }

  public Long getShareCount(Long pdwhPubId) {
    String hql = "select count(t.recordId) from PdwhPubShare t where t.resId=:pdwhPubId and t.resType = 1";
    return (Long) this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).uniqueResult();
  }

}
