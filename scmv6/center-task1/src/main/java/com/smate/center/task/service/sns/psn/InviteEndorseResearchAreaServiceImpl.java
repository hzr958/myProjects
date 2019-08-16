package com.smate.center.task.service.sns.psn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.service.MailHandleOriginalDataService;
import com.smate.center.task.dao.sns.psn.PsnUpdateDiscLogDao;
import com.smate.center.task.dao.sns.quartz.FriendDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.psninfo.PsnInfoUtils;
import com.smate.core.base.utils.security.Des3Utils;

@Service("inviteEndorseResearchAreaService")
@Transactional(rollbackFor = Exception.class)
public class InviteEndorseResearchAreaServiceImpl implements InviteEndorseResearchAreaService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private MailHandleOriginalDataService mailHandleOriginalDataService;
  @Autowired
  private PsnUpdateDiscLogDao psnUpdateDiscLogDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private FriendDao friendDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;

  @Override
  public List<Long> getUpdateRAPsnId(int size) {
    return psnUpdateDiscLogDao.getAllUndealPsnId(size);
  }

  @Override
  public void sendEmail(Long psnId) {
    Person person = personDao.getPeronsForEmail(psnId);
    if (person == null) {
      logger.error("邀请认同研究领域邮件,人员信息为空", psnId);
      psnUpdateDiscLogDao.updatePsnUpdateDiscLog(psnId, 2);
      return;
    }
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(person.getPersonId());
    String viewPsnUrl = "";
    if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
      viewPsnUrl = this.domainscm + "/P/" + psnProfileUrl.getPsnIndexUrl();
    } else {
      viewPsnUrl =
          this.domainscm + "/psnweb/homepage/show?des3PsnId=" + Des3Utils.encodeToDes3(person.getPersonId().toString());
    }
    try {
      // 更新的关键词
      List<String> raList = psnUpdateDiscLogDao.getPsnDiscLogs(psnId);
      if (CollectionUtils.isEmpty(raList)) {
        logger.error("邀请认同研究领域邮件,人员没有更新的研究领域", psnId);
        return;
      }
      List<Long> frdList = friendDao.findFriend(psnId);
      for (Long frdPsnId : frdList) {
        if (frdPsnId.equals(psnId)) {
          continue;
        }
        Person receverPsn = personDao.getPeronsForEmail(frdPsnId);
        if (receverPsn == null) {
          continue;
        }
        String receverLanguage = receverPsn.getEmailLanguageVersion();
        if (StringUtils.isBlank(receverLanguage)) {
          receverLanguage = LocaleContextHolder.getLocale().toString();
        }
        String psnName = PsnInfoUtils.getPersonName(person, receverLanguage);
        String receverPsnName = PsnInfoUtils.getPersonName(receverPsn, receverLanguage);
        PsnProfileUrl receverpsnProfileUrl = psnProfileUrlDao.get(frdPsnId);
        String receverPsnUrl = "";
        if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
          receverPsnUrl = this.domainscm + "/P/" + receverpsnProfileUrl.getPsnIndexUrl();
        } else {
          receverPsnUrl = this.domainscm + "/psnweb/homepage/show?des3PsnId="
              + Des3Utils.encodeToDes3(person.getPersonId().toString());
        }
        // 定义接口接收的参数
        Map<String, String> paramData = new HashMap<String, String>();
        // 定义构造邮件模版参数集
        Map<String, Object> mailData = new HashMap<String, Object>();
        // 构造必需的参数
        String msg = "邀请认同研究领域";
        mailHandleOriginalDataService.buildNecessaryParam(receverPsn.getEmail(), psnId, frdPsnId, 10025, msg,
            paramData);
        // 定义要跟踪的链接集，系统会生成短地址并跟踪访问记录
        List<String> linkList = new ArrayList<String>();
        MailLinkInfo l1 = new MailLinkInfo();
        l1.setKey("domainUrl");
        l1.setUrl(domainscm);
        l1.setUrlDesc("科研之友首页");
        linkList.add(JacksonUtils.jsonObjectSerializer(l1));
        MailLinkInfo l2 = new MailLinkInfo();
        l2.setKey("psnUrl");
        l2.setUrl(viewPsnUrl);
        l2.setUrlDesc("个人主页链接");
        linkList.add(JacksonUtils.jsonObjectSerializer(l2));
        MailLinkInfo l3 = new MailLinkInfo();
        l3.setKey("receverPsnUrl");
        l3.setUrl(receverPsnUrl);
        l3.setUrlDesc("收件人主页链接");
        linkList.add(JacksonUtils.jsonObjectSerializer(l3));
        mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
        List<String> subjectParamLinkList = new ArrayList<String>();
        subjectParamLinkList.add(psnName);
        mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));
        mailData.put("psnName", psnName);
        mailData.put("count", raList.size());
        mailData.put("receverPsnName", receverPsnName);
        Map<String, String> objListMap = new HashMap<String, String>();
        objListMap.put("raList", JacksonUtils.listToJsonStr(raList));
        mailData.put("objListMap", JacksonUtils.mapToJsonStr(objListMap));
        paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
        mailHandleOriginalDataService.doHandle(paramData);
      }
      psnUpdateDiscLogDao.updatePsnUpdateDiscLog(psnId, 1);
    } catch (Exception e) {
      logger.error("给psnId" + psnId + "的好友发邀请认同研究领域邮件", e);
      throw new ServiceException("给psnId" + psnId + "的好友发邀请认同研究领域邮件", e);
    }

  }

}
