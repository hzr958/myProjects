package com.smate.web.psn.service.profile;

import java.util.List;
import java.util.Map;

import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.psn.exception.PsnCnfException;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.form.mobile.PsnHomepageMobileForm;
import com.smate.web.psn.model.attention.AttPerson;
import com.smate.web.psn.model.friend.PsnListViewForm;
import com.smate.web.psn.model.homepage.PersonProfileForm;
import com.smate.web.psn.model.search.PersonSearch;

/**
 * 个人信息服务接口
 * 
 * @author Administrator
 *
 */
public interface PersonManager {

  public Person getPersonInfo(Long psnId);

  /*
   * 获取人员姓名.
   * 
   * @param person
   * 
   * @return
   * 
   * @throws ServiceException
   */

  /**
   * 获取个人基本信息 给移动端 个人主页用
   * 
   * 注意 不要取多于的东西 要什么取什么 多于的字段 不要
   * 
   * @param form
   * @return
   * @throws PsnException
   */
  public void getBaseInfoForMobilHomepage(PsnHomepageMobileForm form) throws PsnException;

  String getPsnName(Person person, String locale) throws PsnException;

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
  void refreshComplete(Long psnId) throws PsnException;

  /**
   * 根据psnId获取个人基本信息
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  Person getPerson(Long psnId) throws PsnException;

  /**
   * 获取个人简介
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  String getPersonBrief(Long psnId) throws PsnException;

  /**
   * 查询人员用于psnHtml
   */
  List<Person> queryPersonForPsnHtml(List<Long> psnIds) throws PsnException;

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
   * 更新用户工作单位和头衔
   * 
   * @param insId
   * @param insName
   * @param personId
   * @param title
   * @return
   * @throws PsnException
   */
  int updateInsAndTitle(Long insId, String insName, Long personId, String title) throws PsnException;

  /**
   * 更新首要单位.
   * 
   * @version 1.0
   * @since Feb 21, 2012 @11:37:53 AM
   * @param insId
   * @return
   */
  int updateIns(Long insId, String insName, Long personId) throws PsnException;

  /**
   * 取得个人基本信息.
   * 
   * @return Person
   * @throws PsnException
   */
  Person getPersonBaseInfo(Long personId) throws PsnException;

  /**
   * 
   * @param person
   * @return
   * @throws PsnException
   */
  int save(Person person) throws PsnException;

  /**
   * 获取人员的一些基本信息------移动端用 只取一些必要的字段，不必取整个person
   * 
   * @param psnId
   * @return
   * @throws PsnException
   */
  Person getPersonInfoByPsnIdForFriend(Long psnId) throws PsnException;

  /**
   * 列表显示人员时显示的 “工作单位，部门，职称” 构建
   * 
   * @param psnId
   * @return
   * @throws PsnException
   */
  String getPsnViewWorkHisInfo(Long psnId) throws PsnException;

  public String getDomain() throws PsnException;

  /**
   * 更新人员信息完整度并返回人员对象-------------scm-8883
   * 
   * @param psnId
   * @return
   * @throws PsnException
   */
  Person refreshCompleteByPsnId(Long psnId) throws PsnException;

  /**
   * 根据人员ID和语言环境获取人员姓名进行显示
   * 
   * @param psnId
   * @param local
   * @return
   */
  String getPsnNameByIdAndLocal(Long psnId, String local);

  /**
   * 获取人员的头像和名字
   */
  public Person getPsnNameAndAvatars(Long psnId) throws ServiceException;

  /**
   * 获取人员信息和统计信息
   * 
   * @param psnId
   * @param needStatistics
   * @return
   */
  PersonProfileForm getUnifiedPsnInfoByPsnId(PersonProfileForm form);

  /**
   * 构建主页各个模块权限信息
   *
   * @param form
   */
  void buildPsnInfoConfig(PersonProfileForm form) throws PsnCnfException;

  /**
   * 构建人员基本信息用于显示
   *
   * @param form
   * @return
   */
  PersonProfileForm getUnifiedPsnInfoByForm(PersonProfileForm form);

  /**
   * 保存人员简介信息
   * 
   * @param brief
   * @param psnId
   */
  void savePersonBrief(String brief, Long psnId);

  /**
   * 获取用户联系信息-------邮箱、电话
   * 
   * @param psnId
   * @return
   */
  Person findPersonContactInfo(Long psnId);

  /**
   * 更新人员联系信息
   * 
   * @param psnId
   * @param email
   * @param tel
   */
  PersonProfileForm updatePersonContactInfo(PersonProfileForm form);

