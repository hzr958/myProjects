package com.smate.center.open.dao.reschproject;



import java.math.BigDecimal;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.nsfc.project.NsfcReschProjectReport;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;



/**
 * 
 * nsfc项目dao.
 * 
 * @author OYH
 * 
 */
@Repository
public class SnsNsfcReschPrjRptDao extends HibernateDao<NsfcReschProjectReport, Long> {

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  public Long getReportId() throws HibernateException, SQLException {

    String sql = "select NSFC_RESCH_PROJECT_REPORT_SEQ.nextval from dual";
    SQLQuery query = this.getSession().createSQLQuery(sql);
    BigDecimal id = (BigDecimal) query.uniqueResult();
    return id.longValue();



  }

  public NsfcReschProjectReport getNsfcProjectReport(Long nsfcPrjId, Integer year) {
    return findUnique("from NsfcReschProjectReport where nsfcProject.nsfcPrjId=? and rptYear=?", nsfcPrjId, year);
  }

  public void updateReschRptStatus(Long rptId, Integer status) throws Exception {
    String hql = "update NsfcReschProjectReport t set t.status = ? where t.rptId = ?";
    super.createQuery(hql, status, rptId).executeUpdate();
  }

}
