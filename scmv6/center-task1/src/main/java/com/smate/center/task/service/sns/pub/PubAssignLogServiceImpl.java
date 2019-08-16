package com.smate.center.task.service.sns.pub;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.service.MailHandleOriginalDataService;
import com.smate.center.task.dao.open.OpenUserUnionDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubIndexUrlDao;
import com.smate.center.task.dao.sns.psn.SendmailPsnLogDao;
import com.smate.center.task.dao.sns.psn.WeChatMessagePsnDao;
import com.smate.center.task.dao.sns.pub.PubAssignLogDao;
import com.smate.center.task.model.pdwh.quartz.PdwhPubIndexUrl;
import com.smate.center.task.model.sns.psn.SendmailPsnLog;
import com.smate.center.task.model.sns.psn.WeChatMessagePsn;
import com.smate.center.task.model.sns.pub.PubInfo;
import com.smate.center.task.service.oauth.OauthLoginService;
import com.smate.center.task.v8pub.dao.sns.PubPdwhDAO;
import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;
import com.smate.core.base.email.service.EmailCommonService;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.wechat.WeChatRelationDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;

@Service("pubAssignLogService")
@Transactional(rollbackFor = Exception.class)
public class PubAssignLogServiceImpl implements PubAssignLogService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainMobile}")
  private String domainMobile;
  @Autowired
  private OauthLoginService oauthLoginService;
  @Autowired
  private MailHandleOriginalDataService mailHandleOriginalDataService;
  @Autowired
  private PubAssignLogDao pubAssignLogDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private PdwhPubIndexUrlDao pdwhPubIndexUrlDao;
  @Autowired
  private WeChatMessagePsnDao weChatMessagePsnDao;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private WeChatRelationDao weChatRelationDao;
  @Autowired
  private SendmailPsnLogDao sendmailPsnLogDao;

  @Override
  public List<SendmailPsnLog> getNeedSendMailPsnIds(Integer size) {
    return sendmailPsnLogDao.getNeedSendMailPsnIds(size);
  }

  @Override
  public List<Long> getReSendMailPsnIds(Integer size) {
    return pubAssignLogDao.getReSendMailPsnIds(size);
  }

  @Override
  public Integer sendConfirmEmailToPsn(SendmailPsnLog sendmailPsnLog) throws Exception {
    try {
      Person person = personDao.getPeronsForEmail(sendmailPsnLog.getPsnId());
      // 人员信息为空，或邮件为空时，不能发送邮件，并将状态置为-3
      if (person == null || person.getEmail() == null) {
        return -3;
      }
      String languageVersion = person.getEmailLanguageVersion();
      String psnName = "";
      if ("zh_CN".equals(languageVersion)) {
        psnName = person.getZhName() != null ? person.getZhName() : person.getEname();
      } else {
        psnName = person.getEname() != null ? person.getEname() : person.getZhName();
      }
      // 收件人主页
      String psnShortUrl = "";
      PsnProfileUrl profileUrl = psnProfileUrlDao.get(person.getPersonId());
      if (profileUrl != null && StringUtils.isNotBlank(profileUrl.getPsnIndexUrl())) {
        psnShortUrl = domainscm + "/P/" + profileUrl.getPsnIndexUrl();
      }
      Long pubSum = pubAssignLogDao.getConfirmPubCount(sendmailPsnLog.getPsnId());
      // 该人员没有需要认领的成果 也将状态置为-3
      if (pubSum == null) {
        return -3;
      }
      // 确认成果链接
      // SCM-11425 重置密码功能实现 调用远程服务获取openId和AID
      Long openId = oauthLoginService.getOpenId("00000000", person.getPersonId(), 2);
      String AID = oauthLoginService.getAutoLoginAID(openId, "ResetPWD");
      String cfmUrl = EmailCommonService.PC_OR_MB_TOKEN + "pc=" + domainscm
          + "/psnweb/homepage/show?module=pub&jumpto=puball&menuId=1200&" + "AID=" + AID + "&resetpwd=true"
          + "&&mobile=" + domainMobile + "/psnweb/mobile/msgbox?model=centerMsg&whoFirst=pubRcmd";
      String pubListUrl = domainscm + "/psnweb/homepage/show?module=pub";
      // 定义接口接收的参数
      Map<String, String> paramData = new HashMap<String, String>();
      // 定义构造邮件模版参数集
      Map<String, String> mailData = new HashMap<String, String>();
      // 构造必需的参数
      Long currentUserId = SecurityUtils.getCurrentUserId();
      String email = person.getEmail();
      Integer templateCode = 10017;
      String msg = "成果认领邮件";
      mailHandleOriginalDataService.buildNecessaryParam(email, currentUserId, sendmailPsnLog.getPsnId(), templateCode,
          msg, paramData);

      // 定义要跟踪的链接集，系统会生成短地址并跟踪访问记录
      List<String> linkList = new ArrayList<String>();
      MailLinkInfo l1 = new MailLinkInfo();
      l1.setKey("domainUrl");
      l1.setUrl(domainscm);
      l1.setUrlDesc("科研之友首页");
      linkList.add(JacksonUtils.jsonObjectSerializer(l1));

      MailLinkInfo l3 = new MailLinkInfo();
      l3.setKey("cfmUrl");
      l3.setUrl(cfmUrl);
      l3.setUrlDesc("认领成果链接");
      linkList.add(JacksonUtils.jsonObjectSerializer(l3));

      MailLinkInfo l4 = new MailLinkInfo();
      l4.setKey("psnUrl");
      l4.setUrl(psnShortUrl);
      l4.setUrlDesc("个人主页链接");
      linkList.add(JacksonUtils.jsonObjectSerializer(l4));

      MailLinkInfo l5 = new MailLinkInfo();
      l5.setKey("pubListUrl");
      l5.setUrl(pubListUrl);
      l5.setUrlDesc("成果列表链接");
      linkList.add(JacksonUtils.jsonObjectSerializer(l5));


      mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
      pubSum = this.buildPubInfoParam(mailData, sendmailPsnLog.getPsnId(), pubSum);
      if (pubSum == 0) {
        return -3;
      }
      mailData.put("pubSum", pubSum.toString());
      mailData.put("psnName", psnName);
      List<String> subjectParamLinkList = new ArrayList<String>();
      subjectParamLinkList.add(psnName);
      subjectParamLinkList.add(pubSum.toString());
      mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));
      paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
      Map<String, String> resutlMap = mailHandleOriginalDataService.doHandle(paramData);
      if ("success".equals(resutlMap.get("result"))) {
        return 1;
      } else {
        return 9;
      }
    } catch (Exception e) {
      logger.error("成果认领邮件参数构造出错", e);
      return 9;
    }
  }

  private Long buildPubInfoParam(Map<String, String> mailData, Long psnId, Long pubSum) {
    Map<String, String> objListMap = new HashMap<String, String>();
    List<Long> pdwhPubIds = pubAssignLogDao.getConfirmPubIds(psnId, pubSum.intValue());
    List<PubPdwhPO> PdwhPublicationList = pubPdwhDAO.getconfirmPubDetail(pdwhPubIds);
    if (PdwhPublicationList == null || PdwhPublicationList.size() == 0) {
      return 0L;
    }
    List<PubInfo> pubdetails = new ArrayList<PubInfo>();
    int i = 0;// 控制最大循环次数为3
    for (PubPdwhPO pdwhPub : PdwhPublicationList) {
      if (i == 3)
        break;
      PubInfo pubInfo = new PubInfo();
      pubInfo.setZhTitle(pdwhPub.getTitle());
      if (pdwhPub.getPubType() != null) {
        if (pdwhPub.getPubType() == 3 || pdwhPub.getPubType() == 4 || pdwhPub.getPubType() == 8) {
          pubInfo.setTypeName("论文");
        } else if (pdwhPub.getPubType() == 5) {
          pubInfo.setTypeName("专利");
        } else if (pdwhPub.getPubType() == 1) {
          pubInfo.setTypeName("奖励");
        } else if (pdwhPub.getPubType() == 2) {
          pubInfo.setTypeName("书/著作");
        } else if (pdwhPub.getPubType() == 7) {
          pubInfo.setTypeName("其他");
        } else if (pdwhPub.getPubType() == 10) {
          pubInfo.setTypeName("书籍章节");
        }
      }
      pubInfo.setBriefDesc(pdwhPub.getBriefDesc());

      String authorNames = pdwhPub.getAuthorNames();
      if (StringUtils.isNotBlank(authorNames)) {
        String endChar = authorNames.substring(authorNames.length() - 1, authorNames.length());
        if (endChar.equalsIgnoreCase(";") || endChar.equalsIgnoreCase("；")) {
          authorNames = authorNames.substring(0, authorNames.length() - 1);
        }
      }
      pubInfo.setAuthorNames(authorNames);
      PdwhPubIndexUrl pdwhPubIndexUrl = pdwhPubIndexUrlDao.get(pdwhPub.getPubId());
      if (pdwhPubIndexUrl != null && StringUtils.isNotBlank(pdwhPubIndexUrl.getPubIndexUrl())) {
        pubInfo.setPdwhPubIndexUrl(domainscm + "/" + ShortUrlConst.S_TYPE + "/" + pdwhPubIndexUrl.getPubIndexUrl());
      } else {
        String url =
            this.domainscm + "/pub/details/pdwh?des3PubId=" + Des3Utils.encodeToDes3(pdwhPub.getPubId().toString());
        pubInfo.setPdwhPubIndexUrl(url);
      }
      pubdetails.add(pubInfo);
      i++;
    }
    objListMap.put("pubDetails", JacksonUtils.listToJsonStr(pubdetails));
    mailData.put("objListMap", JacksonUtils.mapToJsonStr(objListMap));
    return (long) PdwhPublicationList.size();
  }

  @Override
  public void UpdateMailSendStatus(Long id, Integer result) {
    sendmailPsnLogDao.UpdateMailSendStatus(id, result);
  }

  @Override
  public void saveWeChatMessagePsn(Long psnId) {
    Long pubTotal = pubAssignLogDao.getConfirmPubCount(psnId);
    Person person = personDao.getPeronsForEmail(psnId);
    /*
     * {"first":"你有10篇论文待确认","keyword1":"论文推荐",
     * "keyword2":"你可能是\"来自中国猕猴的环孢子虫新种Cyclospora macacae的鉴定分析\"等10篇论文的作者",
     * "remark":"2016-10-31更新,请点击认领","smateTempId":"2"}
     */
    List<Long> pdwhPubIds = pubAssignLogDao.getConfirmPubIds(psnId, 1);
    PubPdwhPO pdwhPub = pubPdwhDAO.get(pdwhPubIds.get(0));
    String pubTitle = HtmlUtils.htmlUnescape(pdwhPub.getTitle());
    String briefDesc = pdwhPub.getBriefDesc();
    Map<String, String> dataMap = new HashMap<String, String>();
    String psnName = person.getZhName() != null ? person.getZhName() : person.getEname();
    String name = StringUtils.isNotBlank(psnName) ? psnName : "你好";
    dataMap.put("first", name + ":\n     你有" + pubTotal + "条成果待认领");
    dataMap.put("keyword1", StringUtils.substring(pubTitle, 0, 500));
    dataMap.put("keyword2", StringUtils.substring(pdwhPub.getAuthorNames(), 0, 500));
    if (StringUtils.isNotBlank(briefDesc)) {
      dataMap.put("keyword3", StringUtils.substring(briefDesc.trim(), 0, 500) + "\n");
    } else {
      dataMap.put("keyword3", "" + "\n");
    }
    dataMap.put("remark", "请点击查看所有待认领成果\n连接科研与创新人员、分享与发现知识、让创新更高效");
    dataMap.put("smateTempId", "5");
    Long openId = openUserUnionDao.getUserOpenId(psnId, "00000000");
    if (openId != null) {
      WeChatMessagePsn message = new WeChatMessagePsn();
      message.setContent(JacksonUtils.mapToJsonStr(dataMap));
      message.setCreateTime(new Date());
      message.setOpenId(openId);
      message.setStatus(0);
      message.setToken("00000000");
      weChatMessagePsnDao.save(message);
    }
  }

  @Override
  public Long getUserOpenId(Long psnId, String token) {
    return openUserUnionDao.getUserOpenId(psnId, token);
  }

  @Override
  public boolean getDataByOpenId(Long openId) {
    List<String> weChatOpenIdList = weChatRelationDao.findWeChatOpenIdList(openId);
    if (CollectionUtils.isNotEmpty(weChatOpenIdList)) {
      return true;
    }
    return false;
  }

}
