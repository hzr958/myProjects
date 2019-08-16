package com.smate.center.task.service.group;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.smate.center.task.dao.group.GrpPubsDao;
import com.smate.center.task.dao.group.PrjGrpTmpDao;
import com.smate.center.task.dao.sns.quartz.GrpPubIndexUrlDao;
import com.smate.center.task.dao.sns.quartz.PubIndexUrlDao;
import com.smate.center.task.dao.sns.quartz.PublicationDao;
import com.smate.center.task.model.group.PrjGrpTmp;
import com.smate.center.task.model.sns.quartz.GrpPubIndexUrl;
import com.smate.center.task.model.sns.quartz.PubIndexUrl;
import com.smate.center.task.model.sns.quartz.Publication;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.string.ServiceUtil;

@Service("sendPrjGrpEmailService")
@Transactional(rollbackOn = Exception.class)
public class SendPrjGrpEmailServiceImpl implements SendPrjGrpEmailService {
  @Autowired
  private PrjGrpTmpDao prjGrpTmpDao;
  @Autowired
  private GrpPubsDao grpPubsDao;
  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;
  @Autowired
  private GrpPubIndexUrlDao grpPubIndexUrlDao;
  @Autowired
  private PersonDao personDao;
  @Value("${domainscm}")
  private String domainscm;

  @Override
  public List<PrjGrpTmp> getPrjGrpInfo(int size) {
    return prjGrpTmpDao.getPrjGrpInfo(size);
  }

  @Override
  public Map<String, Object> buildEamilInfo(PrjGrpTmp prjGrpTmp, Map<String, Object> params) {

    String psnUrl = null;
    String psnName = null;
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.find(prjGrpTmp.getOwnerPsnId());
    if (psnProfileUrl != null) {
      psnUrl = domainscm + "/P/" + psnProfileUrl.getPsnIndexUrl();
    }
    Person psn = personDao.getPeronsForEmail(prjGrpTmp.getOwnerPsnId());
    String languageVersion = psn.getEmailLanguageVersion();
    if (StringUtils.isBlank(languageVersion)) {
      languageVersion = LocaleContextHolder.getLocale().toString();
    }
    if ("zh_CN".equals(languageVersion)) {
      psnName = StringUtils.isNotBlank(psn.getName()) ? psn.getName() : psn.getFirstName() + " " + psn.getLastName();
    } else {
      psnName = StringUtils.isBlank(psn.getFirstName()) && StringUtils.isBlank(psn.getLastName()) ? psn.getName()
          : psn.getFirstName() + " " + psn.getLastName();
    }
    List<Long> pubIds = grpPubsDao.getGroupPubIds(prjGrpTmp.getGrpId(), 2);
    List<Publication> pubList = publicationDao.getPubDetailByPubIds(pubIds);
    List<Publication> pubDetails = new ArrayList<Publication>();
    for (Publication publication : pubList) {
      publication
          .setZhTitle(publication.getZhTitle() == null || publication.getZhTitle().isEmpty() ? publication.getEnTitle()
              : publication.getZhTitle());
      String pubUrl = "";
      GrpPubIndexUrl grpPubIndexUrl = grpPubIndexUrlDao.findByGrpIdAndPubId(prjGrpTmp.getGrpId(), publication.getId());
      if (grpPubIndexUrl != null && StringUtils.isNotBlank(grpPubIndexUrl.getPubIndexUrl())) {
        pubUrl = domainscm + "/" + ShortUrlConst.B_TYPE + "/" + grpPubIndexUrl.getPubIndexUrl();
      } else {
        PubIndexUrl pubIndexUrl = pubIndexUrlDao.get(publication.getId());
        if (pubIndexUrl != null && StringUtils.isNotBlank(pubIndexUrl.getPubIndexUrl())) {
          pubUrl = domainscm + "/" + ShortUrlConst.B_TYPE + "/" + pubIndexUrl.getPubIndexUrl();
        }
      }
      publication.setPubUrl(pubUrl);
      pubDetails.add(publication);
    }
    String prjPubUrl = domainscm + "/groupweb/grpinfo/main?des3GrpId="
        + ServiceUtil.encodeToDes3(prjGrpTmp.getGrpId().toString()) + "&model=pub";
    params.put("psnUrl", psnUrl);
    params.put("psnName", psnName);
    params.put("groupName", prjGrpTmp.getGrpName());
    params.put("prjPubUrl", prjPubUrl);
    params.put("pubDetails", pubDetails);
    String subject = psnName + "，我们为您的项目进展报告匹配了相关成果";
    params.put("email_subject", subject);
    params.put("email_Template", "Project_Pub_Confirm_Remind_zh_CN.ftl");
    params.put("email_receiveEmail", psn.getEmail());
    return params;
  }

  @Override
  public void saveOptResult(Long grpId, int result) {
    prjGrpTmpDao.saveOptResult(grpId, result);

  }

  @Override
  public Long getCountGroupPub(Long grpId) {
    return grpPubsDao.getCountGroupPub(grpId);
  }

}
