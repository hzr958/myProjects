package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.pub.JxkjtPrpInfoTemp;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 
 * @author zzx
 *
 */
@Repository
public class JxkjtPrpInfoTempDao extends PdwhHibernateDao<JxkjtPrpInfoTemp, Long> {

  @SuppressWarnings("unchecked")
  public List<JxkjtPrpInfoTemp> getJxkjtPrpInfoList(Long lastPrpCode, Integer size) {
    String hql = "from JxkjtPrpInfoTemp t where  t.prpCode > :lastPrpCode order by t.prpCode";
    return createQuery(hql).setParameter("lastPrpCode", lastPrpCode).setMaxResults(size).list();
  }

}
