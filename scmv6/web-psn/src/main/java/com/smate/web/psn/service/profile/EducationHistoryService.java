package com.smate.web.psn.service.profile;

import java.util.List;

import com.smate.core.base.psn.model.EducationHistory;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.exception.WorkHistoryException;
import com.smate.web.psn.model.homepage.PersonProfileForm;

/**
 * 人员教育经历服务接口
 * 
 * @author Administrator
 *
 */
public interface EducationHistoryService {

  /**
   * 判断用户是否存在教育经历.
   * 
   * @param psnId
   * @return @throws
   */
  boolean isEduHistoryExit(Long psnId) throws PsnException;

  /**
   * 保存教育经历
   * 
   * @param form
   * @param isSyncAuthority
   * @param anyUser
   * @return
   * @throws PsnException
   */
  Long saveEducationHistory(EducationHistory form, boolean isSyncAuthority, Integer anyUser) throws PsnException;

  /**
   * 保存教育经历,如果ID不为空，则更新，如果为空，则添加.
   * 
   * @param isSyncAuthority
   * 
   * 
   * @throws PsnException
   */
  Long saveEduHistory(EducationHistory form, boolean isSyncAuthority, Integer anyUser) throws PsnException;

  /**
   * 查询人员所有的教育经历
   * 
   * @param psnId
   * @return
   * @throws PsnException
   */
  List<EducationHistory> findPsnAllEducationHistory(Long psnId) throws PsnException;

  /**
   * 查找教育经历
   * 
   * @param eduId
   * @return
   * @throws PsnException
   */
  EducationHistory findEducationHistoryById(Long eduId) throws PsnException;

  /**
   * 删除人员教育经历
   * 
   * @param psnId
   * @param eduId
   * @return
   * @throws PsnException
   */
  String delEducationHistory(Long psnId, Long eduId) throws PsnException;

  /**
   * 构建人员教育经历列表信息
   * 
   * @param form
   * @return
   * @throws WorkHistoryException
   */
  PersonProfileForm buildPsnEduHistoryListInfo(PersonProfileForm form) throws WorkHistoryException;

  /**
   * 保存教育经历，不更新首要工作单位
   * 
   * @param form
   * @param isSyncAuthority
   * @param anyUser
   * @param isPrimary
   * @return
   * @throws PsnException
   */
  Long saveEduHistory(EducationHistory form, boolean isSyncAuthority, Integer anyUser, boolean isPrimary)
      throws PsnException;

  /**
   * 保存教育经历，不更新首要工作单位
   * 
   * @param form
   * @param isSyncAuthority
   * @param anyUser
   * @param isPrimary
   * @return
   * @throws PsnException
   */
  Long saveEducationHistory(EducationHistory form, boolean isSyncAuthority, Integer anyUser, boolean isPrimary)
      throws PsnException;

  /**
   * 是否是某个教育经历的拥有者
   * 
   * @param psnId
   * @param eduId
   * @return
   * @throws PsnException
   */
  boolean isOwnerOfEduHistory(Long psnId, Long eduId) throws PsnException;

  /**
   * 教育经历配置信息是否有丢失
   * 
   * @param cnfId
   * @return
   * @throws PsnException
   */
  boolean hasEduConfigLost(Long psnId, Long cnfId) throws PsnException;

  EducationHistory getEducationHistoryByEduId(Long psnId, Long eduId) throws PsnException;
}
