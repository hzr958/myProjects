package com.smate.center.open.service.profile;

import java.util.List;
import java.util.Locale;

import com.smate.center.open.exception.OpenException;
import com.smate.core.base.utils.model.security.Person;

/**
 * @author ajb
 * 
 */

public interface PersonManager {

  // 缓存名
  public static final String PSN_BRIEF_CACHE = "sns_psn_brief_cache";
  // 缓存过期时间，30分钟
  public static final int PSN_BRIEF_CACHE_TIMEOUT = 60 * 30;

  /**
   * 获取人员姓名.
   * 
   * @param psnId
   * @param locale TODO
   * @return
   * @throws ServiceException
   */
  String getPsnName(Long psnId, Locale locale) throws Exception;

  String getPsnName(Person person, String locale) throws Exception;

  Person getPerson(Long psnId) throws Exception;

  /**
   * 判断psnId是否存在
   * 
   * @param psnId
   * @return
   * @throws Exception
   */
  boolean checkPsnId(Long psnId) throws Exception;

  /**
   * 获取人员信息.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  Person getPersonById(Long psnId) throws Exception;

  List<Person> getPersonList(String psnName, String insName, String email) throws Exception;

  /**
   * 获取联系信息
   * 
   * @param psnId
   * @return
   * @throws Exception
   */
  public Person getContact(Long psnId) throws Exception;

  /**
   * 获取人员简介
   * 
   * @param psnId
   * @return
   * @throws OpenException
   */
  public String getPersonBrief(Long psnId) throws OpenException;

  /**
   * 更新人员信息完整度
   * 
   * @param psnId
   * @throws OpenException
   */
  public void refreshComplete(Long psnId) throws OpenException;

}
