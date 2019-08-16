package com.smate.web.dyn.service.dynamic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.dyn.consts.DynamicConstants;
import com.smate.core.base.dyn.dao.DynamicMsgDao;
import com.smate.core.base.dyn.model.DynamicMsg;
import com.smate.core.base.email.service.EmailCommonService;
import com.smate.core.base.email.service.EmailSendService;
import com.smate.core.base.email.service.MailInitDataService;
import com.smate.core.base.project.model.PrjComment;
import com.smate.core.base.project.model.ProjectStatistics;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.dao.PsnPubDAO;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.pub.dao.PubIndexUrlDao;
import com.smate.core.base.pub.model.PubIndexUrl;
import com.smate.core.base.utils.constant.EmailConstants;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dynamicds.InspgDynamicUtil;
import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.sys.ScmSystemUtil;
import com.smate.web.dyn.dao.dynamic.DynStatisticsDao;
import com.smate.web.dyn.dao.pdwhpub.PdwhPubCommentDAO;
import com.smate.web.dyn.dao.pdwhpub.PdwhPubStatisticsDAO;
import com.smate.web.dyn.dao.pub.PrjCommentsDao;
import com.smate.web.dyn.dao.pub.ProjectStatisticsDao;
import com.smate.web.dyn.dao.pub.PubCommentDAO;
import com.smate.web.dyn.dao.pub.PubFulltextDao;
import com.smate.web.dyn.dao.pub.PubSnsDAO;
import com.smate.web.dyn.dao.pub.PubStatisticsDAO;
import com.smate.web.dyn.dao.reply.DynamicReplyPsnDao;
import com.smate.web.dyn.dao.reply.DynamicReplyResDao;
import com.smate.web.dyn.form.dynamic.DynReplayInfo;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.model.dynamic.DynStatistics;
import com.smate.web.dyn.model.pdwhpub.PdwhPubCommentPO;
import com.smate.web.dyn.model.pdwhpub.PdwhPubStatisticsPO;
import com.smate.web.dyn.model.pub.PubComment;
import com.smate.web.dyn.model.pub.PubCommentPO;
import com.smate.web.dyn.model.pub.PubFulltext;
import com.smate.web.dyn.model.pub.PubSimple;
import com.smate.web.dyn.model.pub.PubSnsPO;
import com.smate.web.dyn.model.pub.PubSnsStatusEnum;
import com.smate.web.dyn.model.pub.PubStatisticsPO;
import com.smate.web.dyn.model.reply.DynamicReplyPsn;
import com.smate.web.dyn.model.reply.DynamicReplyRes;
import com.smate.web.dyn.service.psn.InsInfoService;
import com.smate.web.dyn.service.psn.PersonQueryservice;
import com.smate.web.dyn.service.pub.PublicationService;

/**
 * 动态评论服务类
 * 
 * @author zk
 *
 */
