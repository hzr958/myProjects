package com.smate.center.open.action.login;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.center.open.cache.OpenCacheService;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.form.OpenLoginForm;
import com.smate.center.open.service.login.ThirdLoginService;
import com.smate.core.base.utils.mobile.SmateMobileUtils;
import com.smate.core.base.utils.rsacode.PublicKeyMap;
import com.smate.core.base.utils.rsacode.RSAUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * 第三方登录弹出框控制器
 * 
 * @author LXZ
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Results({@Result(name = "login", location = "/WEB-INF/jsp/login/login.jsp"),
    @Result(name = "no_authority", location = "/WEB-INF/jsp/login/no_authority.jsp"),
    @Result(name = "mobile_no_authority", location = "/WEB-INF/jsp/login/mobile_no_authority.jsp"),
    @Result(name = "mobile_login", location = "/WEB-INF/jsp/login/mobile_login.jsp"),
    @Result(name = "mobile_relation_no_authority",
        location = "/WEB-INF/jsp/interconnection/mobile_relation_no_authority.jsp"),
    @Result(name = "mobile_relation", location = "/WEB-INF/jsp/interconnection/mobile_relation.jsp"),
    @Result(name = "redirectUrl", location = "${forwardUrl}", type = "redirect"),})
public class ThirdLoginAction extends ActionSupport implements ModelDriven<OpenLoginForm>, Preparable {

  private static final long serialVersionUID = 1186743982203276185L;
  @Autowired
  private ThirdLoginService thirdLoginService;
  @Autowired
  private OpenCacheService openCacheService;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private OpenLoginForm form;
  private String forwardUrl;

  /**
   * Open系统登录页面-获取
   * 
   * 只有动态token 加类型参数正确 才能进入这个页面 tsz
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @return
   */
  @Action("/open")
  public String openLogin() {
    boolean isMobile = SmateMobileUtils.isMobileBrowser(Struts2Utils.getRequest().getHeader("user-agent"));

    try { // 根据类型参数 和 动态token参数 从缓存取出数据 token,
      if (form.getToken() != null && form.getType() != null) {
        Object tokenObj = openCacheService.get(OpenConsts.DYN_TOKEN_CACHE, form.getToken() + "_" + form.getType());
        if (tokenObj == null) {
          form.setLoginStatus(0);
          form.setMsg("来源系统未开放权限");
        } else {
          String local = Struts2Utils.getRequest().getParameter("local");
          if ("en_US".equals(local)) {
            form.setLocale(local);
          }
          form.setToken(tokenObj.toString());
          thirdLoginService.getThirdSysNameByToken(form);
        }
      } else {
        form.setLoginStatus(0);
        form.setMsg("来源系统未开放权限");
      }
    } catch (Exception e) {
      logger.error("访问Open系统登录页面出现错误", e);
    }
    if (form.getLoginStatus() == 1) {
      if (isMobile) {
        return "mobile_login";
      } else {
        Struts2Utils.getResponse().setHeader("P3P",
            "CP=\"IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT\"");
        return "login";
      }
    } else {
      // TOKEN不正确
      if (isMobile) {
        return "mobile_no_authority";
      } else {
        return "no_authority";
      }
    }

  }

  /**
   * Open系统登录信息-提交
   * 
   * 这个方法重定向到目标地址后 给到的都是 动态的openid
   * 
   * 获取sessionId请用Struts2Utils.getRequestSessionId()
   * 
   * 不然可能会因为session为空，新建了session导致sessionId改变
   * 
   * 取不到一些缓存里面的东西，比如验证码
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @return
   */
  @Action("/open/login")
  public String login() {
    boolean isMobile = SmateMobileUtils.isMobileBrowser(Struts2Utils.getRequest().getHeader("user-agent"));
    try {
      if (form.getToken() == null || form.getToken().length() != OpenConsts.TOKEN_LENGTH) {
        if (isMobile) {
          return "mobile_no_authority";
        } else {
          return "no_authority";
        }
      }
      form.setPassword(RSAUtils.decryptStringByJs(form.getEncodePassword()));
      form.setUserName(RSAUtils.decryptStringByJs(form.getEncodeUserName()));
      thirdLoginService.openLogin(form);
      if (form.getLoginStatus() == 1) {
        // 获取动态 openid
        String dynOpenid = DigestUtils.md5Hex(UUID.randomUUID().toString() + form.getPsnId());
        // 保存缓存
        openCacheService.put(OpenConsts.DYN_OPENID_CACHE, 1 * 60, dynOpenid + "_" + form.getToken(), form.getPsnId());

        if (form.getBack().indexOf("?") > 0) {
          forwardUrl = form.getBack() + "&openId=" + dynOpenid;
        } else {
          forwardUrl = form.getBack() + "?openId=" + dynOpenid;
        }
        return "redirectUrl";
      }
    } catch (Exception e) {
      logger.error("登录验证出现错误", e);
      form.setMsg("系统错误,请重试");
    }
    if (isMobile) {
      return "mobile_login";
    } else {
      return "login";
    }
  }

  /**
   * Open系统登录信息-提交 该方法用户名，密码没有加密 上面的方法，用户名密码加密 这个方法重定向到目标地址后 给到的都是 动态的openid
   * 
   * @author ajb
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @return
   */
  @Action("/open/mobilelogin")
  public String mobileLogin() {
    try {
      if (form.getToken() == null || form.getToken().length() != OpenConsts.TOKEN_LENGTH) {
        return "mobile_relation_no_authority";
      }
      thirdLoginService.openLogin(form);
      if (form.getLoginStatus() == 1) {
        // 获取动态 openid
        String dynOpenid = DigestUtils.md5Hex(UUID.randomUUID().toString() + form.getPsnId());
        // 保存缓存
        openCacheService.put(OpenConsts.DYN_OPENID_CACHE, 1 * 60, dynOpenid + "_" + form.getToken(), form.getPsnId());

        if (form.getBack().indexOf("?") > 0) {
          forwardUrl = form.getBack() + "&openId=" + dynOpenid;
        } else {
          forwardUrl = form.getBack() + "?openId=" + dynOpenid;
        }
        return "redirectUrl";
      }
    } catch (Exception e) {
      logger.error("登录验证出现错误", e);
      form.setMsg("系统错误,请重试");
    }
    return "mobile_relation";
  }

  /**
   * 获取系数和指数
   * 
   * @return
   * @throws Exception
   */
  @Action("/open/rsa")
  public String keyPair() {
    try {
      PublicKeyMap publicKeyMap = RSAUtils.getPublicKeyMap();
      if (publicKeyMap != null) {
        Struts2Utils.renderJson(publicKeyMap, "encoding:utf-8");
      }
    } catch (Exception e) {
      logger.error("RSA加密出错", e);
    }
    return null;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new OpenLoginForm();
    }
  }

  @Override
  public OpenLoginForm getModel() {
    return form;
  }

  public String getForwardUrl() {
    return forwardUrl;
  }

  public void setForwardUrl(String forwardUrl) {
    this.forwardUrl = forwardUrl;
  }
}
