package com.smate.center.oauth.action.appversion;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.center.oauth.cache.OauthCacheService;
import com.smate.center.oauth.model.consts.mobile.MobileConsts;
import com.smate.center.oauth.model.mobile.version.AppVersionVo;
import com.smate.center.oauth.model.mobile.version.VersionInfoBean;
import com.smate.center.oauth.service.version.MobileAppVersionService;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.mobile.SmateMobileUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * 移动端版本信息控制器
 * 
 * @author wsn
 * @date Jan 2, 2019
 */
@Results({@Result(name = "version_list", location = "/WEB-INF/jsp/version/app_version_list.jsp"),
    @Result(name = "app_download", location = "/WEB-INF/jsp/version/app_download_page.jsp")})
public class MobileVersionAction extends ActionSupport implements Preparable, ModelDriven<AppVersionVo> {

  private static final long serialVersionUID = 5693567449035554933L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MobileAppVersionService mobileAppVersionService;
  private AppVersionVo versionVo;
  @Autowired
  private OauthCacheService oauthCacheService;
  @Value("${domainMobile}")
  private String domainMobile;
  @Value("${domainscm}")
  private String domainScm;


  /**
   * 获取最新的app版本信息
   * 
   * @param versionVo
   * @return
   */
  @Action("/oauth/mobile/version")
  public String checkVersion() {
    VersionInfoBean versionInfo = null;
    String appType = versionVo.getAppType();
    try {
      if (StringUtils.isNotBlank(appType) && MobileConsts.APP_TYPES.contains(appType)) {
        versionInfo = mobileAppVersionService.findLastAppVersionInfo(appType);
      }
    } catch (Exception e) {
      logger.error("获取{}版本app最新的 版本信息出错", appType, e);
    }
    AppActionUtils.renderAPPReturnJson(versionInfo, 0, IOSHttpStatus.OK);
    return null;
  }


  /**
   * 获取某个类型app的所有版本信息
   * 
   * @param versionVo
   * @return
   */
  @Action("/oauth/version/list")
  public String showAndroidAppVersion() {
    String appType = versionVo.getAppType();
    try {
      // 控制一下账号权限，指定只有BPO账号能操作
      if (!checkAuth()) {
        Struts2Utils.getResponse().sendRedirect("/oauth/index");
        return null;
      }
      if (StringUtils.isNotBlank(appType) && MobileConsts.APP_TYPES.contains(appType)) {
        mobileAppVersionService.findAllAppVersionInfo(versionVo);
      }
    } catch (Exception e) {
      logger.error("获取{}版本app的所有版本信息异常", appType, e);
    }
    return "version_list";
  }

  /**
   * 编辑（新增、编辑、删除）版本信息
   * 
   * @return
   */
  @Action("/oauth/version/ajaxupdate")
  public String updateAppVersionInfo() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      // 控制一下账号权限，指定只有BPO账号能操作
      if (!checkAuth()) {
        Struts2Utils.getResponse().sendRedirect("/oauth/index");
        return null;
      }
      map.put("result", mobileAppVersionService.updateAppVersionInfo(versionVo));
    } catch (Exception e) {
      logger.error("更新版本信息出错, optType={}, versionId={}", versionVo.getOptType(), versionVo.getId(), e);
      map.put("result", "error");
    }
    Struts2Utils.renderJson(map, "encoding:utf-8");
    return null;
  }


  @Action("/oauth/version/ajaxget")
  public String findVersionInfoById() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      // 控制一下账号权限，指定只有BPO账号能操作
      if (!checkAuth()) {
        Struts2Utils.getResponse().sendRedirect("/oauth/index");
      }
      VersionInfoBean versionInfo =
          mobileAppVersionService.findAppVersionInfoById(versionVo.getAppType(), versionVo.getId());
      map.put("versionInfo", versionInfo != null ? JacksonUtils.jsonObjectSerializer(versionInfo) : "");
      map.put("result", "success");
    } catch (Exception e) {
      logger.error("获取版本信息异常，versionId={}", versionVo.getId(), e);
      map.put("result", "error");
    }
    Struts2Utils.renderJson(map, "encoding:utf-8");
    return null;
  }


  /**
   * 下载app
   * 
   * @return
   */
  @Action("/oauth/app/download")
  public String downloadApp() {
    // TODO 要是移动端已经安装了科研之友APP,暂时看了qq、领英的安卓的都是直接下载，IOS的都是直接打开app store
    try {
      String userAgent = Struts2Utils.getRequest().getHeader("user-agent");
      // 若不是android或IOS系统，默认跳转登录页面
      String forwardUrl = domainMobile + "/oauth/mobile/index";
      String appType = versionVo.getAppType();
      boolean downloadAndroid = MobileConsts.APP_TYPE_ANDROID.equals(versionVo.getAppType())
          || (StringUtils.isBlank(appType) && SmateMobileUtils.isAndroid(userAgent));
      boolean downloadIos = MobileConsts.APP_TYPE_IOS.equals(versionVo.getAppType())
          || (StringUtils.isBlank(appType) && SmateMobileUtils.isIphone(userAgent));
      // 安卓系统取最新的版本的下载链接
      if (downloadAndroid) {
        VersionInfoBean versionInfo = mobileAppVersionService.findLastAppVersionInfo(MobileConsts.APP_TYPE_ANDROID);
        if (versionInfo != null) {
          forwardUrl = versionInfo.getDownloadUrl();
        }
      } else if (downloadIos) {
        // IOS跳转app store下载页面
        forwardUrl = MobileConsts.SMATE_IOS_APP_STORE_URL;
      }
      Struts2Utils.getResponse().sendRedirect(forwardUrl);
    } catch (Exception e) {
      logger.error("下载app异常", e);
    }
    return null;
  }

  @Action("/oauth/app/get")
  public String showAppDownload() {
    versionVo.setDomainScm(domainMobile);
    return "app_download";
  }


  @Override
  public AppVersionVo getModel() {
    return versionVo;
  }


  @Override
  public void prepare() throws Exception {
    if (versionVo == null) {
      versionVo = new AppVersionVo();
    }
  }


  private boolean checkAuth() {
    boolean canAccess = false;
    String sessionId = Struts2Utils.getRequest().getSession().getId();
    Object uDetails = oauthCacheService.get(SecurityConstants.USER_DETAILS_INFO_CACHE, sessionId);
    if (uDetails != null) {
      Map<String, Object> userDetails = (Map<String, Object>) uDetails;
      Long userId = 0L;
      if (userDetails.get("userId") != null) {
        userId = NumberUtils.toLong(userDetails.get("userId").toString());
      }
      // test的bpo的账号的id不是2
      String sys = System.getenv("RUN_ENV");
      if (NumberUtils.compare(userId, 2L) == 0
          || ("test".equalsIgnoreCase(sys) && CommonUtils.compareLongValue(1000000000195L, userId))) {
        canAccess = true;
      }
    }
    return canAccess;
  }

}
