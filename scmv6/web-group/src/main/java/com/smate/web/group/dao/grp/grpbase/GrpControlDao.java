package com.smate.web.group.dao.grp.grpbase;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.grp.grpbase.GrpControl;

/**
 * 群组基础信息dao
 * 
 * @author AiJiangBin
 *
 */
@Repository
public class GrpControlDao extends SnsHibernateDao<GrpControl, Long> {
  /**
   * 当前群组权限菜单
   */
  public GrpControl getCurrGrpControl(Long grpId) {
    String HQL = "from GrpControl t where t.grpId=:grpId";
    return (GrpControl) getSession().createQuery(HQL).setParameter("grpId", grpId).uniqueResult();
  }
}
