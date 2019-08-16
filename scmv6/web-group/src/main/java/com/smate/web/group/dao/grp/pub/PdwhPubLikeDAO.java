package com.smate.web.group.dao.grp.pub;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.group.model.group.pub.pdwh.PdwhPubLikePO;

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

}
