package com.smate.center.task.dao.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.bdsp.BdspPrjBase;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

@Repository
public class BdspPrjBaseDao extends SnsbakHibernateDao<BdspPrjBase, Long> {

  public List<BdspPrjBase> findUpdateList(Integer pushSize) {
    String hql = "from BdspPrjBase t where exists ("
        + "select 1 from BdspPushPrjDataLog t2 where t2.prjId=t.prjId and t2.pushStatus=0" + ")";
    return this.createQuery(hql).setMaxResults(pushSize).list();
  }

}
