package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.pub.JxkjtPrpInfo;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 
 * @author zzx
 *
 */
@Repository
public class JxkjtPrpInfoDao extends PdwhHibernateDao<JxkjtPrpInfo, Long> {
  public List<JxkjtPrpInfo> findList(int batchSize) {
    String hql = "from JxkjtPrpInfo t1 where not exists(select 1 from JxkjtPrpInfoTemp t2 where t1.prpCode=t2.prpCode)";
    return this.createQuery(hql).setMaxResults(batchSize).list();
  }
}
