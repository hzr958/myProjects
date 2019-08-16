package com.smate.center.open.dao.nsfc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.nsfc.project.NsfcReschProjectReport;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

@Repository
public class ReschPrjRptDao extends HibernateDao<NsfcReschProjectReport, Long> {

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  /**
   * 按项目编号和项目年度查询.
   * 
   * @param nsfcPrjId
   * @param rptYear
   * @return
   * @throws DaoException
   */
  public List<NsfcReschProjectReport> getPrjRptByNsfcPrjIdYear(Long nsfcPrjId, Long rptYear) throws Exception {
    List<NsfcReschProjectReport> list = new ArrayList<NsfcReschProjectReport>();
    if (rptYear == null) {
      String query =
          "select pr from NsfcReschProjectReport pr where pr.nsfcProject.nsfcPrjId=? and pr.rptId is not null order by pr.rptId desc";
      list = super.createQuery(query, new Object[] {nsfcPrjId}).list();
    } else {
      String query =
          "select pr from NsfcReschProjectReport pr where pr.nsfcProject.nsfcPrjId=? and  pr.rptYear=? and pr.rptId is not null order by pr.rptYear desc,pr.rptId desc";
      list = super.createQuery(query, new Object[] {nsfcPrjId, Integer.valueOf(rptYear.toString())}).list();
    }
    return list;
  };

  public NsfcReschProjectReport getProjectReport(Long id) throws Exception {
    return this.get(id);
  }

  public void saveProjectReport(NsfcReschProjectReport obj) throws Exception {
    this.save(obj);
  }

}
