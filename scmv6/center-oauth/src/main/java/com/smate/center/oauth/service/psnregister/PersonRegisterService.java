package com.smate.center.oauth.service.psnregister;

import com.smate.center.oauth.exception.RegisterException;
import com.smate.center.oauth.exception.ServiceException;
import com.smate.center.oauth.model.profile.PersonRegisterForm;

/**
 * 人员注册信息服务接口
 * 
 * @author Administrator
 *
 */

public interface PersonRegisterService {

  /**
   * 验证邮件是否已被占用.
   * 
   * @param email
   * @return Long
   * @throws ServiceException
   */
  boolean findIsEmail(String email) throws RegisterException;

  /**
   * 注册保存人员信息.
   * 
   * @param form
   * @return 注册成功返回openId, 失败返回0L
   */
  Long registerPerson(PersonRegisterForm form);

  /**
   * 调用center-open系统的接口获取自动登录所需加密ID
   * 
   * @return
   */
  String getAutoLoginAID(Long openId, String autoLoginType);

  /**
   * 微信页面注册时，自动绑定 wxopenId 公众平台openid wxUnionId psnOpenId 科研之友开放id bindType 1-pc或 0-移动端绑定
   */
  void autoBindWeixin(String wxOpenId, String wxUnionId, Integer bindType, Long psnOpenId);

  /**
   * 生成短地址
   * 
   * @param psnId
   * @return
   */
  public String produceShortUrl(Long psnId);

  /**
   * 标记注册回调状态为：已同意等待注册状态
   * 
   * @param form
   */
  void doRegusterBack(PersonRegisterForm form) throws Exception;

  /**
   * 通过检查是否有注册回调处理
   * 
   * @param form
   */
  boolean doRegusterBackByCheck(PersonRegisterForm form) throws Exception;

  /**
   * 添加动态token
   * 
   * @param form
   */
  public void addDynamicOpenId(PersonRegisterForm form) throws Exception;

}
