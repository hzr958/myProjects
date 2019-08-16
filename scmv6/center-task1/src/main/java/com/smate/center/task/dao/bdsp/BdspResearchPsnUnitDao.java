package com.smate.center.task.dao.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.bdsp.BdspResearchPsnUnit;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

@Repository
public class BdspResearchPsnUnitDao extends SnsbakHibernateDao<BdspResearchPsnUnit, Long> {

  public List<BdspResearchPsnUnit> findListByPsnId(Long psnId) {
    String hql = "from BdspResearchPsnUnit t where t.psnId=:psnId";
    return this.createQuery(hql).setParameter("psnId", psnId).list();
  }

}
