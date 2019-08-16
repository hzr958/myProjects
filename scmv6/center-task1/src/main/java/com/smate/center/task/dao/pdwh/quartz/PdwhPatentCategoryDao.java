package com.smate.center.task.dao.pdwh.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.pub.PdwhPatentCategory;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PdwhPatentCategoryDao extends PdwhHibernateDao<PdwhPatentCategory, Long> {
  /**
   * 大于0则存在记录
   * 
   * @param pubId
   * @return
   */
  public Long getDupPubId(Long pubId) {
    String hql = "select count(1) from PdwhPatentCategory where pubId=:pubId";

    return (Long) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

}
