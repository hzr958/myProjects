package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.NsfcReschProject;

/**
 * 结题报告项目.
 * 
 * @author LY
 * 
 */
public interface ReschProjectDao {
  /**
   * 项目表查询 根据项目ID.
   * 
   * @param prjId
   * @param long1
   * @return
   * @throws DaoLevelException
   */
  NsfcReschProject getProjectByPrjId(Long prjId, Long psnId) throws DaoException;

  /**
   * 从结题报告中删除成果.
   * 
   * @param pubId
   * @param nodeId
   * @param rptId
   * @throws DaoException
   */
  void deletePubFromRpt(Long pubId, Integer nodeId, Long rptId) throws DaoException;

  NsfcReschProject getProjectByPrjId(Long prjId) throws DaoException;

  /**
   * nsfcPrjId 唯一查询，结题报告项目.
   * 
   * @param nsfcPrjId
   * @return
   * @throws DaoException
   */
  NsfcReschProject getProjectByNsfcPrjId(Long nsfcPrjId) throws DaoException;

  List<Long> getRptPubOrder(Long rptId);

  void updateRptPubOrder(Integer seqNo, Long long1, Long rptId);

}
