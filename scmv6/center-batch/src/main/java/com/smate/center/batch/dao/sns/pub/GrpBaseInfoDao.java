package com.smate.center.batch.dao.sns.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.GrpBaseinfo;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 
 * @author zjh
 *
 */

@Repository
public class GrpBaseInfoDao extends SnsHibernateDao<GrpBaseinfo, Long> {
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
