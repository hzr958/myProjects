package com.smate.web.psn.service.profile;

import com.smate.core.base.psn.dto.profile.Personal;
import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.web.psn.exception.PsnException;

/**
 * 个人专长、研究领域服务接口
 * 
 * @author Administrator
 *
 */
public interface PersonalManager {

  /**
   * 通过id获取学科下拉树JSON，二级,三级...
   * 
   * @param id
   * @return
   * @throws ServiceException
   */
  // String getDiscTreeJsonBySub(Long id, Locale... locales) throws
  // ServiceException;

  int savePersonal(Personal form, Integer anyUser) throws PsnException;

  /**
   * @param personal
   * @throws ServiceException
   */
  int savePersonal(Personal form) throws PsnException;

  /**
   * 同步用户熟悉的领域.
   * 
   * @param psnId
   * @throws ServiceException
   */
  void syncPsnDicipline(Long psnId) throws PsnException;

  /**
   * 获取刷新用户信息完整度的数据.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  boolean isPsnDiscExit(Long psnId) throws PsnException;

  /**
   * 在center-batch那边更新人员solr信息
   * 
   * @param psnId
   */
  public void refreshPsnSolrInfoByTask(Long psnId);

  /**
   * 在center-batch那边删除人员solr信息
   * 
   * @param psnId
   */
  public void deletePsnSolrInfoByTask(Long psnId);

  /**
   * 在center-batch那边初始化人员信息
   * 
   * @param psnId
   */
  public void initPsnConfigInfoByTask(Long psnId);

  /**
   * 查找人员需要更新的配置信息
   * 
   * @param PsnCnfAct 需要检查的配置类型
   * @param psnId 人员ID
   * @param cnfId 配置ID
   * @return
   * @throws PsnException
   */
  public Long findPsnConfigNeedRefreshData(PsnCnfEnum PsnCnfAct, Long psnId, Long cnfId) throws PsnException;

  /**
   * 调用接口同步个人信息到SIE
   * 
   * @param psnId
   * @throws PsnException
   */
  public void updateSIEPersonInfo(Long psnId) throws Exception;

}
