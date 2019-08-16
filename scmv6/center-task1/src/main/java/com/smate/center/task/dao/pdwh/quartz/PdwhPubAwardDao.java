package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.pub.PdwhPubAward;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库成果赞记录dao
 * 
 * @author lhd
 *
 */
@Repository
public class PdwhPubAwardDao extends PdwhHibernateDao<PdwhPubAward, Long> {

  @SuppressWarnings("unchecked")
  public List<PdwhPubAward> listByPdwhPubId(Long pdwhPubId) {
    String hql = "from PdwhPubAward t where t.pubId=:pdwhPubId";
    return this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
  }

  public Long getAwardCount(Long pdwhPubId) {
    String hql = "select count(t.recordId) from PdwhPubAward t where t.pubId=:pdwhPubId and t.status=0";
    return (Long) super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).uniqueResult();
  }

}
