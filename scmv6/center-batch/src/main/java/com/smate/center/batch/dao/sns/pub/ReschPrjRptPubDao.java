package com.smate.center.batch.dao.sns.pub;

import java.util.List;
import java.util.Set;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.NsfcReschPrjRptPub;
import com.smate.center.batch.model.sns.pub.NsfcReschPrjRptPubId;
import com.smate.center.batch.model.sns.pub.NsfcReschProjectReport;

/**
 * 成果研究报告的DAO实现.
 * 
 * @author oyh
 * 
 */
public interface ReschPrjRptPubDao {
  /**
   * 根据结题报告ID 成果列表.
   * 
   * @param rptId
   * @param objectsPerPage
   * @param pageNumber
   * @return
   * @throws DaoException
   */
  public List<NsfcReschPrjRptPub> getProjectReportPubsByRptId(Long rptId, int objectsPerPage, int pageNumber)
      throws DaoException;

  /**
   * 不分页的 成果列表.
   * 
   * @param rptId
   * @return
   * @throws DaoException
   */
  public List<NsfcReschPrjRptPub> getProjectReportPubsByRptId(Long rptId) throws DaoException;

  /**
   * 保存更新成果.
   * 
   * @param pub
   * @throws DaoException
   */
  public void saveProjectReportPub(NsfcReschPrjRptPub pub) throws DaoException;

  /**
   * 批量保存更新成果.
   * 
   * @param saveList
   * @throws DaoException
   */
  public void saveProjectReportPub(List<NsfcReschPrjRptPub> saveList) throws DaoException;

  /**
   * 删除成果.
   * 
   * @param rptId
   * @param pubId
   * @throws DaoException
   */
  public void removeProjectReportPublication(Long rptId, Long pubId) throws DaoException;

  /**
   * ID查询项目成果.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  public NsfcReschPrjRptPub getProjectReportPub(NsfcReschPrjRptPubId id) throws DaoException;

  /**
   * 批量删除成果.
   * 
   * @param rptId
   * @param pubIds pubIds的长度必须大于0；
   */
  public void removeProjectReportPublication(Long rptId, Long[] pubIds) throws DaoException;

  List<NsfcReschProjectReport> getProjectReportPubsByPrjId(Long prjId) throws DaoException;

  Long getCountProjectReportPubsByRptId(Long rptId) throws DaoException;

  Long getNsfcPrjRptPubMaxSeqNo(Long rptId, Integer defType) throws DaoException;

  NsfcReschProjectReport getProjectReportPubsByPrjIdAndYear(Long prjId, Integer year) throws DaoException;

  public Set<Long> getProjectReportPubsIdByRptId(Long rptId) throws DaoException;

  /**
   * 更新是否标注.
   * 
   * @param isTag
   * @param prjId
   * @param rptId
   * @param pubId
   * @throws DaoException
   */
  public void saveProjectReportTag(Integer isTag, Long prjId, Long rptId, Long pubId) throws DaoException;

  /**
   * 更新成果的引用情况.
   * 
   * @param listInfo
   */
  public void saveProjectReportPubList(String listInfo, Long rptId, Long pubId) throws DaoException;

  public NsfcReschPrjRptPub getById(NsfcReschPrjRptPubId rptPubId) throws DaoException;

  /**
   * 获取排序前五条成果
   * 
   * @param rptId
   * @return
   * @throws DaoException
   */

  List<Long> getTop5ProjectReportPubsByRptId(Long rptId) throws DaoException;

  /**
   * 根据PubId获取研究报告成果
   * 
   * @param pubId
   * @return
   * @throws DaoException
   */
  List<NsfcReschPrjRptPub> findNsfcReschPrjRptPubByPubId(Long pubId) throws DaoException;

  /**
   * 计算某个类型的成果个数
   * 
   * @param rptId
   * @param defType
   * @return
   * @throws DaoException
   */
  Long countDefPubType(Long rptId, Integer defType) throws DaoException;

  /**
   * 查找某个类型的成果
   * 
   * @param rptId
   * @param defType
   * @return
   * @throws DaoException
   */
  List<NsfcReschPrjRptPub> findPubsByType(Long rptId, Integer defType) throws DaoException;

  /**
   * 查找某个类型的成果ID
   * 
   * @param rptId
   * @param defType
   * @return
   * @throws DaoException
   */
  List<Long> findPubIdsByType(Long rptId, Integer defType) throws DaoException;

  List<NsfcReschPrjRptPub> getNsfcReschPrjRptPubByPsnId(Long prjId) throws DaoException;

  Long getNsfcReschPrjRptPubCount(Long psnId, String title) throws DaoException;

  void deleteNsfcReschPrjRptPub(Long rptId, Long pubId) throws DaoException;

  void updateNsfcReschPrjRptPub(Long rptId, Long pubId, Long savePsnId) throws DaoException;

}
