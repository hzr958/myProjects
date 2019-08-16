package com.smate.center.task.dao.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.bdsp.BdspPatentBase;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

/**
 * 专利基本信息
 * 
 * @author zzx
 *
 */
@Repository
public class BdspPatentBaseDao extends SnsbakHibernateDao<BdspPatentBase, Long> {

  public List<BdspPatentBase> findUpdateList(Integer pushSize) {
    String hql = "from BdspPatentBase t where exists ("
        + "select 1 from BdspPushPatentDataLog t2 where t2.pubId=t.pubId and t2.pushStatus=0" + ")";
    return this.createQuery(hql).setMaxResults(pushSize).list();
  }

}
