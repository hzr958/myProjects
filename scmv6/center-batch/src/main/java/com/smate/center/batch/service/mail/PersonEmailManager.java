package com.smate.center.batch.service.mail;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.psn.register.PersonRegister;
import com.smate.center.batch.model.sns.pub.PersonEmail;

/**
 * 个人邮件管理.
 * 
 * @author new
 * 
 */
public interface PersonEmailManager {

  /**
   * @return List<PersonEmail>
   * @throws ServiceException
   */
  List<PersonEmail> findPersonEmailList() throws ServiceException;

  /**
   * 添加电子邮件，如果邮件已存在，则返回-1，正确放回EMAIL_ID.
   * 
   * @param email
   * @throws ServiceException
   */
  Long addEmail(String email, Long psnId, Integer isFirstMail, Integer isLoginMail) throws ServiceException;

  /**
   * @param emailId
   * @return PersonEmail
   * @throws ServiceException
   */
  PersonEmail findEmailById(Long emailId) throws ServiceException;

  /**
   * @param emailId
   * @throws ServiceException
   */
  int delete(Long emailId, Long psnId) throws ServiceException;

  /**
   * 发送确认邮件.
   * 
   * @param url
   * 
   * @return int
   * @throws ServiceException
   */
  int sendConfirm(Long emailId) throws ServiceException;

  /**
   * 确认邮件.
   * 
   * @param url
   * @return int
   * @throws ServiceException
   */
  int confirmEmail(Long emailId) throws ServiceException;

  /**
   * 确认邮件.
   */
  int confirmEmailByPsnEmailObj(PersonEmail personEmail) throws ServiceException;

  /**
   * 获取确认的邮件列表.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<String> getConfirmEmail(Long psnId) throws ServiceException;

  /**
   * 设置个人首要/登录邮件(同时更新个人email和系统user login).
   * 
   * @param email
   * @return int
   * @throws ServiceException
   */
  int updateFirstEmail(Long emailId) throws ServiceException;

  /**
   * 设置个人首要/登录邮件(同时更新个人email和系统user login).
   * 
   * @param email
   * @return int
   * @throws ServiceException
   */
  int updateFirstEmail(Long emailId, boolean needMail) throws ServiceException;

  /**
   * 更新邮件，注意，该方法不能用于更新首要邮件.
   * 
   * @param email
   * @throws ServiceException
   */
  void updateEmail(PersonEmail email) throws ServiceException;

  /**
   * 通过EMAIL查询用户ID，用户名列表.
   * 
   * @param email
   * @return
   * @throws DaoException
   */
  List<PersonEmail> findListByEmail(String email) throws ServiceException;

  /**
   * 检索用户是否已存在具体邮件.
   * 
   * @param psnId
   * @param email
   * @return
   * @throws ServiceException
   */
  Boolean isEmailExit(Long psnId, String email) throws ServiceException;

  /**
   * 通过邮件用户邮件.
   * 
   * @param email
   * @return
   * @throws ServiceException
   */
  PersonEmail findPersonEmailByEmail(Long psnId, String email) throws ServiceException;

  /**
   * 通过用户ID在cas上找唯一用户.
   * 
   * @param email
   * @return
   * @throws ServiceException
   */
  PersonEmail findPersonEmailByPsnId(Long psnId) throws ServiceException;

  /**
   * 获取指定用户的首要邮件.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  String getFristMail(Long psnId) throws ServiceException;

  /**
   * 获取指定用户的首要邮件.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  PersonEmail getFirstPersonEmail(Long psnId) throws ServiceException;

  PersonEmail getFirstAndNotVerifyPersonEmail(Long psnId) throws ServiceException;

  /**
   * 查询邮件是否在系统中并且是已经验证过的.
   * 
   * @param email
   * @return psnId
   * @throws ServiceException
   */
  List<Long> isExitEmailVerify(String email) throws ServiceException;

  /**
   * 同步V2.6用户EMAIL数据.
   * 
   * @param psnId
   * @param oldList
   * @throws ServiceException
   */
  void syncOldPersonEmail(Long psnId, List<Map<String, Object>> oldList) throws ServiceException;

  /**
   * 检索用户是否已对该邮件进行过确认
   * 
   * @param psnId
   * @param email
   * @return
   * @throws DaoException
   */
  public boolean isPsnVerified(Long psnId, String email) throws ServiceException;

  /**
   * 更新首要邮件的确认状态
   */
  public void updateLoginEmailVerifyStatus(Long psnId, String email) throws ServiceException;

  /**
   * 取得将此邮箱设为首要邮件的人员总数
   * 
   * @param email
   * @return
   * @throws ServiceException
   */
  Long getPersonEmailCountByEmail(String email) throws ServiceException;

  /**
   * 处理注册时同步执行逻辑.
   * 
   * @param person 注册用户.
   * @params params 需配置传递的参数.
   * @return 跳转地址.
   * @throws ServiceException
   */
  String dealRegistAffairByType(PersonRegister person, Map<String, String> params) throws ServiceException;

  /**
   * 获取自动登录地址固定部分(获取完整的自动登录地址需在返回值后追加参数service及对应值).
   * 
   * @param receiverId
   * @param domain
   * @param languageVersion
   * @return
   * @throws UnsupportedEncodingException
   */
  @SuppressWarnings("deprecation")
  String getAutoLoginUrl(Long personID, String casUrl, String languageVersion) throws UnsupportedEncodingException;

  List<PersonEmail> findPersonEmailList(Long psnId) throws ServiceException, DaoException;

  void delete(PersonEmail pe) throws ServiceException;
}
