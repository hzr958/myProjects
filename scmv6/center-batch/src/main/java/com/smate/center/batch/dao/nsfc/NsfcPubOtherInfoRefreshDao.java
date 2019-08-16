package com.smate.center.batch.dao.nsfc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.nsfc.NsfcPubOtherInfoRefresh;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果其他信息刷新DAO.
 * 
 * @author xys
 *
 */
@Repository
public class NsfcPubOtherInfoRefreshDao extends SnsHibernateDao<NsfcPubOtherInfoRefresh, Long> {

  /**
   * 批量获取待刷新记录.
   * 
   * @param maxSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<NsfcPubOtherInfoRefresh> getNsfcPubOtherInfoRefreshBatch(int maxSize) {
    String hql = "from NsfcPubOtherInfoRefresh t where t.status=0";
    return super.createQuery(hql).setMaxResults(maxSize).list();
  }
}
