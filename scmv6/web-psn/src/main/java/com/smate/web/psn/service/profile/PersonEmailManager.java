package com.smate.web.psn.service.profile;

import java.util.List;

import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.form.PersonEmailInfo;
import com.smate.web.psn.model.psninfo.PersonEmailRegister;

/**
 * 个人邮件管理.
 * 
 * @author zx
 */
public interface PersonEmailManager {

  /**
   * 获取指定用户的首要邮件.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  String getFristMail(Long psnId) throws ServiceException;

  /**
   * 查询邮件是否在系统中并且是已经验证过的.
   * 
   * @param email
   * @return
   * @throws ServiceException
   */
  List<Long> isExitEmailVerify(String email) throws ServiceException;

  /**
   * 更新首要邮件的确认状态
   * 
   * @param psnId
   * @param email
   * @throws ServiceException
   */
  public void updateLoginEmailVerifyStatus(Long psnId, String email) throws ServiceException;

  /**
   * 添加电子邮件，如果邮件已存在，则返回-1，正确放回EMAIL_ID.
   * 
   * @param email
   * @throws ServiceException
   */
  public Long addEmail(String email, Long psnId, Long isFirstMail, Long isLoginMail) throws ServiceException;

  /**
   * 删除邮件
   * 
   * @param emailId
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public int delete(Long emailId, Long psnId) throws ServiceException;

  /**
   * 设置个人首要/登录邮件(同时更新个人email和系统user login).
   * 
   * @param email
   * @return int
   * @throws ServiceException
   */
  int updateFirstEmail(Long emailId, boolean needMail) throws ServiceException;

  /**
   * @return List<PersonEmail>
   * @throws ServiceException
   */
  List<PersonEmailRegister> findPersonEmailList() throws ServiceException;

  List<PersonEmailInfo> findPersonEmailInfoList() throws ServiceException;

  public void sendConfirmEmail(Long mailId) throws ServiceException;

  public PersonEmailRegister findPsnEmailById(Long mailId) throws ServiceException;

  boolean validateEmailHasUsed(Long psnId, Long mailId) throws ServiceException;
}
