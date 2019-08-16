package com.smate.center.open.dao.rcmd.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.rcmd.pub.ImpactGuideRecord;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 影响力引导记录dao
 * 
 * @author yhx
 *
 */
@Repository
public class ImpactGuideRecordDAO extends SnsHibernateDao<ImpactGuideRecord, Long> {

  public ImpactGuideRecord findRecordByPsnId(Long psnId) {
    String hql = "from ImpactGuideRecord t where t.psnId = :psnId";
    Object object = this.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
    if (object != null) {
      return (ImpactGuideRecord) object;
    }
    return null;
  }
}
