package com.smate.center.mail.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.smate.center.mail.connector.dao.MailContentDao;
import com.smate.center.mail.connector.dao.MailOriginalDataDao;
import com.smate.center.mail.connector.dao.MailTemplateDao;
import com.smate.center.mail.connector.mailenum.MailHandleEnum;
import com.smate.center.mail.connector.mailenum.MailSendStatusEnum;
import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.model.MailOriginalData;
import com.smate.center.mail.connector.model.MailTemplate;
import com.smate.center.mail.connector.mongodb.model.MailContent;
import com.smate.center.mail.dao.MailRecordDao;
import com.smate.center.mail.dao.PsnMailSetDao;
import com.smate.center.mail.exception.FirstEmailSameException;
import com.smate.center.mail.exception.InvalidMailboxException;
import com.smate.center.mail.exception.NotReceiveException;
import com.smate.center.mail.exception.NotTemplateException;
import com.smate.center.mail.exception.TemplateTimeLimitException;
import com.smate.center.mail.model.MailDataInfo;
import com.smate.center.mail.model.MailRecord;
import com.smate.center.mail.model.PsnMailSet;
import com.smate.core.base.mail.dao.MailLinkDao;
import com.smate.core.base.mail.model.mongodb.MailLink;
import com.smate.core.base.utils.common.EditValidateUtils;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.template.SmateFreeMarkerTemplateUtil;

/**
 * 构造邮件信息服务
 * 
 * @author zzx
 *
 */
