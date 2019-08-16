package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.pub.JxstcPrpInfo;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 
 * @author zzx
 *
 */
@Repository
public class JxstcPrpInfoDao extends PdwhHibernateDao<JxstcPrpInfo, Long> {
  public List<JxstcPrpInfo> findList(int batchSize) {
    String hql = "from JxstcPrpInfo t1 where not exists(select 1 from JxstcPrpInfoTemp t2 where t1.prpCode=t2.prpCode)";
    return this.createQuery(hql).setMaxResults(batchSize).list();
  }

}
