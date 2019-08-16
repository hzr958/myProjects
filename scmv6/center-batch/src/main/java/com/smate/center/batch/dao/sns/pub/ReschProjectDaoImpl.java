package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.NsfcReschProject;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果研究报告项目DAO.
 * 
 * @author oyh
 * 
 */
@Repository("reschProjectDao")
public class ReschProjectDaoImpl extends SnsHibernateDao<NsfcReschProject, Long> implements ReschProjectDao {
  private static Logger log = LoggerFactory.getLogger(ReschProjectDaoImpl.class);

  @Override
  public NsfcReschProject getProjectByPrjId(Long prjId) throws DaoException {
    return this.get(prjId);
  }

  @Override
  public void deletePubFromRpt(Long pubId, Integer nodeId, Long rptId) throws DaoException {
    String hql = "delete from NsfcReschPrjRptPub t where t.id.pubId=? and t.nodeId=? and t.id.rptId=?";
    super.createQuery(hql, new Object[] {pubId, nodeId, rptId}).executeUpdate();
  }

  @Override
  public NsfcReschProject getProjectByPrjId(Long prjId, Long psnId) throws DaoException {
    String hql = "select t from NsfcReschProject t where t.prjId=? and t.piPsnId=?";
    return this.findUnique(hql, new Object[] {prjId, psnId});
  }

  /**
   * nsfcPrjId唯一查询
   * 
   * @param prjId
   * @param psnId
   * @return
   * @throws DaoException
   */
  @Override
  public NsfcReschProject getProjectByNsfcPrjId(Long nsfcPrjId) throws DaoException {
    String hql = "select t from NsfcReschProject t where t.nsfcPrjId=?";
    return this.findUnique(hql, new Object[] {nsfcPrjId});
  }

  @Override
  public List<Long> getRptPubOrder(Long rptId) {
    String hql = "select t.id.pubId from NsfcReschPrjRptPub t where t.id.rptId=? order by t.seqNo";
    return this.createQuery(hql, new Object[] {rptId}).list();
  }

  @Override
  public void updateRptPubOrder(Integer seqNo, Long pubId, Long rptId) {
    String hql = "update NsfcReschPrjRptPub t set t.seqNo=?  where t.id.rptId=? and t.id.pubId=?";
    this.createQuery(hql, new Object[] {seqNo, rptId, pubId}).executeUpdate();
  }

}
