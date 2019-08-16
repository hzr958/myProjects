package com.smate.center.oauth.service.bind;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qq.connect.api.OpenID;
import com.smate.center.oauth.dao.profile.OpenUserUnionDao;
import com.smate.center.oauth.model.bind.MidBindForm;
import com.smate.center.oauth.model.consts.MidBindConsts;
import com.smate.center.oauth.model.consts.OpenConsts;
import com.smate.center.oauth.model.profile.PersonRegisterForm;
import com.smate.center.oauth.model.thirduser.SysThirdUser;
import com.smate.center.oauth.service.login.OauthLoginService;
import com.smate.center.oauth.service.psnregister.PersonRegisterService;
import com.smate.center.oauth.service.security.UserService;
import com.smate.center.oauth.service.thirdlogin.ThirdLoginService;
import com.smate.center.oauth.utils.EditValidateUtils;
import com.smate.core.base.app.model.AppAuthToken;
import com.smate.core.base.app.service.AppAuthTokenService;
import com.smate.core.base.utils.dao.wechat.WeChatRelationDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;
import com.smate.core.base.utils.model.wechat.WeChatRelation;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.wechat.MessageUtil;
import com.smate.core.base.utils.wechat.OAuth2Service;
import com.smate.core.base.utils.wechat.WeChatRelationService;

/**
 * 移动端绑定实现类
 * 
 * @author zzx
 *
 */
