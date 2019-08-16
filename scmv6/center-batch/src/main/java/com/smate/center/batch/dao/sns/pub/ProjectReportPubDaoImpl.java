package com.smate.center.batch.dao.sns.pub;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.NsfcPrjRptPub;
import com.smate.center.batch.model.sns.pub.NsfcPrjRptPubId;
import com.smate.center.batch.model.sns.pub.NsfcProjectReport;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 结题报告成果的DAO实现.
 * 
 * @author LY
 * 
 */
@Repository("projectReportPubDao")
public class ProjectReportPubDaoImpl extends SnsHibernateDao<NsfcPrjRptPub, NsfcPrjRptPubId>
    implements ProjectReportPubDao {

  @Override
  public void saveProjectReportPub(NsfcPrjRptPub pub) {
    super.save(pub);
  }

  @Override
  public void saveProjectReportPub(List<NsfcPrjRptPub> saveList) {
    // String hql =
    // "update NsfcPrjRptPub t set t.seqNo=? where t.id.rptId=? and t.id.pubId=?";
    // this.batchExecute(hql, saveList.toArray());
    for (NsfcPrjRptPub pub : saveList) {
      this.save(pub);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<NsfcPrjRptPub> getProjectReportPubsByRptId(Long rptId, int objectsPerPage, int pageNumber) {
    String query =
        "from  NsfcPrjRptPub pp where pp.id.rptId=? order by pp.seqNo,nvl(pp.pubYear,0) desc, pp.pubYear desc,pp.pubType,id.pubId desc";
    List<NsfcPrjRptPub> list = super.createQuery(query, new Object[] {rptId}).list();
    return list;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<NsfcPrjRptPub> getProjectReportPubsByRptId(Long rptId) {
    String query = "from  NsfcPrjRptPub pp where pp.id.rptId=?";
    List<NsfcPrjRptPub> list = this.createQuery(query, new Object[] {rptId}).list();
    return list;
  }

  @Override
  public void removeProjectReportPublication(Long rptId, Long pubId) {

    NsfcPrjRptPubId id = new NsfcPrjRptPubId();
    id.setPubId(pubId);
    id.setRptId(rptId);
    this.delete(id);
  }

  @Override
  public NsfcPrjRptPub getProjectReportPub(NsfcPrjRptPubId id) {
    return this.get(id);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<NsfcProjectReport> getProjectReportPubsByPrjId(Long prjId) {
    String hql = "select t from NsfcProjectReport t where t.prjId=? order by rptYear desc,rptId desc";
    return super.createQuery(hql, new Object[] {prjId}).list();
  }

  @Override
  public Long getCountProjectReportPubsByRptId(Long rptId) {
    String query = "from  NsfcPrjRptPub pp where pp.id.rptId=?";
    return this.findUnique(query, new Object[] {rptId});
  }

  @Override
  public void removeProjectReportPublication(Long rptId, Long[] pubIds) {
    StringBuffer sql = new StringBuffer();
    sql.append(" from NsfcPrjRptPub prj where prj.id.pubId in(");
    sql.append(pubIds[0]);
    for (int i = 1; i < pubIds.length; i++) {
      sql.append("," + pubIds[i]);
    }
    sql.append(") and prj.id.rptId=?");
    super.createQuery(sql.toString(), new Object[] {rptId}).executeUpdate();
  }

  @Override
  public Long getNsfcPrjRptPubMaxSeqNo(Long rptId) {
    StringBuffer sql = new StringBuffer();
    sql.append(" select count(*) from NsfcPrjRptPub prj where prj.id.rptId=?");
    Long ret = this.findUnique(sql.toString(), new Object[] {rptId});
    return ret;
  }

  @Override
  public NsfcProjectReport getProjectReportPubsByPrjIdAndYear(Long prjId, Integer year) {
    StringBuffer sql = new StringBuffer();
    sql.append(" from NsfcProjectReport rpt where nsfcProject.prjId=? and rpt.rptYear=?");
    List list = this.createQuery(sql.toString(), new Object[] {prjId, year}).list();
    if (CollectionUtils.isNotEmpty(list)) {
      return (NsfcProjectReport) list.get(0);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Set<Long> getProjectReportPubsIdByRptId(Long rptId) {
    String hql = "select distinct(t.id.pubId) from NsfcPrjRptPub t where t.id.rptId=?";
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
  public void saveProjectReportTag(Integer isTag, Long prjId, Long rptId, Long pubId) {
    String hql = "update NsfcPrjRptPub t set t.isTag=? where t.id.pubId=? and t.id.rptId=?";
    this.createQuery(hql, new Object[] {isTag == null ? 0 : isTag, pubId, rptId}).executeUpdate();
  }

  @Override
  public void saveProjectReportPubList(String listInfo, Long rptId, Long pubId) {
    String hql = "update NsfcPrjRptPub t set t.listInfo=? where t.id.pubId=? and t.id.rptId=?";
    this.createQuery(hql, new Object[] {listInfo, pubId, rptId}).executeUpdate();
  }

  @Override
  public NsfcPrjRptPub getById(NsfcPrjRptPubId rptPubId) {
    return this.get(rptPubId);
  }

  @Override
  public List<NsfcPrjRptPub> findPrjPrtPubByPubId(Long pubId) {
    String hql = "from NsfcPrjRptPub t where t.id.pubId=?";
    return super.createQuery(hql, pubId).list();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<NsfcPrjRptPub> queryNsfcPrjRptPubByPsnId(Long psnId) {
    return super.createQuery("from NsfcPrjRptPub t where t.pubOwnerPsnId=?", psnId).list();
  }

  @Override
  public Long queryNsfcPrjRptPubCount(Long psnId, String title) {
    return super.findUnique("select count(*) from NsfcPrjRptPub t where t.pubOwnerPsnId=? and lower(t.title)=?", psnId,
        title);
  }

  @Override
  public void deleteNsfcPrjRptPub(Long rptId, Long pubId) {
    super.createQuery("delete from NsfcPrjRptPub t where t.id.rptId=? and t.id.pubId=?", rptId, pubId).executeUpdate();
  }

  @Override
  public void updateNsfcPrjRptPub(Long rptId, Long pubId, Long savePsnId) {
    super.createQuery("update NsfcPrjRptPub t set t.pubOwnerPsnId=? where t.id.rptId=? and t.id.pubId=?", savePsnId,
        rptId, pubId).executeUpdate();
  }
}
