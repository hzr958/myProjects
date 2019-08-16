package com.smate.center.open.service.login;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.cache.OauthCacheService;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.dao.data.OpenThirdRegDao;
import com.smate.center.open.dao.data.OpenUserUnionDao;
import com.smate.center.open.dao.login.OpenUserLoginLogDao;
import com.smate.center.open.exception.OpenDataGetThirdRegException;
import com.smate.center.open.exception.OpenDataGetThirdRegNameException;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.form.OpenLoginForm;
import com.smate.center.open.model.OpenUserLoginLog;
import com.smate.center.open.service.open.PersonOpenService;
import com.smate.center.open.service.user.UserService;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * open系统 登录验证服务实现
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Service("thirdLoginService")
@Transactional(rollbackFor = Exception.class)
public class ThirdLoginServiceImpl implements ThirdLoginService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private OpenThirdRegDao openThirdRegDao;
  @Autowired
  private UserService userService;
  @Autowired
  private OauthCacheService oauthCacheService;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private OpenUserLoginLogDao openUserLoginLogDao;
  @Autowired
  private PersonOpenService personOpenService;

  /**
   * 登录验证 方法
   * 
   * @param form
   * @throws OpenException
   */
  @Override
  public void openLogin(OpenLoginForm form) throws OpenException {
    if (form.getToken() != null && form.getToken().length() == OpenConsts.TOKEN_LENGTH) {
      String result = openThirdRegDao.getThirdSysNameByToken(form.getToken());
      if (result == null) {
        form.setLoginStatus(0);
        form.setMsg("来源系统未开放权限");
        return;
      }
    } else {
      form.setLoginStatus(0);
      form.setMsg("来源系统未开放权限");
      return;
    }
    // 判断账号密码
    if (StringUtils.isBlank(form.getUserName())) {
      form.setLoginStatus(0);
      form.setMsg("登录邮箱/手机号/科研号不能为空");
      return;
    }
    if (StringUtils.isBlank(form.getPassword())) {
      form.setLoginStatus(0);
      form.setMsg("密码不能为空");
      return;
    }
    // 判断 token
    // 验证码
    if ("1".equals(form.getNeedValidateCode())) {
      if (StringUtils.isBlank(form.getValidateCode())) {
        form.setLoginStatus(0);
        form.setMsg("验证码不能为空!");
        return;
      }
      Object validateCode =
          oauthCacheService.get(SecurityConstants.OAUTH_VALIDATE_CODE, Struts2Utils.getRequestSessionId());
      // String sessionCode = Struts2Utils.getSession().getAttribute(OpenConsts.VALIDATE_CODE).toString();
      if (!form.getValidateCode().trim().equalsIgnoreCase(ObjectUtils.toString(validateCode, ""))) {
        form.setLoginStatus(0);
        form.setMsg("验证码不正确!");
        return;
      }
    }
    // cas数据原
    User user =
        userService.getUser(form.getUserName().trim().toLowerCase(), DigestUtils.md5Hex(form.getPassword().trim()));
    // 错误 需要记录日志 并记录 错误次数
    if (user == null) {
      loginError(form);
      return;
    }
    loginSucess(form, user);
  }

  /**
   * 登录成果处理
   * 
   * @param form
   * @param user
   * @throws OpenException
   */
  private void loginSucess(OpenLoginForm form, User user) throws OpenException {
    // 正确清除错误次数
    oauthCacheService.remove(OpenConsts.LOGIN_CACHE, Struts2Utils.getRequestSessionId());
    // 取openId
    Long openId = getOpenId(form.getToken(), user.getId(), OpenConsts.OPENID_CREATE_TYPE_0);
    form.setLoginStatus(1);
    form.setOpenId(openId);
    form.setPsnId(user.getId());
    // 登录日志
    OpenUserLoginLog openUserLoginLog = new OpenUserLoginLog();
    openUserLoginLog.setLoginDate(new Date());
    openUserLoginLog.setLoginFromToken(form.getToken());
    openUserLoginLog.setPsnId(user.getId());
    openUserLoginLogDao.saveUserLoginLog(openUserLoginLog);
  }

  /**
   * 账号或密码错误 处理
   * 
   * @param form
   * @throws OpenException
   */
  private void loginError(OpenLoginForm form) throws OpenException {
    try {
      form.setLoginStatus(0);
      form.setMsg("登录邮箱/手机号/科研号或密码不正确!");
      // 记录错误次数
      // HttpSession sesssion = Struts2Utils.getRequest().getRequestSessionId(false);
      Object errorNum = oauthCacheService.get(OpenConsts.LOGIN_CACHE, Struts2Utils.getRequestSessionId());
      int tempNum = 0;
      if (errorNum == null) {
        tempNum = 1;
        oauthCacheService.put(OpenConsts.LOGIN_CACHE, Struts2Utils.getRequestSessionId(), tempNum);
      } else {
        tempNum = (int) errorNum + 1;
        oauthCacheService.put(OpenConsts.LOGIN_CACHE, Struts2Utils.getRequestSessionId(), tempNum);
      }
      if (tempNum >= 3) {
        form.setNeedValidateCode("1");
      }
      OpenUserLoginLog openUserLoginLog = new OpenUserLoginLog();
      openUserLoginLog.setLoginDate(new Date());
      openUserLoginLog.setLoginFromToken(form.getToken());
      openUserLoginLog.setLoginMsg("登录错误！");
      openUserLoginLogDao.saveUserLoginLog(openUserLoginLog);
    } catch (Exception e) {
      logger.warn("记录错误次数异常" + e);
      e.printStackTrace();
    }
  }

  /**
   * 取openId
   * 
   * @param form
   * @param user
   * @return
   * @throws OpenException
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
  public Long getOpenId(String token, Long psnId, int createType) throws OpenException {
    /*
     * Long openId=openUserUnionDao.getOpenIdByPsnId(psnId); if(openId==null){
     * openId=RandomNumber.getRandomNumber(8); //查重 while(true){ //99999999 表示 没有真实用户的 数据交互
     * if(openId==OpenConsts.SYSTEM_OPENID){ continue; } OpenUserUnion temp
     * =openUserUnionDao.getOpenUserUnionByOpenId(openId); if(temp==null){ break; }else{
     * openId=RandomNumber.getRandomNumber(8); } } }
     */
    // 新方式获取 openid
    Long openId;
    try {
      openId = personOpenService.getOpenIdByPsnId(psnId);
    } catch (Exception e) {
      throw new OpenException(e);
    }
    // 判断是否 有关联的第三方系统记录
    OpenUserUnion openUserUnion = openUserUnionDao.getOpenUserUnion(openId, token);
    if (openUserUnion == null) {
      openUserUnion = new OpenUserUnion();
      openUserUnion.setOpenId(openId);
      openUserUnion.setPsnId(psnId);
      openUserUnion.setToken(token);
      openUserUnion.setCreateDate(new Date());
      openUserUnion.setCreateType(createType);
      openUserUnionDao.saveOpenUserUnion(openUserUnion);
    }
    return openId;
  }

  /**
   * 取第三方系统名称方法 没有正确取出名字 直接跳转没有权限的页面
   * 
   * @param form
   * @throws OpenException
   */
  @Override
  public void getThirdSysNameByToken(OpenLoginForm form) throws OpenException {
    try {
      if (form.getToken() != null && form.getToken().length() == OpenConsts.TOKEN_LENGTH) {
        String result = openThirdRegDao.getThirdSysNameByToken(form.getToken());
        if (result == null) {
          form.setLoginStatus(0);
          form.setMsg("来源系统未开放权限");
          return;
        }
        form.setLoginStatus(1);
        form.setThirdSysName(result);
        if ("en_US".equals(form.getLocale())) {
          form.setThirdSysNameUs(openThirdRegDao.getThirdSysNameUsByToken(form.getToken()));
        }
        Object errorNum = oauthCacheService.get(OpenConsts.LOGIN_CACHE, Struts2Utils.getRequestSessionId());
        if (errorNum != null && (int) errorNum >= 3) {
          form.setNeedValidateCode("1");
        }
      } else {
        form.setLoginStatus(0);
        form.setMsg("来源系统未开放权限");
        return;
      }
    } catch (OpenDataGetThirdRegNameException e) {
      logger.error("重数据库获取第三方系统注册名称异常!");
      throw new OpenDataGetThirdRegNameException(e);
    } catch (Exception e) {
      logger.error("进入登录页面 逻辑处理异常");
      throw new OpenException(e);
    }
  }

  public static void main(String[] args) {

    try {
      tet1();
    } catch (OpenDataGetThirdRegException e) {
      System.out.println(0);
    }

  }

  private static void tet1() {
    try {
      tet();
    } catch (OpenDataGetThirdRegNameException e) {
      System.out.println(1);
      throw new OpenDataGetThirdRegNameException();
    }
  }

  private static void tet() {
    throw new OpenDataGetThirdRegException();
  }

}
