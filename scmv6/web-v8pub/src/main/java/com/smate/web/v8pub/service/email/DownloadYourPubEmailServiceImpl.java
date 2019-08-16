package com.smate.web.v8pub.service.email;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.email.dao.MailInitDataDao;
import com.smate.core.base.email.model.MailInitData;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.pub.dao.PubIndexUrlDao;
import com.smate.core.base.utils.constant.EmailConstants;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.v8pub.dao.sns.PubFullTextDAO;
import com.smate.web.v8pub.po.sns.PubFullTextPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.sns.PubSnsService;

/**
 * 下载全文邮件服务实现类
 * 
 * @author lhd
 *
 */
@Service("downloadYourPubEmailService")
@Transactional(rollbackFor = Exception.class)
public class DownloadYourPubEmailServiceImpl implements DownloadYourPubEmailService {

  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private PubSnsService pubSnsService;
  @Autowired
  private MailInitDataDao mailInitDataDao;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private PubFullTextDAO pubFullTextDAO;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;

  @Override
  public void sendDownloadFulltextMail(Map<String, Object> map) throws Exception {
    Map<String, Object> params = buildDownloadFulltextMailData(map);
    MailInitData mid = new MailInitData();
    mid.setCreateDate(new Date());
    mid.setFromNodeId(1);
    mid.setMailData(JacksonUtils.jsonObjectSerializer(params));
    mid.setStatus(1);
    mid.setToAddress(params.get(EmailConstants.EMAIL_RECEIVEEMAIL_KEY) + "");
    mailInitDataDao.saveMailData(mid);
  }

