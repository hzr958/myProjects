package com.smate.center.task.dao.group;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.grp.GrpKwDisc;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 群组基础信息dao
 * 
 * @author AiJiangBin
 *
 */

@Repository
public class GrpKwDiscDao extends SnsHibernateDao<GrpKwDisc, Long> {

  /**
   * 获取群组关键词
   * 
   * @param grpId
   * @return
   */
  public String getGrpKwDisc(Long grpId) {
    String hql = "select t.keywords from GrpKwDisc t where t.grpId=:grpId";
    Object object = super.createQuery(hql).setParameter("grpId", grpId).uniqueResult();
    if (object == null) {
      return null;
    } else {
      return object.toString();
    }

  }

  public GrpKwDisc getGrpCategory(Long grpId) {
    String hql = "select new GrpKwDisc(t.firstCategoryId,t.secondCategoryId) from GrpKwDisc t where t.grpId=:grpId";
    return (GrpKwDisc) super.createQuery(hql).setParameter("grpId", grpId).uniqueResult();
  }

}