@Service("mailBuildInfoService")
@Transactional(rollbackFor = Exception.class)
public class MailBuildInfoServiceImpl implements MailBuildInfoService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  // 邮件调度优先级 规则 ....
  private static final Double LEVEL_A = 0.6;
  private static final Double LEVEL_B = 0.25;
  private static final Double LEVEL_C = 0.1;
  private static final Double LEVEL_D = 0.05;
  @Autowired
  private MailOriginalDataDao mailOriginalDataDao;
  @Autowired
  private MailContentDao mailContentDao;
  @Autowired
  private MailLinkDao mailLinkDao;
  @Autowired
  private MailTemplateDao mailTemplateDao;
  @Autowired
  private SmateFreeMarkerTemplateUtil smateFreeMarkerTemplateUtil;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PsnMailSetDao psnMailSetDao;
  @Autowired
  private MailRecordDao mailRecordDao;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainmail}")
  private String domainmail;

  @Override
  public MailOriginalData getMailOriginalData(Long mailId) {
    MailOriginalData mailOriginalData = mailOriginalDataDao.get(mailId);
    return mailOriginalData;
  }

  @Override
  public List<MailOriginalData> findMailOriginalDataList(int size) throws Exception {
    int LEVEL_A_SIZE = (int) (size * LEVEL_A);
    int LEVEL_B_SIZE = (int) (size * LEVEL_B);
    int LEVEL_C_SIZE = (int) (size * LEVEL_C);
    int LEVEL_D_SIZE = (int) (size * LEVEL_D);
    List<MailOriginalData> resutltList = new ArrayList<MailOriginalData>();
    List<MailOriginalData> resutltListA = mailOriginalDataDao.findListByLevel(LEVEL_A_SIZE, "A");
    resutltList.addAll(resutltListA);
    if (resutltListA.size() <= LEVEL_A_SIZE) {
      LEVEL_B_SIZE = LEVEL_B_SIZE + (LEVEL_A_SIZE - resutltListA.size());
    }
    List<MailOriginalData> resutltListB = mailOriginalDataDao.findListByLevel(LEVEL_B_SIZE, "B");
    resutltList.addAll(resutltListB);
    if (resutltListB.size() <= LEVEL_B_SIZE) {
      LEVEL_C_SIZE = LEVEL_C_SIZE + (LEVEL_B_SIZE - resutltListB.size());
    }
    List<MailOriginalData> resutltListC = mailOriginalDataDao.findListByLevel(LEVEL_C_SIZE, "C");
    resutltList.addAll(resutltListC);
    if (resutltListC.size() <= LEVEL_C_SIZE) {
      LEVEL_D_SIZE = LEVEL_D_SIZE + (LEVEL_C_SIZE - resutltListC.size());
    }
    List<MailOriginalData> resutltListD = mailOriginalDataDao.findListByLevel(LEVEL_D_SIZE, "D");
    resutltList.addAll(resutltListD);
    return resutltList;
  }

  @Override
  public MailDataInfo buildExcuteParam(MailOriginalData one) throws Exception {
    MailDataInfo info = new MailDataInfo();
    info.setMailOriginalData(one);
    MailContent mailContent = mailContentDao.findByMailId(one.getMailId());
    info.setMailContent(mailContent);
    if (mailContent == null) {
      throw new Exception("构建邮件数据查找不到MailContent数据");
    }
    info.setMailData(mailContent.getMailData());
    return info;
  }

  @Override
  public void saveMailRecord(MailDataInfo info) throws Exception {
    MailOriginalData data = info.getMailOriginalData();
    MailRecord mr = new MailRecord();
    Date date = new Date();
    mr.setMailId(data.getMailId());
    mr.setMailTemplateCode(data.getMailTemplateCode());
    mr.setPriorLevel(data.getPriorLevel());
    mr.setStatus(MailSendStatusEnum.STATUS_0.toInt());// 0=待分配
    mr.setReceiver(data.getReceiver());
    mr.setCreateDate(date);
    mr.setUpdateDate(date);
    // 默认科研之友
    mr.setSenderName("科研之友");
    info.setMailRecord(mr);
  }

  @Override
  public void saveTemplateInfo(MailDataInfo info) throws Exception {
    MailOriginalData data = info.getMailOriginalData();
    Map<String, Object> map = info.getMailDataMap();
    MailTemplate template = mailTemplateDao.get(data.getMailTemplateCode());
    String templateName = template.getTemplateName();
    Long psnId = data.getReceiverPsnId();
    Person sendPerson = personDao.get(data.getSenderPsnId());
    String subject = "";
    Integer templateLanguage = template.getTemplateLanguage();
    Object[] subjectParamArr = {};
    if (map.get(MailHandleEnum.SUBJECT_PARAM_LIST.toString()) != null) {// 参数列表，主题有参数的需要拼接
      String subjectParamStr = map.get(MailHandleEnum.SUBJECT_PARAM_LIST.toString()).toString();
      subjectParamArr = JacksonUtils.jsonToList(subjectParamStr).toArray();
    }
    // 模板语言：0.中英文模板都有，1.只有中文模板
    if (templateLanguage == 0) {
      if (psnId != 0) {// 人员存在，则读取接收中英文邮件设置
        if ("en_US".equals(info.getEmailLanguageVersion())) {
          templateName += "_en_US.ftl";
          subject = MessageFormat.format(template.getSubject_en(), subjectParamArr);
        } else {
          templateName += "_zh_CN.ftl";
          subject = MessageFormat.format(template.getSubject_zh(), subjectParamArr);
        }
      } else {// 人员不存在 则根据发件人的语言设置
        if (sendPerson != null) {
          if ("en_US".equals(sendPerson.getEmailLanguageVersion())) {
            templateName += "_en_US.ftl";
            subject = MessageFormat.format(template.getSubject_en(), subjectParamArr);
          } else {
            templateName += "_zh_CN.ftl";
            subject = MessageFormat.format(template.getSubject_zh(), subjectParamArr);
          }
        } else {
          templateName += "_zh_CN.ftl";
          subject = MessageFormat.format(template.getSubject_zh(), subjectParamArr);
        }
      }
    } else {
      templateName += "_zh_CN.ftl";
      subject = MessageFormat.format(template.getSubject_zh(), subjectParamArr);
    }
    info.getMailRecord().setSubject(subject);
    // list模版参数处理
    if (map.get(MailHandleEnum.OBJ_LIST_MAP.toString()) != null) {
      String objListMapStr = map.get(MailHandleEnum.OBJ_LIST_MAP.toString()).toString();
      Map<String, String> objListMap = JacksonUtils.jsonToMap(objListMapStr);
      if (objListMap != null && objListMap.size() > 0) {
        for (Map.Entry<String, String> entry : objListMap.entrySet()) {
          map.put(entry.getKey(), JacksonUtils.jsonObject(entry.getValue()));
        }
      }
    }
    info.setEmailContent(smateFreeMarkerTemplateUtil.produceTemplate(map, templateName));
    info.getMailContent().setContent(info.getEmailContent());
  }

  @Override
  public void saveLinkInfo(MailDataInfo info) throws Exception {
    // 把默认的链接添加追踪（邮件详情、退订邮件）
    buildDefaultShowrLinks(info);
    // 把邮件业务的链接添加追踪（自定义）
    Map<String, Object> mailDataMap = info.getMailDataMap();
    Object linkListObj = mailDataMap.get("linkList");
    // list模版参数处理
    if (mailDataMap.get(MailHandleEnum.OBJ_LIST_MAP.toString()) != null) {
      String objListMapStr = mailDataMap.get(MailHandleEnum.OBJ_LIST_MAP.toString()).toString();
      Map<String, String> objListMap = JacksonUtils.jsonToMap(objListMapStr);
      if (objListMap != null && objListMap.size() > 0) {
        for (Map.Entry<String, String> entry : objListMap.entrySet()) {
          if ("links".equals(entry.getKey())) {
            List<Map<String, Object>> linkList = JacksonUtils.jsonToList(entry.getValue());
            // 放置对象内短地址
            setShortLinks(linkList, info, objListMap);
          }
        }
      }
    }
    if (linkListObj != null) {
      List<String> linkList = JacksonUtils.jsonToList(linkListObj.toString());
      buildShortLinks(linkList, info);
    }
  }

  private void setShortLinks(List<Map<String, Object>> linkList, MailDataInfo info, Map<String, String> objListMap)
      throws Exception {
    Map<String, Object> mailDataMap = info.getMailDataMap();
    List<Map<String, Object>> links = new ArrayList<Map<String, Object>>();
    if (linkList != null && linkList.size() > 0) {
      String host = domainmail + "/EL/";
      for (Map<String, Object> linkDetail : linkList) {
        MailLink ml = new MailLink();
        Date date = new Date();
        ml.setUrl(linkDetail.get("url") + "");
        ml.setCount(0);
        ml.setCreateDate(date);
        ml.setUpdateDate(date);
        ml.setLimitCount(0);
        ml.setMailId(info.getMailOriginalData().getMailId());
        ml.setUrlDesc(linkDetail.get("urlDesc") + "");
        ml.setLinkKey(linkDetail.get("key") + "");
        ml.setTemplateCode(info.getMailOriginalData().getMailTemplateCode());
        mailLinkDao.save(ml);
        String shortLink = ml.getLinkId();
        HashMap<String, Object> url = new HashMap<>();
        url.put("url", host + shortLink);
        links.add(url);
      }
      objListMap.put("links", JacksonUtils.listToJsonStr(links));
      mailDataMap.put("objListMap", JacksonUtils.mapToJsonStr(objListMap));
    }
  }

  private void buildDefaultShowrLinks(MailDataInfo info) {
    String host = domainmail + "/EL/";
    Date date = new Date();
    Map<String, Object> mailDataMap = info.getMailDataMap();
    Object unsubscribeUrl = mailDataMap.get(MailHandleEnum.UNSUBSCRIBE_URL.toString());
    if (unsubscribeUrl != null) {
      MailLink ml2 = new MailLink();
      ml2.setUrl(unsubscribeUrl.toString());
      ml2.setCount(0);
      ml2.setCreateDate(date);
      ml2.setUpdateDate(date);
      ml2.setMailId(info.getMailOriginalData().getMailId());
      ml2.setUrlDesc("邮件退订链接");
      ml2.setLimitCount(0);
      ml2.setTimeOutDate(null);
      ml2.setLinkKey(MailHandleEnum.UNSUBSCRIBE_URL.toString());
      ml2.setTemplateCode(info.getMailOriginalData().getMailTemplateCode());
      mailLinkDao.save(ml2);
      String shortLink = ml2.getLinkId();
      mailDataMap.put(MailHandleEnum.UNSUBSCRIBE_URL.toString(), host + shortLink);
    }
    Object viewMailUrl = mailDataMap.get(MailHandleEnum.VIEW_MAIL_URL.toString());
    if (viewMailUrl != null) {
      MailLink ml = new MailLink();
      ml.setUrl(domainscm + viewMailUrl.toString());
      ml.setCount(0);
      ml.setCreateDate(date);
      ml.setUpdateDate(date);
      ml.setMailId(info.getMailOriginalData().getMailId());
      ml.setUrlDesc("邮件详情链接");
      ml.setLimitCount(0);
      ml.setTimeOutDate(null);
      ml.setLinkKey(MailHandleEnum.VIEW_MAIL_URL.toString());
      ml.setTemplateCode(info.getMailOriginalData().getMailTemplateCode());
      mailLinkDao.save(ml);
      String shortLink = ml.getLinkId();
      mailDataMap.put(MailHandleEnum.VIEW_MAIL_URL.toString(), "/EL/" + shortLink);
    }

  }

  /**
   * 构造链接相关信息
   * 
   * @param linkList 链接集合json数据（包含链接、限制条件、描述等，参考MailLinkInfo对象）
   * @param info 信息类 返回MailLink对象集合、短地址集合shortLinkList
   */
  private void buildShortLinks(List<String> linkList, MailDataInfo info) {
    Map<String, Object> mailDataMap = info.getMailDataMap();
    if (linkList != null && linkList.size() > 0) {
      String host = domainmail + "/EL/";
      for (String link : linkList) {
        MailLinkInfo mlInfo = JacksonUtils.jsonObject(link, MailLinkInfo.class);
        if (checkLinkInfo(mlInfo)) {
          MailLink ml = new MailLink();
          Date date = new Date();
          ml.setUrl(mlInfo.getUrl());
          ml.setCount(0);
          ml.setCreateDate(date);
          ml.setUpdateDate(date);
          ml.setMailId(info.getMailOriginalData().getMailId());
          ml.setUrlDesc(mlInfo.getUrlDesc());
          ml.setLimitCount(mlInfo.getLimitCount());
          ml.setTimeOutDate(mlInfo.getTimeOutDate());
          ml.setLinkKey(mlInfo.getKey());
          ml.setTemplateCode(info.getMailOriginalData().getMailTemplateCode());
          mailLinkDao.save(ml);
          String shortLink = ml.getLinkId();
          mailDataMap.put(mlInfo.getKey(), host + shortLink);
        }
      }
    }

  }

  /**
   * 检查链接参数
   * 
   * @param mlInfo
   * @return
   */
  private boolean checkLinkInfo(MailLinkInfo mlInfo) {
    if (mlInfo == null) {
      return false;
    }
    if (StringUtils.isBlank(mlInfo.getUrl())) {
      return false;
    }
    if (mlInfo.getLimitCount() == null) {
      return false;
    }
    return true;
  }

  @Override
  public void mainSaveData(MailDataInfo info) throws Exception {
    mailRecordDao.save(info.getMailRecord());
    info.setMailData(JacksonUtils.mapToJsonStr(info.getMailDataMap()));
    info.getMailContent().setMailData(info.getMailData());
    mailContentDao.save(info.getMailContent());
    MailOriginalData originalData = info.getMailOriginalData();
    originalData.setStatus(1);
    originalData.setSendStatus(0);
    mailOriginalDataDao.save(originalData);
  }

  @Override
  public void addDefaultParam(MailDataInfo info) throws Exception {
    String unsubscribeUrl = "";// 取消订阅链接
    Map<String, Object> map = JacksonUtils.jsonToMap(info.getMailData());
    String des3MailId = Des3Utils.encodeToDes3(info.getMailOriginalData().getMailId().toString());
    if (info.getMailOriginalData().getReceiverPsnId() != 0 && StringUtils.isNotBlank(info.getDes3MailTypeId())) {
      unsubscribeUrl = domainscm + "/psnweb/unsubscribe/mail?psnid=" + info.getDes3ReceiverPsnId() + "&typeid="
          + info.getDes3MailTypeId() + "&locale=" + info.getEmailLanguageVersion();// 取消订阅链接
    } else {
      unsubscribeUrl = domainscm + "/psnweb/psnsetting/main?model=email";
    }
    String viewMailPath = domainmail;// 查看邮件不能正常显示的链接
    String viewMailUrl = "/scmmanagement/mail/details?des3MailId=" + des3MailId;// 查看邮件不能正常显示的链接
    Object unsubscribeUrlObj = map.get(MailHandleEnum.UNSUBSCRIBE_URL.toString());
    if (unsubscribeUrlObj == null) {
      map.put(MailHandleEnum.UNSUBSCRIBE_URL.toString(), unsubscribeUrl);
    }
    Object viewMailPathObj = map.get(MailHandleEnum.VIEW_MAIL_PATH.toString());
    if (viewMailPathObj == null) {
      map.put(MailHandleEnum.VIEW_MAIL_PATH.toString(), viewMailPath);
    }
    Object viewMailUrlObj = map.get(MailHandleEnum.VIEW_MAIL_URL.toString());
    if (viewMailUrlObj == null) {
      map.put(MailHandleEnum.VIEW_MAIL_URL.toString(), viewMailUrl);
    }
    info.setMailDataMap(map);
  }

  @Override
  public void checkUsable(MailDataInfo info) throws Exception {
    MailOriginalData data = info.getMailOriginalData();
    Long receiverPsnId = info.getMailOriginalData().getReceiverPsnId();
    MailTemplate template = mailTemplateDao.get(data.getMailTemplateCode());
    if (template == null || StringUtils.isBlank(template.getTemplateName())) {
      throw new NotTemplateException("发送邮件的模版不存在！TemplateCode=" + data.getMailTemplateCode());
    }
    info.setMailTemplate(template);// 获取邮件模版
    if (receiverPsnId != 0L && template.getMailTypeId() != null) {
      info.setEmailLanguageVersion(personDao.getEmailLanguByPsnId(receiverPsnId));// 获取接收人的接收邮件语言
      info.setDes3MailTypeId(Des3Utils.encodeToDes3(template.getMailTypeId().toString()));// 获取加密邮件类型
      info.setDes3ReceiverPsnId(Des3Utils.encodeToDes3(receiverPsnId.toString()));// 获取加密接收人psnId
      PsnMailSet psnMailSet = psnMailSetDao.getByPsnIdAndMailTypeId(receiverPsnId, template.getMailTypeId());
      // 判断是否接收此类型邮件
      if (psnMailSet != null && psnMailSet.getIsReceive() == 0) {
        throw new NotReceiveException(
            "用户不接收此类邮件！psnId=" + receiverPsnId + ";TemplateCode=" + data.getMailTemplateCode());
      }
    }
  }

  @Override
  public void buildFailedForNotReceive(MailOriginalData one) {
    one.setStatus(3);
    mailOriginalDataDao.save(one);

  }

  @Override
  public void buildFailedForError(MailOriginalData one) throws Exception {
    one.setStatus(2);
    mailOriginalDataDao.save(one);
  }

  @Override
  public void checkTempLimit(MailDataInfo info) throws Exception {
    MailOriginalData data = info.getMailOriginalData();
    MailTemplate template = info.getMailTemplate();
    // 状态 0=无限制 1=每天对一个邮箱发一封 2=每周对一个邮箱发一封 3=每月对一个邮箱发一封
    Integer limitStatus = template.getLimitStatus();
    if (limitStatus != null && limitStatus != 0) {
      List<MailOriginalData> list =
          mailOriginalDataDao.findByTempNameAndReceiver(data.getReceiver(), data.getMailTemplateCode());
      if (list != null && list.size() == 1) {
        Date date = list.get(0).getCreateDate();
        timeLimit(date, new Date(), limitStatus);
      }
    }
  }

  public void timeLimit(Date startTime, Date endTime, Integer limitStatus) throws Exception {
    long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
    long diff = endTime.getTime() - startTime.getTime();
    long day = diff / nd;// 计算差多少天
    if (limitStatus == 1 && day < 1) {
      throw new TemplateTimeLimitException("模版限制-每天对一个邮箱只能发一封");
    }
    if (limitStatus == 2 && day < 7) {
      throw new TemplateTimeLimitException("模版限制-每周对一个邮箱只能发一封 ");
    }
    if (limitStatus == 3 && day < 31) {
      throw new TemplateTimeLimitException("模版限制-每月对一个邮箱只能发一封 ");
    }
  }

  @Override
  public void buildFailedForTemplateTimeLimit(MailOriginalData one) throws Exception {
    one.setStatus(4);
    mailOriginalDataDao.save(one);
  }

  @Override
  public void checkFristEmail(MailDataInfo info) throws Exception {
    List<Person> personList = personDao.getPersonByEmail(info.getMailOriginalData().getReceiver());
    for (Person person : personList) {
      if (person.getPersonId().equals(info.getMailOriginalData().getSenderPsnId())) {
        throw new FirstEmailSameException("收件人与发送人首要邮件一致");
      }
    }
  }

  @Override
  public void buildFirstEmailSame(MailOriginalData one) throws Exception {
    one.setStatus(5);
    mailOriginalDataDao.save(one);
  }

  @Override
  public void validateEmail(MailDataInfo info) {
    String email = info.getMailOriginalData().getReceiver();
    if (EditValidateUtils.hasParam(email, 50, EditValidateUtils.MAIL_COAD)) {
      throw new InvalidMailboxException("接收邮箱格式不正确");
    }
  }

}