@Service("midBindService")
@Transactional(rollbackFor = Exception.class)
public class MidBindServiceImpl implements MidBindService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private OAuth2Service oAuth2Service;
  @Autowired
  private WeChatRelationService weChatRelationService;
  @Autowired
  private AppAuthTokenService appAuthTokenService;
  @Autowired
  private UserService userService;
  @Autowired
  private WeChatRelationDao weChatRelationDao;
  @Autowired
  private OauthLoginService oauthLoginService;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private PersonRegisterService personRegisterService;
  @Autowired
  private ThirdLoginService thirdLoginService;

  @Override
  public void unbindQQ(MidBindForm form) throws Exception {
    // 效验参数
    unbindQQParam(form);
    // 处理解绑QQ
    dealUnbindQQ(form);
  }

  @Override
  public void unbindWC(MidBindForm form) throws Exception {
    // 效验参数
    unbindWCParam(form);
    // 处理解绑微信
    dealUnbindWC(form);
  }

  @Override
  public void checkIosBingWC(MidBindForm form) throws Exception {
    // 效验参数
    midBingWcCheckParam(form);
    // 获取用户信息
    findWeChatInfo(form);
    // 处理绑定判断逻辑
    dealMidBingWc(form);
  }

  @Override
  public void wcLogin(MidBindForm form) throws Exception {
    // 微信绑定登录效验参数
    wcLoginCheckParam(form);
    // 处理微信绑定登录
    dealWcLogin(form);
  }

  @Override
  public void wcRegistered(MidBindForm form) throws Exception {
    // 微信绑定注册效验参数
    wcRegisteredCheckParam(form);
    // 处理微信绑定注册
    dealWcRegistered(form);
  }

  @Override
  public void checkMidBingQQ(MidBindForm form) throws Exception {
    // QQ绑定注册效验参数
    qqBingCheckParam(form);
    // 处理QQ是否绑定
    dealMidBingQQ(form);
  }

  @Override
  public void qqLogin(MidBindForm form) throws Exception {
    // QQ绑定登录效验参数
    qqBingLoginParam(form);
    // 处理QQ绑定登录
    dealQQBingLogin(form);
  }

  @Override
  public void qqRegistered(MidBindForm form) throws Exception {
    // QQ绑定注册效验参数
    qqRegisteredCheckParam(form);
    // 处理QQ绑定注册
    dealQQRegistered(form);
  }

  private void dealQQRegistered(MidBindForm form) throws Exception {
    if (MidBindConsts.SUCCESS.equals(form.getResultMap().get(MidBindConsts.STATUS))) {
      // 构建注册信息
      buildRegisterInfo(form);
      // 注册保存人员信息
      Long scmOpenId = personRegisterService.registerPerson(form.getRegisterForm());
      if (scmOpenId == null || scmOpenId == 0L) {
        form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
        form.getResultMap().put(MidBindConsts.MSG, "注册失败");
        return;
      }
      form.setScmOpenId(scmOpenId);
      form.setPsnId(form.getRegisterForm().getPersonId());
      form.setDes3PsnId(form.getRegisterForm().getPersonDes3Id());
      // 绑定qq关联关系
      thirdLoginService.saveThirdLogin2(form.getPsnId(), SysThirdUser.TYPE_QQ, form.getQqOpenId(), form.getUnionid(),
          form.getNickName());
      // 返回登录token
      AppAuthToken appAuthToken = appAuthTokenService.getToken(form.getPsnId());
      if (appAuthToken != null && StringUtils.isNotBlank(appAuthToken.getToken())) {
        form.getResultMap().put(MidBindConsts.TOKEN, appAuthToken.getToken());
      } else {
        String token = appAuthTokenService.createToken(form.getPsnId());
        form.getResultMap().put(MidBindConsts.TOKEN, token);
      }
      form.getResultMap().put(MidBindConsts.DES3PSNID, form.getDes3PsnId());
    }
  }

  private void qqRegisteredCheckParam(MidBindForm form) throws Exception {
    String accessToken = StringUtils.trim(objToStr(form.getAccessToken()));
    if (StringUtils.isBlank(accessToken)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "缺失accessToken");
      return;
    }
    String unionid = StringUtils.trim(objToStr(form.getUnionid()));
    if (StringUtils.isBlank(unionid)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "缺失参数unionid");
      return;
    }
    String qqOpenId = StringUtils.trim(objToStr(form.getQqOpenId()));
    if (StringUtils.isBlank(qqOpenId)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "缺失参数qqOpenId");
      return;
    }
    String user_name = StringUtils.trim(objToStr(form.getUserName())).toLowerCase();
    if (StringUtils.isBlank(user_name)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "请输入用户名");
      return;
    }
    if (checkMail(user_name)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "请输入正确的邮箱地址");
      return;
    }
    String passwords = StringUtils.trim(objToStr(form.getPasswords()));
    if (StringUtils.isBlank(passwords)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "请输入密码");
      return;
    }
    // 检查accessToken
    OpenID openIDObj = new OpenID(accessToken);
    if (openIDObj == null) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "accessToken失效");
      return;
    }
    form.setQqOpenId(qqOpenId);
    form.setUnionid(unionid);
    // 检查QQ是否重复绑定
    Long psnId = thirdLoginService.findUnionId(SysThirdUser.TYPE_QQ, form.getUnionid());
    if (psnId != null && psnId != 0L) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "请勿重复绑定");
      return;
    }
    // 检查账号
    User user = userService.getUser(user_name, DigestUtils.md5Hex(passwords));
    if (user != null) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "帐号已存在");
      return;
    }
    String firstName = objToStr(form.getFirstName());
    String lastName = objToStr(form.getLastName());
    // 转换名称中出现的允许使用的特殊字符,避免长度校验出错
    if (StringUtils.isNotBlank(firstName)) {
      firstName = firstName.replaceAll("&middot;", "·");
    }
    if (StringUtils.isNotBlank(lastName)) {
      lastName = lastName.replaceAll("&middot;", "·");
    }
    form.setFirstName(firstName);
    form.setLastName(lastName);
    form.setPsnId(user.getId());
    form.setDes3PsnId(Des3Utils.encodeToDes3(user.getId().toString()));
    form.setNickName(objToStr(form.getNickName()));
    form.setUserName(StringUtils.trim(user_name));
    form.setPasswords(StringUtils.trim(passwords));
    form.setAccessToken(accessToken);
  }

  private void dealQQBingLogin(MidBindForm form) throws Exception {
    if (MidBindConsts.SUCCESS.equals(form.getResultMap().get(MidBindConsts.STATUS))) {
      // 绑定qq关联关系
      thirdLoginService.saveThirdLogin2(form.getPsnId(), SysThirdUser.TYPE_QQ, form.getQqOpenId(), form.getUnionid(),
          form.getNickName());
      // 返回登录token
      AppAuthToken appAuthToken = appAuthTokenService.getToken(form.getPsnId());
      if (appAuthToken != null && StringUtils.isNotBlank(appAuthToken.getToken())) {
        form.getResultMap().put(MidBindConsts.TOKEN, appAuthToken.getToken());
      } else {
        String token = appAuthTokenService.createToken(form.getPsnId());
        form.getResultMap().put(MidBindConsts.TOKEN, token);
      }
      form.getResultMap().put(MidBindConsts.DES3PSNID, form.getDes3PsnId());
    }
  }

  /**
   * QQ绑定登录参数效验
   * 
   * @param form
   * @throws Exception
   */
  private void qqBingLoginParam(MidBindForm form) throws Exception {
    String accessToken = StringUtils.trim(objToStr(form.getAccessToken()));
    if (StringUtils.isBlank(accessToken)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "缺失参数accessToken");
      return;
    }
    String unionid = StringUtils.trim(objToStr(form.getUnionid()));
    if (StringUtils.isBlank(unionid)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "缺失参数unionid");
      return;
    }
    String qqOpenId = StringUtils.trim(objToStr(form.getQqOpenId()));
    if (StringUtils.isBlank(qqOpenId)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "缺失参数qqOpenId");
      return;
    }
    String user_name = StringUtils.trim(objToStr(form.getUserName())).toLowerCase();
    if (StringUtils.isBlank(user_name)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "请输入用户名");
      return;
    }
    if (checkMail(user_name)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "请输入正确的邮箱地址");
      return;
    }
    String passwords = StringUtils.trim(objToStr(form.getPasswords()));
    if (StringUtils.isBlank(passwords)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "请输入密码");
      return;
    }
    // 检查accessToken
    OpenID openIDObj = new OpenID(accessToken);
    if (openIDObj == null) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "accessToken失效");
      return;
    }
    form.setUnionid(unionid);
    form.setQqOpenId(qqOpenId);
    Long psnId = thirdLoginService.findUnionId(SysThirdUser.TYPE_QQ, form.getUnionid());
    if (psnId != null && psnId != 0L) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "请勿重复绑定");
      return;
    }
    // 检查账号
    User user = userService.getUser(user_name, DigestUtils.md5Hex(passwords));
    if (user == null) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "用户名或密码错误");
      return;
    }
    // 在判断当前用户是否已经被，绑定了
    SysThirdUser thirdUser = thirdLoginService.findSysThirdUser(SysThirdUser.TYPE_QQ, user.getId());
    if (thirdUser != null) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "请勿重复绑定");
      return;
    }
    form.setPsnId(user.getId());
    form.setDes3PsnId(Des3Utils.encodeToDes3(user.getId().toString()));
    form.setNickName(objToStr(form.getNickName()));
    form.setUserName(StringUtils.trim(user_name));
    form.setPasswords(StringUtils.trim(passwords));
    form.setAccessToken(accessToken);
  }

  private void dealMidBingQQ(MidBindForm form) throws Exception {
    if (MidBindConsts.SUCCESS.equals(form.getResultMap().get(MidBindConsts.STATUS))) {
      checkQQBing(form);
    }
  }

  private void checkQQBing(MidBindForm form) throws Exception {
    if (MidBindConsts.SUCCESS.equals(form.getResultMap().get(MidBindConsts.STATUS))) {
      Long psnId = thirdLoginService.findUnionId(SysThirdUser.TYPE_QQ, form.getUnionid());
      if (psnId != null && psnId != 0L) {
        // 已绑定
        User user = userService.findUserById(psnId);
        if (user == null) {
          form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
          form.getResultMap().put(MidBindConsts.MSG, "账号已删除");
          return;
        }
        if (!user.getEnabled()) {
          form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
          form.getResultMap().put(MidBindConsts.MSG, "账号不可用");
          return;
        }
        form.getResultMap().put(MidBindConsts.DES3PSNID, Des3Utils.encodeToDes3(psnId.toString()));
        form.getResultMap().put(MidBindConsts.USER_NAME, user.getLoginName());
        AppAuthToken appAuthToken = appAuthTokenService.getToken(psnId);
        if (appAuthToken != null && StringUtils.isNotBlank(appAuthToken.getToken())) {
          // 已绑定--已关联-返回标识权限 token
          form.getResultMap().put(MidBindConsts.TOKEN, appAuthToken.getToken());
          form.getResultMap().put(MidBindConsts.NICKNAME, form.getNickName());
          form.getResultMap().put(MidBindConsts.BING_STATUS, "1");
        } else {
          // 已绑定--未关联 --一般不会有这种情况
          String token = appAuthTokenService.createToken(psnId);
          form.getResultMap().put(MidBindConsts.TOKEN, token);
          form.getResultMap().put(MidBindConsts.BING_STATUS, "2");
          form.getResultMap().put(MidBindConsts.NICKNAME, form.getNickName());
        }
      } else {
        // 未绑定
        form.getResultMap().put(MidBindConsts.BING_STATUS, "0");
        form.getResultMap().put(MidBindConsts.NICKNAME, form.getNickName());
      }
    }
  }

  private void findQQName(MidBindForm form) throws Exception {
    if (MidBindConsts.SUCCESS.equals(form.getResultMap().get(MidBindConsts.STATUS))) {
      try {
        StringBuilder url = new StringBuilder();
        url.append("https://graph.qq.com/user/get_user_info?");
        url.append("access_token=" + form.getAccessToken());
        url.append("&oauth_consumer_key=" + URLEncoder.encode(MidBindConsts.oauth_consumer_key, "UTF-8"));
        url.append("&openid=" + URLEncoder.encode(form.getQqOpenId(), "UTF-8"));
        Map<String, Object> map = MessageUtil.httpRequest(url.toString(), "GET", null);
        if (map != null && map.get("ret") != null && "0".equals(map.get("ret").toString())) {
          form.getResultMap().put(MidBindConsts.NICKNAME, map.get("nickname").toString());
        }
      } catch (Exception e) {
        form.getResultMap().put(MidBindConsts.RESULT, "error");
        form.getResultMap().put(MidBindConsts.MSG, "获取qq用户信息出错");
        logger.error("获取qq用户信息出错", e);
      }
    }
  }

  private void qqBingCheckParam(MidBindForm form) throws Exception {
    String accessToken = StringUtils.trim(objToStr(form.getAccessToken()));
    if (StringUtils.isBlank(accessToken)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "缺失参数accessToken");
      return;
    }
    String unionid = StringUtils.trim(objToStr(form.getUnionid()));
    if (StringUtils.isBlank(unionid)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "缺失参数unionid");
      return;
    }
    form.setUnionid(unionid);
    /*try {
      OpenID openIDObj = new OpenID(accessToken);
      // String qqOpenId = openIDObj.getUserOpenID();
      if (openIDObj == null) {
        throw new Exception("获取qqOpenId失败");
      }
    } catch (Exception e) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "accessToken失效");
      logger.error("qq绑定验证accessToken出错", e);
      return;
    }*/
    form.setAccessToken(accessToken);
  }

  /**
   * 处理微信绑定注册
   * 
   * @param form
   */
  private void dealWcRegistered(MidBindForm form) throws Exception {
    if (MidBindConsts.SUCCESS.equals(form.getResultMap().get(MidBindConsts.STATUS))) {
      // 构建注册信息
      buildRegisterInfo(form);
      // 注册保存人员信息
      Long scmOpenId = personRegisterService.registerPerson(form.getRegisterForm());
      if (scmOpenId == null || scmOpenId == 0L) {
        form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
        form.getResultMap().put(MidBindConsts.MSG, "注册失败");
        return;
      }
      form.setScmOpenId(scmOpenId);
      form.setPsnId(form.getRegisterForm().getPersonId());
      form.setDes3PsnId(form.getRegisterForm().getPersonDes3Id());
      // 绑定微信关联关系
      connectionWeChat(form);
    }
  }

  /**
   * 绑定微信关联关系
   * 
   * @param form
   */
  private void connectionWeChat(MidBindForm form) throws Exception {
    if (MidBindConsts.SUCCESS.equals(form.getResultMap().get(MidBindConsts.STATUS))) {
      Long scmOpenId = form.getScmOpenId();
      if (openUserUnionDao.getOpenUserUnion(scmOpenId, OpenConsts.SMATE_TOKEN) == null) {
        OpenUserUnion ouu = new OpenUserUnion();
        ouu.setCreateType(OpenConsts.OPENID_CREATE_TYPE_3);
        ouu.setOpenId(scmOpenId);
        ouu.setPsnId(form.getPsnId());
        ouu.setToken(OpenConsts.SMATE_TOKEN);
        ouu.setCreateDate(new Date());
        openUserUnionDao.save(ouu);
      }
      WeChatRelation wcr = new WeChatRelation();
      wcr.setCreateTime(new Date());
      wcr.setSmateOpenId(scmOpenId);
      wcr.setWebChatOpenId(form.getWcOpenId());
      wcr.setWeChatUnionId(form.getUnionid());
      wcr.setBindType(2);// 绑定方式 0-微信端 1-PC端 PC端通过开放平台，2=ios,openId默认0
      wcr.setNickName(form.getNickName());// 微信昵称
      weChatRelationDao.save(wcr);
      // 返回登录token
      AppAuthToken appAuthToken = appAuthTokenService.getToken(form.getPsnId());
      if (appAuthToken != null && StringUtils.isNotBlank(appAuthToken.getToken())) {
        form.getResultMap().put(MidBindConsts.TOKEN, appAuthToken.getToken());
      } else {
        String token = appAuthTokenService.createToken(form.getPsnId());
        form.getResultMap().put(MidBindConsts.TOKEN, token);
      }
      form.getResultMap().put(MidBindConsts.DES3PSNID, form.getDes3PsnId());
    }
  }

  private void buildRegisterInfo(MidBindForm form) throws Exception {
    if (MidBindConsts.SUCCESS.equals(form.getResultMap().get(MidBindConsts.STATUS))) {
      PersonRegisterForm registerForm = new PersonRegisterForm();
      registerForm.setIsRegisterR(true);
      registerForm.setNewpassword(DigestUtils.md5Hex(form.getPasswords()));
      registerForm.setEmail(form.getUserName());
      registerForm.setEmailLanguageVersion("");// 设置个人邮件接收的语言版本，默认为当前系统的语言版本
      registerForm.setZhfirstName(form.getFirstName());
      registerForm.setZhlastName(form.getLastName());
      registerForm.setName(form.getLastName() + form.getFirstName());
      form.setRegisterForm(registerForm);
    }
  }

  /**
   * 处理微信绑定登录
   * 
   * @param form
   */
  private void dealWcLogin(MidBindForm form) throws Exception {
    if (MidBindConsts.SUCCESS.equals(form.getResultMap().get(MidBindConsts.STATUS))) {
      // 登录
      Long scmOpenId =
          oauthLoginService.getOpenId(OpenConsts.SMATE_TOKEN, form.getPsnId(), OpenConsts.OPENID_CREATE_TYPE_3);
      WeChatRelation chatRelation = weChatRelationDao.getBySmateOpenId(scmOpenId);// 一个账号只能绑定一个微信
      if (chatRelation != null) {
        form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
        form.getResultMap().put(MidBindConsts.MSG, "请勿重复绑定");
        return;
      }
      form.setScmOpenId(scmOpenId);
      // 绑定微信关联关系
      connectionWeChat(form);
    }
  }

  /**
   * 微信绑定注册效验参数
   * 
   * @param form
   */
  private void wcRegisteredCheckParam(MidBindForm form) throws Exception {
    String unionid = StringUtils.trim(objToStr(form.getUnionid()));
    if (StringUtils.isBlank(unionid)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "缺失参数unionid");
      return;
    }
    String openid = objToStr(form.getWcOpenId());
    if (StringUtils.isBlank(openid)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "缺失参数wcOpenId");
      return;
    }
    String user_name = objToStr(form.getUserName());
    if (StringUtils.isBlank(user_name)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "请输入用户名");
      return;
    }
    if (checkMail(user_name)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "请输入正确的邮箱地址");
      return;
    }
    String passwords = StringUtils.trim(objToStr(form.getPasswords()));
    if (StringUtils.isBlank(passwords)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "请输入密码");
      return;
    }
    user_name = StringUtils.trim(user_name).toLowerCase();
    // 检查账号
    User user = userService.getUser(user_name);
    if (user != null) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "帐号已存在");
      return;
    }
    // 检查是否重复绑定
    if (weChatRelationDao.checkWxUnionId(unionid)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "请勿重复绑定");
      return;
    }
    String firstName = objToStr(form.getFirstName());
    String lastName = objToStr(form.getLastName());
    // 转换名称中出现的允许使用的特殊字符,避免长度校验出错
    if (StringUtils.isNotBlank(firstName)) {
      firstName = firstName.replaceAll("&middot;", "·");
    }
    if (StringUtils.isNotBlank(lastName)) {
      lastName = lastName.replaceAll("&middot;", "·");
    }
    form.setFirstName(firstName);
    form.setLastName(lastName);
    form.setNickName(objToStr(form.getNickName()));
    form.setUnionid(unionid);
    form.setUserName(user_name);
    form.setPasswords(StringUtils.trim(passwords));
  }

  /**
   * 微信绑定登录效验参数
   * 
   * @param form
   */
  private void wcLoginCheckParam(MidBindForm form) throws Exception {
    String unionid = StringUtils.trim(objToStr(form.getUnionid()));
    if (StringUtils.isBlank(unionid)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "缺失参数unionid");
      return;
    }
    String openid = objToStr(form.getWcOpenId());
    if (StringUtils.isBlank(openid)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "缺失参数wcOpenId");
      return;
    }
    String user_name = StringUtils.trim(objToStr(form.getUserName())).toLowerCase();
    if (StringUtils.isBlank(user_name)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "请输入用户名");
      return;
    }
    if (checkMail(user_name)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "请输入正确的邮箱地址");
      return;
    }
    String passwords = StringUtils.trim(objToStr(form.getPasswords()));
    if (StringUtils.isBlank(passwords)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "请输入密码");
      return;
    }
    // 检查是否重复绑定
    if (weChatRelationDao.checkWxUnionId(unionid)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "请勿重复绑定");
      return;
    }
    // 检查账号
    User user = userService.getUser(user_name, DigestUtils.md5Hex(passwords));
    if (user == null) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "用户名或密码错误");
      return;
    }
    form.setPsnId(user.getId());
    form.setDes3PsnId(Des3Utils.encodeToDes3(user.getId().toString()));
    form.setNickName(objToStr(form.getNickName()));
    form.setUnionid(unionid);
    form.setUserName(StringUtils.trim(user_name));
    form.setPasswords(StringUtils.trim(passwords));
  }

  private boolean checkMail(String user_name) throws Exception {
    return EditValidateUtils.hasParam(user_name, 50, EditValidateUtils.MAIL_COAD);
  }

  /**
   * 处理绑定判断逻辑
   * 
   * @param form
   */
  private void dealMidBingWc(MidBindForm form) throws Exception {
    if (MidBindConsts.SUCCESS.equals(form.getResultMap().get(MidBindConsts.STATUS))) {
      // 判断是否绑定过
      Long psnId = weChatRelationService.querypsnIdByWeChatUnionid(form.getUnionid());
      if (psnId != null && psnId != 0L) {
        User user = userService.findUserById(psnId);
        if (user == null) {
          form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
          form.getResultMap().put(MidBindConsts.MSG, "账号已删除");
          return;
        }
        if (!user.getEnabled()) {
          form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
          form.getResultMap().put(MidBindConsts.MSG, "账号不可用");
          return;
        }
        form.getResultMap().put(MidBindConsts.DES3PSNID, Des3Utils.encodeToDes3(psnId.toString()));
        form.getResultMap().put(MidBindConsts.USER_NAME, user.getLoginName());
        AppAuthToken appAuthToken = appAuthTokenService.getToken(psnId);
        if (appAuthToken != null && StringUtils.isNotBlank(appAuthToken.getToken())) {
          // 已绑定--已关联-返回标识权限 token
          form.getResultMap().put(MidBindConsts.TOKEN, appAuthToken.getToken());
          form.getResultMap().put(MidBindConsts.BING_STATUS, "1");
        } else {
          // 已绑定--未关联 --一般不会有这种情况
          String token = appAuthTokenService.createToken(psnId);
          form.getResultMap().put(MidBindConsts.TOKEN, token);
          form.getResultMap().put(MidBindConsts.BING_STATUS, "2");
        }
      } else {
        // 没绑定
        form.getResultMap().put(MidBindConsts.BING_STATUS, "0");
      }
    }
  }

  /**
   * 获取用户信息
   * 
   * @param form
   */
  private void findWeChatInfo(MidBindForm form) throws Exception {
    try {
      if (MidBindConsts.SUCCESS.equals(form.getResultMap().get(MidBindConsts.STATUS))) {
        Map<String, Object> weChatInfo = oAuth2Service.getOpenWeChatInfo(form.getAccessToken(), form.getWcOpenId());
        form.getResultMap().put(MidBindConsts.WE_CHAT_INFO, JacksonUtils.mapToJsonStr(weChatInfo));
      }
    } catch (Exception e) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG,
          "获取用户信息出错，openid=" + form.getWcOpenId() + ",access_token=" + form.getAccessToken());
      logger.error("ios绑定微信-获取用户信息出错", e);
    }

  }

  /**
   * 必要参数效验参数
   * 
   * @param form
   */
  private void midBingWcCheckParam(MidBindForm form) throws Exception {
    String unionid = objToStr(form.getUnionid());
    if (StringUtils.isBlank(unionid)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "缺失参数unionid");
      return;
    }
    String openid = objToStr(form.getWcOpenId());
    if (StringUtils.isBlank(openid)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "缺失参数wcOpenId");
      return;
    }
    String access_token = objToStr(form.getAccessToken());
    if (StringUtils.isBlank(access_token)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "缺失参数accessToken");
      return;
    }
    access_token = URLEncoder.encode(access_token, "utf-8");
    form.setAccessToken(access_token);
  }

  private void dealUnbindQQ(MidBindForm form) throws Exception {
    if (MidBindConsts.SUCCESS.equals(form.getResultMap().get(MidBindConsts.STATUS))) {
      thirdLoginService.delete(form.getPsnId(), form.getUnionid(), SysThirdUser.TYPE_QQ);
      form.getResultMap().put(MidBindConsts.MSG, "解除绑定成功");
    }
  }

  private void unbindQQParam(MidBindForm form) throws Exception {
    String unionid = objToStr(form.getUnionid());
    if (StringUtils.isBlank(unionid)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "缺失参数unionid");
      return;
    }
    String des3_psn_id = StringUtils.trim(objToStr(form.getDes3PsnId()));
    if (StringUtils.isBlank(des3_psn_id)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "缺失参数des3PsnId");
      return;
    }
    String token = StringUtils.trim(objToStr(form.getMidToken()));
    if (StringUtils.isBlank(token)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "缺失参数midToken");
      return;
    }
    // 检查权限
    form.setPsnId(Long.parseLong(Des3Utils.decodeFromDes3(des3_psn_id)));
    token = token.replace(",", "");
    AppAuthToken appAuthToken = appAuthTokenService.getToken(form.getPsnId(), token);
    if (appAuthToken == null) {
      appAuthToken = appAuthTokenService.getToken(form.getPsnId(), URLEncoder.encode(token, "utf-8"));
      if (appAuthToken == null) {
        appAuthToken = appAuthTokenService.getToken(form.getPsnId(), URLDecoder.decode(token, "utf-8"));
        if (appAuthToken == null) {
          form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
          form.getResultMap().put(MidBindConsts.MSG, "midToken和psnId不匹配");
          return;
        }
      }
    }
    String accessToken = StringUtils.trim(objToStr(form.getAccessToken()));
    if (StringUtils.isBlank(accessToken)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "缺失参数accessToken");
      return;
    }
    // 检查accessToken
    OpenID openIDObj = new OpenID(accessToken);
    if (openIDObj == null) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "accessToken失效");
      return;
    }
    form.setUnionid(unionid);
    Long psnId = thirdLoginService.findUnionId(SysThirdUser.TYPE_QQ, form.getUnionid());
    if (psnId == null || psnId == 0L) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "QQ没有绑定不能解绑");
      return;
    }
    form.setAccessToken(accessToken);
    form.setDes3PsnId(des3_psn_id);
    form.setMidToken(token);
  }

  private void dealUnbindWC(MidBindForm form) throws Exception {
    if (MidBindConsts.SUCCESS.equals(form.getResultMap().get(MidBindConsts.STATUS))) {
      Long scmOpenId =
          oauthLoginService.getOpenId(OpenConsts.SMATE_TOKEN, form.getPsnId(), OpenConsts.OPENID_CREATE_TYPE_3);
      Integer deleteBy = weChatRelationDao.deleteBy(scmOpenId, form.getUnionid());
      if (deleteBy != 1) {
        form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
        form.getResultMap().put(MidBindConsts.MSG, "删除失败");
      }
    }
  }

  private void unbindWCParam(MidBindForm form) throws Exception {
    String accessToken = StringUtils.trim(objToStr(form.getAccessToken()));
    if (StringUtils.isBlank(accessToken)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "缺失accessToken");
      return;
    }
    String unionid = StringUtils.trim(objToStr(form.getUnionid()));
    if (StringUtils.isBlank(unionid)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "缺失参数unionid");
      return;
    }
    String openid = objToStr(form.getWcOpenId());
    if (StringUtils.isBlank(openid)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "缺失参数wcOpenId");
      return;
    }
    String des3_psn_id = StringUtils.trim(objToStr(form.getDes3PsnId()));
    if (StringUtils.isBlank(des3_psn_id)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "缺失参数des3PsnId");
      return;
    }
    String token = StringUtils.trim(objToStr(form.getMidToken()));
    if (StringUtils.isBlank(token)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "缺失参数midToken");
      return;
    }
    // 检查权限
    form.setPsnId(Long.parseLong(Des3Utils.decodeFromDes3(des3_psn_id)));
    AppAuthToken appAuthToken = appAuthTokenService.getToken(form.getPsnId(), token);
    if (appAuthToken == null) {
      appAuthToken = appAuthTokenService.getToken(form.getPsnId(), URLEncoder.encode(token, "utf-8"));
      if (appAuthToken == null) {
        appAuthToken = appAuthTokenService.getToken(form.getPsnId(), URLDecoder.decode(token, "utf-8"));
        if (appAuthToken == null) {
          form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
          form.getResultMap().put(MidBindConsts.MSG, "midToken和psnId不匹配");
          return;
        }
      }
    }
    // 检查是否绑定
    if (!weChatRelationDao.checkWxUnionId(unionid)) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "微信帐号没有绑定");
      return;
    }
    accessToken = URLEncoder.encode(accessToken, "utf-8");
    form.setUnionid(unionid);
    form.setAccessToken(accessToken);
    form.setMidToken(token);
    form.setDes3PsnId(des3_psn_id);
  }

  private String objToStr(Object obj) {
    return obj == null ? "" : obj.toString();
  }

  public static void main(String[] args) {
    // j7So8WJ61jGTgluG8w3%2FfCiBO09vOxTxdjKt8I47iuk%3D
    // j7So8WJ61jGTgluG8w3%2FfCiBO09vOxTxdjKt8I47iuk%3D
    System.out.println(Des3Utils.decodeFromDes3("j7So8WJ61jGTgluG8w3%2FfCiBO09vOxTxdjKt8I47iuk%3D"));
    System.out.println(Des3Utils.decodeFromDes3("JvUzHyT7%252BGIREMN6j9noqA%253D%253D"));
  }



}
