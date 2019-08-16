package com.smate.center.task.service.email;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.email.PubConfirmPromotePsnDao;
import com.smate.center.task.dao.rol.quartz.PsnPubSendFlagDao;
import com.smate.center.task.dao.sns.quartz.ArchiveFileDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.email.PubConfirmPromotePsn;
import com.smate.center.task.model.rcmd.quartz.PubConfirmRolPub;
import com.smate.center.task.model.sns.quartz.ArchiveFile;
import com.smate.center.task.service.oauth.OauthLoginService;
import com.smate.center.task.single.constants.EtemplateConstant;
import com.smate.center.task.single.dao.rol.psn.RolPsnInsDao;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.utils.constant.EmailConstants;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.sys.ScmSystemUtil;

/**
 * 成果认领邮件推广 服务层
 * 
 * @author zk
 */

@Service("pubConfirmPromoteService")
@Transactional(rollbackFor = Exception.class)
public class PubConfirmPromoteServiceImpl implements PubConfirmPromoteService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${domainscm}")
  private String domainscm;

  @Autowired
  private RolPsnInsDao rolPsnInsDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private ArchiveFileDao archiveFileDao;
  @Autowired
  private PsnPubSendFlagDao psnPubSendFlagDao;
  @Autowired
  private PromoteMailInitDataService promoteMailInitDataService;
  @Autowired
  private OauthLoginService oauthLoginService;
  @Autowired
  private ScmSystemUtil scmSystemUtil;
  @Autowired
  private PubConfirmPromotePsnDao pubConfirmPromotePsnDao;
  @Autowired
  private SnsPubConfirmPromoteService snsPubConfirmPromoteService;

  /**
   * 获取信息
   */

  @Override
  public List<PubConfirmPromotePsn> getPubConfirmPromotePsn(Integer size) throws ServiceException {

    try {
      return pubConfirmPromotePsnDao.getPubConfirmPromotePsn(size);
    } catch (Exception e) {
      logger.error("成果认领推广邮件获取推广人员id时出错", e.getMessage());
      throw new ServiceException(e);

    }
  }

  /**
   * 统计还未发送人员数量
   * 
   * @return
   * @throws ServiceException
   */
  @Override
  public Long countNotSend() throws ServiceException {
    try {
      return pubConfirmPromotePsnDao.countNotSend();
    } catch (Exception e) {
      logger.error("统计还未发送人员数量时出错", e.getMessage());
      throw new ServiceException(e);
    }
  }

  /**
   * 判断状态类型
   */
  @Override
  public void dealStatus(PubConfirmPromotePsn pcpp) throws ServiceException {

    Integer result = -1;
    try {
      if (IS_NOT_SEND.intValue() == pcpp.getStatus().intValue()) {
        // 触发指派
        result = callRolPubAssign(pcpp);
      } else if (IS_SEND.intValue() == pcpp.getStatus().intValue()) {
        // 发送成果认领推广邮件
        result = dealPubTitlePlus(pcpp);
      } else if (RE_SEND.intValue() == pcpp.getStatus().intValue()) {
        // 再次发送成果认领推广邮件,第一次发送的邮件未被点击的,重新生成并发送
        result = reDealPubTitlePlus(pcpp);
      }
      savePubConfirmPromotePsn(pcpp, result);
    } catch (Exception e) {
      logger.error("成果认领邮件发送失败", e);
      savePubConfirmPromotePsn(pcpp, 9);
    }

  }

  /**
   * 调用 rol的指派
   */
  private Integer callRolPubAssign(PubConfirmPromotePsn pcpp) throws ServiceException {

    this.rolPsnInsDao.setPsnLogin(pcpp.getPsnId());
    // 记录冗余登录记录
    psnPubSendFlagDao.saveRolPsnSendFlag(pcpp.getPsnId(), true);
    return IS_SEND.intValue();

  }

  // 处理收件人
  private Person getPerson(Long psnId) throws ServiceException {

    return personDao.getPeronsForEmail(psnId);
  }

  /**
   * 发送成果认领推广邮件
   */
  private Integer dealPubTitlePlus(PubConfirmPromotePsn pcpp) throws ServiceException {
    try {

      Person person = getPerson(pcpp.getPsnId());
      // 人员信息为空，或邮件为空时，不能发送邮件，并将状态置为-1
      if (person == null || person.getEmail() == null) {
        logger.error("成果认领推广邮件--无人员数据!" + pcpp.getPsnId());
        return -3;
      }
      Map<String, Object> params = new HashMap<String, Object>();
      params.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, pcpp.getPsnId());
      params.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, person.getEmail());
      params.put("domainUrl", domainscm);
      // 处理成果
      params = handlePubInfo(params, pcpp);
      if (params == null) {
        return -2;
      }
      // 处理邮件数据
      params = dealEmailData(person, params, pcpp);
      if (params == null) {
        return -1;
      }
      // 保存邮件
      return saveEmail(person, params, pcpp, MAIL_TEMPLATE);
    } catch (Exception e) {
      logger.error("成果认领推广邮件处理邮件数据时出错!" + pcpp.getPsnId(), e);
      return -1;
    }
  }

  /**
   * 发送成果认领推广邮件
   */
  private Integer reDealPubTitlePlus(PubConfirmPromotePsn pcpp) throws ServiceException {
    try {

      Person person = getPerson(pcpp.getPsnId());
      // 人员信息为空，或邮件为空时，不能发送邮件，并将状态置为-1
      if (person == null || person.getEmail() == null) {
        logger.error("成果认领推广邮件--无人员数据!" + pcpp.getPsnId());
        return -3;
      }
      Map<String, Object> params = new HashMap<String, Object>();
      params.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, pcpp.getPsnId());
      params.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, person.getEmail());
      // 处理成果
      params = reHandlePubInfo(params, pcpp);
      if (params == null) {
        return -2;
      }
      // 处理邮件数据
      params = dealEmailData(person, params, pcpp);
      if (params == null) {
        return -1;
      }
      // 保存邮件
      return saveEmail(person, params, pcpp, MAIL_TEMPLATE);
    } catch (Exception e) {
      logger.error("成果认领推广邮件处理邮件数据时出错!" + pcpp.getPsnId(), e);
      return -1;
    }

  }

  // 处理邮件数据
  private Map<String, Object> dealEmailData(Person person, Map<String, Object> params, PubConfirmPromotePsn pcpp)
      throws ServiceException {

    try {

      // 获取人名
      String psnName = person.getName() == null ? person.getFirstName() + " " + person.getLastName() : person.getName();
      params.put("psnName", psnName);
      // 收件人主页
      String psnShortUrl = "";
      PsnProfileUrl profileUrl = psnProfileUrlDao.get(person.getPersonId());
      if (profileUrl != null && StringUtils.isNotBlank(profileUrl.getPsnIndexUrl())) {
        psnShortUrl = domainscm + "/P/" + profileUrl.getPsnIndexUrl();
      }
      params.put("psnUrl", psnShortUrl);
      // 链接日志记录
      String emaillog = "mailEventLogByNewThesis|psnId=" + pcpp.getPsnId() + ",urlId=8";
      // 确认成果链接
      // SCM-11425 重置密码功能实现 调用远程服务获取openId和AID
      Long openId = oauthLoginService.getOpenId("00000000", person.getPersonId(), 2);
      String AID = oauthLoginService.getAutoLoginAID(openId, "ResetPWD");
      String cfmUrl =
          domainscm + "/psnweb/homepage/show?module=pub&jumpto=puball&menuId=1200&" + "AID=" + AID + "&resetpwd=true";
      params.put("cfmUrl", cfmUrl);
      // emaillog加密编码
      String key15 = java.net.URLEncoder.encode(ServiceUtil.encodeToDes3(emaillog), ScmSystemUtil.ENCODING);
      params.put("key15", key15);
      return params;
    } catch (Exception e) {
      logger.error("成果认领推广邮件,处理邮件数据时出错!" + pcpp.getPsnId(), e);
      return null;
    }

  }

  // 保存邮件数据
  private Integer saveEmail(Person person, Map<String, Object> params, PubConfirmPromotePsn pcpp, String template)
      throws ServiceException {

    try {
      params.put(EmailConstants.EMAIL_TEMPLATE_KEY, template + "_zh_CN.ftl");
      promoteMailInitDataService.saveMailInitData(params);
      return pcpp.getStatus() + 1;
    } catch (Exception e) {
      logger.error("成果认领推广邮件,保存邮件数据时出错!" + pcpp.getPsnId(), e);
      return -1;
    }
  }

  // 处理成果信息

  @SuppressWarnings({"rawtypes", "unchecked"})
  private Map<String, Object> handlePubInfo(Map<String, Object> params, PubConfirmPromotePsn pcpp)
      throws ServiceException {

    // 获取是否有未确认的成果
    Person person = getPerson(pcpp.getPsnId());
    // 获取人名
    String psnName = person.getName() == null ? person.getFirstName() + " " + person.getLastName() : person.getName();
    params.put("psnName", psnName);
    Map<String, Object> pubParams = snsPubConfirmPromoteService.getPubTitlePlusByScore(pcpp.getPsnId());
    if (pubParams == null || pubParams.get("pubSum").equals("0")) {
      logger.error("成果认领推广邮件--无成果数据!" + pcpp.getPsnId());
      return null;
    }
    params.putAll(pubParams);
    logger.info("成果数" + params.get("pubSum"));
    List<PubConfirmRolPub> pubDetails = new ArrayList<PubConfirmRolPub>();
    pubDetails = (List<PubConfirmRolPub>) params.get("pubDetails");
    logger.info("成果信息" + pubDetails.get(0).getAuthorNames() + pubDetails.get(0).getBriefDesc());
    if (StringUtils.isNotBlank(ObjectUtils.toString(params.get("fulltextId")))) {
      params.put("fullImg0",
          getFullTextImg(String.valueOf(params.get("fulltextId")), String.valueOf(params.get("rolPubId"))));
    }
    params.put(EmailConstants.EMAIL_SUBJECT_KEY, psnName + "，你有" + params.get("pubSum").toString() + "篇新成果。");
    return params;
  }

  // 处理第二次发送前次未点击邮件的人的成果数据
  @SuppressWarnings({"rawtypes"})
  private Map<String, Object> reHandlePubInfo(Map<String, Object> params, PubConfirmPromotePsn pcpp)
      throws ServiceException {

    // 获取是否有未确认的成果
    // List<PublicationConfirm> statec =
    // snsPubConfirmPromoteService.getPublicationConfirm(pcpp.getPsnId());
    // pubInfo {pubSum:String,title0..2:String}
    Map pubInfo = snsPubConfirmPromoteService.getPubTitlePlusByScore(pcpp.getPsnId());
    if (pubInfo == null) {
      logger.error("成果认领推广邮件--无成果数据!" + pcpp.getPsnId());
      return null;
    }
    // 主题
    // String subject = "您可能是" + pubInfo.get("pubSum") + "篇论文的作者";
    // params.put("key5", pubInfo.get("pubSum"));
    params.put(EmailConstants.EMAIL_SUBJECT_KEY, "确认您的论文成果，提高引用和科研影响力");
    params.put("tempContext", "你可能是以下" + pubInfo.get("pubSum") + "篇论文的作者");
    return params;
  }

  // 得到全文的图片
  private String setFullTextPdfImg(Long rolPubId) throws ServiceException {

    // return
    // remotingServiceFactory.getServiceByNodeId(ServiceConstants.ROL_NODE_ID,
    // PubFulltextRolService.class)
    // .getFulltextImageUrl(rolPubId);
    return null;
  }

  private String getFullTextImg(String fileId, String rolPubId) throws ServiceException {

    String fullTextImg = EtemplateConstant.BASE_URL + EtemplateConstant.NO_FULLTEXT_IMG;
    try {
      ArchiveFile af = archiveFileDao.findArchiveFileById(Long.valueOf(fileId));

      if (af != null && StringUtils.isNotBlank(af.getFileName())) {
        String fileName = af.getFileName();
        if (fileName.toLowerCase().endsWith("doc") || fileName.toLowerCase().endsWith("docx")) {
          fullTextImg = EtemplateConstant.BASE_URL + PubConfirmPromoteService.DOC_IMG;
        } else if (fileName.toLowerCase().endsWith("txt")) {
          fullTextImg = EtemplateConstant.BASE_URL + PubConfirmPromoteService.txt_IMG;
        } else if (fileName.toLowerCase().endsWith("zip")) {
          fullTextImg = EtemplateConstant.BASE_URL + PubConfirmPromoteService.ZIP_IMG;
        } else if (fileName.toLowerCase().endsWith("html")) {
          fullTextImg = EtemplateConstant.BASE_URL + PubConfirmPromoteService.HTML_IMG;
        } else if (fileName.toLowerCase().endsWith("pdf")) {
          fullTextImg = setFullTextPdfImg(Long.valueOf(rolPubId));
        }
      }
    } catch (Exception e) {
    }
    return fullTextImg;
  }

  @Override
  public void savePubConfirmPromotePsn(PubConfirmPromotePsn pcpp, Integer status) throws ServiceException {
    pcpp.setStatus(status);
    pcpp.setDealDt(new Date());
    pubConfirmPromotePsnDao.save(pcpp);
  }
}
