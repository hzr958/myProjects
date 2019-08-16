package com.smate.center.task.dao.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.bdsp.BdspPrjUnit;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

/**
 * 任务记录
 * 
 * @author zzx
 *
 */
@Repository
public class BdspPrjUnitDao extends SnsbakHibernateDao<BdspPrjUnit, Long> {

  public List<BdspPrjUnit> findListByPrjId(Long prjId) {
    String hql = "from BdspPrjUnit t where t.prjId=:prjId";
    return this.createQuery(hql).setParameter("prjId", prjId).list();
  }

}
