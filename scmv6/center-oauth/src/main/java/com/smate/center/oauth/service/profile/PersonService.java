package com.smate.center.oauth.service.profile;

import java.util.List;

import com.smate.center.oauth.exception.OauthException;
import com.smate.center.oauth.exception.ServiceException;
import com.smate.core.base.utils.model.security.Person;

/**
 * 个人信息服务接口
 * 
 * @author Administrator
 *
 */
public interface PersonService {

  // 缓存名
  public static final String PSN_BRIEF_CACHE = "sns_psn_brief_cache";
  // 缓存过期时间，30分钟
  public static final int PSN_BRIEF_CACHE_TIMEOUT = 60 * 30;

  /**
   * 刷新人员信息完整度
   * 
   * @param psnId
   * @throws ServiceException
   */
  void refreshComplete(Long psnId) throws OauthException;

  /**
   * 根据psnId获取个人基本信息
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  Person getPerson(Long psnId) throws OauthException;

  /**
   * 获取个人简介
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  String getPersonBrief(Long psnId) throws OauthException;

  /**
   * 查询人员用于psnHtml
   */
  List<Person> queryPersonForPsnHtml(List<Long> psnIds) throws OauthException;

  /**
   * 刷新人员主页地址
   * 
   * @param psnId
   * @param sex
   * @param avator
   * @return
   */
  String refreshRemoteAvatars(Long psnId, Integer sex, String avator);

  /**
   * 获取个人联系方式.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  Person getContact(Long psnId) throws Exception;


}
