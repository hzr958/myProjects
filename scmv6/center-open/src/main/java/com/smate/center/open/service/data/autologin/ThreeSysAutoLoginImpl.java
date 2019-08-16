package com.smate.center.open.service.data.autologin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.cache.OpenCacheService;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.dao.group.OpenGroupUnionDao;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.exception.OpenSerGetPsnInfoException;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.dao.security.AutoLoginOauthInfoDao;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * 第3方系统 自动登录用
 * 
 * @author zk
 *
 */

@Transactional(rollbackFor = Exception.class)
public class ThreeSysAutoLoginImpl extends ThirdDataTypeBase {

  // url
  private static Map<String, String> urlMap = new HashMap<String, String>() {
    private static final long serialVersionUID = 5907973853291213049L;
    {
      put("puburl01", "/pubweb/publication/show");
      put("grouppuburl01", "/groupweb/groupmain/show?des3GroupId=");
      put("groupcreateburl01", "/groupweb/creategroup/create");
    }
  };

  @Autowired
  private AutoLoginOauthInfoDao autoLoginOauthInfoDao;

  @Autowired
  private OpenCacheService openCacheService;

  @Autowired
  private OpenGroupUnionDao openGroupUnionDao;

  @Value("${domainscm}")
  private String domainscm;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    // 没有 参数校验 直接返回成功
    Map<String, Object> temp = new HashMap<String, Object>();
    String urlCode = (String) paramet.get(OpenConsts.MAP_AUTO_LOGIN_URL_CODE);
    if (StringUtils.isNotBlank(urlCode) && StringUtils.isNotBlank(urlMap.get(urlCode))) {
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    } else {
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
      temp.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_401);
    }
    return temp;
  }

  /**
   * 根据一定的规则 生成加密 ID 并放入缓存中
   * 
   * @param psnId
   * @throws OpenException
   */
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    try {
      // 获取 加密id 把加密id当做 cache的key 放入缓存中 注意保证唯一性（统一使用 唯一编码做md5加密
      // (openId+时间挫)
      /*
       * String oauthid = DigestUtils.md5Hex(UUID.randomUUID().toString() + paramet.get("openId")); // 缓存
       * 过期时间可以通过 参数的方式 来区分 不同的需求 不一样的过期时间，比如 邮件的自动登录，记住登录的自动登录 // 第3方系统 跳转的自动登录 // 不保存缓存 （如果重启缓存服务器的话
       * 所有缓存都会失效） 存数据库 Date tempDate = new Date(); AutoLoginOauthInfo autoLogin = new
       * AutoLoginOauthInfo(); autoLogin.setSecurityId(oauthid); autoLogin.setCreateTime(tempDate);
       * autoLogin.setLoginType((String) paramet.get(OpenConsts.MAP_TOKEN));
       * autoLogin.setOverdueTime(getDate(tempDate, 2 * 60 * 1000L)); autoLogin
       * .setPsnId(Long.valueOf(paramet.get("psnId").toString())); autoLogin.setUseTimes(0);
       * autoLogin.setToken(paramet.get("token").toString()); autoLoginOauthInfoDao.save(autoLogin);
       * 
       * openCacheService.put(SecurityConstants.AUTO_LOGIN_INFO_CACHE, 2 * 60,
       * oauthid,autoLogin.getPsnId()+","+autoLogin.getLoginType());
       */
      Map<String, Object> temp = new HashMap<String, Object>();
      /*
       * List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>(); Map<String, Object>
       * dataMap = new HashMap<String, Object>();
       * if("puburl01".equals(paramet.get(OpenConsts.MAP_AUTO_LOGIN_URL_CODE ))){
       * dataMap.put(SecurityConstants.AUTO_LOGIN_URL, this.handleAutoLoginUrl(paramet, oauthid)); }else
       * if("grouppuburl01" .equals(paramet.get(OpenConsts.MAP_AUTO_LOGIN_URL_CODE))){
       * dataMap.put(SecurityConstants.AUTO_LOGIN_URL, this.handleGroupAutoLoginUrl(paramet, oauthid));
       * }else if("groupcreateburl01" .equals(paramet.get(OpenConsts.MAP_AUTO_LOGIN_URL_CODE))){
       * dataMap.put(SecurityConstants.AUTO_LOGIN_URL, this.handleGreateGroupAutoLoginUrl(paramet,
       * oauthid)); } dataList.add(dataMap);
       */
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      temp.put(OpenConsts.RESULT_MSG, "scm-0000");// 响应成功
      temp.put(OpenConsts.RESULT_DATA, null);
      return temp;
    } catch (Exception e) {
      logger.error("获取自动登录链接异常 map=" + paramet.toString(), e);
      throw new OpenSerGetPsnInfoException(e);
    }
  }

  // 整理要自动登录的链接(创建群组)
  private String handleGreateGroupAutoLoginUrl(Map<String, Object> paramet, String oauthid) {
    String targetUrl =
        this.domainscm + urlMap.get(paramet.get(OpenConsts.MAP_AUTO_LOGIN_URL_CODE)) + "?threesys=1&AID=" + oauthid;
    String preTegetUrl = "";
    try {
      preTegetUrl = this.domainscm + "/scmwebsns/loginout?targetUrl=" + URLEncoder.encode(targetUrl, "utf-8");// "/scmwebsns/loginout?service="
    } catch (UnsupportedEncodingException e) {
      preTegetUrl = this.domainscm + targetUrl + oauthid;
    }
    return preTegetUrl;
  }

  // 整理要自动登录的链接(个人成果)
  private String handleAutoLoginUrl(Map<String, Object> paramet, String oauthid) {
    String targetUrl =
        this.domainscm + urlMap.get(paramet.get(OpenConsts.MAP_AUTO_LOGIN_URL_CODE)) + "?threesys=1&AID=" + oauthid;
    String preTegetUrl = "";
    try {
      preTegetUrl = this.domainscm + "/scmwebsns/loginout?targetUrl=" + URLEncoder.encode(targetUrl, "utf-8");// "/scmwebsns/loginout?service="
    } catch (UnsupportedEncodingException e) {
      preTegetUrl = this.domainscm + targetUrl + oauthid;
    }
    return preTegetUrl;
  }

  // 整理要自动登录的链接(群组成果)
  private String handleGroupAutoLoginUrl(Map<String, Object> paramet, String oauthid) {
    Long groupId =
        openGroupUnionDao.findGroupIdByGroupCode(String.valueOf(paramet.get(OpenConsts.MAP_AUTO_LOGIN_GROUP_CODE)),
            Long.valueOf(paramet.get("psnId").toString()));
    if (groupId == null || groupId <= 0) {
      return "";
    }
    String targetUrl = this.domainscm + urlMap.get(paramet.get(OpenConsts.MAP_AUTO_LOGIN_URL_CODE))
        + Des3Utils.encodeToDes3(groupId.toString()) + "&threesys=1&AID=" + oauthid + "&backType=3";
    String preTegetUrl = "";
    try {
      preTegetUrl = this.domainscm + "/scmwebsns/loginout?targetUrl=" + URLEncoder.encode(targetUrl, "utf-8");// "/scmwebsns/loginout?service="
    } catch (UnsupportedEncodingException e) {
      preTegetUrl = targetUrl + oauthid;
    }
    return preTegetUrl;
  }

  /**
   * 计算时间上的差额
   * 
   * @param date
   * @param saveTime 时间差额(s)
   * @return
   */
  private Date getDate(Date date, Long saveTime) {
    // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return new Date(date.getTime() + saveTime);
  }

  public static void main(String[] args) {
    String oauthid = UUID.randomUUID().toString();

    System.out.println(DigestUtils.md5Hex(oauthid + "12345678"));
    Date tempDate = new Date();
    System.out.println("今天的日期：" + tempDate);
    System.out.println("两天前的日期：" + new Date(tempDate.getTime() - 2 * 24 * 60 * 60 * 1000));
    System.out.println("三天后的日期：" + new Date(tempDate.getTime() + 3 * 24 * 60 * 60 * 1000));

    System.out.println(tempDate);
    System.out.println(new Date(tempDate.getTime() + 2 * 60 * 1000));

  }

}
