package com.smate.web.dyn.dao.pdwh;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.dyn.model.pdwhpub.PdwhPubAward;

/**
 * 基准库成果赞记录dao
 * 
 * @author lhd
 *
 */
@Repository
public class PdwhPubAwardDao extends PdwhHibernateDao<PdwhPubAward, Long> {

  /**
   * 用户赞记录
   * 
   * @param form
   */
  public int awardRecord(Long psnId, Long pubId) {
    String hql =
        "select count(t.recordId) from PdwhPubAward t where t.awardPsnId=:awardPsnId and t.pubId=:pubId and t.status=0";
    Long count =
        (Long) super.createQuery(hql).setParameter("awardPsnId", psnId).setParameter("pubId", pubId).uniqueResult();
    return count.intValue();
  }

  /**
   * 获取赞记录
   * 
   * @param form
   * @return
   */
  public PdwhPubAward getPdwhPubAward(Long psnId, Long pubId) {
    String hql = "from PdwhPubAward t where t.awardPsnId=:awardPsnId and t.pubId=:pubId";
    return (PdwhPubAward) super.createQuery(hql).setParameter("awardPsnId", psnId).setParameter("pubId", pubId)
        .uniqueResult();
  }

}
