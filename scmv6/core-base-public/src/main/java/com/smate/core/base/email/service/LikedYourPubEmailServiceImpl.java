package com.smate.core.base.email.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.email.model.MailOriginalDataPublicInfo;
import com.smate.core.base.mail.model.mongodb.MailLinkPublicInfo;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.pub.dao.PubIndexUrlDao;
import com.smate.core.base.pub.dao.PubSnsPublicDAO;
import com.smate.core.base.pub.model.PubIndexUrl;
import com.smate.core.base.pub.model.PubSnsPublicPO;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.exception.PublicException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * [人名] 人赞了您的论文: [论文名]
 * 
 * @author zk
 *
 */
@Service("likedYourPubEmailService")
@Transactional(rollbackOn = Exception.class)
public class LikedYourPubEmailServiceImpl extends AbstractEmailSend {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainMobile}")
  private String domainMobile;
  @Autowired
  private EmailCommonService emailCommonService;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;
  @Autowired
  private PubSnsPublicDAO pubSnsPublicDAO;

  // psnId：被赞人员
  // likedPsnId：赞人员
  @SuppressWarnings("unchecked")
  @Override
  public Map<String, String> invoke(Object... params) throws PublicException {

    Map<String, Object> ctxMap = (Map<String, Object>) params[0];
    if (!(ctxMap.containsKey("psnId") && ctxMap.containsKey("likedPsnId") && ctxMap.containsKey("pubId"))) {
      logger.error("map对象缺少传入的参数！");
      throw new PublicException();
    }
    Long psnId = (Long) ctxMap.get("psnId");
    Long likedPsnId = (Long) ctxMap.get("likedPsnId");
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    Person person = null;
    Person likedPerson = null;
    try {
      person = personProfileDao.getPersonForEmail(psnId);
      likedPerson = personProfileDao.getPersonForEmail(likedPsnId);
    } catch (Exception e) {
      logger.error(" [人名] 人赞了您的论文: [论文名]邮件，查询人员信息出错.psnId=" + psnId + ",likedPsnId=" + likedPsnId, e);
      throw new PublicException(" [人名] 人赞了您的论文: [论文名]邮件，查询人员信息出错.psnId=" + psnId + ",likedPsnId=" + likedPsnId);
    }
    this.generateMailData(ctxMap, person, likedPerson, paramData);
    return paramData;
  }

  // 获取邮件数据
  private void generateMailData(Map<String, Object> ctxMap, Person person, Person likedPerson,
      Map<String, String> paramData) throws PublicException {
    // 定义构造邮件模版参数集
    Map<String, String> mailData = new HashMap<String, String>();
    // 获取用户设置接收邮件的语言.
    if (StringUtils.isBlank(person.getEmailLanguageVersion())) {
      person.setEmailLanguageVersion(LocaleContextHolder.getLocale().toString());
    }
    // 被赞人员姓名
    mailData.put("psnName", emailCommonService.getPsnNameByEmailLangage(person, person.getEmailLanguageVersion()));
    // 赞人员姓名
    mailData.put("likedPsnName",
        emailCommonService.getPsnNameByEmailLangage(likedPerson, person.getEmailLanguageVersion()));

    // 成果url
    String pubShortUrl = "";
    PubIndexUrl pubIndexUrl = pubIndexUrlDao.get((Long) ctxMap.get("pubId"));
    if (pubIndexUrl != null && StringUtils.isNotBlank(pubIndexUrl.getPubIndexUrl())) {
      pubShortUrl = domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl();
    } else {
      pubShortUrl = domainscm + "/pub/details?des3PubId=" + ServiceUtil.encodeToDes3(ctxMap.get("pubId").toString());
    }
    // 定义要跟踪的链接集
    this.buildMailIink(person, mailData, pubShortUrl);
    // 获取成果信息
    this.generatePubData(ctxMap, mailData, person.getEmailLanguageVersion());
    // 生成主题参数
    this.generateSubjectParam(person, paramData, mailData);
  }

  private void generateSubjectParam(Person person, Map<String, String> paramData, Map<String, String> mailData) {
    // 构造必需的参数
    MailOriginalDataPublicInfo info = new MailOriginalDataPublicInfo();
    Integer templateCode = 10051;
    info.setReceiver(person.getEmail());// 接收邮箱
    info.setSenderPsnId(SecurityUtils.getCurrentUserId());// 发送人psnId，0=系统邮件
    info.setReceiverPsnId(person.getPersonId());// 接收人psnId，0=非科研之友用户
    info.setMailTemplateCode(templateCode);// 模版标识，参考V_MAIL_TEMPLATE
    info.setMsg("成果赞邮件");// 描述
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializer(info));
    // 主题参数，添加如下：
    List<String> subjectParamLinkList = new ArrayList<String>();
    subjectParamLinkList.add(mailData.get("likedPsnName"));
    subjectParamLinkList.add(HtmlUtils.htmlUnescape(mailData.get("pubTitle")));
    mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));
    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
  }

  // 定义要跟踪的链接集
  private void buildMailIink(Person person, Map<String, String> mailData, String pubShortUrl) throws PublicException {
    String psnShortUrl = emailCommonService.getFrdUrl(SecurityUtils.getCurrentUserId());
    // 定义要跟踪的链接集，系统会生成短地址并跟踪访问记录
    List<String> linkList = new ArrayList<String>();
    MailLinkPublicInfo l1 = new MailLinkPublicInfo();
    l1.setKey("domainUrl");
    l1.setUrl(domainscm);
    l1.setUrlDesc("科研之友首页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));
    MailLinkPublicInfo l2 = new MailLinkPublicInfo();
    l2.setKey("likedPsnUrl");
    l2.setUrl(psnShortUrl);
    l2.setUrlDesc("好友主页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l2));
    MailLinkPublicInfo l3 = new MailLinkPublicInfo();
    l3.setKey("pubDetailUrl");
    l3.setUrl(pubShortUrl);
    l3.setUrlDesc("成果详情");
    linkList.add(JacksonUtils.jsonObjectSerializer(l3));
    MailLinkPublicInfo l4 = new MailLinkPublicInfo();
    l4.setKey("impactsUrl");
    l4.setUrl(EmailCommonService.PC_OR_MB_TOKEN + "pc="
        + emailCommonService.getImpactsUrl(person.getPersonId(), person.getEmailLanguageVersion()) + "&&mobile="
        + domainMobile + "/psnweb/outside/mobile/influence");
    l4.setUrlDesc("影响力页面");
    linkList.add(JacksonUtils.jsonObjectSerializer(l4));
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
  }

  // 获取成果信息
  private void generatePubData(Map<String, Object> ctxMap, Map<String, String> mailData, String emailLanguage)
      throws PublicException {
    PubSnsPublicPO pub = pubSnsPublicDAO.get((Long) ctxMap.get("pubId"));
    if (pub == null) {
      throw new PublicException();
    }
    // 成果标题
    mailData.put("pubTitle", pub.getTitle());
    // 成果作者
    Map<String, Object> pubAuthorInfoMap = emailCommonService.getNewPubAuthor(pub, 3);
    mailData.put("pubAuthors", (String) pubAuthorInfoMap.get("authors"));
    mailData.put("authorNum", String.valueOf(pubAuthorInfoMap.get("authorNum")));
    mailData.put("dbId", String.valueOf(EmailCommonService.ZH_LOCALE.equals(emailLanguage) ? 0 : 1));
    // 成果来源
    mailData.put("pubBrief", pub.getBriefDesc());
    // 成果全文图片
    mailData.put("fullTextUrl", emailCommonService.getNewFullTextImg(pub.getPubId()));
  }

}
