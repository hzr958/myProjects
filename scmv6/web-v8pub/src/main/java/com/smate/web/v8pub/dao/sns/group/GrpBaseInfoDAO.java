package com.smate.web.v8pub.dao.sns.group;

import org.springframework.stereotype.Repository;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.group.GrpBaseinfo;

/**
 * 群组基础信息dao
 * 
 * @author AiJiangBin
 *
 */

@Repository
public class GrpBaseInfoDAO extends SnsHibernateDao<GrpBaseinfo, Long> {

  /**
   * 获取项目编号
   * 
   * @param grpId
   * @return
   */
  public String getProjectNo(Long grpId) {
    String hql = "select g.projectNo from GrpBaseinfo g where g.grpId =:grpId";
    return (String) super.createQuery(hql).setParameter("grpId", grpId).uniqueResult();
  }
}