  /**
   * 保存人员信息---------主页，编辑人员单位、部门、头衔等用
   * 
   * @param form
   * @return
   */
  public void savePsnInfoNew(PersonProfileForm form) throws PsnException;

  /**
   * 保存人员头像
   * 
   * @param psnId
   * @param avatars
   * @return
   */
  public Integer saveAvatars(Long psnId, String avatars);

  /**
   * 设置工作经历为首要工作单位
   * 
   * @param psnId
   * @param workId
   */
  public void setWorkHistoryToPrimary(Person psn, PersonProfileForm form);

  /**
   * 根据PsnId获取构建机构信息
   * 
   * @param psnIds
   * @return
   * @throws ServiceException
   */
  public List<Map<String, String>> findInsNamesBypsnIds(List<Long> psnIds) throws ServiceException;

  /**
   * 根据InsId获取构建机构信息
   * 
   * @param insIds
   * @return
   * @throws ServiceException
   */
  public List<Map<String, String>> findInsNamesByinsIds(List<Long> insIds) throws ServiceException;

  /**
   * 根据psnId获取insId和insId数量
   * 
   * @param psnIds
   * @param languageVersion
   * @return
   * @throws ServiceException
   */

  public List<Map<String, Long>> findInsIdByPsnId(List<Long> psnIds) throws ServiceException;

  List<Map<String, Object>> sortPsnByReg(PsnListViewForm form) throws Exception;

  /**
   * 根据psnId获取地区
   * 
   * @param psnIds
   * @return
   * @throws ServiceException
   */
  public List<Map<String, String>> findInsNamesByregIds(List<Long> psnIds) throws ServiceException;

  /**
   * 根据psnId获取regsId和regId数量
   * 
   * @param psnIds
   * @return
   * @throws ServiceException
   */
  public List<Map<String, Object>> findRegIdByPsnId(List<Long> psnIds) throws ServiceException;

  /**
   * 获取机构
   */
  public List<Map<String, String>> findInsNames(Long psnIds) throws ServiceException;

  /**
   * 获取地区
   */
  public List<Map<String, String>> findRegNames(Long psnIds) throws ServiceException;

  /**
   * 检索人员后构建人员头像地址
   * 
   * @param psnList
   * @throws ServiceException
   */
  public void buildPsnAvatars(List<PersonSearch> psnList) throws ServiceException;

  /**
   * 用邮箱格式字符串匹配人员账号
   * 
   * @param page
   * @param searchString
   * @return
   * @throws PsnException
   */
  Page<PersonSearch> matchPsnByEmail(Page<PersonSearch> page, String searchString) throws PsnException;

  /**
   * 构建地区和机构的数据回显
   * 
   * @param psnIds
   * @param regionId
   * @param insId
   * @return
   * @throws ServiceException
   */
  public String getRecommendInsReg(List<Long> psnIds, PsnListViewForm form) throws ServiceException;

  /**
   * 构建人员单位部门职称信息
   * 
   * @param psn
   * @param locale
   * @return
   * @throws PsnException
   */
  public String getPsnWrokInfo(Person psn, String locale) throws PsnException;

  /**
   * 获取人员地区信息
   * 
   * @param psn
   * @param locale
   * @return
   * @throws PsnException
   */
  public String getPsnRegionInfo(Person psn, String locale) throws PsnException;

  /**
   * 修改人员姓名
   * 
   * @param psnId
   * @return
   * @throws PsnException
   */
  public String editPsnName(PersonProfileForm form) throws PsnException;

  /**
   * 构建人员列表信息
   * 
   * @param psnIds
   * @return
   * @throws PsnException
   */
  public List<PersonSearch> buildPsnInfoForList(List<Long> psnIds) throws PsnException;

  /**
   * 获取用户信息(包含头衔的显示信息).
   * 
   * @param personId
   * @return
   * @throws ServiceException
   */
  public Person getPersonByRecommend(Long personId) throws ServiceException;

  /**
   * 保存当前人邮件语言版本
   * 
   * @param lanVer
   * @return
   */
  public boolean saveCurrPsnEmailLanguageVersion(String lanVer);

  /**
   * 判断此人是否被关注
   * 
   * @param psnId
   * @return
   * @throws Exception
   */
  public AttPerson payAttention(Long psnId, Long reqPsnId) throws Exception;

  /**
   * 更新人员别名常量表
   * 
   * @param psnId
   * @author LIJUN
   * @date 2018年5月29日
   */
  void updatePersonPmName(Long psnId);
}