@Service("dynamicReplyService")
@Transactional(rollbackFor = Exception.class)
public class DynamicReplyServiceImpl implements DynamicReplyService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private DynamicReplyResDao dynamicReplyResDao;
  @Autowired
  private DynamicRealtimeService dynamicRealtimeService;
  @Autowired
  private DynamicReplyPsnDao dynamicReplyPsnDao;
  @Autowired
  private DynStatisticsDao dynStatisticsDao;
  @Autowired
  private PubStatisticsDAO pubStatisticsDAO;
  @Autowired
  private PersonQueryservice personQueryservic;
  @Autowired
  private PublicationService publicationService;
  @Autowired
  private InsInfoService insInfoService;
  @Autowired
  private EmailSendService commentedYourPubEmailService;
  @Autowired
  private DynamicMsgDao dynamicMsgDao;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private EmailCommonService emailCommonService;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;
  @Autowired
  private MailInitDataService mailInitDataService;
  @Autowired
  private PrjCommentsDao prjCommentsDao;
  @Autowired
  private ProjectStatisticsDao projectStatisticsDao;
  @Autowired
  private ScmSystemUtil scmSystemUtil;
  @Autowired
  private PubFulltextDao pubFulltextDao;
  @Autowired
  private PsnPubDAO psnPubDAO;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PubCommentDAO pubCommentDAO;
  @Autowired
  private PdwhPubCommentDAO pdwhPubCommentDAO;
  @Autowired
  private PdwhPubStatisticsDAO pdwhStatisticsDAO;

  @Override
  public void replyDyn(DynamicForm form) throws DynException {
    form.setResNode(1);
    Person person = personQueryservic.findPerson(form.getPsnId());
    if (person == null || form.getDynId() == null
        || (StringUtils.isBlank(form.getReplyContent()) && form.getPubId() == null)) {
      throw new DynException("评论动态时，不能获取到评论人员的person对象或者获取不到评论的资源或者没有输入评论的内容");
    }
    DynamicMsg d = dynamicMsgDao.get(form.getDynId());
    if (d == null || d.getDelstatus() == 99) {
      throw new DynException("动态已删除！");
    }
    // 评论都绑定到父动态，子动态的评论也要从父动态读取
    form.setParentDynId(d.getSameFlag());
    DynamicReplyPsn dynamicReplyPsn = null;
    // 定义资源id，纯成果动态这里是成果id，普通动态这里是dynId
    Long resId = null;
    if ("B2TEMP".equals(form.getDynType()) || "B3TEMP".equals(form.getDynType())) {
      resId = form.getResId();
      dynamicReplyPsn = this.saveReplyRecord(form, person, resId);
      switch (form.getResType()) {
        case DynamicConstants.RES_TYPE_PUB:
        case DynamicConstants.RES_TYPE_REF:
          addPubComment(form, dynamicReplyPsn);
          break;
        case DynamicConstants.RES_TYPE_PRJ:
          addPrjComment(form, dynamicReplyPsn);
          break;
        case DynamicConstants.RES_TYPE_PUB_PDWH:
        case DynamicConstants.RES_TYPE_JOURNALAWARD:
          // 现在动态评论基准库和基金是直接跳转详情页面
          addPdwhPubComment(form, dynamicReplyPsn);
          break;
        case DynamicConstants.RES_TYPE_FUND:
          // 现在动态评论基准库和基金是直接跳转详情页面
          addFundComment(form, dynamicReplyPsn);
          break;
        default:
          break;
      }
    } else {
      // 页面上可能会传递resType值为1的
      if (form.getResType() == null) {
        form.setResType(DynamicConstants.RES_TYPE_NORMAL);
      }
      resId = form.getParentDynId();
      dynamicReplyPsn = this.saveReplyRecord(form, person, resId);
    }
    // 对同一资源评论，每人每天只能产生一条动态
    int count = dynamicReplyPsnDao.getCommentCount(resId, form.getPsnId(), form.getResType());
    if (count <= 1) {
      if (form.getNextDynType() != null) {
        // A->B1;B2->B2;B3->B2。
        form.setDynType(form.getNextDynType());
      }
      dynamicRealtimeService.dynamicRealtime(form);
      // 发邮件--因为现在所以B2B3类型的动态评论都直接跳转到了成果详情--所以这里不会再满足条件
      if (("B2TEMP".equals(form.getDynType()) || "B3TEMP".equals(form.getDynType()))
          && form.getResType() == DynamicConstants.RES_TYPE_PUB) {
        PubSnsPO pub = pubSnsDAO.get(resId);
        Long ownerPsnId = psnPubDAO.getPsnOwner(resId);
        if (pub != null && pub.getStatus() != PubSnsStatusEnum.DELETED && !form.getPsnId().equals(ownerPsnId)) {
          // newSendCommentEmail(form.getPsnId(), pub, form);
          sendCommentEmail(form.getPsnId(), ownerPsnId, resId);
        }
      }
    }
    // 获取刚刚评论的数据，加载到页面
    List<DynamicReplyPsn> dynReplyList = new ArrayList<DynamicReplyPsn>();
    dynReplyList.add(dynamicReplyPsn);
    List<DynReplayInfo> dynReplayInfoList = this.rebuildShowDynReply(dynReplyList);
    form.setDynReplayInfoList(dynReplayInfoList);
  }

  private void addFundComment(DynamicForm form, DynamicReplyPsn dynamicReplyPsn) {
    // TODO Auto-generated method stub
  }

  private void addPubComment(DynamicForm form, DynamicReplyPsn dynamicReplyPsn) {
    // 如果是纯成果动态的评论，要另外多绑定到成果 目前B2会跳转详情
    PubCommentPO pubCommentPO = new PubCommentPO();
    pubCommentPO.setContent(form.getReplyContent());
    pubCommentPO.setPsnId(form.getPsnId());
    pubCommentPO.setPubId(form.getResId());
    pubCommentPO.setStatus(0);// 状态 0=正常 ； 9=删除
    pubCommentPO.setGmtCreate(new Date());
    pubCommentPO.setGmtModified(new Date());
    pubCommentDAO.save(pubCommentPO);
    // 更新成果的评论数
    PubStatisticsPO ps = pubStatisticsDAO.get(form.getResId());
    if (ps == null) {
      ps = new PubStatisticsPO(form.getResId());
    }
    ps.setCommentCount(ps.getCommentCount() + 1);
    pubStatisticsDAO.save(ps);
  }

  private void addPdwhPubComment(DynamicForm form, DynamicReplyPsn dynamicReplyPsn) {
    PdwhPubCommentPO pdwhPubCommentPO = new PdwhPubCommentPO();
    pdwhPubCommentPO.setContent(form.getReplyContent());
    pdwhPubCommentPO.setPdwhPubId(form.getResId());
    pdwhPubCommentPO.setPsnId(form.getPsnId());
    pdwhPubCommentPO.setStatus(0);// 状态 0=正常 ; 9=删除
    pdwhPubCommentPO.setGmtCreate(new Date());
    pdwhPubCommentPO.setGmtModified(new Date());
    pdwhPubCommentDAO.save(pdwhPubCommentPO);
    // 更新统计数
    PdwhPubStatisticsPO pdwhPubStatisticsPO = pdwhStatisticsDAO.get(form.getResId());
    if (pdwhPubStatisticsPO == null) {
      pdwhPubStatisticsPO = new PdwhPubStatisticsPO();
      pdwhPubStatisticsPO.setPdwhPubId(form.getResId());
      pdwhPubStatisticsPO.setCommentCount(1);
    } else {
      int commentCount = pdwhPubStatisticsPO.getCommentCount() == null ? 0 : pdwhPubStatisticsPO.getCommentCount();
      pdwhPubStatisticsPO.setCommentCount(commentCount + 1);
    }
    pdwhStatisticsDAO.save(pdwhPubStatisticsPO);
  }

  private void addPrjComment(DynamicForm form, DynamicReplyPsn dynamicReplyPsn) {
    PrjComment pc = new PrjComment();
    pc.setPsnId(form.getPsnId());
    pc.setPrjId(form.getResId());
    pc.setCommentsContent(form.getReplyContent());
    pc.setIsAudit(1);
    pc.setCreateDate(new Date());
    pc.setPsnAvatars(dynamicReplyPsn.getReplyerAvatar());
    pc.setPsnName(dynamicReplyPsn.getReplyerName() == null ? dynamicReplyPsn.getReceiverEnName()
        : dynamicReplyPsn.getReplyerName());
    prjCommentsDao.save(pc);
    ProjectStatistics statistics = projectStatisticsDao.get(form.getResId());
    if (statistics == null) {
      statistics = new ProjectStatistics();
      statistics.setProjectId(form.getResId());
      statistics.setCommentCount(1);
    } else {
      if (statistics.getCommentCount() == null) {// 解决部分老数据评论数据为null
        statistics.setCommentCount(0);
      }
      statistics.setCommentCount(statistics.getCommentCount() + 1);
    }
    projectStatisticsDao.save(statistics);
  }

  private void newSendCommentEmail(Long psnId, PubSimple pub, DynamicForm form) {
    try {
      HashMap<String, Object> emailMap = new HashMap<String, Object>();
      Person awarderPsn = personDao.get(psnId);
      Person ownerPsn = personDao.get(pub.getOwnerPsnId());
      String languageVersion = ownerPsn.getEmailLanguageVersion();
      if (StringUtils.isBlank(languageVersion)) {
        languageVersion = LocaleContextHolder.getLocale().toString();
      }
      String mailTemplate = "";
      String pubTitle = "";
      String awarderPsnName = emailCommonService.getPsnNameByEmailLangage(awarderPsn, languageVersion);
      String ownerPsnName = emailCommonService.getPsnNameByEmailLangage(ownerPsn, languageVersion);
      if ("en_US".equals(languageVersion)) {
        mailTemplate = "commented_your_pub_en_US.ftl";
        pubTitle = StringUtils.isBlank(pub.getEnTitle()) ? pub.getZhTitle() : pub.getEnTitle();
        emailMap.put(EmailConstants.EMAIL_SUBJECT_KEY, awarderPsnName + "commented your publications :" + pubTitle);// 主题
      } else {
        mailTemplate = "commented_your_pub_zh_CN.ftl";
        pubTitle = StringUtils.isBlank(pub.getZhTitle()) ? pub.getEnTitle() : pub.getZhTitle();
        emailMap.put(EmailConstants.EMAIL_SUBJECT_KEY, awarderPsnName + "评论了你的论文：" + pubTitle);// 主题
      }
      String psnShortUrl = "";
      String pubShortUrl = "";
      PsnProfileUrl profileUrl = psnProfileUrlDao.get(SecurityUtils.getCurrentUserId());
      if (profileUrl != null && StringUtils.isNotBlank(profileUrl.getPsnIndexUrl())) {
        psnShortUrl = domainscm + "/" + ShortUrlConst.P_TYPE + "/" + profileUrl.getPsnIndexUrl();
      }
      PubIndexUrl pubIndexUrl = pubIndexUrlDao.get(form.getResId());
      if (pubIndexUrl != null && StringUtils.isNotBlank(pubIndexUrl.getPubIndexUrl())) {
        pubShortUrl = domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl();
      }
      emailMap.put("domainUrl", domainscm);
      emailMap.put("commentedPsnUrl", psnShortUrl);
      emailMap.put("pubDetailUrl", pubShortUrl);
      emailMap.put("fullTextUrl", getFullTextImg(pub.getFullTextField(), pub.getPubId()));
      emailMap.put("impactsUrl", domainscm + "/psnweb/homepage/show?module=influence");
      emailMap.put("pubTitle", pubTitle);
      emailMap.put("pubAuthors", pub.getAuthorNames());
      emailMap.put("pubBrief", pub.getBriefDesc());
      emailMap.put("commentedPsnName", awarderPsnName);
      emailMap.put("psnName", ownerPsnName);
      emailMap.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, ownerPsn.getEmail());// 收件人邮箱
      emailMap.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, ownerPsn.getPersonId());// 收件人邮箱
      emailMap.put(EmailConstants.EMAIL_SENDER_PSNID_KEY, awarderPsn.getPersonId());// 发件人ID
      emailMap.put(EmailConstants.EMAIL_TEMPLATE_KEY, mailTemplate);// 邮件模板名称
      mailInitDataService.saveMailInitData(emailMap);
    } catch (Exception e) {
      logger.error("动态评论成果发邮件失败！", e);
    }
  }

  private void sendCommentEmail(Long replyerPsnId, Long resOwnerPsnId, Long resId) {
    try {
      if (!replyerPsnId.equals(resOwnerPsnId)) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("commentedPsnId", replyerPsnId);
        map.put("psnId", resOwnerPsnId);
        map.put("pubId", resId);
        commentedYourPubEmailService.syncEmailInfo(map);
      }
    } catch (Exception e) {
      logger.error("发送评论邮件错误：resId:{},replyerPsnId:{}", resId, replyerPsnId, e);
    }
  }

  /**
   * 保存评论记录
   * 
   * @param form
   */
  private DynamicReplyPsn saveReplyRecord(DynamicForm form, Person person, Long resId) throws DynException {
    DynamicReplyRes dynRes = dynamicReplyResDao.getDynResByResId(resId, form.getResType());
    Date now = new Date();
    if (dynRes == null) {
      dynRes = new DynamicReplyRes();
      dynRes.setPsnId(form.getPsnId());
      dynRes.setResId(resId);
      dynRes.setResNode(1);
      dynRes.setResType(form.getResType());
      dynRes.setReplyTimes(1L);
      dynRes.setPlatform(form.getPlatform());
    } else {
      dynRes.setReplyTimes(dynRes.getReplyTimes() + 1);
    }
    dynRes.setUpdateDate(now);
    this.dynamicReplyResDao.save(dynRes);
    // 更新被评论的动态的评论数
    DynStatistics DynStatistics = dynStatisticsDao.getDynamicStatistics(resId);
    if (DynStatistics != null) {
      if (DynStatistics.getCommentCount() == null) {
        DynStatistics.setCommentCount(0);
      }
      dynStatisticsDao.updateDynamicCommentStatistics(resId, DynStatistics.getCommentCount() + 1);
    }
    // 添加评论记录.
    DynamicReplyPsn dynamicReplyPsn = new DynamicReplyPsn();
    dynamicReplyPsn.setReplyId(dynRes.getReplyId());
    dynamicReplyPsn.setReplyerId(form.getPsnId());
    dynamicReplyPsn.setReplyerName(personQueryservic.getPsnName(person, "zh_CN"));
    dynamicReplyPsn.setReplyerEnName(personQueryservic.getPsnName(person, "en_US"));
    dynamicReplyPsn.setReplyerAvatar(person.getAvatars());
    dynamicReplyPsn.setReplyContent(form.getReplyContent());
    dynamicReplyPsn.setSyncFlag(resId.toString());
    dynamicReplyPsn.setReplyDate(now);
    dynamicReplyPsn.setStatus(0);
    dynamicReplyPsn.setReplyAddResId(form.getPubId());
    dynamicReplyPsn.setPlatform(form.getPlatform());
    this.dynamicReplyPsnDao.save(dynamicReplyPsn);
    return dynamicReplyPsn;
  }

  /**
   * 保存资源相关记录
   * 
   * @param form
   * @param pub
   * @param recordId
   * @param person
   * @throws DynException
   */
  // private void saveResRecord(DynamicForm form, Publication pub, Long
  // recordId, Person person) throws DynException {
  // PubComment pubComment = new PubComment();
  // pubComment.setDynReplyRecordId(recordId);
  // pubComment.setCommentsContent(StringHtml.toHtmlInput(form.getReplyContent()));
  // pubComment.setCommentsStart(0);
  // pubComment.setCreateDate(new Date());
  // pubComment.setIsAudit(1);
  // pubComment.setPsnId(form.getPsnId());
  // pubComment.setPubId(pub.getPubId());
  // pubComment.setSyncFlag(form.getDynId() + "");
  // wrapPubComment(pubComment, person);
  // // pubCommentsDao.save(pubComment);
  // Publication publication =
  // publicationService.getPubForComments(pubComment.getPubId());
  // publication.setPubReviews((publication.getPubReviews() == null ? 0 :
  // publication.getPubReviews()) + 1);
  // // if (pubComment.getCommentsStart() != null &&
  // // pubComment.getCommentsStart() > 0) {
  // publication.setPubStart(((publication.getPubStart() == null ? 0 :
  // publication.getPubStart())
  // + pubComment.getCommentsStart().intValue()));
  // publication.setPubStartPsns((publication.getPubStartPsns() == null ? 0 :
  // publication.getPubStartPsns()) + 1);
  // // }
  // publicationService.updatePub(publication);
  // // 更新成果的评论数
  // PubStatisticsPO ps = pubStatisticsDAO.get(pub.getPubId());
  // if (ps == null) {
  // ps = new PubStatisticsPO(pub.getPubId());
  // }
  // ps.setCommentCount(ps.getCommentCount() + 1);
  // pubStatisticsDAO.save(ps);
  //
  // }
  /**
   * 封装保存的评论.
   * 
   * @param pubComment
   * @param commentPerson
   */
  private void wrapPubComment(PubComment pubComment, Person commentPerson) {
    if ("zh".equals(LocaleContextHolder.getLocale().getLanguage())) {
      pubComment.setPsnName(commentPerson.getName() == null ? commentPerson.getEname() : commentPerson.getName());
    } else if ("en".equals(LocaleContextHolder.getLocale().getLanguage())) {
      pubComment.setPsnName(commentPerson.getEname() == null ? commentPerson.getName() : commentPerson.getEname());
    }
    if (commentPerson.getAvatars().startsWith("http")) {
      pubComment.setPsnAvatars(commentPerson.getAvatars());
    } else {
      pubComment.setPsnAvatars(commentPerson.getAvatars());
    }
    pubComment
        .setPsnSiteUrl("/resume/view?des3PsnId=" + ServiceUtil.encodeToDes3(commentPerson.getPersonId().toString()));
  }

  @Override
  public void loadDynReply(DynamicForm form) throws Exception {
    DynamicMsg d = dynamicMsgDao.get(form.getDynId());
    if (form.getDynId() == null || d == null) {
      throw new DynException("动态id为空或动态不存在！dynId=" + form.getDynId());
    }
    List<DynamicReplyPsn> dynReplyList = null;
    List<DynReplayInfo> dynReplayInfoList = null;
    Long resId = null;
    Integer resType = null;
    // 取动态的评论
    if (d.getDynType().equals("ATEMP") || d.getDynType().equals("B1TEMP")) {
      resId = d.getSameFlag();
      resType = DynamicConstants.RES_TYPE_NORMAL;
      dynReplyList = dynamicReplyPsnDao.getDynReply2(resId, form);
      if (CollectionUtils.isNotEmpty(dynReplyList)) {
        dynReplayInfoList = this.rebuildShowDynReply(dynReplyList);
      }
    }
    // 取资源的评论
    if (d.getDynType().equals("B2TEMP") || d.getDynType().equals("B3TEMP")) {
      resId = d.getResId();
      resType = d.getResType();
      switch (d.getResType()) {
        case 1:
        case 2:
          dynReplayInfoList = buildSnsPubComment(resId, form);
          break;
        case 11:
          break;
        case 22:
        case 24:
          dynReplayInfoList = buildPdwhPubComment(resId, form);
          break;
        case 4:
          dynReplayInfoList = buildPrjComment(resId, form);
          break;
        default:
          break;
      }
    }
    // 返回给页面的数据
    if (CollectionUtils.isNotEmpty(dynReplayInfoList)) {
      Collections.reverse(dynReplayInfoList);
      form.setDynReplayInfoList(dynReplayInfoList);
    }
    // 人员头像
    Person person = personQueryservic.findPersonBase(form.getPsnId());
    form.setPsnAvatars(person.getAvatars());
  }

  private List<DynReplayInfo> buildPrjComment(Long resId, DynamicForm form) throws Exception {
    List<PrjComment> list = prjCommentsDao.getPrjReply(resId, form);
    if (list != null && list.size() > 0) {
      List<DynReplayInfo> dynReplayInfoList = new ArrayList<DynReplayInfo>();
      for (PrjComment pr : list) {
        DynReplayInfo dynReplayInfo = new DynReplayInfo();
        Map<String, String> findPsnInsInfo = null;
        findPsnInsInfo = insInfoService.findPsnInsInfo(pr.getPsnId());
        if (findPsnInsInfo != null) {
          dynReplayInfo.setPsnInsInfo(findPsnInsInfo.get(InsInfoService.INS_INFO_ZH));
        }
        String locale = LocaleContextHolder.getLocale().toString();
        dynReplayInfo.setDes3ReplyerId(ServiceUtil.encodeToDes3(pr.getPsnId().toString()));
        dynReplayInfo.setRebuildDate("en_US".equals(locale) ? InspgDynamicUtil.formatDateUS(pr.getCreateDate())
            : InspgDynamicUtil.formatDate(pr.getCreateDate()));
        dynReplayInfo.setReplyContent(pr.getCommentsContent());
        Person person = personQueryservic.findPersonBase(pr.getPsnId());
        if (person != null && StringUtils.isNotBlank(person.getAvatars())) {
          dynReplayInfo.setReplyerAvatar(person.getAvatars());
        }
        dynReplayInfo.setReplyerEnName(personQueryservic.getPsnName(person, "en_US"));
        dynReplayInfo.setReplyerName(pr.getPsnName());
        dynReplayInfo.setReplyerId(pr.getPsnId());
        // 评论人的主页url
        dynReplayInfo
            .setHomePageUrl(domainscm + "/psnweb/mobile/homepage?Des3PsnId=" + dynReplayInfo.getDes3ReplyerId());
        dynReplayInfoList.add(dynReplayInfo);
      }
      return dynReplayInfoList;
    }
    return null;
  }

  private List<DynReplayInfo> buildPdwhPubComment(Long resId, DynamicForm form) throws Exception {
    List<DynReplayInfo> dynReplayInfoList = null;
    List<PdwhPubCommentPO> list = pdwhPubCommentDAO.queryComments(resId, form);
    if (list != null && list.size() > 0) {
      dynReplayInfoList = new ArrayList<DynReplayInfo>();
      for (PdwhPubCommentPO p : list) {
        DynReplayInfo info = new DynReplayInfo();
        Map<String, String> findPsnInsInfo = null;
        findPsnInsInfo = insInfoService.findPsnInsInfo(p.getPsnId());
        if (findPsnInsInfo != null) {
          info.setPsnInsInfo(findPsnInsInfo.get(InsInfoService.INS_INFO_ZH));
        }
        info.setDes3ReplyerId(Des3Utils.encodeToDes3(p.getPsnId().toString()));
        Person person = personQueryservic.findPersonBase(p.getPsnId());
        if (person != null && StringUtils.isNotBlank(person.getAvatars())) {
          info.setReplyerAvatar(person.getAvatars());
          info.setReplyerEnName(StringUtils.isBlank(person.getEname()) ? (person.getName()) : person.getEname());
          info.setReplyerName(StringUtils.isBlank(person.getName()) ? (person.getEname()) : person.getName());
        }
        info.setReplyContent(p.getContent());
        String locale = LocaleContextHolder.getLocale().toString();
        info.setRebuildDate("en_US".equals(locale) ? InspgDynamicUtil.formatDateUS(p.getGmtCreate())
            : InspgDynamicUtil.formatDate(p.getGmtCreate()));
        info.setHomePageUrl(domainscm + "/psnweb/mobile/homepage?Des3PsnId=" + info.getDes3ReplyerId());
        dynReplayInfoList.add(info);
      }
    }
    return dynReplayInfoList;
  }

  private List<DynReplayInfo> buildSnsPubComment(Long resId, DynamicForm form) throws Exception {
    List<DynReplayInfo> dynReplayInfoList = null;
    List<PubCommentPO> reply = pubCommentDAO.getPubReply(resId, form);
    if (reply != null && reply.size() > 0) {
      dynReplayInfoList = rebuildShowPubReply(reply);
      /*
       * DynReplayInfoList = new ArrayList<DynReplayInfo>(); for(PubComment p:reply) { DynReplayInfo info
       * = new DynReplayInfo(); info.setDes3ReplyerId(Des3Utils.encodeToDes3(p.getPsnId(). toString()));
       * info.setReplyerAvatar(p.getPsnAvatars()); info.setReplyContent(p.getCommentsContent());
       * info.setReplyerEnName(p.getPsnName()); info.setReplyerName(p.getPsnName()); String locale =
       * LocaleContextHolder.getLocale().toString(); info.setRebuildDate("en_US".equals(locale) ?
       * InspgDynamicUtil.formatDateUS(p.getCreateDate()) :
       * InspgDynamicUtil.formatDate(p.getCreateDate())); info.setHomePageUrl(domainscm +
       * "/psnweb/mobile/homepage?Des3PsnId=" + info.getDes3ReplyerId()); DynReplayInfoList.add(info); }
       */
    }
    return dynReplayInfoList;
  }

  @Override
  public void loadSampleDynReply(DynamicForm form) throws DynException {
    if (form.getResId() == null || form.getResType() == null) {
      throw new DynException("动态id 或  resType 不能为空！form=" + form);
    }
    List<DynamicReplyPsn> dynReplyList = null;
    List<DynReplayInfo> dynReplayInfoList = null;
    // 取动态评论
    dynReplyList = dynamicReplyPsnDao.getSampleDynReply(form.getResId(), form.getResType(), form.getPageNumber(),
        form.getPageSize());
    if (CollectionUtils.isNotEmpty(dynReplyList)) {
      dynReplayInfoList = this.rebuildShowDynReply(dynReplyList);
    }
    // 返回给页面的数据
    if (CollectionUtils.isNotEmpty(dynReplayInfoList)) {
      Collections.reverse(dynReplayInfoList);
      form.setDynReplayInfoList(dynReplayInfoList);
    }
    // 人员头像
    Person person = personQueryservic.findPersonBase(form.getPsnId());
    form.setPsnAvatars(person.getAvatars());
  }

  /**
   * pub重新构造显示数据： 时间、人员职位
   * 
   * @param dynReplyList
   * @return
   * @throws DynException
   */
  private List<DynReplayInfo> rebuildShowPubReply(List<PubCommentPO> pubReply) throws DynException {
    List<DynReplayInfo> dynReplayInfoList = new ArrayList<DynReplayInfo>();
    for (PubCommentPO pr : pubReply) {
      DynReplayInfo dynReplayInfo = new DynReplayInfo();
      Map<String, String> findPsnInsInfo = null;
      findPsnInsInfo = insInfoService.findPsnInsInfo(pr.getPsnId());
      if (findPsnInsInfo != null) {
        dynReplayInfo.setPsnInsInfo(findPsnInsInfo.get(InsInfoService.INS_INFO_ZH));
      }
      String locale = LocaleContextHolder.getLocale().toString();
      dynReplayInfo.setDes3ReplyerId(ServiceUtil.encodeToDes3(pr.getPsnId().toString()));
      dynReplayInfo.setRebuildDate("en_US".equals(locale) ? InspgDynamicUtil.formatDateUS(pr.getGmtCreate())
          : InspgDynamicUtil.formatDate(pr.getGmtCreate()));
      dynReplayInfo.setReplyContent(pr.getContent());
      Person person = personQueryservic.findPersonBase(pr.getPsnId());
      if (person != null && StringUtils.isNotBlank(person.getAvatars())) {
        dynReplayInfo.setReplyerAvatar(person.getAvatars());
        dynReplayInfo.setReplyerEnName(personQueryservic.getPsnName(person, "en_US"));
        dynReplayInfo.setReplyerName(person.getPsnName());
      }
      dynReplayInfo.setReplyerId(pr.getPsnId());
      // 评论人的主页url
      dynReplayInfo.setHomePageUrl(domainscm + "/psnweb/mobile/homepage?Des3PsnId=" + dynReplayInfo.getDes3ReplyerId());
      dynReplayInfoList.add(dynReplayInfo);
    }
    return dynReplayInfoList;
  }

  /**
   * dyn重新构造显示数据： 时间、人员职位
   * 
   * @param dynReplyList
   * @return
   * @throws DynException
   */
  @Override
  public List<DynReplayInfo> rebuildShowDynReply(List<DynamicReplyPsn> dynReplyList) throws DynException {
    List<DynReplayInfo> dynReplayInfoList = new ArrayList<DynReplayInfo>();
    for (DynamicReplyPsn drp : dynReplyList) {
      DynReplayInfo dynReplayInfo = new DynReplayInfo();
      Map<String, String> findPsnInsInfo = insInfoService.findPsnInsInfo(drp.getReplyerId());
      if (findPsnInsInfo != null) {
        dynReplayInfo.setPsnInsInfo(findPsnInsInfo.get(InsInfoService.INS_INFO_ZH));
      }
      dynReplayInfo.setDes3ReplyerId(
          StringUtils.isBlank(drp.getDes3ReceiverId()) ? ServiceUtil.encodeToDes3(drp.getReplyerId().toString())
              : drp.getDes3ReceiverId());
      String locale = LocaleContextHolder.getLocale().toString();
      dynReplayInfo.setRebuildDate("en_US".equals(locale) ? InspgDynamicUtil.formatDateUS(drp.getReplyDate())
          : InspgDynamicUtil.formatDate(drp.getReplyDate()));
      dynReplayInfo
          .setReplyContent(drp.getReplyContent() == null ? "" : drp.getReplyContent().replaceAll("\n", "<br/>"));
      dynReplayInfo.setReplyerAvatar(drp.getReplyerAvatar());
      dynReplayInfo.setReplyerEnName(drp.getReceiverEnName());
      dynReplayInfo.setReplyerName(drp.getReplyerName());
      dynReplayInfo.setReplyerEnName(drp.getReplyerEnName());
      dynReplayInfo.setReplyerId(drp.getReplyId());
      // 评论人的主页url
      dynReplayInfo.setHomePageUrl(domainscm + "/psnweb/mobile/homepage?Des3PsnId=" + dynReplayInfo.getDes3ReplyerId());
      // 构建评论中的成果信息
      buildReplyPub(drp, dynReplayInfo);
      dynReplayInfoList.add(dynReplayInfo);
    }
    return dynReplayInfoList;
  }

  /**
   * 构建评论中的成果信息
   * 
   * @param drp
   * @param dynReplayInfo
   */
  private void buildReplyPub(DynamicReplyPsn drp, DynReplayInfo dynReplayInfo) {
    if (drp.getReplyAddResId() != null) {
      PubSnsPO pub = pubSnsDAO.get(drp.getReplyAddResId());
      if (pub != null) {
        dynReplayInfo.setReplyPubTitleZh(pub.getTitle());
        dynReplayInfo.setReplyPubTitleEn(pub.getTitle());
        // 构建成果短地址
        PubIndexUrl pubIndexUrl = pubIndexUrlDao.get(pub.getPubId());
        if (pubIndexUrl != null && StringUtils.isNotBlank(pubIndexUrl.getPubIndexUrl())) {
          dynReplayInfo.setShortUrl(domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl());
        }
        dynReplayInfo.setDes3ReplyPubId(Des3Utils.encodeToDes3(pub.getPubId().toString()));
      }
      String language = LocaleContextHolder.getLocale().getLanguage();
      if (language.equals("zh")) {
        dynReplayInfo.setReplyPubTitle(dynReplayInfo.getReplyPubTitleZh());
      } else {
        dynReplayInfo.setReplyPubTitle(dynReplayInfo.getReplyPubTitleEn());
      }
    }
  }

  public String getFullTextImg(String fileId, Long pubId) {
    String fullTextImg = scmSystemUtil.getSysDomain() + DEFAULT_PUBFULLTEXT_IMAGE;
    String pubFulltextImage = null;
    PubFulltext fullText = pubFulltextDao.get(pubId);
    if (fullText != null) {
      pubFulltextImage = fullText.getFulltextImagePath();
    }
    if (StringUtils.isBlank(pubFulltextImage)) {
      if (fullText != null && fullText.getFulltextFileId() != null) {
        fullTextImg = scmSystemUtil.getSysDomain() + DEFAULT_PUBFULLTEXT_IMAGE1;
      } else {
        fullTextImg = scmSystemUtil.getSysDomain() + DEFAULT_PUBFULLTEXT_IMAGE;
      }
    } else {
      fullTextImg = scmSystemUtil.getSysDomain() + pubFulltextImage;
    }
    return fullTextImg;
  }
}
