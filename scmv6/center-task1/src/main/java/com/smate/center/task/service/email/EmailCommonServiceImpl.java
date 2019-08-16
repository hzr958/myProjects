package com.smate.center.task.service.email;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acegisecurity.util.EncryptionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.smate.center.task.model.sns.quartz.Publication;
import com.smate.core.base.utils.exception.PublicException;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.sys.ScmSystemUtil;

/**
 * 
 * 邮件公共方法
 * 
 * @author zk
 * 
 */
@Service("emailCommonService")
@Transactional(rollbackFor = Exception.class)
public class EmailCommonServiceImpl implements EmailCommonService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ScmSystemUtil scmSystemUtil;

  /**
   * 获取影响力链接
   * 
   * @param psnId
   * @return
   * @throws PublicException
   */
  @Override
  public String getImpactsUrl(Long psnId, String locale) throws PublicException {

    String impactsUrl = null;
    try {
      String autoLoginUrl = scmSystemUtil.getAutoLoginUrl(psnId, null);
      String optUrl = scmSystemUtil.getSysDomain() + "/psnweb/homepage/show?module=influence";
      impactsUrl = autoLoginUrl + "&service=" + java.net.URLEncoder.encode(optUrl, ScmSystemUtil.ENCODING);
    } catch (Exception e) {
      logger.error(" 获取影响力链接！" + e.getMessage());
      throw new PublicException(e);
    }
    return impactsUrl;
  }

  // 获取感兴趣的研究领域链接
  @Override
  public String getFavPubsUrl(Long psnId) throws PublicException {

    String moreFavPubsUrl = null;
    try {
      String autoLoginUrl = scmSystemUtil.getAutoLoginUrl(psnId, null);
      String optUrl = scmSystemUtil.getSysDomain() + "/scmwebsns/reference/refrecommend?reNewAction=need&menuId=4";
      moreFavPubsUrl = autoLoginUrl + "&service=" + java.net.URLEncoder.encode(optUrl, ScmSystemUtil.ENCODING);
    } catch (Exception e) {
      logger.error(" 获取影响力链接！" + e.getMessage());
      throw new PublicException(e);
    }
    return moreFavPubsUrl;
  }

  /**
   * 通到前num位作者名
   */
  @Override
  public Map<String, Object> getPubAuthor(Publication pub, Integer num) throws PublicException {

    Map<String, Object> authorMap = new HashMap<String, Object>();
    StringBuffer sb = new StringBuffer();
    if (StringUtils.isNotBlank(pub.getAuthorNames())) {
      if (num == 0) {
        num = 3;
      }
      String[] names = StringUtils.split(HtmlUtils.htmlUnescape(pub.getAuthorNames()), ";");
      sb.append(names[0]);
      authorMap.put("authorNum", names.length);
      for (int i = 1; i < num && i < names.length; i++) {
        sb.append(";");
        sb.append(names[i]);
      }
      authorMap.put("authors", sb.toString());
    }
    return authorMap;
  }

  /**
   * 获取成果来源信息
   */
  @Override
  public String getPubBrief(Publication pub, String emailLanguage) throws PublicException {

    if (ZH_LOCALE.equals(emailLanguage)) {
      return StringUtils.isNotBlank(pub.getBriefDesc()) ? pub.getBriefDesc() : pub.getBriefDescEn();
    } else {
      return StringUtils.isNotBlank(pub.getBriefDescEn()) ? pub.getBriefDescEn() : pub.getBriefDesc();
    }
  }

  /**
   * 获取成果标题
   */
  @Override
  public String getPubTitle(Publication pub, String emailLanguage) throws PublicException {

    if (ZH_LOCALE.equals(emailLanguage)) {
      return StringUtils.isNotBlank(pub.getZhTitle()) ? pub.getZhTitle() : pub.getEnTitle();
    } else {
      return StringUtils.isNotBlank(pub.getEnTitle()) ? pub.getEnTitle() : pub.getZhTitle();
    }
  }

  /**
   * 获取全文图片
   */
  @Override
  public String getFullTextImg(String fileId, Long pubId) throws PublicException {

    /*
     * String fullTextImg = BASE_URL + NO_FULLTEXT_IMG; try { if(StringUtils.isBlank(fileId)){ return
     * null; } ArchiveFiles af = archiveFilesDao.findArchiveFilesById(Long.valueOf(fileId));
     * 
     * if (af != null && StringUtils.isNotBlank(af.getFileName())) { String fileName = af.getFileName();
     * if (fileName.toLowerCase().endsWith("doc") || fileName.toLowerCase().endsWith("docx")) {
     * fullTextImg = BASE_URL + DOC_IMG; } else if (fileName.toLowerCase().endsWith("txt")) {
     * fullTextImg = BASE_URL + txt_IMG; } else if (fileName.toLowerCase().endsWith("zip")) {
     * fullTextImg = BASE_URL + ZIP_IMG; } else if (fileName.toLowerCase().endsWith("html")) {
     * fullTextImg = BASE_URL + HTML_IMG; } else if (fileName.toLowerCase().endsWith("pdf")) {
     * fullTextImg = null; } } } catch (Exception e) { }
     */
    return BASE_URL + NO_FULLTEXT_IMG;
  }

  /**
   * 判断成果来源是否来自isi库 用来多个作者“等”的中英文处理
   */
  @Override
  public Integer isIsi(Integer sourceDbid) throws PublicException {
    // isi库
    List<Integer> isiDbId = new ArrayList<Integer>();
    isiDbId.add(15);
    isiDbId.add(16);
    isiDbId.add(17);
    return isiDbId.contains(sourceDbid) ? 1 : 0;
  }

  /**
   * 根据邮件接收语言，获取用户名
   */
  @Override
  public String getPsnName(Person person) throws PublicException {
    if (person == null) {
      return null;
    }
    String psnName = "";
    if (ZH_LOCALE.equals(person.getEmailLanguageVersion())) {
      psnName = person.getName();
      if (StringUtils.isBlank(psnName)) {
        psnName = person.getFirstName() + " " + person.getLastName();
      }
    } else {
      psnName = person.getFirstName() + " " + person.getLastName();
      if (StringUtils.isBlank(person.getFirstName()) || StringUtils.isBlank(person.getLastName())) {
        psnName = person.getName();
      }
    }
    return psnName;
  }

  /**
   * 根据设置邮件的接受语言，获取用户名.
   * 
   * @param person
   * @param emailLangage
   * @return personName
   */
  @Override
  public String getPsnNameByEmailLangage(Person person, String emailLangage) throws PublicException {
    if (person == null) {
      return null;
    }
    if (emailLangage == null) {
      emailLangage = LocaleContextHolder.getLocale().toString();
    }
    String psnName = "";
    if (ZH_LOCALE.equals(emailLangage)) {
      psnName = person.getName();
      if (StringUtils.isBlank(psnName)) {
        psnName = person.getFirstName() + " " + person.getLastName();
      }
    } else {
      psnName = person.getFirstName() + " " + person.getLastName();
      if (StringUtils.isBlank(person.getFirstName()) || StringUtils.isBlank(person.getLastName())) {
        psnName = person.getName();
      }
    }
    return psnName;
  }

  /***
   * 得到成果详情url
   */

  @Override
  public String getPubDetail(Long psnId, Long pubId, Integer nodeId) throws PublicException {
    String pubDetailUrl = null;
    try {
      String domain = scmSystemUtil.getSysDomain();
      String autoLoginUrl = scmSystemUtil.getAutoLoginUrl(psnId, null);
      String optUrl = domain + "/scmwebsns/publication/loginView?des3Id=" + ServiceUtil.encodeToDes3(pubId.toString())
          + "," + nodeId;
      pubDetailUrl = autoLoginUrl + "&service=" + java.net.URLEncoder.encode(optUrl, ScmSystemUtil.ENCODING);
    } catch (Exception e) {
      logger.error("获取成果详情链接时出错 ！" + e.getMessage());
      throw new PublicException(e);
    }
    return pubDetailUrl;
  }

  /***
   * 消息中心，全文请求url
   */

  @Override
  public String getMsgFullTextUrl(Long psnId) throws PublicException {
    String pubDetailUrl = null;
    try {
      String domain = scmSystemUtil.getSysDomain();
      String autoLoginUrl = scmSystemUtil.getAutoLoginUrl(psnId, null);
      String optUrl = domain + "/scmwebsns/msgbox/ftRequestMain?menuId=1200";
      pubDetailUrl = autoLoginUrl + "&service=" + java.net.URLEncoder.encode(optUrl, ScmSystemUtil.ENCODING);
    } catch (Exception e) {
      logger.error("获取成果详情链接时出错 ！" + e.getMessage());
      throw new PublicException(e);
    }
    return pubDetailUrl;
  }

  /**
   * 获取站内信链接
   */
  @Override
  public String getEmailUrl(Long psnId) throws PublicException {
    String emailUrl = null;
    try {
      String domain = scmSystemUtil.getSysDomain();
      String autoLoginUrl = scmSystemUtil.getAutoLoginUrl(psnId, null);
      String optUrl = domain + "/scmwebsns/msgbox/smsMain?menuId=1200?status=0&&msgboxFlag=-1";
      emailUrl = autoLoginUrl + "&service=" + java.net.URLEncoder.encode(optUrl, ScmSystemUtil.ENCODING);
    } catch (Exception e) {
      logger.error("获取站内邮件链接时出错 ！" + e.getMessage());
      throw new PublicException(e);
    }
    return emailUrl;

  }

  /**
   * 成果在我的成果中的链接地址
   */
  @Override
  public String getMyPubUrl(Long psnId, String zhTitle) throws PublicException {
    String myPubUrl = null;
    try {
      String domain = scmSystemUtil.getSysDomain();
      String autoLoginUrl = scmSystemUtil.getAutoLoginUrl(psnId, null);
      String optUrl = domain + "/pubweb/publication/show?menuId=1300&searchKey=" + zhTitle;
      myPubUrl = autoLoginUrl + "&service=" + java.net.URLEncoder.encode(optUrl, ScmSystemUtil.ENCODING);
    } catch (Exception e) {
      logger.error("获取站内邮件链接时出错 ！" + e.getMessage());
      throw new PublicException(e);
    }
    return myPubUrl;

  }

  @Override
  public String getFrdUrl(Long psnId, Long toPsnId, String casUrl, Integer isAddFrd) throws PublicException {
    String frdUrl = null;
    try {
      String domain = scmSystemUtil.getSysDomain();
      if (StringUtils.isBlank(casUrl)) {
        casUrl = domain + "/cas/";
      }
      String autoLoginUrl = scmSystemUtil.getAutoLoginUrl(psnId, null);
      String operateUrl = domain + "/scmwebsns/resume/psnView?des3PsnId=" + ServiceUtil.encodeToDes3(toPsnId.toString())
          + "&menuId=1100";
      if (isAddFrd == 1) {
        operateUrl += "&isLinkedByMail=true";
      }
      frdUrl = autoLoginUrl + "&service=" + java.net.URLEncoder.encode(operateUrl, ScmSystemUtil.ENCODING);
    } catch (UnsupportedEncodingException e) {
      logger.error("处理好友链接出错！" + e.getMessage());
      throw new PublicException(e);
    }
    return frdUrl;
  }

  /**
   * 获取已验证的预览链接地址.
   * 
   * @param casUrl
   * @param tempPsnId
   * @param frdDomain
   * @param languageVersion
   * @return
   * @throws UnsupportedEncodingException
   */
  @Override
  public String getValidatedViewPsnUrl(String casUrl, Long tempPsnId, Long viewPsnId, String frdDomain,
      String languageVersion) throws PublicException {
    try {
      String userType = "CITE";
      String passStr = userType + "|" + tempPsnId;
      String encpassword = EncryptionUtils.encrypt("111111222222333333444444", passStr);
      String cite_password = java.net.URLEncoder.encode(encpassword, EmailCommonService.ENCODING);
      String targetUrl =
          java.net.URLEncoder.encode(
              frdDomain + "/scmwebsns/resume/psnView?des3PsnId=" + ServiceUtil.encodeToDes3(viewPsnId.toString())
                  + "&menuId=1100&locale=" + languageVersion + "&email2log="
                  + ServiceUtil.encodeToDes3(
                      "mailEventLogByAddFriend|psnId=" + tempPsnId + ",urlId=1" + ",sendPsnId=" + viewPsnId),
              EmailCommonService.ENCODING);
      String inviteUrl = casUrl + "login?submit=true&username=CITE&password=" + cite_password + "&service=" + targetUrl;
      return inviteUrl;
    } catch (Exception e) {
      throw new PublicException(e);
    }

  }

  @Override
  public String getFriendsPage(Long psnId, Long friendPsnId) throws PublicException {
    String friendPageUrl = null;
    try {
      String domain = scmSystemUtil.getSysDomain();
      String autoLoginUrl = scmSystemUtil.getAutoLoginUrl(psnId, null);
      String optUrl =
          domain + "/scmwebsns/resume/psnView?des3PsnId=" + ServiceUtil.encodeToDes3(friendPsnId.toString());
      friendPageUrl = autoLoginUrl + "&service=" + java.net.URLEncoder.encode(optUrl, ScmSystemUtil.ENCODING);
    } catch (UnsupportedEncodingException e) {
      logger.error("处理好友主页链接出错！" + e.getMessage());
      throw new PublicException(e);
    }
    return friendPageUrl;
  }

  @Override
  public String getUrl(Long psnId, String optUrl) throws Exception {
    String friendPageUrl = null;
    try {
      String domain = scmSystemUtil.getSysDomain();
      String autoLoginUrl = scmSystemUtil.getAutoLoginUrl(psnId, null);
      friendPageUrl = autoLoginUrl + "&service=" + java.net.URLEncoder.encode(optUrl, ScmSystemUtil.ENCODING);
    } catch (UnsupportedEncodingException e) {
      logger.error("处理好友主页链接出错！" + e.getMessage());
      throw new Exception(e);
    }
    return friendPageUrl;
  }
}
