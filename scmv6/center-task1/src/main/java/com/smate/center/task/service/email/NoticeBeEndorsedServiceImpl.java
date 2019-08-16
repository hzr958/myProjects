package com.smate.center.task.service.email;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.service.MailHandleOriginalDataService;
import com.smate.center.task.dao.sns.psn.KeywordIdentificationDao;
import com.smate.center.task.dao.sns.psn.PsnDisciplineKeyDao;
import com.smate.center.task.dao.sns.quartz.KeyWordAgreeStatusDao;
import com.smate.center.task.model.sns.quartz.KeyWordAgreeStatus;
import com.smate.center.task.single.constants.TemplateConstants;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.profile.KeywordIdentification;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.security.PsnPrivateDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;

@Service("noticeBeEndorsedService")
@Transactional(rollbackFor = Exception.class)
public class NoticeBeEndorsedServiceImpl implements NoticeBeEndorsedService {
  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PersonDao personDao;
  @Autowired
  private PsnPrivateDao psnPrivateDao;
  @Autowired
  private MailInitDataService mailInitDataService;
  @Autowired
  private PsnDisciplineKeyDao psnDisciplineKeyDao;
  @Autowired
  private KeywordIdentificationDao keywordIdentificationDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private KeyWordAgreeStatusDao keyWordAgreeStatusDao;
  @Value("${domainscm}")
  private String domainscm;

  @Autowired
  private MailHandleOriginalDataService mailHandleOriginalDataService;