  /**
   * 构建下载全文邮件信息
   * 
   * @param map
   * @return
   * @throws Exception
   */
  public Map<String, Object> buildDownloadFulltextMailData(Map<String, Object> map) throws Exception {
    Map<String, Object> mailMap = new HashMap<String, Object>();
    Object psnIdObj = map.get("psnId");
    Long resOwnerPsnId = NumberUtils.toLong(psnIdObj + "");
    Object downlodedPsnIdObj = map.get("downlodedPsnId");
    Long downlodedPsnId = NumberUtils.toLong(downlodedPsnIdObj + "");
    Object pubIdObj = map.get("pubId");
    Long pubId = NumberUtils.toLong(pubIdObj + "");
    PubSnsPO pub = pubSnsService.get(pubId);
    Person person = personProfileDao.getPersonForEmail(resOwnerPsnId);
    Person downlodedPerson = personProfileDao.getPersonForEmail(downlodedPsnId);
    // email_sender_psnId
    mailMap.put(EmailConstants.EMAIL_SENDER_PSNID_KEY, downlodedPsnId);
    // email_receive_psnId
    mailMap.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, resOwnerPsnId);
    // downlodedPsnName
    mailMap.put("downlodedPsnName", getPsnNameByEmailLangage(downlodedPerson, person.getEmailLanguageVersion()));
    // emailTypeKey
    mailMap.put(EmailConstants.EMAIL_TYPE_KEY, EmailConstants.COMMON_EMAIL);
    // dbId
    mailMap.put("dbId", EmailConstants.ZH_LOCALE.equals(person.getEmailLanguageVersion()) ? 0 : 1);
    // email_receiveEmail
    mailMap.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, person.getEmail());
    // email_Template
    mailMap.put(EmailConstants.EMAIL_TEMPLATE_KEY, "downloded_your_pub_" + person.getEmailLanguageVersion() + ".ftl");
    // psnName
    mailMap.put("psnName", getPsnNameByEmailLangage(person, person.getEmailLanguageVersion()));
    // pubTitle
    mailMap.put("pubTitle", getPubTitle(pub, person.getEmailLanguageVersion()));
    // fullTextUrl
    mailMap.put("fullTextUrl", getFullTextImg(pub.getPubId()));
    // 构建人员信息短地址
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.find(downlodedPsnId);
    StringBuilder downlodedPsnUrl = new StringBuilder();
    downlodedPsnUrl.append(domainscm).append("/");
    if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
      downlodedPsnUrl.append(ShortUrlConst.P_TYPE).append("/").append(psnProfileUrl.getPsnIndexUrl());
    } else {
      downlodedPsnUrl.append("psnweb/outside/homepage?des3PsnId=").append(Des3Utils.encodeToDes3(downlodedPsnId + ""));
    }
    // downlodedPsnUrl
    mailMap.put("downlodedPsnUrl", downlodedPsnUrl);
    // impactsUrl
    mailMap.put("impactsUrl", domainscm + "/psnweb/homepage/show?module=influence");
    Map<String, Object> pubAuthorInfoMap = getPubAuthor(pub, 3);
    // authorNum
    mailMap.put("authorNum", pubAuthorInfoMap.get("authorNum"));
    // pubBrief
    mailMap.put("pubBrief", getPubBrief(pub, person.getEmailLanguageVersion()));
    // domainUrl
    mailMap.put("domainUrl", domainscm);
    // pubAuthors
    mailMap.put("pubAuthors", pubAuthorInfoMap.get("authors"));
    // email_subject
    mailMap.put(EmailConstants.EMAIL_SUBJECT_KEY, getEmailTitle(mailMap, person.getEmailLanguageVersion()));
    // 构建成果短地址
    String pubDetailUrl = domainscm + "/pub/outside/details?des3PubId=" + ServiceUtil.encodeToDes3(pub.getPubId() + "");
    String pubIndexUrl = pubIndexUrlDao.getPubIndexUrl(pub.getPubId());
    if (StringUtils.isNotBlank(pubIndexUrl)) {
      pubDetailUrl = domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl;
    }
    // pubDetailUrl
    mailMap.put("pubDetailUrl", pubDetailUrl);
    return mailMap;
  }

  /**
   * 根据设置邮件的接收语言，获取用户名
   * 
   * @param person
   * @param emailLangage
   * @return
   */
  private String getPsnNameByEmailLangage(Person person, String emailLangage) {
    if (person == null) {
      return null;
    }
    if (emailLangage == null) {
      emailLangage = LocaleContextHolder.getLocale().toString();
    }
    String psnName = "";
    if ("zh_CN".equals(emailLangage)) {
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
   * 获取成果标题
   * 
   * @param pub
   * @param emailLanguage
   * @return
   */
  public String getPubTitle(PubSnsPO pub, String emailLanguage) {
    return pub.getTitle();
  }

  /**
   * 获取全文图片
   * 
   * @param fileId
   * @param pubId
   * @return
   */

  public String getFullTextImg(Long pubId) {
    String fullTextImg = domainscm + DEFAULT_PUBFULLTEXT_IMAGE;
    String pubFulltextImage = null;
    PubFullTextPO fullText = pubFullTextDAO.getPubFullTextByPubId(pubId);
    if (fullText != null) {
      pubFulltextImage = fullText.getThumbnailPath();
    }
    if (StringUtils.isBlank(pubFulltextImage)) {
      if (fullText != null && fullText.getFileId() != null) {
        fullTextImg = domainscm + DEFAULT_PUBFULLTEXT_IMAGE1;
      } else {
        fullTextImg = domainscm + DEFAULT_PUBFULLTEXT_IMAGE;
      }
    } else {
      fullTextImg = domainscm + pubFulltextImage;
    }
    return fullTextImg;
  }

  /**
   * 通到前num位作者名
   * 
   * @param pub
   * @param num
   * @return
   */
  public Map<String, Object> getPubAuthor(PubSnsPO pub, Integer num) {
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
  public String getPubBrief(PubSnsPO pub, String emailLanguage) {
    return pub.getBriefDesc();
  }

  /**
   * 邮件标题
   * 
   * @param mailMap
   * @param emailLanguage
   * @return
   */
  private String getEmailTitle(Map<String, Object> mailMap, String emailLanguage) {
    StringBuffer emailTitle = new StringBuffer();
    if (EmailConstants.ZH_LOCALE.equals(emailLanguage)) {
      emailTitle.append(mailMap.get("downlodedPsnName").toString());
      emailTitle.append("下载了你的论文：");
      if (mailMap.get("pubTitle").toString().length() > 500) {
        emailTitle.append(StringUtils.substring(mailMap.get("pubTitle").toString(), 0, 450));
        emailTitle.append(".....");
      } else {
        emailTitle.append(mailMap.get("pubTitle").toString());
      }
    } else {
      emailTitle.append(mailMap.get("downlodedPsnName"));
      emailTitle.append("downloaded your publications :");
      if (mailMap.get("pubTitle").toString().length() > 500) {
        emailTitle.append(StringUtils.substring(mailMap.get("pubTitle").toString(), 0, 450));
        emailTitle.append(".....");
      } else {
        emailTitle.append(mailMap.get("pubTitle").toString());
      }
    }
    return emailTitle.toString();
  }

}
