package com.smate.center.batch.service.pub;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.NsfcReschPrjRptPub;
import com.smate.center.batch.model.sns.pub.NsfcReschProject;
import com.smate.center.batch.model.sns.pub.NsfcReschProjectReport;
import com.smate.center.batch.model.sns.pub.PublicationForm;
import com.smate.center.batch.model.sns.pub.ReschProjectReportModel;
import com.smate.center.batch.model.sns.pub.ReschProjectReportPubModel;
import com.smate.core.base.utils.model.Page;

/**
 * 成果研究报告.
 * 
 * @author oyh
 * 
 */
public interface PubResearchRptService {
  /**
   * 成果研究报告列表. TODO 未实现.
   * 
   * @return
   * @throws ServiceException
   */
  Page getProjectReports() throws ServiceException;

  /**
   * 根据用户ID 获取 成果研究报告列表.
   * 
   * @return
   * @throws ServiceException
   */
  List<NsfcReschProjectReport> getProjectReportsByPsnId(Long psnId) throws ServiceException;

  /**
   * 根据登录用户ID 成果研究列表.
   * 
   * @return
   * @throws ServiceException
   * @throws DaoLevelException
   */
  Page getProjectReportsByPsnId() throws ServiceException;

  /**
   * 根据成果研究报告ID 成果研究报告列表.为实现.
   * 
   * @return
   * @throws ServiceException
   */
  List<NsfcReschProjectReport> getProjectReportByPrjId(Long prjId) throws ServiceException;

  /**
   * 根据结题报告ID、year查询 结题报告列表.
   * 
   * @return
   * @throws ServiceException
   */
  ReschProjectReportModel getProjectReportByPrjIdAndYear(Long prjId, String year) throws ServiceException;

  /**
   * 成果列表.
   * 
   * @param prjId 项目ID
   * @param rptId 结题报告ID
   * @param pageNumber
   * @param objectsPerPage
   * @return
   * @throws DaoLevelException
   */
  Page getProjectReportSubmits(Long rptId, int objectsPerPage, int pageNumber) throws ServiceException;

  /**
   * 获得成果列表的ID字符串.
   * 
   * @return
   */
  String getProjectReportPubIds(Long rptId) throws ServiceException;

  /**
   * 添加成果列表 注意这里用个同步字段的设置 值为1.
   * 
   * @param list
   * @throws ServiceException
   * @throws HibernateUtilException
   */
  void saveAddProjectReportPub(List<Map> list, Long rptId, Boolean isAdd) throws ServiceException;

  /**
   * 删除成果.
   * 
   * @param rptId
   * @param pubId
   * @throws ServiceException
   */
  void removeProjectReportPub(Long rptId, Long pubId) throws ServiceException;

  /**
   * 更新是否标注和是否公开.
   * 
   * @param isOpens
   * @param isTags
   * @param oldTags
   * @param oldOpens
   * @param rptId
   */
  void saveProjectReportPubList(String isOpens, String isTags, String cancelIsOpens, String cancelIsTags, Long rptId)
      throws ServiceException;

  /**
   * 从ISIS 传递过来的 nsfcPrjId.
   * 
   * @param nsfcPrjId
   * @return
   */
  NsfcReschProject getProjectByNsfcPrjId(Long nsfcPrjId) throws ServiceException;

  /**
   * 批处理删除成果.
   * 
   * @param rptId
   * @param selectedDelValues
   * @throws ServiceException
   */
  void removeProjectReportPub(Long rptId, String selectedDelValues) throws ServiceException;

  /**
   * 成果列表 所有记录.
   * 
   * @param prjId 项目ID
   * @param rptId 结题报告ID
   * @param pageSize
   * @param pageNumber
   * @param objectsPerPage
   * @return
   * @throws DaoLevelException
   */
  List<NsfcReschPrjRptPub> getProjectReportSubmitsAll(Long rptId) throws ServiceException;

  /**
   * 保存CiteWrite中的收录情况.
   * 
   * @param citation
   * @param pubId
   * @param rptId
   */
  void saveProjectReportCitation(String citation, Long pubId, Long rptId) throws ServiceException;

  /**
   * 判断项目是否是操作者所有.
   * 
   * @param prjId
   * @return
   */
  Boolean isProjectOwner(Long prjId) throws ServiceException;

  /**
   * 保存排序.
   * 
   * @param form TODO
   */
  void savePublicationSort(ReschProjectReportPubModel form) throws ServiceException;

  /**
   * 更改研究报告成果的类型
   * 
   * @param form
   * @return
   * @throws ServiceException
   */
  boolean updateDefType(ReschProjectReportPubModel form) throws ServiceException;

  /**
   * 根据prjId取最新一年的结题报表.
   * 
   * @param prjId
   * @param year
   * @return
   * @throws ServiceException
   */
  ReschProjectReportModel getProjectReportLastYear(Long prjId, String year) throws ServiceException;

