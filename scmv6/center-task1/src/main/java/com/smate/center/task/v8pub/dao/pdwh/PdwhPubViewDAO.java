package com.smate.center.task.v8pub.dao.pdwh;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.pdwh.po.PdwhPubViewPO;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果查看、访问dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Repository
public class PdwhPubViewDAO extends PdwhHibernateDao<PdwhPubViewPO, Long> {

  public Long getViewCount(Long pdwhPubId) {
    String hql = "select sum(t.totalCount) from PdwhPubViewPO t where t.pdwhPubId=:pdwhPubId";
    return (Long) this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).uniqueResult();
  }

  /**
   * 获取阅读记录
   * 
   * @param pdwhPubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PdwhPubViewPO> findByPubId(Long pdwhPubId) {
    String hql = "from PdwhPubViewPO t where t.pdwhPubId=:pdwhPubId";
    return this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
  }

}
