package com.smate.sie.center.task.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.sie.center.task.otherlibrary.model.SnsPubViewPO;

/**
 * 成果查看、访问dao
 * 
 * @author hd
 * @date 2018年5月31日
 */

@Repository
public class SnsPubViewDAO extends SnsHibernateDao<SnsPubViewPO, Long> {
  /**
   * 获取阅读记录
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SnsPubViewPO> getPubView(Long pubId) {
    String hql = "from SnsPubViewPO t where t.pubId =:pubId";
    return this.createQuery(hql).setParameter("pubId", pubId).list();
  }

}
