package com.smate.center.open.dao.prj;



import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.nsfc.project.NsfcProjectReport;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;



/**
 * 
 * nsfc项目dao.
 * 
 * 
 */
@Repository
public class SnsNsfcProjectReportDao extends HibernateDao<NsfcProjectReport, Long> {

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

  public NsfcProjectReport findReportByNsfcRptId(Long nsfcRptId) throws Exception {
    String hql = "from NsfcProjectReport t where t.nsfcRptId = ? order by t.rptYear";
    List<NsfcProjectReport> list = super.createQuery(hql, nsfcRptId).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    } else {
      return null;
    }
  }



}
