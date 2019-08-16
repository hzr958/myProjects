package com.smate.center.batch.dao.sns.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.GrpKwDisc;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 
 * @author zjh 群组关键词dao
 *
 */
@Repository
public class GrpKwDiscDao extends SnsHibernateDao<GrpKwDisc, Long> {
  public String getGrpKwDiscList(Long grpId) {
    String hql = "select t.keywords from GrpKwDisc t where t.grpId=:grpId";
    Object object = super.createQuery(hql).setParameter("grpId", grpId).uniqueResult();
    if (object == null) {
      return null;
    } else {
      return object.toString();
    }

  }
}
