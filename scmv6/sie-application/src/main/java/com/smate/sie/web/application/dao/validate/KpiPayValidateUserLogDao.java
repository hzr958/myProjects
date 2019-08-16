package com.smate.sie.web.application.dao.validate;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.web.application.model.validate.KpiPayValidateUserLog;

/**
 * 人员付费记录DAO
 * 
 * @author wsn
 * @date Feb 27, 2019
 */
@Repository
public class KpiPayValidateUserLogDao extends SieHibernateDao<KpiPayValidateUserLog, Long> {

  public KpiPayValidateUserLog findPayLogByOrderNum(String orderNum) {
    String hql = "from KpiPayValidateUserLog t where t.orderNum = :orderNum";
    return (KpiPayValidateUserLog) super.createQuery(hql).setParameter("orderNum", orderNum).uniqueResult();
  }

}
