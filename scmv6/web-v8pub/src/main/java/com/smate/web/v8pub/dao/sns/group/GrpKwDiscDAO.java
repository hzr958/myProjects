package com.smate.web.v8pub.dao.sns.group;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.group.GrpKwDisc;

/**
 * 群组基础信息dao
 * 
 * @author AiJiangBin
 *
 */

@Repository
public class GrpKwDiscDAO extends SnsHibernateDao<GrpKwDisc, Long> {

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

}
