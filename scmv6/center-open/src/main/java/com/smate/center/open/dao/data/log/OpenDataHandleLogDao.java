package com.smate.center.open.dao.data.log;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.data.log.OpenDataHandleLog;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 日志统计记录dao
 * 
 * @author tsz
 *
 */
@Repository
public class OpenDataHandleLogDao extends SnsHibernateDao<OpenDataHandleLog, Long> {

  public OpenDataHandleLog getByTokenAndType(String token, String serviceType) {
    String hql = "from OpenDataHandleLog where token=:token and serviceType=:serviceType ";
    Object obj =
        super.createQuery(hql).setParameter("token", token).setParameter("serviceType", serviceType).uniqueResult();
    if (obj != null) {
      return (OpenDataHandleLog) obj;
    }
    return null;
  }

}
