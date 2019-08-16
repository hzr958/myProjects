package com.smate.center.open.service.wechat.bind;

import java.net.URLEncoder;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.cache.OpenCacheService;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.dao.data.OpenUserUnionDao;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.model.wechat.bind.WeChatBindForm;
import com.smate.center.open.service.login.ThirdLoginService;
import com.smate.center.open.service.user.UserService;
import com.smate.core.base.utils.cache.SnsCacheService;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.dao.wechat.WeChatRelationDao;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;
import com.smate.core.base.utils.model.wechat.WeChatRelation;
import com.smate.core.base.utils.service.msg.MobileMessageWwxyService;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.base.utils.wechat.MessageUtil;
import com.smate.core.base.utils.wechat.OAuth2Service;

/**
 * 微信绑定服务类
 * 
 * @author zk
 * @since 6.0.1
 */
@Service("weChatBindService")
@Transactional(rollbackFor = Exception.class)
public class WeChatBindServiceImpl implements WeChatBindService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private WeChatRelationDao weChatRelationDao;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private UserService userService;
  @Autowired
  private ThirdLoginService thirdLoginService;
  @Autowired
  private OpenCacheService openCacheService;
  @Autowired
  private OAuth2Service oAuth2Service;
  @Autowired
  private SnsCacheService snsCacheService;

  @Override
  public void cancelBindUser(String wxOpenId) throws Exception {
    // 获取access_token
    String weChatToken = oAuth2Service.getWeChatToken();
    // 通过accessToken和微信openId获取用户信息（主要是获取unionId）
    Map<String, Object> weChatInfo = oAuth2Service.getWeChatInfo(weChatToken, wxOpenId);
    boolean existsRecord = false;
    String wxUnionId = "";
    if (weChatInfo != null && weChatInfo.get("unionid") != null) {
      wxUnionId = weChatInfo.get("unionid").toString();
      // 用unionId查找关联关系，存在的则删除掉关联关系
      if (weChatRelationDao.checkWxUnionId(wxUnionId)) {
        existsRecord = true;
        weChatRelationDao.cancelBindByUnionId(wxUnionId);
      }
    }
    if (!existsRecord) {
      throw new OpenException("微信openid=" + wxOpenId + ", wxUnionId = " + wxUnionId + "的记录不存在!");
    }
  }

  @Deprecated
  @Override
  public void bindUser(WeChatBindForm form) throws OpenException {
    if (weChatRelationDao.checkWxOpenId(form.getWxOpenId())) {
      logger.info("已经存在wxOpenId=" + form.getWxOpenId() + "的记录!");
      form.setSuccess(true);
      form.setMsg("请勿重复绑定!");
    } else {
      if (form.getIsFirst() != 1 && this.checkBindData(form)) {
        // cas数据原
        User user = userService.getUserByUsername(form.getUserName().trim().toLowerCase());
        // User user =
        // userService.getUser(form.getUserName().trim().toLowerCase(),DigestUtils.md5Hex(form.getPassword().trim()));
        if (user == null) {
          form.setMsg("帐号不存在，绑定失败");
          this.matchFail(form);
        } else if (user.getPassword().equalsIgnoreCase(DigestUtils.md5Hex(form.getPassword().trim()))) {
          // 判断当前用户是否已经绑定微信帐号
          Long smateOpenId = weChatRelationDao.findSmateOpenIdByPsnId(user.getId());
          WeChatRelation weChatRelation = weChatRelationDao.getBySmateOpenId(smateOpenId);
          if (weChatRelation != null) {
            form.setMsg("该帐号已经被绑定，请输入新帐号!");
            this.matchFail(form);
          } else {
            form.setPsnId(user.getId());
            this.matchSuccess(form);
          }

        } else {
          form.setMsg("帐号或者密码错误，绑定失败");
          this.matchFail(form);
        }
      }
    }
  }

  /**
   * 检查数据
   * 
   * @param form
   * @return
   */
  private boolean checkBindData(WeChatBindForm form) {
    if (!form.getMobileCodeLogin()) {
      return checkUserNameAndPasswordBind(form);
    } else {
      return checkMobileCodeBind(form);
    }
  }

  /**
   * 持久化OpenUserUnion
   * 
   * @param form
   */
  private void persistenceUserUnion(WeChatBindForm form) {
    if (openUserUnionDao.getOpenUserUnion(form.getScmOpenId(), OpenConsts.SMATE_TOKEN) == null) {
      OpenUserUnion ouu = new OpenUserUnion();
      ouu.setCreateType(0);
      ouu.setOpenId(form.getScmOpenId());
      ouu.setPsnId(form.getPsnId());
      ouu.setToken(OpenConsts.SMATE_TOKEN);
      ouu.setCreateDate(new Date());
      openUserUnionDao.save(ouu);
    }
  }

  /**
   * 持久化WeChatRelation
   * 
   * @param form
   */
  private void persistenceWeChatRelation(WeChatBindForm form) {
    String nickname = null;
    try {
      String weChatToken = oAuth2Service.getWeChatToken();
      StringBuilder url = new StringBuilder();
      url.append("https://api.weixin.qq.com/cgi-bin/user/info?");
      url.append("access_token=" + URLEncoder.encode(weChatToken, "UTF-8"));
      url.append("&openid=" + URLEncoder.encode(form.getWxOpenId(), "UTF-8"));
      Map map = MessageUtil.httpRequest(url.toString(), "GET", null);
      if (MapUtils.isNotEmpty(map)) {
        nickname = Objects.toString(map.get("nickname"), "");
        Object unionid = map.get("unionid");
        if (StringUtils.isBlank(form.getWxUnionId())) {
          form.setWxUnionId(unionid != null ? unionid.toString() : "");
        }
      }
      WeChatRelation wcr = new WeChatRelation();
      wcr.setCreateTime(new Date());
      wcr.setSmateOpenId(form.getScmOpenId());
      wcr.setNickName(nickname);
      wcr.setWebChatOpenId(form.getWxOpenId());
      wcr.setWeChatUnionId(form.getWxUnionId());
      wcr.setBindType(form.getBindType());
      weChatRelationDao.save(wcr);
      logger.info("WeChatRelation数据：nickName=" + wcr.getNickName() + ", 微信openId=" + wcr.getWebChatOpenId()
          + ", 微信unionId=" + wcr.getWeChatUnionId() + ", ID=" + wcr.getId());
    } catch (Exception e) {
      logger.error("获取accessToken失败, wxOpenId={}, wxUnionId={}", form.getWxOpenId(), form.getWxUnionId(), e);
      throw new OpenException(e);
    }
  }

  /**
   * 绑定匹配成功
   * 
   * @param form
   */
  private void matchSuccess(WeChatBindForm form) {
    form.setScmOpenId(
        thirdLoginService.getOpenId(OpenConsts.SMATE_TOKEN, form.getPsnId(), OpenConsts.OPENID_CREATE_TYPE_3));
    this.persistenceUserUnion(form);
    this.persistenceWeChatRelation(form);
    // 正确清除错误次数
    openCacheService.remove(OpenConsts.LOGIN_CACHE, Struts2Utils.getSession().getId());
    form.setSuccess(true);
    form.setMsg("绑定成功!");
  }

  /**
   * 绑定匹配失败
   * 
   * @param form
   */
  private void matchFail(WeChatBindForm form) {
    form.setSuccess(false);
    // 2018-12-29 ajb 手机验证码登录，是否要验证码？
    // SCM-23137 登录-短信验证码登录，3次手机号/验证码错误登录失败，未弹出系统验证码框
    /*
     * if (form.getMobileCodeLogin()) { return; }
     */
    // form.setMsg("用户名或密码不正确!");
    // 记录错误次数
    Object errorNum = openCacheService.get(OpenConsts.LOGIN_CACHE, Struts2Utils.getSession().getId());
    int tempNum = 0;
    if (errorNum == null) {
      tempNum = 1;
      openCacheService.put(OpenConsts.LOGIN_CACHE, Struts2Utils.getSession().getId(), tempNum);
    } else {
      tempNum = (int) errorNum + 1;
      openCacheService.put(OpenConsts.LOGIN_CACHE, Struts2Utils.getSession().getId(), tempNum);
    }
    if (tempNum >= 3) {
      form.setNeedValidateCode(1);
    }
  }

  @Override
  public void bindUserWithWxUnionId(WeChatBindForm form) throws OpenException {
    if (weChatRelationDao.checkWxUnionId(form.getWxUnionId())) {
      logger.info("已经存在wxUnionId=" + form.getWxUnionId() + "的记录!");
      form.setSuccess(true);
      form.setMsg("请勿重复绑定!");
    } else if (form.getIsFirst() != 1 && this.checkBindData(form)) {
      // cas数据原
      User user = null;
      if (form.getMobileCodeLogin()) {
        Object obj = openCacheService.get(MobileMessageWwxyService.CACHE_NAME_LOGIN, form.getMobileNum());
        if (obj != null && obj.toString().equals(form.getMobileCode())) {
          user = userService.findUserByMobile(form.getMobileNum());
        }
      } else {
        user =
            userService.getUser(form.getUserName().trim().toLowerCase(), DigestUtils.md5Hex(form.getPassword().trim()));
      }
      if (user == null) {
        if (form.getMobileCodeLogin()) {
          User byMobile = userService.findUserByMobile(form.getMobileNum());
          if (byMobile == null) {
            form.setMsg("手机号未注册，绑定失败");
          } else {
            form.setMsg("验证码错误，绑定失败");
          }
        } else {
          form.setMsg("邮箱/手机号或者密码错误，绑定失败");
        }
        this.matchFail(form);
      } else {
        // 判断当前用户是否已经绑定微信帐号
        Long smateOpenId = weChatRelationDao.findSmateOpenIdByPsnId(user.getId());
        WeChatRelation weChatRelation = weChatRelationDao.getBySmateOpenId(smateOpenId);
        if (weChatRelation != null) {
          form.setMsg("该帐号已经被绑定，请输入新帐号!");
          this.matchFail(form);
        } else {
          form.setPsnId(user.getId());
          this.matchSuccess(form);
        }
      }
    }
  }

  @Override
  public void getPsnWxUnionIdAndWxOpenId(WeChatBindForm form) throws OpenException {
    try {
      if (StringUtils.isNotBlank(form.getCode())) {
        // 通过code获取wxOpenId
        String weChatOpenId = oAuth2Service.getWeChatOpenId(form.getCode());
        // 通过公众号access_token和wxOpenId获取用户信息，获取unionId
        String weChatToken = oAuth2Service.getWeChatToken();
        Map<String, Object> weChatInfo = oAuth2Service.getWeChatInfo(weChatToken, weChatOpenId);
        String wxUnionId = weChatInfo.get("unionid") == null ? null : weChatInfo.get("unionid").toString();
        String wxOpenId = weChatInfo.get("openid") == null ? null : weChatInfo.get("openid").toString();
        form.setWxOpenId(wxOpenId);
        form.setWxUnionId(wxUnionId);
        logger.info("尝试用微信返回的code获取的微信unionId为：" + wxUnionId + ", 微信openId为：" + wxOpenId);
      }
    } catch (Exception e) {
      logger.error("尝试通过code获取人员微信openID和微信unionId出错", e);
      throw new OpenException(e);
    }
  }


  /**
   * 校验手机验证码登录
   *
   * @param form
   */
  protected boolean checkMobileCodeBind(WeChatBindForm form) {
    boolean isEn = LocaleContextHolder.getLocale().equals(Locale.US);
    if (StringUtils.isBlank(form.getMobileNum())) {
      if (isEn) {
        form.setMsg("Please enter a valid phone number.");
      } else {
        form.setMsg("请输入手机号");
      }
      this.matchFail(form);
      return false;
    }
    if (StringUtils.isBlank(form.getMobileCode())) {
      if (isEn) {
        form.setMsg("Please enter a valid SMS verification code");
      } else {
        form.setMsg("请输入短信验证码");
      }
      this.matchFail(form);
      return false;
    }
    if (!Pattern.matches("^[1][3,4,5,7,8][0-9]{9}$", form.getMobileNum())) {
      if (isEn) {
        form.setMsg("Please enter a valid phone number.");
      } else {
        form.setMsg("请输入正确的手机号");
      }
      this.matchFail(form);
      return false;
    }
    Object obj = openCacheService.get(MobileMessageWwxyService.CACHE_NAME_LOGIN, form.getMobileNum());
    if (obj == null || !obj.toString().equals(form.getMobileCode())) {
      if (isEn) {
        form.setMsg("Please enter a valid SMS verification code.");
      } else {
        form.setMsg("短信验证码错误或已过期");
      }
      this.matchFail(form);
      return false;
    }
    // 验证码
    Object errorNum = openCacheService.get(OpenConsts.LOGIN_CACHE, Struts2Utils.getSession().getId());
    if (errorNum != null && (int) errorNum >= 3) {
      if (StringUtils.isBlank(form.getValidateCode())) {
        if (isEn) {
          form.setMsg("The system verification code is empty !");
        } else {
          form.setMsg("请输入系统验证码");
        }
        this.matchFail(form);
        return false;
      }
      // String validateCode =
      // Struts2Utils.getSession().getAttribute(OpenConsts.VALIDATE_CODE).toString();
      String validateCode =
          (String) snsCacheService.get(SecurityConstants.OAUTH_VALIDATE_CODE, Struts2Utils.getSession().getId());
      if (!form.getValidateCode().trim().equalsIgnoreCase(validateCode)) {
        if (isEn) {
          form.setMsg("The system verification code is error !");
        } else {
          form.setMsg("系统验证码输入错误或已过期");
        }
        this.matchFail(form);
        return false;
      }
    }
    return true;
  }


  /**
   * 校验账号密码登录
   *
   * @param form
   */
  protected boolean checkUserNameAndPasswordBind(WeChatBindForm form) {
    boolean isEn = LocaleContextHolder.getLocale().equals(Locale.US);
    if (StringUtils.isBlank(form.getUserName())) {
      if (isEn) {
        form.setMsg("Please enter a valid email address ， phone number or Scholar ID.");
      } else {
        form.setMsg("请输入邮箱/手机号/科研号");
      }
      this.matchFail(form);
      return false;
    }
    if (StringUtils.isBlank(form.getPassword())) {
      if (isEn) {
        form.setMsg("Please enter a password.");
      } else {
        form.setMsg("请输入密码");
      }
      this.matchFail(form);
      return false;
    }
    // 验证码
    Object errorNum = openCacheService.get(OpenConsts.LOGIN_CACHE, Struts2Utils.getSession().getId());
    if (errorNum != null && (int) errorNum >= 3) {
      if (StringUtils.isBlank(form.getValidateCode())) {
        if (isEn) {
          form.setMsg("The system verification code is empty !");
        } else {
          form.setMsg("请输入验证码");
        }
        this.matchFail(form);
        return false;
      }
      // String validateCode =
      // Objects.toString(Struts2Utils.getSession().getAttribute(OpenConsts.VALIDATE_CODE));
      String validateCode =
          (String) snsCacheService.get(SecurityConstants.OAUTH_VALIDATE_CODE, Struts2Utils.getSession().getId());
      if (!form.getValidateCode().trim().equalsIgnoreCase(validateCode)) {
        if (isEn) {
          form.setMsg("The system verification code is error !");
        } else {
          form.setMsg("验证码输入错误或已过期");
        }
        this.matchFail(form);
        return false;
      }
    }
    if (form.getUserName().length() > 50 || form.getPassword().length() > 40) {
      if (isEn) {
        form.setMsg("Please make sure the email and password are correct.");
      } else {
        form.setMsg("邮箱/手机号或密码错误");
      }
      this.matchFail(form);
      return false;
    }
    return true;
  }
}
