package com.smate.web.fund.service.agency;

import com.smate.core.base.exception.ServiceException;
import com.smate.web.fund.agency.model.AgencySearchForm;

/**
 * 机构检索接口
 * 
 * @author wsn
 *
 */
public interface AgencySearchService {

  /**
   * 检索机构
   * 
   * @return
   * @throws ServiceException
   */
  public AgencySearchForm searchInstitution(AgencySearchForm form) throws ServiceException;

  /**
   * 检索机构后回显信息查询
   * 
   * @param form
   * @return
   * @throws ServiceException
   */
  public String searchInsCallback(AgencySearchForm form) throws ServiceException;

  public String searchInsCallbackNew(AgencySearchForm form) throws ServiceException;

  /**
   * 赞、取消赞操作
   * 
   * @param form
   * @return -1：忽略， >=0: 赞统计数
   * @throws ServiceException
   */
  public Integer awardInsOpt(AgencySearchForm form) throws ServiceException;

  /**
   * 初始化机构信息
   * 
   * @param form
   * @return
   * @throws ServiceException
   */
  public String initInsInfo(AgencySearchForm form) throws ServiceException;

  /**
   * 获取机构URL
   * 
   * @param form
   * @return
   * @throws ServiceException
   */
  public String findInsUrls(AgencySearchForm form) throws ServiceException;

  /**
   * 社交元素操作
   * 
   * @param form
   * @throws ServiceException
   */
  public String optIns(AgencySearchForm form) throws ServiceException;

  /**
   * 检索机构
   * 
   * @return
   * @throws ServiceException
   */
  public AgencySearchForm searchNewInstitution(AgencySearchForm form) throws ServiceException;
}
