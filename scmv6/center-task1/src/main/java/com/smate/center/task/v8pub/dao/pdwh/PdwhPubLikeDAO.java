package com.smate.center.task.v8pub.dao.pdwh;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.pdwh.po.PdwhPubLikePO;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果 赞dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Repository
public class PdwhPubLikeDAO extends PdwhHibernateDao<PdwhPubLikePO, Long> {

  public PdwhPubLikePO findByPubIdAndPsnId(Long pdwhPubId, Long psnId) {
    String hql = "from PdwhPubLikePO p where p.pdwhPubId =:pdwhPubId and p.psnId =:psnId";
    Object object =
        this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).setParameter("psnId", psnId).uniqueResult();
    if (object != null) {
      return (PdwhPubLikePO) object;
    }
    return null;
  }


  /**
   * 获取赞记录
   * 
   * @param pubId
   * @param psnId
   * @return
   */
  public long getLikeRecord(Long pdwhPubId, Long psnId) {
    String hql = "select count(*) from PdwhPubLikePO t where t.pdwhPubId=:pdwhPubId and t.psnId=:psnId and t.status=1";
    return (Long) super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).setParameter("psnId", psnId)
        .uniqueResult();
  }


  public Long getAwardCount(Long pdwhPubId) {
    String hql = "select count(t.likeId) from PdwhPubLikePO t where t.pdwhPubId=:pdwhPubId and t.status=1";
    return (Long) super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).uniqueResult();
  }

  /**
   * 获取赞记录
   * 
   * @param pdwhPubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PdwhPubLikePO> findByPubId(Long pdwhPubId) {
    String hql = "from PdwhPubLikePO t where t.pdwhPubId =:pdwhPubId and t.status=1 ";
    return this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
  }

}
