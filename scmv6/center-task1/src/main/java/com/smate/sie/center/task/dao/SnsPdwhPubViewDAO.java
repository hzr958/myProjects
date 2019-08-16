package com.smate.sie.center.task.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.sie.center.task.otherlibrary.model.SnsPdwhPubViewPO;

/**
 * 成果查看、访问dao
 * 
 * @author hd
 */

@Repository
public class SnsPdwhPubViewDAO extends PdwhHibernateDao<SnsPdwhPubViewPO, Long> {

  /**
   * 获取阅读记录
   * 
   * @param pdwhPubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SnsPdwhPubViewPO> findByPubId(Long pdwhPubId) {
    String hql = "from SnsPdwhPubViewPO t where t.pdwhPubId=:pdwhPubId";
    return this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
  }

}
