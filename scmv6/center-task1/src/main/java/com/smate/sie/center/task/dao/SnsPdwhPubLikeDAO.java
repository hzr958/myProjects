package com.smate.sie.center.task.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.sie.center.task.otherlibrary.model.SnsPdwhPubLikePO;

/**
 * 成果 赞dao
 * 
 * @author hd
 * @date 2018年5月31日
 */

@Repository
public class SnsPdwhPubLikeDAO extends PdwhHibernateDao<SnsPdwhPubLikePO, Long> {

  /**
   * 获取赞记录
   * 
   * @param pdwhPubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SnsPdwhPubLikePO> findByPubId(Long pdwhPubId) {
    String hql = "from SnsPdwhPubLikePO p where p.pdwhPubId =:pdwhPubId and p.status=1";
    return this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
  }

}
