package com.smate.center.task.v8pub.dao.sns;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.sns.po.PubViewPO;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果查看、访问dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Repository
public class PubViewDAO extends SnsHibernateDao<PubViewPO, Long> {

  public Long countPubView(Long pubId) {
    String hql = "select sum(t.totalCount) from PubViewPO t where t.pubId =:pubId";
    return (Long) this.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<PubViewPO> queryNeedInsertDate(int start, int size) {
    try {
      String hql = "from PubViewPO v order by v.id";
      return super.createQuery(hql).setFirstResult(start).setMaxResults(size).list();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }


  public Long getConutNum() {
    String hql = "select count(1) from PubViewPO";
    return (Long) this.createQuery(hql).uniqueResult();

  }

  /**
   * 获取阅读记录
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubViewPO> getPubView(Long pubId) {
    String hql = "from PubViewPO t where t.pubId =:pubId";
    return this.createQuery(hql).setParameter("pubId", pubId).list();
  }

}
