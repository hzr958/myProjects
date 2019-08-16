package com.smate.web.psn.service.share;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.model.MailOriginalDataInfo;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.utils.constant.MsgConstants;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.psn.dao.fund.ConstFundAgencyDao;
import com.smate.web.psn.dao.fund.ConstFundCategoryDao;
import com.smate.web.psn.dao.share.FundShareBaseDao;
import com.smate.web.psn.dao.share.FundShareRecordDao;
import com.smate.web.psn.model.fund.ConstFundAgency;
import com.smate.web.psn.model.fund.ConstFundCategory;
import com.smate.web.psn.model.share.FundMainForm;
import com.smate.web.psn.model.share.FundShareBase;
import com.smate.web.psn.model.share.FundShareRecord;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * 基金分享服务
 * 
 * @author WSN
 *
 *         2017年8月29日 下午12:00:49
 *
 */
@Service("fundShareService")
@Transactional(rollbackFor = Exception.class)
public class FundShareServiceImpl implements FundShareService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private FundShareRecordDao fundShareRecordDao;
  @Autowired
  private FundShareBaseDao fundShareBaseDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String openResfulUrl;
  @Resource(name = "freemarkerConfiguration")
  private Configuration freemarkerConfiguration;
  @Value("${domainscm}")
  private String domainscm;
  private static final String ENCODING = "utf-8";
  @Autowired
  private ConstFundCategoryDao constFundCategoryDao;
  @Autowired
  private ConstFundAgencyDao constFundAgencyDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Value(value = "${sendEmail.restful.url}")
  private String sendRestfulUrl;

  @Override
  public void shareFunds(FundMainForm form) throws Exception {
    try {
      // 获取分组 分享id
      Long id = fundShareBaseDao.getId();
      form.setShareBaseId(id);
      // 循环保存记录 循环发送消息
      String[] des3ReceiverIds = form.getDes3ReceiverIds().split(",");
      String[] des3FundIds = form.getDes3FundIds().split(",");
      String firstFundName = "";
      // 当前时间
      Date currentDat = new Date();
      Person sender = personDao.get(form.getPsnId());
      StringBuilder msgRelationIds = new StringBuilder();
      for (String receiverStr : des3ReceiverIds) {
        Long receiverId = 0L;// 接收人id为0则是站外人员
        if (isEmail(receiverStr)) {// 如果当前字符为邮箱号码且没有在站内注册，就直接发送邮件
          Long personId = personDao.findPsnIdByEmail(receiverStr);// 再次判断是否为站内人员
          if (personId != null && personId != 0) {
            receiverId = personId;
          }
        } else {
          receiverId = Long.parseLong(Des3Utils.decodeFromDes3(receiverStr));
        }
        if (sender.getPersonId().equals(receiverId)) {// 发送者和接收者是同一个人
          continue;
        }
        // 循环保存记录 与发送消息
        for (String des3FundId : des3FundIds) {
          Long fundId = null;
          if (NumberUtils.isNumber(des3FundId)) {
            fundId = Long.parseLong(des3FundId);
          } else {
            fundId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3FundId));
          }
          if (fundId != null) {
            // 发送消息
            // 保存记录,由于前端传递过来的接收者的样式可能是des3PsnId和邮箱号码，这样的格式，所以根据不同的规则来处理这两种。为des3PsnId时要发送站内信，为邮箱时表示分享给站外人员，直接发送邮件
            // 先发消息
            // 调open接口发送消息
            if (StringUtils.isBlank(firstFundName)) {
              firstFundName = constFundCategoryDao.getFundNameByLocale(fundId, "zh_CN");
            }
            Person receiver = null;
            if (receiverId != 0) {// 如果不是站外人员，要给站内人员发送消息
              Map<String, Object> map1 = this.buildSendMsgParam(form, receiverId, fundId);
              Object obj = restTemplate.postForObject(this.openResfulUrl, map1, Object.class);
              Map<String, Object> resultMap = JacksonUtils.jsonToMap(obj.toString());
              Long msgRelationId = 0L;
              if (resultMap != null && "success".equals(resultMap.get("status"))) {
                List<Map<String, Object>> result = (List<Map<String, Object>>) resultMap.get("result");
                if (result != null && result.size() > 0 && "success".equals(result.get(0).get("status"))) {
                  msgRelationId = NumberUtils.toLong(result.get(0).get("msgRelationId").toString());
                }
              }
              // 在发送保存记录
              FundShareRecord record = new FundShareRecord();
              record.setSharerId(form.getPsnId());
              record.setReveiverId(receiverId);
              record.setFundId(fundId);
              record.setStatus(0);
              record.setCreateDate(currentDat);
              record.setUpdateDate(currentDat);
              record.setMsgRelationId(msgRelationId);
              record.setShareBaseId(form.getShareBaseId());
              fundShareRecordDao.save(record);
              receiver = personDao.get(receiverId);// 站内人员，获取接收者信息
            } else {// 发送邮件个站外人员，初始化默认收件人信息
              receiver = new Person();
              receiver.setPersonId(0L);
              receiver.setEmail(receiverStr);
            }
            // 发送邮件给这个人-----分享基金邮件
            restSendShareFundsEmail(sender, receiver, fundId, firstFundName, des3FundIds[0], form.getTextContent(),
                des3FundIds.length);
          }
        }
        if (receiverId != 0) {// 只有分享给站内人员才发送站内信
          if (StringUtils.isNotBlank(form.getTextContent())) {
            // 发送消息
            Map<String, Object> map1 = this.buildSendMsgParam(form, receiverId);
            Object obj = restTemplate.postForObject(this.openResfulUrl, map1, Object.class);
            // 取得消息id 保存到分享基础表 给来 取消分享用
            Map<String, Object> resultMap = JacksonUtils.jsonToMap(obj.toString());
            if (resultMap != null && "success".equals(resultMap.get("status"))) {
              List<Map<String, Object>> result = (List<Map<String, Object>>) resultMap.get("result");
              if (result != null && result.size() > 0) {
                msgRelationIds.append(result.get(0).get("msgRelationId").toString() + ",");
              }
            }
          }
        }
      }
      // 更新 分享记录基础表
      FundShareBase shareBase = new FundShareBase();
      shareBase.setId(id);
      shareBase.setSharerId(form.getPsnId());
      shareBase.setStatus(0);
      shareBase.setCreateDate(new Date());
      shareBase.setUpdateDate(shareBase.getCreateDate());
      if (StringUtils.isNotBlank(msgRelationIds) && msgRelationIds.length() > 0) {
        shareBase.setShareContentRel(msgRelationIds.substring(0, msgRelationIds.lastIndexOf(",")));
      }
      fundShareBaseDao.save(shareBase);
    } catch (Exception e) {
      logger.error("分享基金给好友出错， sharePsnId = " + form.getPsnId() + ", fundId = " + form.getDes3FundIds(), e);
      throw new Exception(e);
    }
  }

  /**
   * 调用接口发送分享基金邮件
   * 
   * @param sender
   * @param receiver
   * @param fundId
   * @param string
   * @param string2
   * @param textContent
   * @param length
   * @param shareBaseId
   * @param isEmail
   * @throws Exception
   */
  private void restSendShareFundsEmail(Person sender, Person receiver, Long fundId, String file, String des3FundId,
      String content, int total) throws Exception {
    ConstFundCategory constFundCategory = constFundCategoryDao.get(fundId);
    Long agencyId = constFundCategory.getAgencyId();
    ConstFundAgency constFundAgency = new ConstFundAgency();
    if (agencyId != null) {
      constFundAgency = constFundAgencyDao.findFundAgencyInfo(agencyId);
    }
    // 全文请求使用新模板
    if (sender == null || receiver == null || file == null) {
      throw new Exception("发送文件回复，邮件对象为空" + this.getClass().getName());
    }
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, Object> mailData = new HashMap<String, Object>();
    String language = receiver.getEmailLanguageVersion();
    if (StringUtils.isBlank(language)) {
      language = LocaleContextHolder.getLocale().getLanguage();
    }
    String senderName = getPersonName(sender, language);// 构建邮件双方姓名
    String receiverName = getPersonName(receiver, language);
    // 发件人头衔
    mailData.put("psnName", senderName);
    mailData.put("total", total);
    // 0：其他，1：成果，2：文献， 3：工"\"" + file + "\""作文档；4：项目；5：基金
    mailData.put("type", "5");
    mailData.put("email_receive_psnId", receiver.getPersonId());
    mailData.put("email_sender_psnId", sender.getPersonId());
    mailData.put("email_receiveEmail", receiver.getEmail());
    mailData.put("recvName", receiverName);
    // 发件人主页
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(sender.getPersonId());
    if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
      mailData.put("senderPersonUrl", domainscm + "/" + ShortUrlConst.P_TYPE + "/" + psnProfileUrl.getPsnIndexUrl());
    }
    mailData.put("recommendReason", content);
    mailData.put("minEnShareTitle", "\"" + file.trim() + "\"");
    mailData.put("minZhShareTitle", "“" + file.trim() + "”");
    mailData.put("emailTypeKey", 0);
    mailData.put("fundAgencyName", buildFundAgency(constFundAgency, language));
    mailData.put("fundtitle", buildFundName(constFundCategory, language));
    SimpleDateFormat simpDate = new SimpleDateFormat("yyyy-MM-dd");
    mailData.put("fundStartDate",
        constFundCategory.getStartDate() == null ? "" : simpDate.format(constFundCategory.getStartDate()));
    mailData.put("fundEndDate",
        constFundCategory.getEndDate() == null ? "" : simpDate.format(constFundCategory.getEndDate()));
    String fundLogoUrl = "";
    if (constFundAgency != null) {
      fundLogoUrl = constFundAgency.getLogoUrl();
    }
    String defaultFundLogoUrl = domainscm + "/ressns/images/default/default_fund_logo.jpg";
    mailData.put("defaultFundLogoUrl", defaultFundLogoUrl);// 默认的基金图标，当某些图标对应的路径失效无法访问时，将会在<img>的onerror中设置为默认的图标
    if (StringUtils.isNotBlank(fundLogoUrl)) {
      if (fundLogoUrl.indexOf("scholarmate") == -1) {
        fundLogoUrl = domainscm + "/resmod" + fundLogoUrl;
      }
    } else {
      fundLogoUrl = defaultFundLogoUrl;
    }
    mailData.put("fundLogoUrl", fundLogoUrl);
    // 跟踪链接 根据key放置到模板中 所有的链接不再需要放到mailData中
    List<String> linkList = new ArrayList<String>();
    MailLinkInfo l1 = new MailLinkInfo();
    l1.setKey("domainUrl");
    l1.setUrl(domainscm);
    l1.setUrlDesc("科研之友首页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));
    MailLinkInfo l2 = new MailLinkInfo();
    l2.setKey("fundUrl");
    l2.setUrl(domainscm + "/prjweb/funddetails/show?encryptedFundId=" + des3FundId);
    l2.setUrlDesc("基金详情链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l2));
    MailLinkInfo l3 = new MailLinkInfo();
    l3.setKey("viewUrl");
    l3.setUrl(domainscm + "/dynweb/showmsg/msgmain?model=chatMsg");
    l3.setUrlDesc("查看所有分享链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l3));
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
    // 构造必需的参数
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    Integer tempcode = 10075;
    info.setSenderPsnId(sender.getPersonId());
    info.setReceiverPsnId(receiver.getPersonId());
    info.setReceiver(receiver.getEmail());
    info.setMsg("分享基金邮件");
    info.setMailTemplateCode(tempcode);
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializerNoNull(info));
    // 主题参数，添加如下：
    List<String> subjectParamLinkList = new ArrayList<String>();
    String subjectReceiverName = receiverName;
    if (StringUtils.isBlank(subjectReceiverName)) {
      if ("zh".equals(language) || "zh_CN".equals(language)) {
        subjectReceiverName = "你";
      } else {
        subjectReceiverName = "you";
      }
    }
    subjectParamLinkList.add(senderName);
    subjectParamLinkList.add(subjectReceiverName);
    mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));
    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
    restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
  }

  /**
   * 构建发消息需要的参数
   * 
   * @param form
   * @return
   */
  private Map<String, Object> buildSendMsgParam(FundMainForm form, Long receiverId, Long fundId) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> dataMap = new HashMap<String, Object>();
    map.put("openid", "99999999");
    map.put("token", "00000000msg77msg");
    dataMap.put(MsgConstants.MSG_TYPE, MsgConstants.MSG_TYPE_SMATE_INSIDE_LETTER);
    dataMap.put(MsgConstants.MSG_SENDER_ID, form.getPsnId());
    dataMap.put(MsgConstants.MSG_RECEIVER_IDS, receiverId);
    dataMap.put(MsgConstants.MSG_FUND_ID, fundId);
    dataMap.put(MsgConstants.MSG_CONTENT, form.getTextContent());
    dataMap.put(MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE, "fund");
    dataMap.put(MsgConstants.MSG_BELONG_PERSON, "true");
    if (StringUtils.isBlank(form.getResInfoJson())) {
      ConstFundCategory currFund = constFundCategoryDao.get(fundId);
      if (currFund != null) {
        Map<String, String> fundInfoMap = new HashMap<String, String>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String endDate = currFund.getEndDate() != null ? simpleDateFormat.format(currFund.getEndDate()) : "";
        String startDate = currFund.getStartDate() != null ? simpleDateFormat.format(currFund.getStartDate()) : "";
        ConstFundAgency currAgency = constFundAgencyDao.get(currFund.getAgencyId());
        String zhAgencyName = currAgency != null ? currAgency.getNameZh() : "";
        String enAgencyName = currAgency != null ? currAgency.getNameEn() : "";
        fundInfoMap.put("fund_desc_zh", buildFundDesc(zhAgencyName, startDate, endDate, "，", " ~ "));
        fundInfoMap.put("fund_desc_en", buildFundDesc(enAgencyName, startDate, endDate, ",", " ~ "));
        fundInfoMap.put("fund_desc_zh_br", buildFundDesc(zhAgencyName, startDate, endDate, "[br]", " ~ "));
        fundInfoMap.put("fund_desc_en_br", buildFundDesc(enAgencyName, startDate, endDate, "[br]", " ~ "));
        form.setResInfoJson(JacksonUtils.mapToJsonStr(fundInfoMap));
      }
    }
    dataMap.put(MsgConstants.MSG_FUND_INFO, form.getResInfoJson());
    map.put("data", JacksonUtils.mapToJsonStr(dataMap));
    return map;
  }

  /**
   * 只发文本消息
   * 
   * @param form
   * @return
   */
  private Map<String, Object> buildSendMsgParam(FundMainForm form, Long receiverId) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> dataMap = new HashMap<String, Object>();
    map.put("openid", "99999999");
    map.put("token", "00000000msg77msg");
    dataMap.put(MsgConstants.MSG_TYPE, MsgConstants.MSG_TYPE_SMATE_INSIDE_LETTER);
    dataMap.put(MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE, "text");
    dataMap.put(MsgConstants.MSG_SENDER_ID, form.getPsnId());
    dataMap.put(MsgConstants.MSG_RECEIVER_IDS, receiverId);
    dataMap.put(MsgConstants.MSG_CONTENT, form.getTextContent());
    map.put("data", JacksonUtils.mapToJsonStr(dataMap));
    return map;
  }

  /**
   * 构建基金名称
   * 
   * @param constFundCategory
   * @param form
   */
  private String buildFundName(ConstFundCategory constFundCategory, String language) {
    String enName = constFundCategory.getNameEn();
    String zhName = constFundCategory.getNameZh();
    if ("zh".equals(language) || "zh_CN".equals(language)) {
      if (StringUtils.isNotBlank(zhName)) {
        return zhName;
      } else {
        return enName;
      }
    } else {
      if (StringUtils.isNotBlank(enName)) {
        return enName;
      } else {
        return zhName;
      }
    }
  }

  /**
   * 构建资助机构名称
   * 
   * @param constFundCategory
   * @param form
   */
  private String buildFundAgency(ConstFundAgency constFundAgency, String language) {
    String fundAgencyName = "";
    if (constFundAgency != null) {
      String enName = constFundAgency.getNameEn();
      String zhName = constFundAgency.getNameZh();
      if ("zh".equals(language) || "zh_CN".equals(language)) {
        if (StringUtils.isNotBlank(zhName)) {
          fundAgencyName = zhName;
        } else {
          fundAgencyName = enName;
        }
      } else {
        if (StringUtils.isNotBlank(enName)) {
          fundAgencyName = enName;
        } else {
          fundAgencyName = zhName;
        }
      }
    }
    return fundAgencyName;
  }

  /**
   * 
   * @param person
   * @param language zh=中文
   * @return
   */
  private String getPersonName(Person person, String language) {
    if ("zh".equals(language) || "zh_CN".equals(language)) {
      if (StringUtils.isNotBlank(person.getName())) {
        return person.getName();
      } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
        return person.getFirstName() + " " + person.getLastName();
      } else {
        return person.getEname();
      }
    } else {
      if (StringUtils.isNotBlank(person.getEname())) {
        return person.getEname();
      } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
        return person.getFirstName() + " " + person.getLastName();
      } else {
        return person.getName();
      }
    }
  }

  private String buildEmailTitle(Map<String, Object> map) throws Exception {
    String msg = "";
    try {
      msg = FreeMarkerTemplateUtils.processTemplateIntoString(
          freemarkerConfiguration.getTemplate(ObjectUtils.toString(map.get("tmpUrl")), ENCODING), map);
    } catch (IOException e) {
      logger.error("构建Email标题失败，没有找到对应的Email标题模板！", e);
    } catch (TemplateException e) {
      logger.error("构造Email标题失败,FreeMarker处理失败", e);
    }
    return msg;
  }

  /**
   * 判断当前输入字符是否为邮箱地址
   * 
   * @param str
   * @return
   */
  private boolean isEmail(String str) {
    String reg = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    return Pattern.matches(reg, str);
  }


  private String buildFundDesc(String name, String startDate, String endDate, String nameCharacter,
      String timeCharacter) {
    StringBuffer desc = new StringBuffer();
    StringBuffer descTime = new StringBuffer();
    if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
      descTime.append(startDate).append(timeCharacter).append(endDate);
    } else {
      descTime.append(StringUtils.defaultIfEmpty(startDate, "")).append(StringUtils.defaultIfEmpty(endDate, ""));
    }
    if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(descTime)) {
      desc.append(name).append(nameCharacter).append(descTime);
    } else {
      desc.append(name).append(descTime);
    }
    return desc.toString();
  }
}
