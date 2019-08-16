package com.smate.web.psn.service.profile;

import java.util.List;

import com.smate.core.base.psn.model.WorkHistory;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.exception.WorkHistoryException;
import com.smate.web.psn.model.homepage.PersonProfileForm;

/**
 * 人员工作经历服务接口
 * 
 * @author Administrator
 *
 */
public interface WorkHistoryService {

  /**
   * 判断用户是否存在工作经历.
   * 
   * @param psnId
   * @return @throws
   */
  boolean isWorkHistoryExit(Long psnId) throws PsnException;

  /**
   * 保存工作经历，编辑、添加.
   */
  Long saveWorkHistory(WorkHistory workHistory, boolean isSyncAuthority, Integer anyUser) throws WorkHistoryException;

  /**
   * 保存工作经历，编辑、添加.
   * 
   * @param workHistory @param isSyncAuthority
   * 
   * @throws
   */
  Long saveWorkHistory(WorkHistory workHistory, Integer anyUser) throws WorkHistoryException;

  /**
   * 删除人员工作经历
   * 
   * @param workId
   * @param psnId
   * @throws WorkHistoryException
   */
  String delWorkHistory(Long workId, Long psnId) throws WorkHistoryException;

  /**
   * 构建人员工作经历选项，供编辑人员信息是选择
   * 
   * @return
   * @throws WorkHistoryException
   */
  PersonProfileForm buildPsnWorkHistorySelector(PersonProfileForm form) throws WorkHistoryException;

  /**
   * 构建人员工作经历列表信息
   * 
   * @param form
   * @return
   * @throws WorkHistoryException
   */
  PersonProfileForm buildPsnWorkHistoryListInfo(PersonProfileForm form) throws WorkHistoryException;

  /**
   * 新的保存或更新工作经历，没有设置首要工作单位，默认不更新首要工作单位
   * 
   * @param workHistory
   * @param anyUser
   * @param isPrimary
   * @return
   * @throws WorkHistoryException
   */
  Long saveWorkHistory(WorkHistory workHistory, Integer anyUser, Long isPrimary) throws WorkHistoryException;

  /**
   * 新的保存或更新工作经历
   * 
   * @param workHistory
   * @param isSyncAuthority
   * @param anyUser
   * @param isPrimary
   * @return
   * @throws WorkHistoryException
   */
  Long saveWorkHistory(WorkHistory workHistory, boolean isSyncAuthority, Integer anyUser, Long isPrimary)
      throws WorkHistoryException;

  /**
   * 重新设置人员单位信息
   * 
   * @param psnId
   */
  void resetPsnInsInfo(Long psnId);

  /**
   * 是否是某个工作经历的拥有者
   * 
   * @param psnId
   * @param workId
   * @return
   * @throws WorkHistoryException
   */
  boolean isOwnerOfWorkHistory(Long psnId, Long workId) throws WorkHistoryException;

  /**
   * 是否是首要工作经历
   * 
   * @param psnId
   * @param workId
   * @return
   * @throws WorkHistoryException
   */
  Long isPrimaryWorkHistory(Long psnId, Long workId) throws WorkHistoryException;

  /**
   * 获取指定人员的工作经历单位ID.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<Long> findWorkByPsnId() throws ServiceException;

  /**
   * 是否有工作经历配置信息丢失
   * 
   * @param cnfId
   * @return
   * @throws WorkHistoryException
   */
  public boolean hasPsnConfigWorkLost(Long psnId, Long cnfId) throws WorkHistoryException;

  /**
   * 获取工作经历
   * 
   * @param psnId
   * @return
   * @throws WorkHistoryException
   */
  WorkHistory getWorkHistoryByPsnId(Long psnId) throws WorkHistoryException;

  List<WorkHistory> buildSimplePsnWorkHistorySelector(Long psnId) throws WorkHistoryException;

}
