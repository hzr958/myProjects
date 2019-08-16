package com.smate.center.task.service.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.model.MailOriginalDataInfo;
import com.smate.center.task.dao.sns.psn.ETemplateInfluenceCountDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.psn.ETemplateInfluenceCount;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.sys.ScmSystemUtil;



/**
 * 科研影响力邮件
 * 
 * @author hht
 * @Time 2018年10月24日 下午2:34:38
 */
@Service("influenceStatisticalService")
@Transactional(rollbackFor = Exception.class)
public class InfluenceStatisticalServiceImpl implements InfluenceStatisticalService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ETemplateInfluenceCountDao eTemplateInfluenceCountDao;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private ScmSystemUtil scmSystemUtil;



  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;

  @Value(value = "${sendEmail.restful.url}")
  private String sendRestfulUrl;



  /**
   * 整理邮件数据并发送
   */
  @Override
  public int sendMail(ETemplateInfluenceCount influence) throws ServiceException {
    try {
      // 获取人员信息
      Person person = personProfileDao.get(influence.getPsnId());
      if (person == null || person.getEmail() == null) {
        eTemplateInfluenceCountDao.updateInfluence(influence.getPsnId());
        return -1;
      }
      String locale;
      String subject;
      String psnName = null;
      // String viewDetail =scmSystemUtil.getSysDomain()+ "/scmwebsns/spread/pubConsequence?menuId=" + 801
      // + "&email2log=" + ServiceUtil.encodeToDes3("mailEventLogByInfluenceStatis|psnId=" +
      // influence.getPsnId() + ",urlId=19");
      String viewDetail = scmSystemUtil.getSysDomain() + "/psnweb/homepage/show?module=influence";
      String addPub = scmSystemUtil.getSysDomain() + "/psnweb/homepage/show?module=pub&menuId=1200";

      // 整理数据
      // 定义接口接收的参数
      Map<String, String> paramData = new HashMap<String, String>();
      // 定义构造邮件模版参数集
      Map<String, String> mailData = new HashMap<String, String>();
      // 构造必需的参数
      MailOriginalDataInfo info = new MailOriginalDataInfo();
      Integer templateCode = 10033;
      info.setReceiver(person.getEmail());// 接收邮箱
      info.setSenderPsnId(0L);// 发送人psnId，0=系统邮件
      info.setReceiverPsnId(person.getPersonId());// 接收人psnId，0=非科研之友用户
      info.setMailTemplateCode(templateCode);// 模版标识，参考V_MAIL_TEMPLATE
      info.setMsg("科研影响力分析统计");// 描述
      paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializer(info));

      // 定义要跟踪的链接集，系统会生成短地址并跟踪访问记录
      List<String> linkList = new ArrayList<String>();

      MailLinkInfo l1 = new MailLinkInfo();
      l1.setKey("viewDetail");
      l1.setUrl(viewDetail);
      l1.setUrlDesc("查看详情");
      linkList.add(JacksonUtils.jsonObjectSerializer(l1));
      MailLinkInfo l2 = new MailLinkInfo();
      l2.setKey("addPub");
      l2.setUrl(addPub);
      l2.setUrlDesc("上传我的论文");
      linkList.add(JacksonUtils.jsonObjectSerializer(l2));
      mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));


      mailData.put("psnName", psnName);
      buildParams(influence, mailData);
      // 获得标题和数量
      if (person.getEmailLanguageVersion() == null || "zh_CN".equals(person.getEmailLanguageVersion())) {
        locale = "zh_CN";
      } else {
        locale = "en_US";
      }

      this.compare(influence.getMonthDownloadCount(), influence.getMonthReadCount(), influence.getMonthAwardCount(),
          influence.getMonthShareCount(), mailData, locale);
      // 得到标题中的数字和与数字对应的文字

      // 如果比较后的最大数为0那么不发送邮件
      if ("0".equals(mailData.get("titleCount").toString())) {
        return 2;
      }
      if (person.getEmailLanguageVersion() == null || "zh_CN".equals(person.getEmailLanguageVersion())) {
        locale = "zh_CN";
        psnName = person.getName() == null ? person.getFirstName() + " " + person.getLastName() : person.getName();
        subject = psnName + "，最近有" + mailData.get("titleCount") + "" + mailData.get("titlesName");

      } else {
        locale = "en_US";
        psnName = person.getLastName() == null || person.getFirstName() == null ? person.getName()
            : person.getFirstName() + " " + person.getLastName();
        subject = psnName + ",You have" + " " + mailData.get("titleCount") + " " + mailData.get("mailSubject");

      }

      // 社交中赞和分享的和
      mailData.put("socialMonth", (influence.getMonthAwardCount() + influence.getMonthShareCount()) + "");
      mailData.put("socialCount", (influence.getAwardCount() + influence.getShareCount()) + "");
      paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));

      // etemplateEmailService.syncEmailInfo(mailMap);
      restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);

      return 1;
    } catch (Exception e) {
      logger.error("科研影响力推广邮件发送时出错", e.getMessage());
      throw new ServiceException(e);
    }
  }

  private void buildParams(ETemplateInfluenceCount influence, Map<String, String> mailData) {
    // 下载数
    if (influence.getDownloadCount() != null && influence.getDownloadCount() > 0) {
      mailData.put("downloadCount", influence.getDownloadCount().toString());
      if (influence.getMonthDownloadCount() != null && influence.getMonthDownloadCount() != 0) {
        mailData.put("downloadMonth", influence.getMonthDownloadCount().toString());
      }
    }
    // 阅读数
    if (influence.getReadCount() != null && influence.getReadCount() > 0) {
      mailData.put("readCount", influence.getReadCount().toString());
      if (influence.getMonthReadCount() != null && influence.getMonthReadCount() != 0) {
        mailData.put("readMonth", influence.getMonthReadCount().toString());
      }
    }
    // 赞
    if (influence.getAwardCount() != null && influence.getAwardCount() > 0) {
      mailData.put("awardCount", influence.getAwardCount().toString());
      if (influence.getMonthAwardCount() != null && influence.getMonthAwardCount() != 0) {
        mailData.put("awardMonth", influence.getMonthAwardCount().toString());
      }
    }
    // 分享
    if (influence.getShareCount() != null && influence.getShareCount() > 0) {
      mailData.put("shareCount", influence.getShareCount().toString());
      if (influence.getMonthShareCount() != null && influence.getMonthShareCount() != 0) {
        mailData.put("shareMonth", influence.getMonthShareCount().toString());
      }
    }
    // 引用
    if (influence.getCitedTimesCount() != null && influence.getCitedTimesCount() > 0) {
      mailData.put("citedCount", influence.getCitedTimesCount().toString());
      if (influence.getMonthCitedTimesCount() != null && influence.getMonthCitedTimesCount() != 0) {
        mailData.put("citedMonth", influence.getMonthCitedTimesCount().toString());
      }
    }
    // H指数
    if (influence.getHindex() != null && influence.getHindex() > 0) {
      mailData.put("hindex", influence.getHindex().toString());
    }
    // 得到新增成果（论文）数据
    if (influence.getPubCount() != null && influence.getPubCount() > 0) {
      mailData.put("pubCount", influence.getPubCount().toString());
      if (influence.getMonthPubCount() != null && influence.getMonthPubCount() > 0) {
        mailData.put("pubMonth", influence.getMonthPubCount().toString());
      }
    }
  }

  public void compare(Long long1, Long long2, Long long3, Long long4, Map<String, String> mailData, String locale) { // 每周的下载数，阅读数，赞数，分享数，论文引用先不做（这一页先不做），需要论文引用的数据

    Long big0 = Math.max(long1, long2);
    Long big1 = Math.max(long3, long4);
    Long biggest = Math.max(big0, big1);
    mailData.put("titleCount", biggest.toString());
    if ("zh_CN".equals(locale)) {
      if (biggest.intValue() == long2) {
        mailData.put("titlesName", "人阅读了你的论文");
      }
      if (biggest.intValue() == long3) {
        mailData.put("titlesName", "人赞了你的论文");
      }
      if (biggest.intValue() == long4) {
        mailData.put("titlesName", "人分享了你的论文");
      }
      if (biggest.intValue() == long1) {
        mailData.put("titlesName", "人下载了你的论文");// 当赞，分享，下载，阅读数量相同时，显示下载了你的论文
      }
    } else {
      if (biggest.intValue() == long2) {
        mailData.put("titlesName", "Researchers viewed your publication");
        mailData.put("mailSubject", "more publication viewes");// 邮件主题
      }
      if (biggest.intValue() == long3) {
        mailData.put("titlesName", "Researchers liked your publication");
        mailData.put("mailSubject", "more publication likes");// 邮件主题
      }
      if (biggest.intValue() == long4) {
        mailData.put("titlesName", "Researchers shared your publication");
        mailData.put("mailSubject", "more publication shares");// 邮件主题
      }
      if (biggest.intValue() == long1) {
        mailData.put("titlesName", "Researchers downloaded your publication");
        mailData.put("mailSubject", "more publication downloads");// 邮件主题，当赞，分享，下载，阅读数量相同时，显示下载了你的论文
      }
    }
  }



}
