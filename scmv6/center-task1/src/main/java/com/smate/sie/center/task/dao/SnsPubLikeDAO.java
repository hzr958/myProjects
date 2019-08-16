package com.smate.sie.center.task.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.sie.center.task.otherlibrary.model.SnsPubLikePO;

/**
 * 成果 赞dao
 * 
 * @author hd
 * @date 2018年5月31日
 */

@Repository
public class SnsPubLikeDAO extends SnsHibernateDao<SnsPubLikePO, Long> {


  /**
   * 获取赞记录
   * 
   * @param pdwhPubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SnsPubLikePO> findByPubId(Long pubId) {
    String hql = "from SnsPubLikePO p where p.pubId =:pubId and p.status=1";
    return this.createQuery(hql).setParameter("pubId", pubId).list();
  }
}
