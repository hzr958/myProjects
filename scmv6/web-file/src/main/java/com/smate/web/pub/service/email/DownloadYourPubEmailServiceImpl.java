package com.smate.web.pub.service.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import com.smate.center.mail.connector.model.MailOriginalDataInfo;
import com.smate.core.base.email.service.EmailCommonService;
import com.smate.core.base.mail.model.mongodb.MailLinkPublicInfo;
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
import com.smate.web.file.dao.PubFullTextDAO;
import com.smate.web.file.dao.PubSnsDAO;
import com.smate.web.file.model.PubSimpleQuery;
import com.smate.web.file.model.PubSnsPO;
import com.smate.web.file.model.fulltext.PubFullTextPO;

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
  private PsnProfileUrlDao psnProfileUrlDao;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainMobile}")
  private String domainMobile;
  @Autowired
  private PubFullTextDAO pubFullTextDAO;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value(value = "${sendEmail.restful.url}")
  private String sendRestfulUrl;

  @Override
  public void sendDownloadFulltextMail(Map<String, Object> map) throws Exception {
    // 定义构造模板参数集
    Map<String, Object> mailData = new HashMap<String, Object>();
    Map<String, String> paramData = new HashMap<String, String>();
    Long resOwnerPsnId = (Long) map.get("psnId");
    Long downlodedPsnId = (Long) map.get("downlodedPsnId");
    Long pubId = (Long) map.get("pubId");
    PubSnsPO pub = pubSnsDAO.get(pubId);
    Person person = personProfileDao.getPersonForEmail(resOwnerPsnId);
    Person downlodedPerson = personProfileDao.getPersonForEmail(downlodedPsnId);
    // 模板编号
    Integer templateCode = 10053;
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    info.setMsg("下载论文邮件");
    info.setReceiverPsnId(resOwnerPsnId);
    info.setMailTemplateCode(templateCode);
    info.setSenderPsnId(downlodedPsnId);
    info.setReceiver(person.getEmail());
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializer(info));
    String downlodedPsnName = getPsnNameByEmailLangage(downlodedPerson, person.getEmailLanguageVersion());
    String psnName = getPsnNameByEmailLangage(person, person.getEmailLanguageVersion());
    String pubTitle = pub.getTitle();
    // 放置邮件模板信息
    // downlodedPsnName
    mailData.put("downlodedPsnName", downlodedPsnName);
    // psnName
    mailData.put("psnName", psnName);
    // pubTitle
    mailData.put("pubTitle", pubTitle);
    // 构建人员信息短地址
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.find(downlodedPsnId);
    StringBuilder downlodedPsnUrl = new StringBuilder();
    downlodedPsnUrl.append(domainscm).append("/");
    if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
      downlodedPsnUrl.append(ShortUrlConst.P_TYPE).append("/").append(psnProfileUrl.getPsnIndexUrl());
    } else {
      downlodedPsnUrl.append("psnweb/outside/homepage?des3PsnId=").append(Des3Utils.encodeToDes3(downlodedPsnId + ""));
    }
    // impactsUrl
    String impactsUrl = domainscm + "/psnweb/homepage/show?module=influence";
    Map<String, Object> pubAuthorInfoMap = getPubAuthor(pub, 3);
    // authorNum
    mailData.put("authorNum", pubAuthorInfoMap.get("authorNum"));
    // pubBrief
    mailData.put("pubBrief", pub.getBriefDesc());
    // domainUrl
    mailData.put("domainUrl", domainscm);
    // pubAuthors
    mailData.put("pubAuthors", pubAuthorInfoMap.get("authors"));
    // 构建成果短地址
    String pubDetailUrl = domainscm + "/pub/outside/details?des3PubId=" + ServiceUtil.encodeToDes3(pub.getPubId() + "");
    String pubIndexUrl = pubIndexUrlDao.getPubIndexUrl(pub.getPubId());
    if (StringUtils.isNotBlank(pubIndexUrl)) {
      pubDetailUrl = domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl;
    }
    // 主题参数，添加如下：
    List<String> subjectParamLinkList = new ArrayList<String>();
    subjectParamLinkList.add(downlodedPsnName);
    subjectParamLinkList.add(HtmlUtils.htmlUnescape(pubTitle));
    mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));

    // 链接跟踪
    List<String> linkList = new ArrayList<String>();
    MailLinkPublicInfo l1 = new MailLinkPublicInfo();
    l1.setKey("domainUrl");
    l1.setUrl(domainscm);
    l1.setUrlDesc("科研之友首页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));
    MailLinkPublicInfo l2 = new MailLinkPublicInfo();
    l2.setKey("downlodedPsnUrl");
    l2.setUrl(downlodedPsnUrl.toString());
    l2.setUrlDesc("好友主页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l2));
    MailLinkPublicInfo l3 = new MailLinkPublicInfo();
    l3.setKey("pubDetailUrl");
    l3.setUrl(pubDetailUrl);
    l3.setUrlDesc("成果详情");
    linkList.add(JacksonUtils.jsonObjectSerializer(l3));
    MailLinkPublicInfo l4 = new MailLinkPublicInfo();
    l4.setKey("impactsUrl");
    // 设置需要针对不同的方式跳到不同页面的标识 最好不容易和地址栏的地址相同 scmdistributionaddr:
    l4.setUrl(EmailCommonService.PC_OR_MB_TOKEN + "pc=" + impactsUrl + "&&mobile=" + domainMobile
        + "/psnweb/outside/mobile/influence");
    l4.setUrlDesc("影响力页面地址包含移动和pc");
    linkList.add(JacksonUtils.jsonObjectSerializer(l4));
    MailLinkPublicInfo l5 = new MailLinkPublicInfo();
    l5.setKey("fullTextUrl");
    l5.setUrl(getFullTextImg(pub.getPubId()));
    l5.setUrlDesc("全文链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l5));
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
    try {
      restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
    } catch (Exception e) {
      return;
    }
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
  public String getPubTitle(PubSimpleQuery pub, String emailLanguage) {
    if (EmailConstants.ZH_LOCALE.equals(emailLanguage)) {
      return StringUtils.isNotBlank(pub.getZhTitle()) ? pub.getZhTitle() : pub.getEnTitle();
    } else {
      return StringUtils.isNotBlank(pub.getEnTitle()) ? pub.getEnTitle() : pub.getZhTitle();
    }
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
  public String getPubBrief(PubSimpleQuery pub, String emailLanguage) {

    if (EmailConstants.ZH_LOCALE.equals(emailLanguage)) {
      return StringUtils.isNotBlank(pub.getBriefDesc()) ? pub.getBriefDesc() : pub.getBriefDescEn();
    } else {
      return StringUtils.isNotBlank(pub.getBriefDescEn()) ? pub.getBriefDescEn() : pub.getBriefDesc();
    }
  }

}
