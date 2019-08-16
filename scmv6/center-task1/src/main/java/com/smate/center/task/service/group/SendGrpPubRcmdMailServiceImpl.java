package com.smate.center.task.service.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.service.MailHandleOriginalDataService;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubIndexUrlDao;
import com.smate.center.task.dao.sns.grp.GrpPubRcmdDao;
import com.smate.center.task.dao.sns.quartz.GrpBaseInfoDao;
import com.smate.center.task.model.grp.GrpBaseinfo;
import com.smate.center.task.model.pdwh.quartz.PdwhPubIndexUrl;
import com.smate.center.task.model.sns.pub.PubInfo;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubFullTextDAO;
import com.smate.center.task.v8pub.dao.sns.PubPdwhDAO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubFullTextPO;
import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;

@Service("sendGrpPubRcmdMailService")
@Transactional(rollbackOn = Exception.class)
public class SendGrpPubRcmdMailServiceImpl implements SendGrpPubRcmdMailService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private MailHandleOriginalDataService mailHandleOriginalDataService;
  @Autowired
  private GrpBaseInfoDao GrpBaseInfoDao;
  @Autowired
  private GrpPubRcmdDao grpPubRcmdDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private PdwhPubIndexUrlDao pdwhPubIndexUrlDao;
  @Autowired
  private PdwhPubFullTextDAO pdwhPubFullTextDAO;

  @Override
  public List<GrpBaseinfo> getNeedSendMailJzData(Long grpId, Integer size) {
    return GrpBaseInfoDao.getNeedSendMailJzData(grpId, size);
  }

  @Override
  public List<GrpBaseinfo> getNeedSendMailJtData(Long grpId, Integer size) {
    return GrpBaseInfoDao.getNeedSendMailJtData(grpId, size);
  }

  @Override
  public void sendMailToGroupOwner(GrpBaseinfo jtGrpBaseInfo) {
    List<Long> pubIds = grpPubRcmdDao.getPubIdByGrpId(jtGrpBaseInfo.getGrpId());
    if (pubIds == null) {
      return;
    }
    try {
      Person person = personDao.getPeronsForEmail(jtGrpBaseInfo.getOwnerPsnId());
      // 人员信息为空，或邮件为空时，不能发送邮件，并将状态置为-3
      if (person == null || person.getEmail() == null) {
        return;
      }
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
      // 定义构造邮件模版参数集
      Map<String, String> mailData = new HashMap<String, String>();
      // 构造必需的参数
      Long currentUserId = SecurityUtils.getCurrentUserId();
      String email = person.getEmail();
      Integer templateCode = 10112;
      String msg = "群组成果推荐";
      mailHandleOriginalDataService.buildNecessaryParam(email, currentUserId, jtGrpBaseInfo.getOwnerPsnId(),
          templateCode, msg, paramData);
      String grpUrl =
          domainscm + "/groupweb/grpinfo/main?des3GrpId=" + Des3Utils.encodeToDes3(jtGrpBaseInfo.getGrpId().toString());
      // 定义要跟踪的链接集，系统会生成短地址并跟踪访问记录
      List<String> linkList = new ArrayList<String>();
      MailLinkInfo l1 = new MailLinkInfo();
      l1.setKey("domainUrl");
      l1.setUrl(domainscm);
      l1.setUrlDesc("科研之友首页");
      linkList.add(JacksonUtils.jsonObjectSerializer(l1));

      MailLinkInfo l3 = new MailLinkInfo();
      l3.setKey("grpUrl");
      l3.setUrl(grpUrl);
      l3.setUrlDesc("群组链接");
      linkList.add(JacksonUtils.jsonObjectSerializer(l3));

      MailLinkInfo l4 = new MailLinkInfo();
      l4.setKey("psnUrl");
      l4.setUrl(psnShortUrl);
      l4.setUrlDesc("个人主页链接");
      linkList.add(JacksonUtils.jsonObjectSerializer(l4));
      mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
      List<String> subjectParamLinkList = new ArrayList<String>();
      subjectParamLinkList.add(psnName);
      subjectParamLinkList.add(String.valueOf(pubIds.size()));
      mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));
      mailData.put("psnName", psnName);
      mailData.put("fullImg", psnName);
      mailData.put("grpName", jtGrpBaseInfo.getGrpName());
      mailData.put("pubSum", String.valueOf(pubIds.size()));
      this.buildPubInfoParam(mailData, pubIds);
      paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
      mailHandleOriginalDataService.doHandle(paramData);
    } catch (Exception e) {
      logger.error("成果认领邮件参数构造出错", e);
    }
  }

  private void buildPubInfoParam(Map<String, String> mailData, List<Long> pubIds) {
    Map<String, String> objListMap = new HashMap<String, String>();
    List<PubInfo> pubdetails = new ArrayList<PubInfo>();
    List<PubPdwhPO> pdwhPubs = pubPdwhDAO.getPubDetails(pubIds);
    for (PubPdwhPO pdwhPub : pdwhPubs) {
      PubInfo pubInfo = new PubInfo();
      pubInfo.setTitle(pdwhPub.getTitle());
      pubInfo.setAuthorNames(pdwhPub.getAuthorNames());
      pubInfo.setBriefDesc(pdwhPub.getBriefDesc());
      String authorNames = pdwhPub.getAuthorNames();
      if (StringUtils.isNotBlank(authorNames)) {
        String endChar = authorNames.substring(authorNames.length() - 1, authorNames.length());
        if (endChar.equalsIgnoreCase(";") || endChar.equalsIgnoreCase("；")) {
          authorNames = authorNames.substring(0, authorNames.length() - 1);
        }
      }
      pubInfo.setAuthorNames(authorNames);
      PdwhPubIndexUrl pdwhPubIndexUrl = pdwhPubIndexUrlDao.get(pdwhPub.getPubId());
      if (pdwhPubIndexUrl != null && StringUtils.isNotBlank(pdwhPubIndexUrl.getPubIndexUrl())) {
        pubInfo.setPdwhPubIndexUrl(domainscm + "/" + ShortUrlConst.S_TYPE + "/" + pdwhPubIndexUrl.getPubIndexUrl());
      } else {
        String url =
            this.domainscm + "/pub/details/pdwh?des3PubId=" + Des3Utils.encodeToDes3(pdwhPub.getPubId().toString());
        pubInfo.setPdwhPubIndexUrl(url);
      }
      String fullImg = this.domainscm + "/resmod/images_v5/images2016/file_img.jpg";
      String pubFulltextImage = null;
      PdwhPubFullTextPO fullText = pdwhPubFullTextDAO.getPdwhPubFulltext(pdwhPub.getPubId());
      if (fullText != null) {
        pubFulltextImage = fullText.getThumbnailPath();
      }
      if (StringUtils.isBlank(pubFulltextImage)) {
        if (fullText != null && fullText.getFileId() != null) {
          fullImg = this.domainscm + "/resmod/images_v5/images2016/file_img1.jpg";
        }
      } else {
        fullImg = this.domainscm + pubFulltextImage;
      }
      pubInfo.setFulltextImg(fullImg);
      pubdetails.add(pubInfo);
    }
    objListMap.put("pubDetails", JacksonUtils.listToJsonStr(pubdetails));
    mailData.put("objListMap", JacksonUtils.mapToJsonStr(objListMap));
  }

}
