package com.smate.center.task.dao.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.bdsp.BdspPrjMember;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

/**
 * 项目成员
 * 
 * @author zzx
 *
 */
@Repository
public class BdspPrjMemberDao extends SnsbakHibernateDao<BdspPrjMember, Long> {

  public List<BdspPrjMember> findListByPrjId(Long prjId) {
    String hql = "from BdspPrjMember t where t.prjId=:prjId";
    return this.createQuery(hql).setParameter("prjId", prjId).list();
  }

}
