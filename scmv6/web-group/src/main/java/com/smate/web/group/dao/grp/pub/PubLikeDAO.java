package com.smate.web.group.dao.grp.pub;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.group.pub.PubLikePO;

/**
 * 成果 赞dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Repository
public class PubLikeDAO extends SnsHibernateDao<PubLikePO, Long> {

  /**
   * 获取赞记录
   * 
   * @param pubId
   * @param psnId
   * @return
   */
  public long getLikeRecord(Long pubId, Long psnId) {
    String hql = "select count(*) from PubLikePO t where t.pubId=:pubId and t.psnId=:psnId and t.status=1";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("pubId", pubId).uniqueResult();
  }
}
