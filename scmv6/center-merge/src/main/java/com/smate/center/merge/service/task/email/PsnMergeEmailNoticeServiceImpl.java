package com.smate.center.merge.service.task.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.model.MailOriginalDataInfo;
import com.smate.center.merge.dao.person.PersonDao;
import com.smate.center.merge.dao.person.SysMergeUserHistoryDao;
import com.smate.center.merge.model.sns.person.Person;
import com.smate.center.merge.model.sns.person.SysMergeUserHis;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 合并 发送邮件实现类
 * 
 * @author yhx
 *
 */
@Service("psnMergeEmailNoticeService")
@Transactional(rollbackFor = Exception.class)
public class PsnMergeEmailNoticeServiceImpl implements PsnMergeEmailNoticeService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PersonDao personDao;
  @Autowired
  private SysMergeUserHistoryDao sysMergeUserHistoryDao;
  @Value("${domainscm}")
  private String domainscm;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value(value = "${sendEmail.restful.url}")
  private String sendRestfulUrl;

  /**
   * 合并失败 发送邮件给管理员
   */
  @Override
  public void sendEmailToAdmin(Long psnId) {
    Person person = personDao.findPsnInfoForEmail(psnId);
    try {
      if (person != null) {
        // 只有生产机 才发管理员通知邮件
        String env = System.getenv("RUN_ENV");
        List<String> emailList = new ArrayList<String>();
        if ("run".equals(env)) {
          emailList.add("yanmingzhuang@irissz.com");
          emailList.add("zhiqiangfan@irissz.com");
        }
        emailList.add("shaozhitan@irissz.com");
        emailList.add("huixiaye@irissz.com");
        for (String toEmail : emailList) {
          Long toPsnId = personDao.findPsnIdByEmail(toEmail);
          // 定义接口接收的参数
          Map<String, String> paramData = new HashMap<String, String>();
          // 定义构造邮件模版参数集
          Map<String, String> mailData = new HashMap<String, String>();
          // 构造必需的参数
          MailOriginalDataInfo info = new MailOriginalDataInfo();
          Integer templateCode = 10045;
          info.setReceiver(toEmail);// 接收邮箱
          info.setSenderPsnId(0L);// 发送人psnId，0=系统邮件
          info.setReceiverPsnId(toPsnId);// 接收人psnId，0=非科研之友用户
          info.setMailTemplateCode(templateCode);// 模版标识，参考V_MAIL_TEMPLATE
          info.setMsg("人员合并失败邮件");// 描述
          paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializer(info));
          // 定义要跟踪的链接集，系统会生成短地址并跟踪访问记录
          List<String> linkList = new ArrayList<String>();
          MailLinkInfo l1 = new MailLinkInfo();
          l1.setKey("domainUrl");
          l1.setUrl(domainscm);
          l1.setUrlDesc("科研之友主页");
          linkList.add(JacksonUtils.jsonObjectSerializer(l1));
          mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
          mailData.put("psnId", psnId.toString());
          mailData.put("env", env);
          paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
          restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
        }
        sysMergeUserHistoryDao.updateMailStatus(psnId, 1);// 是否已发邮箱通知0-未发送；1-已发送；2-发送失败.
      }
    } catch (Exception e) {
      logger.error("合并失败-发送邮件给管理员失败， psnId=" + psnId, e);
      sysMergeUserHistoryDao.updateMailStatus(psnId, 2);
    }

  }

  @SuppressWarnings("unchecked")
  private void updateMailStatus(Map<String, String> paramData, Long psnId) {
    try {
      Map<String, String> resultMap =
          (Map<String, String>) restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
      if ("success".equals(resultMap.get("result"))) {
        sysMergeUserHistoryDao.updateMailStatus(psnId, 1);// 是否已发邮箱通知0-未发送；1-已发送；2-发送失败.
      } else {
        sysMergeUserHistoryDao.updateMailStatus(psnId, 2);
      }
    } catch (Exception e) {
      sysMergeUserHistoryDao.updateMailStatus(psnId, 2);
      logger.error("发送人员合并通知邮件失败， psnId=" + psnId, e);
    }
  }

  /**
   * 合并成功 发送邮件给被合并人
   */
  @Override
  public void sendEmailToPsn(Long psnId) throws Exception {
    List<String> emailList = getEmailList(psnId);
    try {
      Person person = personDao.findPsnInfoForEmail(psnId);
      if (person == null || StringUtils.isBlank(person.getEmail())) {
        return;
      }
      // 定义接口接收的参数
      Map<String, String> paramData = new HashMap<String, String>();
      // 定义构造邮件模版参数集
      Map<String, String> mailData = new HashMap<String, String>();
      String psnName = null;
      if (person.getEmailLanguageVersion() == null || "zh_CN".equalsIgnoreCase(person.getEmailLanguageVersion())) {
        psnName = StringUtils.isNotBlank(person.getName()) ? person.getName()
            : person.getLastName() + " " + person.getFirstName();
      } else {
        psnName = StringUtils.isNotBlank(person.getFirstName()) || StringUtils.isNotBlank(person.getLastName())
            ? person.getFirstName() + " " + person.getLastName()
            : person.getName();
      }
      // 构造必需的参数
      MailOriginalDataInfo info = new MailOriginalDataInfo();
      Integer templateCode = 10044;
      info.setReceiver(person.getEmail());// 接收邮箱
      info.setSenderPsnId(0L);// 发送人psnId，0=系统邮件
      info.setReceiverPsnId(psnId);// 接收人psnId，0=非科研之友用户
      info.setMailTemplateCode(templateCode);// 模版标识，参考V_MAIL_TEMPLATE
      info.setMsg("人员合并成功邮件");// 描述
      paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializer(info));
      // 定义要跟踪的链接集，系统会生成短地址并跟踪访问记录
      List<String> linkList = new ArrayList<String>();
      MailLinkInfo l1 = new MailLinkInfo();
      l1.setKey("domainUrl");
      l1.setUrl(domainscm);
      l1.setUrlDesc("科研之友主页");
      linkList.add(JacksonUtils.jsonObjectSerializer(l1));
      mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
      mailData.put("psnId", psnId.toString());
      mailData.put("email", person.getEmail());
      mailData.put("psnName", psnName);
      mailData.put("emailList", JacksonUtils.listToJsonStr(emailList));
      paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
      updateMailStatus(paramData, psnId);
    } catch (Exception e) {
      logger.error("发送人员合并成功通知邮件失败， psnId=" + psnId, e);
    }
  }

  private List<String> getEmailList(Long psnId) {
    // 获取对应数据
    List<SysMergeUserHis> hisList = sysMergeUserHistoryDao.getHisNotSendByPsnId(psnId);
    List<String> emailList = new ArrayList<String>();
    if (CollectionUtils.isNotEmpty(hisList)) {
      for (SysMergeUserHis his : hisList) {
        if (his.getMergeStatus() == 3) {// 合并状态0-已初始化；1-正在合并中；2-合并失败；3-已合并成功.
          emailList.add(his.getEmail());
        }
      }
    }
    return emailList;
  }


}
