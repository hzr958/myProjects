package com.smate.center.batch.service.psn;

import java.util.List;
import java.util.Locale;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.psn.model.WorkHistory;
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

  /**
   * @param personId
   * @return @
   */
  Person getPersonByRecommend(Long personId);

  /**
   * 获取人员姓名.
   * 
   * @param person
   * @return @
   */
  String getPsnName(Person person);

  public String getPsnName(Person person, String locale);

  Person getPerson(Long psnId);

  /**
   * 读取个人简介.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  String getPersonBrief(Long psnId) throws ServiceException;

  /**
   * 获取个人头像信息.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  Person getAvatarsForEdit(Long psnId) throws ServiceException;

  /**
   * 获取用户头像，用户可能是其他节点，需要拼接URL.
   * 
   * @param psnId
   * @param avatorr
   * @return
   * @throws ServiceException
   */
  String refreshRemoteAvatars(Long psnId, Integer sex, String avator);

  /**
   * 获取人员联系
   * 
   * @param psnId
   * @return
   * @throws Exception
   */
  public Person getContact(Long psnId) throws Exception;

  public String initPersonAvatars(Integer nodeId, Integer sex);

  public List<Person> findPersonList(List<Long> psnIdList) throws ServiceException;

  public Person getPsnNameAndAvatars(Long psnId) throws ServiceException;

  public List<WorkHistory> findWorkAndEdu(Long psnId) throws ServiceException;

  /**
   * 获取人员头衔显示信息<显示人员工作单位并取消头衔>_MJG_SCM-5707.
   * 
   * @param psn
   * @return
   * @throws ServiceException
   */
  public String getPsnViewTitolo(Person psn) throws ServiceException;

  public Person getPersonForEmail(Long psnId) throws ServiceException;

  Person getPersonById(Long psnId) throws ServiceException;

  /**
   * 获取人员姓名.
   * 
   * @param psnId
   * @param locale TODO
   * @return
   * @throws ServiceException
   */
  String getPsnName(Long psnId, Locale locale) throws ServiceException;

  /**
   * 更新信息完整度百分比.
   * 
   * 完整度标准:
   * 
   * 工作经历 20% 教育经历 20% 姓名20%.
   * 
   * 联系方式 15%（填写一项） 所在地 5% .
   * 
   * 头像 5% 熟悉学科10% 个人简介5%.
   * 
   * @param psnId
   * @throws ServiceException
   */
  public void refreshComplete(Long psnId) throws ServiceException;

}
