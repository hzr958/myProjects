package com.smate.center.oauth.service.bind;

import java.util.Date;

import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.oauth.dao.profile.OpenUserUnionDao;
import com.smate.center.oauth.model.bind.ThirdBindForm;
import com.smate.center.oauth.model.consts.OpenConsts;
import com.smate.center.oauth.model.consts.SmateLoginConsts;
import com.smate.center.oauth.service.login.OauthLoginService;
import com.smate.center.oauth.service.mobile.MobileLoginService;
import com.smate.core.base.utils.dao.wechat.WeChatRelationDao;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;
import com.smate.core.base.utils.model.wechat.WeChatRelation;
import com.smate.core.base.utils.struts2.Struts2Utils;

@Service("weChatBindService")
@Transactional(rollbackFor = Exception.class)
public class WeChatBindServiceImpl implements WeChatBindService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private WeChatRelationDao weChatRelationDao;
  @Autowired
  private OauthLoginService oauthLoginService;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private MobileLoginService mobileLoginService;

  @Override
  public void pcBindUser(ThirdBindForm form) throws Exception {
    if (weChatRelationDao.checkWxUnionId(form.getUnionid())) {
      logger.info("已经存在wxUnionId=" + form.getUnionid() + "的记录!");
      form.setMsg("请勿重复绑定!");
      return;
    } else {
      this.matchSuccess(form);
      // 绑定成功后自动登录
      try {
        String AID = null;
        Long openId = oauthLoginService.getOpenId(SmateLoginConsts.SNS_DEFAULT_TOKEN, form.getPsnId(),
            SmateLoginConsts.SNS_OPENID_TYPE_SIX);
        if (openId != null && openId != 0L) {
          AID = oauthLoginService.getAutoLoginAID(openId, SmateLoginConsts.SNS_REMEMBER_ME);
        }
        if (StringUtils.isNotBlank(AID)) {
          String redirectUrl = "/oauth/wechat/login/refresh" + "?AID=" + AID;
          // ROL-6514 机构版支持微信登录，携带host参数
          redirectUrl = this.addParameterNameHost(redirectUrl);
          Struts2Utils.getResponse().sendRedirect(redirectUrl);
        }
      } catch (Exception e) {
        logger.error("pc端绑定操作出错, psnId = " + form.getPsnId(), e);
      }
    }
  }

  // ROL-6514 机构版支持微信登录，携带host参数
  private String addParameterNameHost(String redirectUrl) {
    Cookie[] cookies = Struts2Utils.getRequest().getCookies();
    String host = "";
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("host")) {
        String hosts = cookie.getValue();
        host = hosts.split(",")[0];
      }
    }
    if (redirectUrl.indexOf("?") > 0) {
      redirectUrl += "&host=" + host;
    } else {
      redirectUrl += "?host=" + host;
    }
    return redirectUrl;
  }


  /**
   * 绑定匹配成功
   * 
   * @param form
   */
  private void matchSuccess(ThirdBindForm form) {
    form.setScmOpenId(
        oauthLoginService.getOpenId(OpenConsts.SMATE_TOKEN, form.getPsnId(), OpenConsts.OPENID_CREATE_TYPE_3));
    // 持久化
    this.persistenceUserUnion(form);
    this.persistenceWeChatRelation(form);
    form.setMsg("绑定成功!");
  }

  /**
   * 持久化OpenUserUnion
   * 
   * @param form
   */
  private void persistenceUserUnion(ThirdBindForm form) {
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
  @Override
  public void persistenceWeChatRelation(ThirdBindForm form) {
    WeChatRelation wcr = new WeChatRelation();
    wcr.setCreateTime(new Date());
    wcr.setSmateOpenId(form.getScmOpenId());
    wcr.setWebChatOpenId(form.getOpenid());
    wcr.setWeChatUnionId(form.getUnionid());
    wcr.setBindType(form.getBindType());
    wcr.setNickName(form.getWechatName());
    weChatRelationDao.save(wcr);
  }

  @Override
  public WeChatRelation findWeChatRelationByPsnId(Long psnId) {
    Long openId = openUserUnionDao.getOpenIdByPsnId(psnId);
    WeChatRelation weChatRelation = weChatRelationDao.getBySmateOpenId(openId);
    return weChatRelation;
  }

  @Override
  public boolean wxHasBinded(String wxUnionId) {
    return weChatRelationDao.checkWxUnionId(wxUnionId);
  }

  @Override
  public Long findPsnOpenIdInUserUnion(Long psnId) {
    return openUserUnionDao.getOpenIdByPsnId(psnId);
  }

}
