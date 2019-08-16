package com.smate.web.group.dao.grp.grpbase;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.grp.grpbase.GrpKwDisc;

/**
 * 群组基础信息dao
 * 
 * @author AiJiangBin
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
