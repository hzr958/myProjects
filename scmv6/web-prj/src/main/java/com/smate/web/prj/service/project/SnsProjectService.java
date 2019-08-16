package com.smate.web.prj.service.project;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.transaction.Transactional;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.project.model.PrjMember;
import com.smate.core.base.project.model.Project;
import com.smate.web.prj.form.ProjectForm;

/**
 * 项目服务接口.
 * 
 * @author liqinghua
 * 
 */
/**
 * @author Administrator
 * 
 */
@Transactional(rollbackOn = Exception.class)
public interface SnsProjectService extends Serializable {

  /**
   * 删除项目.
   * 
   * @param prjId
   * @throws ServiceException
   */
  void deleteProject(String getPrjIds) throws ServiceException;

  /**
   * 删除项目.
   * 
   * @param prjIds
   * @throws ServiceException
   */
  void deleteProjectForBpo(String prjIds) throws ServiceException;

  /**
   * 新增项目.
   * 
   * @param postData
   * @return
   * @throws ServcieException
   */
  @SuppressWarnings("unchecked")
  Long addProject(Map postData) throws ServiceException;

  /**
   * 同步V2.6项目.
   * 
   * @param oldData
   * @throws ServiceException
   */
  void syncOldProject(Map<String, Object> oldData) throws ServiceException;

  /**
   * 更新项目.
   * 
   * @return prjId
   * @param postData
   * 
   * @throws ServcieException
   */
  @SuppressWarnings("unchecked")
  Long updateProject(long prjId, Map postData) throws ServiceException;

  /**
   * 读取项目.
   * 
   * @param form
   * @return ProjectForm
   * @throws ServiceException
   */
  public ProjectForm getProject(ProjectForm form) throws ServiceException;

  /**
   * 读取项目.
   * 
   * @param form
   * @return ProjectForm
   * @throws ServiceException
   */
  public ProjectForm getProject(Long prjId) throws ServiceException;

  /**
   * 通过prjId获取成果XML.
   * 
   * @param prjId
   * @return
   * @throws ServiceException
   */
  public String getPrjXmlById(Long prjId) throws ServiceException;

  /**
   * 直接用XML更新项目数据.
   * 
   * @param xml
   * @param prjId
   * @throws ServiceException
   */
  /* public void updateProject(String xml, PrjXmlProcessContext context) throws ServiceException; */

  /**
   * 获取项目数据主体.
   * 
   * @param prjId
   * @return
   * @throws ServiceException
   */
  public Project getProjectById(Long prjId) throws ServiceException;

  /**
   * 导入他人的到我的项目库.
   * 
   * @throws ServiceException
   */
  public void importPrjToMyLib(String jsonParams) throws ServiceException;

  /**
   * 获取项目总数.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public Integer getSumProject(Long psnId) throws ServiceException;

  /**
   * 批量获取Project表数据，数据同步后重构XML用.
   * 
   * @param lastId
   * @param batchSize
   * @return
   * @throws ServiceException
   */
  public List<Project> getPrjByBatchForOld(Long lastId, int batchSize) throws ServiceException;

  /**
   * 重构导入的项目数据.
   * 
   * @param pub
   * @throws ServiceException
   */
  public void rebuildOldPrjData(Project prj) throws ServiceException;

  /**
   * 重构成果全文附件权限.
   * 
   * @param pub
   * @throws ServiceException
   */
  public void rebuildPrjFulltext(Project prj) throws ServiceException;

  /**
   * dbid=13，修正原项目资金(单位)错误.
   * 
   * @param lastId
   * @param batchSize
   * @return
   * @throws ServiceException
   */
  List<Long> getPrjByBatchAmountUnitDbId13(Long lastId, int batchSize) throws ServiceException;

  /**
   * 修正xml中原项目资金(单位)错误.
   * 
   * @param prjId
   * @throws ServiceException
   */
  void rebuildPrjXMLAmountUnit(Long prjId) throws ServiceException;

  /**
   * 批量获取数据库表中的数据重构XML用.
   * 
   * @param lastId
   * @param batchSize
   * @return
   * @throws ServiceException
   */
  public List<Project> loadRebuildPrjId(Long lastId, int batchSize) throws ServiceException;

  /**
   * 检查是否存在重复的项目.
   * 
   * @param jsonParams
   * @return
   * @throws ServiceException
   */
  Object[] getProjectDup(String jsonParams) throws ServiceException;

  /**
   * 批量检索项目ID，用于跑任务.
   * 
   * @param lastId
   * @param batchSize
   * @return
   * @throws ServiceException
   */
  List<Long> findPrjIdsBatch(Long lastId, int batchSize) throws ServiceException;

  /**
   * 重构来源字段.
   * 
   * @param lastId
   * @param batchSize
   * @return
   * @throws ServiceException
   */
  Long briefDescTask(Long prjId) throws ServiceException;

  /**
   * 获取项目拥有者Id和项目状态.
   * 
   * @param prjId
   * @return
   * @throws ServiceException
   */
  Project getPrjOwnerPsnIdOrStatus(Long prjId) throws ServiceException;

  /**
   * 获取项目所有者.
   * 
   * @param prjId
   * @return
   * @throws ServiceException
   */
  Long getPrjOwner(Long prjId) throws ServiceException;

  /**
   * 
   * @author liangguokeng
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<Project> findPrjIdsByPsnId(Long psnId) throws ServiceException;

  /**
   * @param lastId
   * @param batchSize
   * @return
   * @throws ServiceException
   */
  List<Long> findRebuildPrjId(int batchSize) throws ServiceException;

  /**
   * 重新构造项目XML中的author_names字段并且同步更新project表和group_prjs表中的author_names冗余字段.
   * 
   * @param prjId
   * @throws ServiceException
   */
  void rebuildSnsPrjAuthorNames(Long prjId) throws ServiceException;

  /**
   * 更新重构项目XML中的author_names字段任务列表.
   * 
   * @param prjId
   * @throws ServiceException
   */
  void updateTaskPrjAuthor(Long prjId) throws ServiceException;

  /**
   * 更新项目全文附件.
   * 
   * @param pubId
   * @param params
   * @return
   * @throws ServiceException
   */
  Map<String, String> updatePrjXmlFulltext(Long prjId, Map<String, Object> params) throws ServiceException;

  /**
   * 通过项目ID查询项目合作者
   * 
   * @param prjId
   * @return
   * @throws ServiceException
   */
  public List<PrjMember> getPrjMembersByPrjId(Long prjId) throws ServiceException;

  /**
   * 获取项目全文附件相关信息.
   * 
   * @param prjId
   * @param psnId
   * @param des3ResSendId
   * @param des3ResRecId
   * @param locale
   * @return
   * @throws ServiceException
   */
  String getPrjFullTextInfo(Long prjId, Long psnId, String des3ResSendId, String des3ResRecId, Locale locale)
      throws ServiceException;

  /**
   * 获取项目全文附件相关信息.
   * 
   * @param prjId
   * @return
   * @throws ServiceException
   */
  Map<String, String> getPrjFullTextInfo(Long prjId) throws ServiceException;

  /**
   * 判断项目是否删除.
   * 
   * @param prjId
   * @return
   * @throws ServiceException
   */
  boolean checkPrjIsDeleteByPrjId(Long prjId) throws ServiceException;

  public Map<String, Object> getShowPrjData(Long prjId) throws ServiceException;

  /**
   * 构建项目参与人信息
   * 
   * @param prjMemberList
   * @return
   * @throws ServiceException
   */
  public List<Map<String, Object>> buildInvestigators(List<Map<String, Object>> prjMemberList) throws ServiceException;
}