  /**
   * 已经加入结题报告的成果ID.
   * 
   * @param rptId
   * @return
   */
  Set<Long> getPubIdsPrjFinalPubByRptId(Long rptId) throws ServiceException;

  /**
   * 把我的成果添加到结题报告中. <br/>
   * <ul>
   * <li>000000:操作成功</li>
   * <li>000001:title超长</li>
   * </ul>
   * 
   * @param pubIds
   * @param rptId
   * @return
   */
  boolean addPublicationFromMyMate(ReschProjectReportPubModel form) throws ServiceException;

  /**
   * 更新是否标识.
   * <p/>
   * 1:标注；0：不标注
   * 
   * @param isTag
   * @param pubId
   * @param rptId
   */
  void saveProjectReportTag(Integer isTag, Long pubId, Long rptId) throws ServiceException;

  /**
   * 更新是否开发.
   * <p/>
   * 1:开放；0：不开放
   * 
   * @param isOpen
   * @param pubId
   * @param rptId
   */
  void saveProjectReportOpen(Integer isOpen, Long pubId, Long rptId) throws ServiceException;

  /**
   * 从结题报告成果列表中删除成果.
   * 
   * @param form
   */
  void removeProjectReportPub(ReschProjectReportPubModel form) throws ServiceException;

  /**
   * 根据prjId查询项目.
   * 
   * @param prjId
   * @return
   * @throws ServiceException
   */
  NsfcReschProject getProjectByPrjIdAndPsnId(Long prjId) throws ServiceException;

  /**
   * 完成结题报告的填写.
   * 
   * @param form
   * @throws ServiceException
   */
  void saveReportPublication(ReschProjectReportPubModel form) throws ServiceException;

  /**
   * 根据条件找出最相近的解题报告.
   * 
   * @param nsfcPrjId
   * @param rptYear
   * @return
   * @throws ServiceException
   */
  NsfcReschProjectReport getSimilarProjectReport(Long nsfcPrjId, Long rptYear) throws ServiceException;

  /**
   * CiteWrit的已添加到结题报告中的成果列表.
   * 
   * @param rptId
   * @return
   */
  List<NsfcReschPrjRptPub> getProjectReportSubmitsAllForCw(Long rptId) throws ServiceException;

  /**
   * 同步V2.6数据.
   * 
   * @param obj
   * @throws ServiceException
   */
  void syncOldNsfcProjectReport(Map<String, Object> oldData) throws ServiceException;

  /**
   * 同步v2.6数据.
   * 
   * @param oldData
   * @throws ServiceException
   */
  void syncOldNsfcPrjRptPub(Map<String, Object> oldData) throws ServiceException;

  void syncPublicationToFinalReport(PublicationForm loadXml) throws ServiceException;

  /**
   * 根据条件查询的结题报告成果.
   * 
   * @param nsfcPrjId
   * @param rptYear
   * @return
   * @throws ServiceException
   */
  List<NsfcReschPrjRptPub> getProjectFinalPubs(Long nsfcPrjId, Integer rptYear) throws ServiceException;

  /**
   * 验证成果研究报告前五条是否有附件
   * 
   * @param nsfcPrjId
   * @param rptYear
   * @return
   * @throws ServiceException
   */
  boolean validateTop5PubAttachment(Long nsfcPrjId, Integer rptYear) throws ServiceException;

  /**
   * 修改成果研究报告版本Id
   * 
   * @param form
   * @throws ServiceException
   */
  void updateReschRptVersionId(ReschProjectReportPubModel form) throws ServiceException;

  /**
   * 获取报告的版本号
   * 
   * @param nsfcPrjId
   * @param rptYear
   * @return
   * @throws ServiceException
   */

  Long getReschRptVersionId(Long nsfcPrjId, Integer rptYear) throws ServiceException;


  /**
   * 根据PubId查找基金委研究报告成果
   * 
   * @param PubId
   * @return
   * @throws ServiceException
   */
  List<NsfcReschPrjRptPub> findNsfcReschPrjRptPubByPubId(Long pubId) throws ServiceException;

  /**
   * 保存基金委研究报告成果
   * 
   * @param nrpppList
   * @throws ServiceException
   */
  void saveNsfcReschPrjRptPubList(List<NsfcReschPrjRptPub> nrpppList) throws ServiceException;

  /**
   * 查找研究成果报告状态
   * 
   * @param rptId
   * @return
   * @throws ServiceException
   */
  Integer findStatusByRptId(Long rptId) throws ServiceException;

  /**
   * 根据ID查找报告
   * 
   * @param rptId
   * @return
   * @throws ServiceException
   */
  NsfcReschProjectReport findById(Long rptId) throws ServiceException;
}
