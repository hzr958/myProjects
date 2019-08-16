package com.smate.center.open.dao.prj;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.nsfc.project.NsfcProjectReport;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 结题报告.
 * 
 * @author LY
 * 
 */
@Repository("projectReportDao")
public class ProjectReportDao extends SnsHibernateDao<NsfcProjectReport, Long> {

  private static Logger log = LoggerFactory.getLogger(ProjectReportDao.class);


  /**
   * 按报告编号查找报告
   * 
   * @param nsfcRptId
   * @return
   * @throws DaoException
   */
  public NsfcProjectReport getPrjRptByNsfcRptId(Long nsfcRptId) throws Exception {
    String hql = "from NsfcProjectReport t where t.nsfcRptId = ? order by t.rptYear desc";
    List<NsfcProjectReport> list = super.createQuery(hql, nsfcRptId).list();
    if (list.size() > 0)
      return list.get(0);
    else
      return null;
  }

}
