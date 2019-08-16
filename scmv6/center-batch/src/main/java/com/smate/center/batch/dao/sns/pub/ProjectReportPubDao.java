package com.smate.center.batch.dao.sns.pub;

import java.util.List;
import java.util.Set;

import com.smate.center.batch.model.sns.pub.NsfcPrjRptPub;
import com.smate.center.batch.model.sns.pub.NsfcPrjRptPubId;
import com.smate.center.batch.model.sns.pub.NsfcProjectReport;


/**
 * 结题报告成果DAO.
 * 
 * @author LY
 * 
 */
public interface ProjectReportPubDao {
  /**
   * 根据结题报告ID 成果列表.
   * 
   * @param rptId @param objectsPerPage @param pageNumber @return @throws
   */
  public List<NsfcPrjRptPub> getProjectReportPubsByRptId(Long rptId, int objectsPerPage, int pageNumber);

  /**
   * 不分页的 成果列表.
   * 
   * @param rptId @return @throws
   */
  public List<NsfcPrjRptPub> getProjectReportPubsByRptId(Long rptId);

  /**
   * 保存更新成果.
   * 
   * @param pub @throws
   */
  public void saveProjectReportPub(NsfcPrjRptPub pub);

  /**
   * 批量保存更新成果.
   * 
   * @param saveList @throws
   */
  public void saveProjectReportPub(List<NsfcPrjRptPub> saveList);

  /**
   * 删除成果.
   * 
   * @param rptId @param pubId @throws
   */
  public void removeProjectReportPublication(Long rptId, Long pubId);

  /**
   * ID查询项目成果.
   * 
   * @param id @return @throws
   */
  public NsfcPrjRptPub getProjectReportPub(NsfcPrjRptPubId id);

  /**
   * 批量删除成果.
   * 
   * @param rptId
   * @param pubIds pubIds的长度必须大于0；
   */
  public void removeProjectReportPublication(Long rptId, Long[] pubIds);

  List<NsfcProjectReport> getProjectReportPubsByPrjId(Long prjId);

  Long getCountProjectReportPubsByRptId(Long rptId);

  Long getNsfcPrjRptPubMaxSeqNo(Long rptId);

  NsfcProjectReport getProjectReportPubsByPrjIdAndYear(Long prjId, Integer year);

  public Set<Long> getProjectReportPubsIdByRptId(Long rptId);

  /**
   * 更新是否标注.
   * 
   * @param isTag @param prjId @param rptId @param pubId @throws
   */
  public void saveProjectReportTag(Integer isTag, Long prjId, Long rptId, Long pubId);

  /**
   * 更新成果的引用情况.
   * 
   * @param listInfo
   */
  public void saveProjectReportPubList(String listInfo, Long rptId, Long pubId);

  public NsfcPrjRptPub getById(NsfcPrjRptPubId rptPubId);

  /**
   * 根据PubId查找结题报告成果
   * 
   * @param pubId @return @throws
   */
  public List<NsfcPrjRptPub> findPrjPrtPubByPubId(Long pubId);

  public List<NsfcPrjRptPub> queryNsfcPrjRptPubByPsnId(Long psnId);

  public Long queryNsfcPrjRptPubCount(Long psnId, String title);

  public void deleteNsfcPrjRptPub(Long rptId, Long pubId);

  public void updateNsfcPrjRptPub(Long rptId, Long pubId, Long savePsnId);
}
