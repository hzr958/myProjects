package com.smate.center.batch.dao.sns.pub;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.NsfcReschProjectReport;
import com.smate.center.batch.model.sns.pub.SyncNsfcReschProjectReportTemp;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果研究报告.
 * 
 * @author oyh
 * 
 */
@Repository("reschPrjRptDao")
public class ReschPrjRptDaoImpl extends SnsHibernateDao<NsfcReschProjectReport, Long> implements ReschPrjRptDao {

  private static Logger log = LoggerFactory.getLogger(ReschPrjRptDaoImpl.class);

  @Override
  public NsfcReschProjectReport getProjectReport(Long id) throws DaoException {
    return this.get(id);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<NsfcReschProjectReport> getProjectReports() throws DaoException {
    String query = " from NsfcReschProjectReport  pr";
    return super.createQuery(query).list();
  }

  @Override
  public void removeProjectReport(Long id) throws DaoException {

  }

  @Override
  public void saveProjectReport(NsfcReschProjectReport obj) throws DaoException {
    this.save(obj);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<NsfcReschProjectReport> getProjectReportsByInsId(Long insId) throws DaoException {
    String query = " from NsfcReschProjectReport pr ";
    List<NsfcReschProjectReport> list = this.createQuery(query).list();
    return list;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<NsfcReschProjectReport> getProjectReportsByPsnId(Long psnId) throws DaoException {
    String query =
        "select pr from NsfcReschProjectReport pr where pr.nsfcProject.piPsnId=? and pr.rptId is not null order by pr.rptYear desc,pr.rptId desc";
    List<NsfcReschProjectReport> list = super.createQuery(query, new Object[] {psnId}).list();
    return list;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<NsfcReschProjectReport> getProjectReportByPrjId(Long prjId, Long psnId) throws DaoException {
    String query =
        "select pr from NsfcReschProjectReport pr where pr.nsfcProject.prjId=? and  pr.nsfcProject.piPsnId=? and pr.rptId is not null order by pr.rptYear desc,pr.rptId desc";
    List<NsfcReschProjectReport> list = super.createQuery(query, new Object[] {prjId, psnId}).list();
    return list;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<NsfcReschProjectReport> getProjectReportByNsfcPrjId(Long nsfcPrjId, Long psnId) throws DaoException {
    List<Object> params = new ArrayList<Object>();
    String query = "select pr from NsfcReschProjectReport pr where pr.nsfcProject.nsfcPrjId=? ";
    params.add(nsfcPrjId);
    if (psnId != null && psnId.longValue() > 0) {
      query += " and  pr.nsfcProject.piPsnId=? ";
      params.add(psnId);
    }
    query += " and pr.rptId is not null order by pr.rptYear desc,pr.rptId desc";
    List<NsfcReschProjectReport> list = super.createQuery(query, params.toArray()).list();
    return list;
  }

  @Override
  public void saveSyncNsfcProjectReportTemp(SyncNsfcReschProjectReportTemp obj) throws DaoException {

    super.getSession().save(obj);

  }

  @SuppressWarnings("unchecked")
  @Override
  public List<NsfcReschProjectReport> getPrjRptByNsfcPrjIdYear(Long nsfcPrjId, Integer rptYear) throws DaoException {
    String query =
        "select pr from NsfcReschProjectReport pr where pr.nsfcProject.nsfcPrjId=? and  pr.rptYear=? and pr.rptId is not null order by pr.rptYear desc,pr.rptId desc";
    List<NsfcReschProjectReport> list = super.createQuery(query, new Object[] {nsfcPrjId, rptYear}).list();
    return list;
  }

  @Override
  public Integer findStatusByRptId(Long rptId) throws DaoException {
    String query = "select pr.status from NsfcReschProjectReport pr where pr.rptId=? ";
    return (Integer) super.createQuery(query, rptId).uniqueResult();
  }

}