  /**
   * 获取前一天日期
   * 
   * @return
   * @throws ParseException
   */
  public Date getBeforeDate() throws ParseException {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.DAY_OF_YEAR, -1);// 一天后的时间
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 格式化日期
    Date beforeDay = dateFormat.parse(dateFormat.format(calendar.getTime()));
    return beforeDay;
  }

  @Override
  public List<KeywordIdentification> getCurrentEndorsedInfo(Integer startSize) throws Exception {
    List<KeywordIdentification> IdList = null;
    try {
      IdList = keywordIdentificationDao.getCurrentEndorsedInfo(startSize, getBeforeDate());
    } catch (Exception e) {
      logger.error("获取赞研究领域记录失败");
      throw new Exception("获取赞研究领域记录失败", e);
    }

    return IdList;
  }

  @Override
  public List<Long> getBeEndorsedPsnId(Integer startSize) throws Exception {
    List<Long> psnIdList = null;
    try {
      psnIdList = keywordIdentificationDao.getEndorsedPsnId(startSize, getBeforeDate());
    } catch (Exception e) {
      logger.error("获取赞研究领域的人员记录失败");
      throw new Exception("获取赞研究领域的人员记录失败", e);
    }

    return psnIdList;
  }

  @Override
  public void savetoStatus(KeywordIdentification sa) {
    KeyWordAgreeStatus keyWordAgreeStatus = null;
    try {
      keyWordAgreeStatus = keyWordAgreeStatusDao.get(sa.getId());
      if (keyWordAgreeStatus == null) {
        keyWordAgreeStatus = new KeyWordAgreeStatus();
        keyWordAgreeStatus.setId(sa.getId());
        keyWordAgreeStatus.setPsnId(sa.getPsnId());
        keyWordAgreeStatus.setOperatePsnId(sa.getFriendId());
        keyWordAgreeStatus.setKwId(sa.getKeywordId().intValue());
        keyWordAgreeStatus.setOperateDate(sa.getOpDate());
        keyWordAgreeStatus.setStatus(0);
        keyWordAgreeStatusDao.save(keyWordAgreeStatus);
      }
    } catch (Exception e) {
      logger.error("同步认同领域数据到状态表出错！Id:" + sa.getId(), e);
    }

  }

  @Override
  public void sendEmail(Long psnId) throws Exception {
    try {
      Person person = personDao.getPeronsForEmail(psnId);
      if (person == null || StringUtils.isBlank(person.getEmail()) || psnPrivateDao.get(psnId) != null) {
        // 如果人员信息为空则不对外开放，则不发送邮件返回
        logger.error("通知被认同并认同好友邮件，{}人员为空、或邮箱为空、或不公开", psnId);
        return;
      }
      // 获取操作人Id
      List<Long> friendIds = keyWordAgreeStatusDao.getFriendId(person.getPersonId(), getBeforeDate(), 3);

      // SCM-7603,更改新的邮件模板，单独发送邮件
      for (Long friendId : friendIds) {
        // 生成邮件id
        newSendNoticeBeEndorsedInvite(person, getBeforeDate(), friendId);
        keyWordAgreeStatusDao.updateEmailBuildStatus(psnId, 2);
      }
    } catch (Exception e) {
      logger.error("生成初始化邮件信息失败 psnId:" + psnId, e);
      keyWordAgreeStatusDao.updateEmailBuildStatus(psnId, 1);
      throw new Exception("给psnId发送通知邮件", e);
    }

  }

  private void newSendNoticeBeEndorsedInvite(Person person, Date date, Long friendId) throws Exception {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    Long endorsedCount = keywordIdentificationDao.getEndorsedCount(person.getPersonId(), date);
    if (friendId == null || endorsedCount < 1) {
      logger.info(person.getPersonId() + "这个人员id没有获取到赞他关键词的好友");
    }
    Long psnId = person.getPersonId();
    // 得到个人主页链接
    String email2logPsn = "mailEventLogByNoticeBeEndorse|psnId=" + psnId + ",frdPsnId=" + friendId + ",urlId=1";
    String psnUrl = domainscm + "/P/" + psnProfileUrlDao.find(psnId).getPsnIndexUrl() + "?email2log="
        + ServiceUtil.encodeToDes3(email2logPsn);
    // 获取好友主页地址
    String email2log = "mailEventLogByNoticeBeEndorse|psnId=" + psnId + ",frdPsnId=" + friendId + ",urlId=1";
    String frdUrl = domainscm + "/P/" + psnProfileUrlDao.find(friendId).getPsnIndexUrl() + "?email2log="
        + ServiceUtil.encodeToDes3(email2log);
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, String> mailData = new HashMap<String, String>();
    // 构造必需的参数
    String email = person.getEmail();
    Integer templateCode = 10032;
    String msg = "认同研究领域";
    mailHandleOriginalDataService.buildNecessaryParam(email, currentUserId, psnId, templateCode, msg, paramData);

    // 定义要跟踪的链接集，系统会生成短地址并跟踪访问记录
    List<String> linkList = new ArrayList<String>();

    MailLinkInfo l1 = new MailLinkInfo();
    l1.setKey("domainUrl");
    l1.setUrl(domainscm);
    l1.setUrlDesc("科研之友首页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));

    MailLinkInfo l2 = new MailLinkInfo();
    l2.setKey("psnUrl");
    l2.setUrl(psnUrl);
    l2.setUrlDesc("个人主页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l2));

    MailLinkInfo l3 = new MailLinkInfo();
    l3.setKey("frdUrl");
    l3.setUrl(frdUrl);
    l3.setUrlDesc("好友主页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l3));

    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));

    String locale = null;
    if (StringUtils.isBlank(person.getEmailLanguageVersion())
        || TemplateConstants.ZH_LOCALE.equals(person.getEmailLanguageVersion())) {

      locale = TemplateConstants.ZH_LOCALE;
    } else {
      locale = TemplateConstants.EN_LOCALE;
    }

    mailData.put("locale", locale);
    this.getNewFrdRaMap(mailData, psnId, locale, friendId);

    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));

    Map<String, String> resutlMap = mailHandleOriginalDataService.doHandle(paramData);
    if ("success".equals(resutlMap.get("result"))) {
      // 构造邮件成功
    } else {
      // 构造邮件失败
      logger.error(resutlMap.get("msg"));
    }

  }

  public Map<String, String> getNewFrdRaMap(Map<String, String> mailData, Long psnId, String locale, Long frdPsnId)
      throws Exception {
    try {
      Map<String, String> objListMap = new HashMap<String, String>();
      List<Map<String, Object>> frdRaList = new ArrayList<Map<String, Object>>();
      Person frdPsn = personDao.getPeronsForEmail(frdPsnId);
      if (frdPsn == null) {// 好友不存在，则不执行下面部分
        return null;
      }
      Map<String, Object> frdMap = new HashMap<String, Object>();
      List<Integer> kwIds = keyWordAgreeStatusDao.getPsnKwId(psnId, frdPsnId, getBeforeDate());

      List<String> friendKeyWord = new ArrayList<String>();

      for (Integer kwId : kwIds) {
        String kwName = psnDisciplineKeyDao.getKwNameById(kwId.longValue());
        if (StringUtils.isNotEmpty(kwName)) {
          friendKeyWord.add(kwName);
        }

      }
      frdMap.put("raList", friendKeyWord);// 当前好友认同的研究领域
      frdMap.put("avatars", personDao.getPsnImgByObjectId(frdPsnId));// 好友的头像
      // 好友姓名
      if (TemplateConstants.ZH_LOCALE.equals(locale)) {
        frdMap.put("frdName", StringUtils.isBlank(frdPsn.getName()) ? frdPsn.getFirstName() + " " + frdPsn.getLastName()
            : frdPsn.getName());
      } else {
        frdMap.put("frdName",
            StringUtils.isBlank(frdPsn.getFirstName()) && StringUtils.isBlank(frdPsn.getLastName()) ? frdPsn.getName()
                : frdPsn.getFirstName() + " " + frdPsn.getLastName());
      }
      frdRaList.add(frdMap);

      if (CollectionUtils.isEmpty(frdRaList)) {
        mailData = null;
      } else {
        mailData.put("avatars", frdRaList.get(0).get("avatars").toString());
        mailData.put("frdPsnName", frdRaList.get(0).get("frdName").toString());
        objListMap.put("frdRaList", JacksonUtils.listToJsonStr(frdRaList));
        mailData.put("objListMap", JacksonUtils.mapToJsonStr(objListMap));
      }

    } catch (Exception e) {
      logger.error("getFrdRaMap 构建邮件出错", e);
      throw new Exception(e);

    }
    return mailData;

  }

}
