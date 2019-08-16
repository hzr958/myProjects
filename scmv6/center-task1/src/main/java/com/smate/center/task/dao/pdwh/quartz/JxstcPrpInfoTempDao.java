package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.pub.JxstcPrpInfoTemp;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 
 * @author zzx
 *
 */
@Repository
public class JxstcPrpInfoTempDao extends PdwhHibernateDao<JxstcPrpInfoTemp, Long> {

  @SuppressWarnings("unchecked")
  public List<JxstcPrpInfoTemp> getJxstcPrpInfoList(Long lastPrpCode, Integer size) {
    String hql = "from JxstcPrpInfoTemp t where  t.prpCode > :lastPrpCode order by t.prpCode";
    return createQuery(hql).setParameter("lastPrpCode", lastPrpCode).setMaxResults(size).list();
  }

}
