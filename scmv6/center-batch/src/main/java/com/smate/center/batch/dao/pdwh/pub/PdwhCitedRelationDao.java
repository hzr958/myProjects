package com.smate.center.batch.dao.pdwh.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.PdwhCitedRelation;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 
 * @author zjh 成果引用关系dao
 *
 */
@Repository
public class PdwhCitedRelationDao extends PdwhHibernateDao<PdwhCitedRelation, Long> {
  public PdwhCitedRelation getPdwhCitedRelation(Long pubId, Long citedPubId) {
    String hql = "from PdwhCitedRelation t where t.pdwdPubId = :pubId and t.pdwhCitedPubId = :citedPubId "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pdwhPubId)";
    return (PdwhCitedRelation) super.createQuery(hql).setParameter("pubId", pubId)
        .setParameter("citedPubId", citedPubId).uniqueResult();

  }

  /**
   * @Author LIJUN
   * @Description //TODO 根据成果id查询改成果的被引用次数
   * @Date 16:14 2018/7/30
   * @Param [pdwdPubId]
   * @return java.lang.Long
   **/
  public Long getPdwhCitedCount(Long pdwdPubId) {
    String hql = "select count(1) from PdwhCitedRelation where pdwdPubId = :pdwdPubId "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pdwhPubId)";
    return (Long) super.createQuery(hql).setParameter("pdwdPubId", pdwdPubId).uniqueResult();

  }

}
