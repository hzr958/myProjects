package com.smate.center.open.dao.publication;

import org.springframework.stereotype.Repository;
import com.smate.center.open.model.publication.PsnPubTimestamp;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * PsnPubTimestamp表实体Dao
 * 
 * @author ajb
 * 
 */
@Repository
public class PsnPubTimestampDao extends SnsHibernateDao<PsnPubTimestamp, Long> {
  /**
   * 通过psnId 查找对象
   * 
   * @author ajb
   */
  public PsnPubTimestamp findPsnPubTimestampByPsnId(Long psnId) {
    String hql = "  from PsnPubTimestamp t where t.psnId=:psnId";
    return (PsnPubTimestamp) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

}
