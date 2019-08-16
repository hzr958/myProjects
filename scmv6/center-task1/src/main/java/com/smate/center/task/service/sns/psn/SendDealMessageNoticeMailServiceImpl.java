package com.smate.center.task.service.sns.psn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.service.MailHandleOriginalDataService;
import com.smate.center.task.dao.group.GrpProposerDao;
import com.smate.center.task.dao.sns.msg.MsgRelationDao;
import com.smate.center.task.dao.sns.psn.FriendTempDao;
import com.smate.center.task.dao.sns.pub.PubAssignLogDao;
import com.smate.center.task.dao.sns.quartz.PubFulltextPsnRcmdDao;
import com.smate.center.task.model.sns.quartz.PubFulltextPsnRcmd;
import com.smate.center.task.service.oauth.OauthLoginService;
import com.smate.center.task.v8pub.dao.sns.PubPdwhDAO;
import com.smate.center.task.v8pub.dao.sns.PubSnsDAO;
import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;
import com.smate.center.task.v8pub.sns.po.PubSnsPO;
import com.smate.core.base.email.service.EmailCommonService;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;

@Service("sendDealMessageNoticeMailService")
@Transactional(rollbackFor = Exception.class)
public class SendDealMessageNoticeMailServiceImpl implements SendDealMessageNoticeMailService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private MailHandleOriginalDataService mailHandleOriginalDataService;
  @Autowired
  private OauthLoginService oauthLoginService;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private MsgRelationDao msgRelationDao;
  @Autowired
  private PubAssignLogDao pubAssignLogDao;
  @Autowired
  private PubFulltextPsnRcmdDao pubFulltextPsnRcmdDao;
  @Autowired
  private FriendTempDao friendTempDao;
  @Autowired
  private GrpProposerDao grpProposerDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Value("${domainMobile}")
  private String domainMobile;

  @Override
  public List<Person> getpsnIds(Integer size, Long lastPsnId) {

    return personDao.getPeronsForEmail(size, lastPsnId);
  }

  @Override
  public void sendNoticeMail(Person person) {
    try {
      // 定义构造邮件模版参数集
      Map<String, Object> mailData = new HashMap<String, Object>();
      // 统计站内信条数;统计其他消息条数;统计请求全文条数
      Long count = msgRelationDao.countUnreadMsg(person.getPersonId(), mailData);
      // 统计成果认领条数
      Long pubConfirmCount = pubAssignLogDao.getconfirmCount(person.getPersonId());
      // 统计全文认领条数
      Long pubFulltextCount = getFulltextCount(person.getPersonId());
      // 统计好友请求个数
      Long psnFrdReqCount = friendTempDao.getReqFriendCount(person.getPersonId());
      // 统计群组请求个数
      Long psnGroupReqCount = grpProposerDao.getpsnGroupReqCount(person.getPersonId());
      // 统计群组邀请请求个数
      Long psnGroupInviteCount = grpProposerDao.getpsnInviteReqCount(person.getPersonId());
      Long totalCount =
          count + pubConfirmCount + pubFulltextCount + psnFrdReqCount + psnGroupReqCount + psnGroupInviteCount;
      if (totalCount == 0) {
        return;
      } else {
        mailData.put("pubConfirmCount", pubConfirmCount);
        mailData.put("pubFulltextCount", pubFulltextCount);
        mailData.put("psnFrdReqCount", psnFrdReqCount);
        mailData.put("psnGroupReqCount", psnGroupReqCount);
        mailData.put("psnGroupInviteCount", psnGroupInviteCount);
        // 发送邮件
        this.sendNoticeMailToPsn(person, mailData);
      }
    } catch (Exception e) {
      logger.error("给人员发送待处理消息通知邮件出错", e);
    }

  }

  @Override
  public Long getFulltextCount(Long currentUserId) throws Exception {
    // 数据库获取的全文认领记录数还需要判断其来源成果是否删除
    List<PubFulltextPsnRcmd> result = pubFulltextPsnRcmdDao.queryRcmdFulltextCount(currentUserId);
    Long count = 0L;
    for (PubFulltextPsnRcmd pubFulltextPsnRcmd : result) {
      if (pubFulltextPsnRcmd.getSrcPubId() != null) {
        // 先判断来源成果是否已删除，删除不推荐
        PubSnsPO srcPubSns = pubSnsDAO.getPubsnsById(pubFulltextPsnRcmd.getSrcPubId());
        PubPdwhPO pubPdwh = pubPdwhDAO.get(pubFulltextPsnRcmd.getSrcPubId());
        if (srcPubSns != null || pubPdwh != null) {
          count++;
        }
      }
    }
    /* return pubFulltextPsnRcmdDao.queryRcmdFulltextCount(currentUserId); */
    return count;
  }

  private void sendNoticeMailToPsn(Person person, Map<String, Object> mailData) {
    try {
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
      // 定义接口接收的参数
      Map<String, String> paramData = new HashMap<String, String>();
      String msg = "待处理消息通知";
      mailHandleOriginalDataService.buildNecessaryParam(person.getEmail(), 0L, person.getPersonId(), 10106, msg,
          paramData);
      // 确认成果链接
      Long openId = oauthLoginService.getOpenId("00000000", person.getPersonId(), 2);
      String AID = oauthLoginService.getAutoLoginAID(openId, "ResetPWD");
      // 成果认领url
      String cfmPubUrl = EmailCommonService.PC_OR_MB_TOKEN + "pc=" + domainscm
          + "/psnweb/homepage/show?module=pub&jumpto=puball&menuId=1200&" + "AID=" + AID + "&resetpwd=true"
          + "&&mobile=" + domainMobile + "/psnweb/mobile/msgbox?model=centerMsg&whoFirst=pubRcmd";
      // 全文认领url
      String cfmfulltextUrl = domainscm + "/psnweb/homepage/show?jumpto=pubfulltextall&module=pub&menuId=1200&" + "AID="
          + AID + "&resetpwd=true";
      // 群组邀请url
      String grpReqUrl =
          domainscm + "/groupweb/mygrp/main?menuId=3&model=myGrp&jumpto=grpReq&" + "AID=" + AID + "&resetpwd=true";
      // 群组请求url
      String grpInviteUrl =
          domainscm + "/groupweb/mygrp/main?menuId=3&model=myGrp&jumpto=grpInvite&" + "AID=" + AID + "&resetpwd=true";
      // 全文请求url
      String fulltextReqUrl =
          domainscm + "/dynweb/showmsg/msgmain?model=reqFullTextMsg&" + "AID=" + AID + "&resetpwd=true";
      // 站内信url
      String insideLetterUrl = domainscm + "/dynweb/showmsg/msgmain?model=chatMsg&" + "AID=" + AID + "&resetpwd=true";
      // 消息中心url
      String centerMsgUrl = domainscm + "/dynweb/showmsg/msgmain?model=centerMsg&" + "AID=" + AID + "&resetpwd=true";
      // 好友请求url
      String frdReqUrl = domainscm + "/psnweb/friend/main?module=rec&" + "AID=" + AID + "&resetpwd=true";

      // 定义要跟踪的链接集，系统会生成短地址并跟踪访问记录
      List<String> linkList = new ArrayList<String>();
      MailLinkInfo l1 = new MailLinkInfo();
      l1.setKey("domainUrl");
      l1.setUrl(domainscm);
      l1.setUrlDesc("科研之友首页");
      linkList.add(JacksonUtils.jsonObjectSerializer(l1));
      MailLinkInfo l2 = new MailLinkInfo();
      l2.setKey("psnUrl");
      l2.setUrl(psnShortUrl);
      l2.setUrlDesc("个人主页链接");
      linkList.add(JacksonUtils.jsonObjectSerializer(l2));
      MailLinkInfo l3 = new MailLinkInfo();
      l3.setKey("cfmPubUrl");
      l3.setUrl(cfmPubUrl);
      l3.setUrlDesc("认领成果链接");
      linkList.add(JacksonUtils.jsonObjectSerializer(l3));
      MailLinkInfo l4 = new MailLinkInfo();
      l4.setKey("cfmfulltextUrl");
      l4.setUrl(EmailCommonService.PC_OR_MB_TOKEN + "pc=" + cfmfulltextUrl + "&&mobile=" + domainMobile
          + "/psnweb/mobile/msgbox?model=centerMsg");
      l4.setUrlDesc("全文认领链接包含移动地址");
      linkList.add(JacksonUtils.jsonObjectSerializer(l4));
      MailLinkInfo l5 = new MailLinkInfo();
      l5.setKey("grpReqUrl");
      l5.setUrl(grpReqUrl);
      l5.setUrlDesc("群组请求链接");
      linkList.add(JacksonUtils.jsonObjectSerializer(l5));
      MailLinkInfo l6 = new MailLinkInfo();
      l6.setKey("grpInviteUrl");
      l6.setUrl(grpInviteUrl);
      l6.setUrlDesc("群组邀请链接");
      linkList.add(JacksonUtils.jsonObjectSerializer(l6));
      MailLinkInfo l7 = new MailLinkInfo();
      l7.setKey("fulltextReqUrl");
      l7.setUrl(fulltextReqUrl);
      l7.setUrlDesc("全文请求链接");
      linkList.add(JacksonUtils.jsonObjectSerializer(l7));
      MailLinkInfo l8 = new MailLinkInfo();
      l8.setKey("insideLetterUrl");
      l8.setUrl(insideLetterUrl);
      l8.setUrlDesc("站内信链接");
      linkList.add(JacksonUtils.jsonObjectSerializer(l8));
      MailLinkInfo l9 = new MailLinkInfo();
      l9.setKey("centerMsgUrl");
      l9.setUrl(centerMsgUrl);
      l9.setUrlDesc("消息中心链接");
      linkList.add(JacksonUtils.jsonObjectSerializer(l9));
      MailLinkInfo l0 = new MailLinkInfo();
      l0.setKey("frdReqUrl");
      l0.setUrl(frdReqUrl);
      l0.setUrlDesc("好友请求链接");
      linkList.add(JacksonUtils.jsonObjectSerializer(l0));
      mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
      mailData.put("psnName", psnName);
      paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
      mailHandleOriginalDataService.doHandle(paramData);
    } catch (Exception e) {
      logger.error("待处理消息通知邮件参数构造出错", e);
    }
  }

  @Override
  public Long getPsnCount() {
    return personDao.getPsnCount();
  }

}
