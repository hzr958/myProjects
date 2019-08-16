package com.smate.web.v8pub.dao.sns;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.PubRecommendRecordPO;

/**
 * 论文推荐操作记录dao
 * 
 * @author yhx
 *
 */
@Repository
public class PubRecommendRecordDAO extends SnsHibernateDao<PubRecommendRecordPO, Long> {

  @SuppressWarnings("unchecked")
  public List<Long> getPubIdsByPsnId(Long psnId, Integer status) {
    String hql = "select t.pubId from PubRecommendRecordPO t where t.psnId = :psnId and t.status = :status";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("status", status).list();
  }

  public PubRecommendRecordPO findRecordByPubIdAndPsnId(Long psnId, Long pubId) {
    String hql = "from PubRecommendRecordPO t where t.psnId = :psnId and t.pubId = :pubId";
    Object object = this.createQuery(hql).setParameter("pubId", pubId).setParameter("psnId", psnId).uniqueResult();
    if (object != null) {
      return (PubRecommendRecordPO) object;
    }
    return null;
  }
}
