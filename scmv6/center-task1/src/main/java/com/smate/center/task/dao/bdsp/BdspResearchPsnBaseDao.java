package com.smate.center.task.dao.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.bdsp.BdspResearchPsnBase;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

/**
 * 科研人员基础信息
 * 
 * @author zzx
 *
 */
@Repository
public class BdspResearchPsnBaseDao extends SnsbakHibernateDao<BdspResearchPsnBase, Long> {

  public List<BdspResearchPsnBase> findUpdateList(Integer pushSize) {
    String hql = "from BdspResearchPsnBase t where exists ("
        + "select 1 from BdspPushPsnDataLog t2 where t2.psnId=t.psnId and t2.pushStatus=0" + ")";
    return this.createQuery(hql).setMaxResults(pushSize).list();
  }

}
