package com.smate.center.task.service.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.model.MailOriginalDataInfo;
import com.smate.center.mail.connector.service.MailHandleOriginalDataService;
import com.smate.center.task.dao.group.GroupDynamicMsgDao;
import com.smate.center.task.dao.group.GrpKwDiscDao;
import com.smate.center.task.dao.sns.grp.GrpMemberDao;
import com.smate.center.task.dao.sns.quartz.CategoryScmDao;
import com.smate.center.task.dao.sns.quartz.GrpBaseInfoDao;
import com.smate.center.task.dao.sns.quartz.GrpIndexUrlDao;
import com.smate.center.task.model.grp.GrpBaseinfo;
import com.smate.center.task.model.grp.GrpKwDisc;
import com.smate.center.task.model.sns.pub.CategoryScm;
import com.smate.center.task.model.sns.quartz.GrpIndexUrl;
import com.smate.center.task.single.service.person.PersonService;
import com.smate.core.base.email.service.EmailCommonService;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.psninfo.PsnInfoUtils;
import com.smate.core.base.utils.security.Des3Utils;

@Service("groupEmailService")
@Transactional(rollbackFor = Exception.class)
public class GroupEmailServiceImpl implements GroupEmailService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainMobile}")
  private String domainMobile;
  @Autowired
  private PersonService personService;
  @Autowired
  private MailHandleOriginalDataService mailHandleOriginalDataService;
  @Autowired
  private GroupDynamicMsgDao groupDynamicMsgDao;
  @Autowired
  private GrpMemberDao grpMemberDao;
  @Autowired
  private GrpBaseInfoDao grpBaseInfoDao;
  @Autowired
  private GrpKwDiscDao grpKwDiscDao;
  @Autowired
  private CategoryScmDao categoryScmDao;
  @Autowired
  private GrpIndexUrlDao grpIndexUrlDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;

  @Override
  public List<Long> getInstGrpPsnId() {
    return grpMemberDao.getInstGrpPsnId();
  }

  @Override
  public List<Long> getPsnInstGrpIds(Long psnId) {
    return grpMemberDao.getPsnInstGrpIds(psnId);
  }

  @Override
  public List<Long> getNeedSendMailGrpId(List<Long> grpIds) {
    return groupDynamicMsgDao.getNeedSendMailGrpId(grpIds);
  }

  @Override
  public void sendGrpDnyUpdateEmail(List<Long> sendMailGrpId, Long psnId) {
    Map<String, Object> mailData = new HashMap<String, Object>();
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    try {
      // 构造必需的参数
      MailOriginalDataInfo info = new MailOriginalDataInfo();
      Person receiver = personService.getPeronsForEmail(psnId);
      String language = "";
      language = receiver.getEmailLanguageVersion();
      if (StringUtils.isBlank(language)) {
        language = LocaleContextHolder.getLocale().toString();
      }
      Integer tempCode = 10079;
      info.setSenderPsnId(0L);
      info.setReceiverPsnId(psnId);
      info.setMsg("兴趣群组动态更新提醒");
      info.setReceiver(receiver.getEmail());
      info.setMailTemplateCode(tempCode);
      paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializerNoNull(info));
      // 收件人主页
      String psnShortUrl = "";
      PsnProfileUrl profileUrl = psnProfileUrlDao.get(psnId);
      if (profileUrl != null && StringUtils.isNotBlank(profileUrl.getPsnIndexUrl())) {
        psnShortUrl = domainscm + "/P/" + profileUrl.getPsnIndexUrl();
      }
      String receiverName = PsnInfoUtils.getPersonName(receiver, language);
      mailData.put("psnName", receiverName);
      mailData.put("psnUrl", psnShortUrl);
      String grpListUrl = domainscm + "/groupweb/mygrp/main?model=myGrp";
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
      l2.setUrlDesc("个人主页");
      linkList.add(JacksonUtils.jsonObjectSerializer(l2));
      MailLinkInfo l3 = new MailLinkInfo();
      l3.setKey("grpListUrl");
      l3.setUrl(grpListUrl);
      l3.setUrlDesc("我的群组列表");
      linkList.add(JacksonUtils.jsonObjectSerializer(l3));
      this.buildGrpInfo(mailData, sendMailGrpId, linkList);
      mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
      paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
      Map<String, String> resutlMap = mailHandleOriginalDataService.doHandle(paramData);
      if (!"success".equals(resutlMap.get("result"))) {
        logger.error("兴趣群组动态更新提醒 邮件发送出错 psnId:" + psnId);
      }
    } catch (Exception e) {
      logger.error("兴趣群组动态更新提醒 邮件发送出错 psnId:" + psnId);
    }

  }

  private void buildGrpInfo(Map<String, Object> mailData, List<Long> sendMailGrpId, List<String> linkList) {
    Map<String, String> objListMap = new HashMap<String, String>();
    List<Map<String, String>> grpdetails = new ArrayList<Map<String, String>>();
    for (int j = 0; j < sendMailGrpId.size(); j++) {
      Map<String, String> grpMap = new HashMap<String, String>();
      GrpBaseinfo grpBaseInfo = grpBaseInfoDao.getGrpNameAndAuatars(sendMailGrpId.get(j));
      GrpKwDisc grpKwDisc = grpKwDiscDao.getGrpCategory(sendMailGrpId.get(j));
      if (grpKwDisc != null) {
        Optional.ofNullable(grpKwDisc.getFirstCategoryId()).ifPresent(fCategoryId -> {
          CategoryScm categoryscm = categoryScmDao.get(Long.valueOf(fCategoryId));
          grpMap.put("firstCatName", categoryscm.getCategoryZh());
        });
        Optional.ofNullable(grpKwDisc.getSecondCategoryId()).ifPresent(sCategoryId -> {
          CategoryScm categoryscm = categoryScmDao.get(Long.valueOf(sCategoryId));
          grpMap.put("SecondCatName", categoryscm.getCategoryZh());
        });
      }
      grpMap.put("grpName", grpBaseInfo.getGrpName());
      grpMap.put("grpAuatars", grpBaseInfo.getGrpAuatars());
      GrpIndexUrl grpIndexUrl = grpIndexUrlDao.get(sendMailGrpId.get(j));
      String grpUrl = "";
      if (grpIndexUrl != null) {
        grpUrl = domainscm + "/G/" + grpIndexUrl.getGrpIndexUrl();
      } else {
        grpUrl =
            domainscm + "/groupweb/grpinfo/main?des3GrpId=" + Des3Utils.encodeToDes3(sendMailGrpId.get(j).toString());
      }
      String grpUrlMobile =
          domainMobile + "/grp/main?des3GrpId=" + Des3Utils.encodeToDes3(sendMailGrpId.get(j).toString());

      String viewGroupUrl = EmailCommonService.PC_OR_MB_TOKEN + "pc=" + grpUrl + "&&mobile=" + grpUrlMobile;
      MailLinkInfo l4 = new MailLinkInfo();
      l4.setKey("groupUrl_" + j);
      l4.setUrl(viewGroupUrl);
      l4.setUrlDesc("群组详情");
      linkList.add(JacksonUtils.jsonObjectSerializer(l4));
      grpdetails.add(grpMap);
    }
    objListMap.put("groupList", JacksonUtils.listToJsonStr(grpdetails));
    mailData.put("objListMap", JacksonUtils.mapToJsonStr(objListMap));
  }

}
