package com.smate.center.task.single.service.person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.service.MailHandleOriginalDataService;
import com.smate.center.task.dao.sns.psn.InvitePsnValidateDao;
import com.smate.center.task.model.sns.psn.InvitePsnValidate;
import com.smate.core.base.utils.json.JacksonUtils;

@Service("invitePsnValidateMailService")
@Transactional(rollbackFor = Exception.class)
public class InvitePsnValidateMailServiceImpl implements InvitePsnValidateMailService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainvalidate}")
  private String domainvalidate;
  @Autowired
  private InvitePsnValidateDao invitePsnValidateDao;
  @Autowired
  private MailHandleOriginalDataService mailHandleOriginalDataService;


  @Override
  public List<InvitePsnValidate> getInvitePsnValidate() {
    return invitePsnValidateDao.getInvitePsnValidate();
  }

  @Override
  public Integer sendInviteEmailToPsn(InvitePsnValidate person) {
    try {
      // 定义接口接收的参数
      Map<String, String> paramData = new HashMap<String, String>();
      // 定义构造邮件模版参数集
      Map<String, String> mailData = new HashMap<String, String>();
      // 收件人主页
      String psnShortUrl = domainscm + "/P/" + person.getPsnIndexUrl();
      String validateUrl = null;
      // 定义要跟踪的链接集，系统会生成短地址并跟踪访问记录
      List<String> linkList = new ArrayList<String>();
      MailLinkInfo l1 = new MailLinkInfo();
      l1.setKey("domainUrl");
      l1.setUrl(domainscm);
      l1.setUrlDesc("科研之友首页");
      linkList.add(JacksonUtils.jsonObjectSerializer(l1));
      if ("2".equals(person.getFlag())) {// 发给个人用户
        validateUrl = domainscm + "/application/validate/maint";
        mailHandleOriginalDataService.buildNecessaryParam(person.getEmail(), 0L, person.getPsnId(), 10118,
            "邀请个人订购科研验证服务", paramData);
        MailLinkInfo l3 = new MailLinkInfo();
        l3.setKey("validateUrl");
        l3.setUrl(validateUrl);
        l3.setUrlDesc("订购科研验证服务链接");
        linkList.add(JacksonUtils.jsonObjectSerializer(l3));

        MailLinkInfo l4 = new MailLinkInfo();
        l4.setKey("psnUrl");
        l4.setUrl(psnShortUrl);
        l4.setUrlDesc("个人主页链接");
        linkList.add(JacksonUtils.jsonObjectSerializer(l4));
      } else if ("1".equals(person.getFlag())) {// 发给单位管理员
        validateUrl = domainvalidate + "/validate/index";
        mailHandleOriginalDataService.buildNecessaryParam(person.getEmail(), 0L, 0L, 10119, "邀请单位订购科研验证服务", paramData);
        MailLinkInfo l3 = new MailLinkInfo();
        l3.setKey("validateUrl");
        l3.setUrl(validateUrl);
        l3.setUrlDesc("单位订购科研验证服务链接");
        linkList.add(JacksonUtils.jsonObjectSerializer(l3));
      }
      mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
      mailData.put("psnName", person.getName());
      paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
      Map<String, String> resutlMap = mailHandleOriginalDataService.doHandle(paramData);
      if ("success".equals(resutlMap.get("result"))) {
        return 1;
      } else {
        return 9;
      }
    } catch (Exception e) {
      logger.error("邀请订购科研验证服务邮件参数构造出错", e);
      return 9;
    }
  }

  @Override
  public void updateSendStatus(Long psnId, Integer result) {
    invitePsnValidateDao.updateSendStatus(psnId, result);
  }



}
