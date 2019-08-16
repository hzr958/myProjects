package com.smate.sie.web.application.dao.validate;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.web.application.model.validate.KpiPayValidateInvoice;


/**
 * 科研验证付费记录发票信息DAO
 * 
 * @author wsn
 * @date Mar 7, 2019
 */
@Repository
public class KpiPayValidateInvoiceDao extends SieHibernateDao<KpiPayValidateInvoice, Long> {


  /**
   * 通过logId找到发票记录
   * 
   * @param logId
   * @return
   */
  public KpiPayValidateInvoice findKpiPayValidateInvoiceByLogId(Long logId) {
    String hql = " from KpiPayValidateInvoice t where t.logId = :logId";
    return (KpiPayValidateInvoice) super.createQuery(hql).setParameter("logId", logId).uniqueResult();
  }
}
