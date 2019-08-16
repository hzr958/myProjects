package com.smate.center.open.service.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.model.MailOriginalDataInfo;
import com.smate.center.open.service.profile.PersonManager;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;

@Service("psnConCodeSendEmail")
@Transactional(rollbackFor = Exception.class)
public class PsnConCodeSendEmail extends BaseEmail {

  @Autowired
  private PersonManager personManager;
  @Autowired
  private ScmEmailService scmEmailService;
  @Value("${domainscm}")
  private String domainscm;

  @Override
  public Map<String, Object> invoke(Object... params) throws Exception {
    Person person = (Person) params[0];
    String code = (String) params[1];
    String languageVersion = scmEmailService.getReceiverLanguage(person);
    // 定义接口接收的参数
    Map<String, Object> paramData = new HashMap<String, Object>();
    // 定义构造邮件模版参数集
    Map<String, String> mailData = new HashMap<String, String>();

    mailData.put("code", code);
    mailData.put("psnName", personManager.getPsnName(person, languageVersion));
    // 构造必需的参数
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    Integer templateCode = 10042;
    info.setReceiver(person.getEmail());// 接收邮箱
    info.setSenderPsnId(0L);// 发送人psnId，0=系统邮件
    info.setReceiverPsnId(person.getPersonId());// 接收人psnId，0=非科研之友用户
    info.setMailTemplateCode(templateCode);// 模版标识，参考V_MAIL_TEMPLATE
    info.setMsg("账号关联验证码邮件");// 描述
    // 定义要跟踪的链接集，系统会生成短地址并跟踪访问记录
    List<String> linkList = new ArrayList<String>();
    MailLinkInfo l1 = new MailLinkInfo();
    l1.setKey("domainUrl");
    l1.setUrl(domainscm);
    l1.setUrlDesc("科研之友主页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializer(info));
    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
    return paramData;
  }
}
