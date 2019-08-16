package com.smate.center.open.dao.nsfc.continueproject;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.nsfc.continueproject.ContinueProjectReport;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;


/**
 * 延续项目报告
 * 
 * @author tsz
 *
 */

@Repository
public class ContinueProjectReportDao extends HibernateDao<ContinueProjectReport, Long> {

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  /**
   * 获取报告信息
   * 
   * @param nsfcRptId
   * @return
   */
  public ContinueProjectReport findReportInfo(Long nsfcRptId) {
    String hql = "from ContinueProjectReport t where t.nsfcRptId = ?";
    return (ContinueProjectReport) this.createQuery(hql, nsfcRptId).uniqueResult();
  }
}
