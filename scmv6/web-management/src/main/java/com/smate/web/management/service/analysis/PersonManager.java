package com.smate.web.management.service.analysis;

import java.util.List;

import com.smate.core.base.utils.model.security.Person;


/**
 * @author zt
 * 
 */
public interface PersonManager {

  // 缓存名
  public static final String PSN_BRIEF_CACHE = "sns_psn_brief_cache";
  // 缓存过期时间，30分钟
  public static final int PSN_BRIEF_CACHE_TIMEOUT = 60 * 30;


  Person getPerson(Long psnId) throws Exception;

  /**
   * 过滤用户id取得合作者id.
   * 
   * @param insId
   * @param psnIdList
   * @return
   * @throws ServiceException
   */
  public List<Long> getCooperatorPsnId(Long insId, List<Long> psnIdList) throws Exception;

  /**
   * 构建人员显示名称_MJG_SCM-5707.
   * 
   * @param zhName
   * @param firstName
   * @param lastName
   * @return
   */
  public String getPsnViewName(String zhName, String firstName, String lastName);

  /**
   * 获取人员头衔显示信息,包含了dept,ins,title,用于用户推荐
   * 
   * @param psn
   * @return
   * @throws ServiceException
   */
  public String getPsnViewTitoloApplication(Person psn);

  Person getPersonByRecommend(Long mergePsnId) throws Exception;

}
