package com.smate.web.psn.service.psnemail;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.model.MailOriginalDataInfo;
import com.smate.core.base.email.dao.MailInitDataDao;
import com.smate.core.base.email.model.MailInitData;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.psn.dao.profile.PersonEmailDao;

/**
 * 人员首要邮件服务实现类
 *
 * @author wsn
 * @createTime 2017年3月28日 上午10:50:32
 *
 */
@Service("psnFirstEmailService")
@Transactional(rollbackFor = Exception.class)
public class PsnFirstEmailServiceImpl implements PsnFirstEmailService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private MailInitDataDao mailInitDataDao;
  @Autowired
  private PersonEmailDao personEmailDao;
  @Autowired
  private RestTemplate restTemplate;
  @Value(value = "${sendEmail.restful.url}")
  private String sendRestfulUrl;

  @Override
  public void saveMailInitData(Map<String, Object> dataMap) throws Exception {
    MailInitData mid = new MailInitData();
    mid.setCreateDate(new Date());
    mid.setFromNodeId(1);
    mid.setMailData(JacksonUtils.jsonObjectSerializer(dataMap));
    mid.setStatus(1);
    mid.setToAddress(dataMap.get("toemail").toString());
    mailInitDataDao.saveMailData(mid);
  }

  @Override
  public boolean isPsnFirstEmail(Long psnId, String email) throws Exception {
    String psnEmail = personEmailDao.getfirstMail(psnId);
    if (StringUtils.isNotBlank(psnEmail)) {
      return psnEmail.equalsIgnoreCase(email);
    }
    return false;
  }

  @Override
  public void restSendUpdateFirstEmail(Person person, boolean flag) {
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, Object> mailData = new HashMap<String, Object>();
    // 构造必需的参数
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    String psName = null;
    // 获取用户设置接收邮件的语言
    String languageVersion = person.getEmailLanguageVersion();
    if (languageVersion == null) {
      languageVersion = LocaleContextHolder.getLocale().toString();
    }
    Integer tempcode = flag ? 10001 : 10000;
    info.setSenderPsnId(0L);// 0是系统发送
    info.setReceiverPsnId(person.getPersonId());
    info.setReceiver(person.getEmail());
    info.setMsg("更新首要邮件通知邮件");
    info.setMailTemplateCode(tempcode);
    if (Locale.CHINA.toString().equals(languageVersion)) {
      psName = person.getName();
      if (StringUtils.isBlank(psName)) {
        psName = person.getFirstName() + " " + person.getLastName();
      }
    } else {
      psName = person.getFirstName() + " " + person.getLastName();
      if (StringUtils.isBlank(person.getFirstName()) || StringUtils.isBlank(person.getLastName())) {
        psName = person.getName();
      }
    }
    // 跟踪链接 根据key放置到模板中 所有的链接不再需要放到mailData中
    List<String> linkList = new ArrayList<String>();
    MailLinkInfo l1 = new MailLinkInfo();
    l1.setKey("domainUrl");
    l1.setUrl(domainscm);
    l1.setUrlDesc("科研之友首页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
    mailData.put("zh_CN_psnname", psName);
    mailData.put("toemail", person.getEmail());
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializerNoNull(info));
    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
    restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
  }
}
