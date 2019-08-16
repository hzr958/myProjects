package com.smate.center.task.dao.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.bdsp.BdspPaperBase;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

/**
 * 论文基础信息
 * 
 * @author zzx
 *
 */
@Repository
public class BdspPaperBaseDao extends SnsbakHibernateDao<BdspPaperBase, Long> {

  public List<BdspPaperBase> findUpdateList(Integer pushSize) {
    String hql = "from BdspPaperBase t where exists ("
        + "select 1 from BdspPushPaperDataLog t2 where t2.pubId=t.pubId and t2.pushStatus=0" + ")";
    return this.createQuery(hql).setMaxResults(pushSize).list();
  }

}
