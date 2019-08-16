package com.smate.center.batch.dao.sns.pub;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.NsfcReschPrjRptPub;
import com.smate.center.batch.model.sns.pub.NsfcReschPrjRptPubId;
import com.smate.center.batch.model.sns.pub.NsfcReschProjectReport;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果研究报告的DAO实现.
 * 
 * @author oyh
 * 
 */
@Repository("reschPrjRptPubDao")
public class ReschPrjRptPubDaoImpl extends SnsHibernateDao<NsfcReschPrjRptPub, NsfcReschPrjRptPubId>
    implements ReschPrjRptPubDao {

  @Override
  public void saveProjectReportPub(NsfcReschPrjRptPub pub) throws DaoException {
    super.save(pub);
  }

  @Override
  public void saveProjectReportPub(List<NsfcReschPrjRptPub> saveList) throws DaoException {
    // String hql =
    // "update NsfcPrjRptPub t set t.seqNo=? where t.id.rptId=? and t.id.pubId=?";
    // this.batchExecute(hql, saveList.toArray());
    for (NsfcReschPrjRptPub pub : saveList) {
      this.save(pub);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<NsfcReschPrjRptPub> getProjectReportPubsByRptId(Long rptId, int objectsPerPage, int pageNumber)
      throws DaoException {
    String query =
        "from  NsfcReschPrjRptPub pp where pp.id.rptId=? order by pp.defType asc, pp.seqNo,nvl(pp.pubYear,0) desc, pp.pubYear desc,pp.pubType,id.pubId desc";
    List<NsfcReschPrjRptPub> list = super.createQuery(query, new Object[] {rptId}).list();
    return list;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<NsfcReschPrjRptPub> getProjectReportPubsByRptId(Long rptId) throws DaoException {
    String query = "from  NsfcReschPrjRptPub pp where pp.id.rptId=?";
    List<NsfcReschPrjRptPub> list = this.createQuery(query, new Object[] {rptId}).list();
    return list;
  }

  @Override
  public void removeProjectReportPublication(Long rptId, Long pubId) throws DaoException {

    NsfcReschPrjRptPubId id = new NsfcReschPrjRptPubId();
    id.setPubId(pubId);
    id.setRptId(rptId);
    this.delete(id);
  }

  @Override
  public NsfcReschPrjRptPub getProjectReportPub(NsfcReschPrjRptPubId id) throws DaoException {
    return this.get(id);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<NsfcReschProjectReport> getProjectReportPubsByPrjId(Long prjId) throws DaoException {
    String hql = "select t from NsfcReschProjectReport t where t.prjId=? order by rptYear desc,rptId desc";
    return super.createQuery(hql, new Object[] {prjId}).list();
  }

  @Override
  public Long getCountProjectReportPubsByRptId(Long rptId) throws DaoException {
    String query = "from  NsfcReschPrjRptPub pp where pp.id.rptId=?";
    return this.findUnique(query, new Object[] {rptId});
  }

  @Override
  public void removeProjectReportPublication(Long rptId, Long[] pubIds) {
    StringBuffer sql = new StringBuffer();
    sql.append(" from NsfcReschPrjRptPub prj where prj.id.pubId in(");
    sql.append(pubIds[0]);
    for (int i = 1; i < pubIds.length; i++) {
      sql.append("," + pubIds[i]);
    }
    sql.append(") and prj.id.rptId=?");
    super.createQuery(sql.toString(), new Object[] {rptId}).executeUpdate();
  }

  @Override
  public Long getNsfcPrjRptPubMaxSeqNo(Long rptId, Integer defType) throws DaoException {
    StringBuffer sql = new StringBuffer();
    sql.append(" select count(*) from NsfcReschPrjRptPub prj where prj.id.rptId=? and prj.defType = ?");
    Long ret = this.findUnique(sql.toString(), new Object[] {rptId, defType});
    return ret;
  }

  @Override
  public NsfcReschProjectReport getProjectReportPubsByPrjIdAndYear(Long prjId, Integer year) throws DaoException {
    StringBuffer sql = new StringBuffer();
    sql.append(" from NsfcReschProjectReport rpt where nsfcProject.prjId=? and rpt.rptYear=?");
    List list = this.createQuery(sql.toString(), new Object[] {prjId, year}).list();
    if (CollectionUtils.isNotEmpty(list)) {
      return (NsfcReschProjectReport) list.get(0);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Set<Long> getProjectReportPubsIdByRptId(Long rptId) throws DaoException {
    String hql = "select distinct(t.id.pubId) from NsfcReschPrjRptPub t where t.id.rptId=?";
    List list = this.createQuery(hql, new Object[] {rptId}).list();
    Set<Long> retList = new HashSet<Long>();
    if (CollectionUtils.isNotEmpty(list)) {
      for (int i = 0; i < list.size(); i++) {
        Object obj = (Object) list.get(i);
        if (obj != null) {
          Long pubId = Long.valueOf(ObjectUtils.toString(obj));
          retList.add(pubId);
        }
      }
    }
    return retList;
  }

  @Override
  public void saveProjectReportTag(Integer isTag, Long prjId, Long rptId, Long pubId) throws DaoException {
    String hql = "update NsfcReschPrjRptPub t set t.isTag=? where t.id.pubId=? and t.id.rptId=?";
    this.createQuery(hql, new Object[] {isTag == null ? 0 : isTag, pubId, rptId}).executeUpdate();
  }

  @Override
  public void saveProjectReportPubList(String listInfo, Long rptId, Long pubId) throws DaoException {
    String hql = "update NsfcReschPrjRptPub t set t.listInfo=? where t.id.pubId=? and t.id.rptId=?";
    this.createQuery(hql, new Object[] {listInfo, pubId, rptId}).executeUpdate();
  }

  @Override
  public NsfcReschPrjRptPub getById(NsfcReschPrjRptPubId rptPubId) throws DaoException {
    return this.get(rptPubId);
  }

  @Override
  public List<Long> getTop5ProjectReportPubsByRptId(Long rptId) throws DaoException {

    String hql = "select t.id.pubId from NsfcReschPrjRptPub t where t.id.rptId=? and t.seqNo<=5 order by t.seqNo";

    return super.createQuery(hql, new Object[] {rptId}).list();

  }

  @Override
  public List<NsfcReschPrjRptPub> findNsfcReschPrjRptPubByPubId(Long pubId) throws DaoException {
    String hql = "from NsfcReschPrjRptPub t where t.id.pubId=?";
    return super.createQuery(hql, pubId).list();
  }

  @Override
  public Long countDefPubType(Long rptId, Integer defType) throws DaoException {
    String hql = "select count(*) from NsfcReschPrjRptPub t where t.id.rptId = ? and t.defType = ?";
    return (Long) super.createQuery(hql, rptId, defType).uniqueResult();
  }

  @Override
  public List<NsfcReschPrjRptPub> findPubsByType(Long rptId, Integer defType) throws DaoException {
    String hql = "from NsfcReschPrjRptPub t where t.id.rptId = ? and t.defType = ? order by t.seqNo asc";
    return super.createQuery(hql, rptId, defType).list();
  }

  @Override
  public List<Long> findPubIdsByType(Long rptId, Integer defType) throws DaoException {
    String hql =
        "select t.id.pubId from NsfcReschPrjRptPub t where t.id.rptId = ? and t.defType = ? order by t.seqNo asc";
    return super.createQuery(hql, rptId, defType).list();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<NsfcReschPrjRptPub> getNsfcReschPrjRptPubByPsnId(Long psnId) throws DaoException {
    return super.createQuery("from NsfcReschPrjRptPub t where t.pubOwnerPsnId=?", psnId).list();
  }

  @Override
  public Long getNsfcReschPrjRptPubCount(Long psnId, String title) throws DaoException {
    return (Long) super.findUnique(
        "select count(*) from NsfcReschPrjRptPub t where t.pubOwnerPsnId=? and lower(t.title)=?", psnId, title);
  }

  @Override
  public void deleteNsfcReschPrjRptPub(Long rptId, Long pubId) throws DaoException {
    super.createQuery("delete from NsfcReschPrjRptPub t where t.id.rptId=? and t.id.pubId=?", rptId, pubId)
        .executeUpdate();

  }

  @Override
  public void updateNsfcReschPrjRptPub(Long rptId, Long pubId, Long savePsnId) throws DaoException {
    super.createQuery("update NsfcReschPrjRptPub t set t.pubOwnerPsnId=? where t.id.rptId=? and t.id.pubId=?",
        savePsnId, rptId, pubId).executeUpdate();
  }
}
