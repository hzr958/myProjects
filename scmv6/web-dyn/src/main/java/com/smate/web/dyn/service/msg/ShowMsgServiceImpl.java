package com.smate.web.dyn.service.msg;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.model.MailOriginalDataInfo;
import com.smate.core.base.dyn.consts.DynamicConstants;
import com.smate.core.base.email.service.MailInitDataService;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.privacy.dao.PrivacySettingsDao;
import com.smate.core.base.privacy.model.PrivacySettings;
import com.smate.core.base.project.dao.ProjectDao;
import com.smate.core.base.project.model.Project;
import com.smate.core.base.project.model.ProjectStatistics;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.pub.dao.PubIndexUrlDao;
import com.smate.core.base.pub.model.PubIndexUrl;
import com.smate.core.base.utils.constant.EmailConstants;
import com.smate.core.base.utils.constant.MsgConstants;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.psninfo.PsnInfoUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.dyn.dao.file.PsnFileShareBaseDao;
import com.smate.web.dyn.dao.file.PsnFileShareRecordDao;
import com.smate.web.dyn.dao.grp.GrpProposerDao;
import com.smate.web.dyn.dao.msg.MsgChatRelationDao;
import com.smate.web.dyn.dao.msg.MsgContentDao;
import com.smate.web.dyn.dao.msg.MsgRelationDao;
import com.smate.web.dyn.dao.pdwhpub.PubPdwhDAO;
import com.smate.web.dyn.dao.psn.FriendDao;
import com.smate.web.dyn.dao.psn.FriendTempDao;
import com.smate.web.dyn.dao.psn.RecentSelectedDao;
import com.smate.web.dyn.dao.pub.ProjectStatisticsDao;
import com.smate.web.dyn.dao.pub.PubAssignLogDao;
import com.smate.web.dyn.dao.pub.PubSnsDAO;
import com.smate.web.dyn.dao.pub.PublicationConfirmDao;
import com.smate.web.dyn.dao.rcmd.PubFulltextPsnRcmdDao;
import com.smate.web.dyn.dao.share.ShareStatisticsDao;
import com.smate.web.dyn.form.msg.MsgEmailForm;
import com.smate.web.dyn.form.msg.MsgShowForm;
import com.smate.web.dyn.form.msg.mobile.MobileMsgShowForm;
import com.smate.web.dyn.form.news.NewsForm;
import com.smate.web.dyn.model.file.PsnFileShareBase;
import com.smate.web.dyn.model.file.PsnFileShareRecord;
import com.smate.web.dyn.model.msg.MsgChatRelation;
import com.smate.web.dyn.model.msg.MsgContent;
import com.smate.web.dyn.model.msg.MsgRelation;
import com.smate.web.dyn.model.msg.MsgShowInfo;
import com.smate.web.dyn.model.psn.RecentSelected;
import com.smate.web.dyn.model.pub.PubSnsPO;
import com.smate.web.dyn.model.share.ShareStatistics;
import com.smate.web.dyn.service.news.NewsOptService;
import com.smate.web.dyn.service.statistic.StatisticsService;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * 消息显示实现服务类
 * 
 * @author zzx
 *
 */
@Transactional(rollbackOn = Exception.class)
public class ShowMsgServiceImpl extends BuildMsgInfoBase implements ShowMsgService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Resource
  private MsgChatRelationDao msgChatRelationDao;

  @Resource
  private MsgContentDao msgContentDao;

  @Resource
  private MsgRelationDao msgRelationDao;

  @Resource
  private PersonDao personDao;

  @Autowired
  private RestTemplate restTemplate;

  @Value("${initOpen.restful.url}")
  private String openResfulUrl;

  @Autowired
  private MailInitDataService mailInitDataService;

  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;

  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;

  @Autowired
  private RecentSelectedDao recentSelectedDao;

  @Resource(name = "freemarkerConfiguration")
  private Configuration freemarkerConfiguration;

  @Value("${domainscm}")
  private String domainscm;

  @Autowired
  private FriendTempDao friendTempDao;

  @Autowired
  private GrpProposerDao grpProposerDao;

  @Autowired
  private FriendDao friendDao;

  @Autowired
  private PublicationConfirmDao publicationConfirmDao;

  @Autowired
  private PubFulltextPsnRcmdDao pubFulltextPsnRcmdDao;

  @Autowired
  private FileDownloadUrlService fileDownUrlService;
  @Autowired
  private PsnFileShareBaseDao psnFileShareBaseDao;
  @Autowired
  private PsnFileShareRecordDao psnFileShareRecordDao;
  @Autowired
  private PubAssignLogDao pubAssignLogDao;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private ProjectStatisticsDao projectStatisticsDao;
  @Autowired
  private StatisticsService statisticsService;
  @Autowired
  private ShareStatisticsDao shareStatisticsDao;
  @Autowired
  private ProjectDao projectDao;
  @Autowired
  private UserDao userDao;
  @Value(value = "${sendEmail.restful.url}")
  private String sendRestfulUrl;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private PrivacySettingsDao privacySettingsDao;
  // 各消息类型服务类 使用xmL注入
  private Map<String, BuildMsgInfoService> buildMsgInfoServiceMap;
  @Autowired
  private NewsOptService newsOptService;

  public Map<String, BuildMsgInfoService> getBuildMsgInfoServiceMap() {
    return buildMsgInfoServiceMap;
  }

  public void setBuildMsgInfoServiceMap(Map<String, BuildMsgInfoService> buildMsgInfoServiceMap) {
    this.buildMsgInfoServiceMap = buildMsgInfoServiceMap;
  }

  @Override
  public void getMsgInfo(MsgShowForm form) throws Exception {
    List<MsgRelation> msgListByType = null;
    if (MsgConstants.MSG_TYPE_SMATE_INSIDE_LETTER.equals(form.getMsgType())) {// 站内信单独查询
      msgListByType = msgRelationDao.getMsgListByChat(form);
    } else if (MsgConstants.MSG_TYPE_PUBFULLTEXT_REQUEST.equals(form.getMsgType())) {// 全文请求
      msgListByType = msgRelationDao.getMsgListByFulltextRequest(form);
    } else {// 其他消息类型
      if ("mobile".equals(form.getPageSource())) {
        msgListByType = msgRelationDao.getMidMsgListByType(form);
      } else {
        msgListByType = msgRelationDao.getMsgListByType(form);
      }
    }
    if (msgListByType != null && msgListByType.size() > 0) {
      form.setMsgShowInfoList(new ArrayList<MsgShowInfo>());
      for (MsgRelation m : msgListByType) {
        BuildMsgInfoService buildMsgInfoService = buildMsgInfoServiceMap.get(m.getType().toString());
        if (buildMsgInfoService != null) {
          try {
            buildMsgInfoService.buildMsgShowInfo(form, m);
          } catch (Exception e) {
            logger.error("解析消息出错,ContentId()=" + m.getContentId(), e);
          }
        }
      }
      // 情况数据
      form.setDupReqPubFulltext(new HashMap<>());
    }
  }

  @Override
  public Map<String, String> countUnreadMsg(MsgShowForm form) throws Exception {
    form.getResultMap().putAll(msgRelationDao.countUnreadMsg(form));
    return null;
  }

  @Override
  public void sendMsg(MsgShowForm form) throws Exception {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null || currentUserId == 0L) {
      throw new Exception("没有登录不能发送消息");
    }
    if (StringUtils.isNoneBlank(form.getReceiverIds())) {
      // 调open接口发送消息
      Map<String, Object> map = buildSendMsgParam(form);
      Object obj = restTemplate.postForObject(this.openResfulUrl, map, Object.class);
      sendMsgCallBack(obj, form);
    }
    // 文件消息要添加分享记录
    if (form.getSmateInsideLetterType().equalsIgnoreCase("file")) {
      addPsnFileShareRecord(form);
    } else if (form.getSmateInsideLetterType().equalsIgnoreCase("prj")) {// 当为发送项目内容给联系人时，代表分享项目的操作此时要记录分享项目记录，
      String des3PrjId = form.getDes3PrjId();// 获取当前MsgShowForm对象中的加密后的prjId字符des3PrjId，并解码为数据库中对应的prjId
      Long prjId = Long.parseLong(Des3Utils.decodeFromDes3(des3PrjId));
      Project project = projectDao.get(prjId);
      try {
        String allReceivers = (StringUtils.isBlank(form.getReceiverIds()) ? "" : form.getReceiverIds()) + ","
            + (StringUtils.isBlank(form.getReceiverEmails()) ? "" : form.getReceiverEmails());// 邮件分享和psnId分享的总数
        // 项目分享时，可以选择多个人进行分享，当选中多人进行分享时，对应插入多条数据
        String[] receivers = allReceivers.split(",");
        if (receivers.length > 0) {
          int receiverNum = 0;
          for (String receiver : receivers) {
            if (StringUtils.isNotBlank(receiver)) {
              receiverNum++;
            }
          }
          addShareRecord(form.getPsnId(), project.getPsnId(), prjId, 4);// ResPsnId
          addPrjShareStatistics(prjId, receiverNum);
        }
      } catch (Exception e) {
        logger.error("项目分享回调异常,prjId=" + prjId, e);
      }

    } else if (form.getSmateInsideLetterType().equalsIgnoreCase("news")) {
      addNewsShareStatistic(form, currentUserId);
    } else if (form.getSmateInsideLetterType().equalsIgnoreCase("ins")) {
      addInsShareStatistic(form, "0", currentUserId);
    }
  }

  public void addInsShareStatistic(MsgShowForm form, String platform, Long currentUserId) throws ServiceException {
    Long insId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3InsId()));
    try {
      if (StringUtils.isNotBlank(form.getReceiverIds())) {
        String[] split = form.getReceiverIds().split(",");
        for (String s : split) {
          statisticsService.addInsRecord(currentUserId, platform, insId);
        }
      }
    } catch (Exception e) {
      logger.error("调用机构社交化分享接口出错， 机构ID : " + insId + "psnId:" + currentUserId, e);
      throw new ServiceException();
    }
  }

  /**
   * 新增新聞分享統計數
   * 
   * @param form
   * @param currentUserId
   */
  public void addNewsShareStatistic(MsgShowForm form, Long currentUserId) {
    if (StringUtils.isNotBlank(form.getReceiverIds())) {
      String[] split = form.getReceiverIds().split(",");
      for (String s : split) {
        long receiver = NumberUtils.toLong(s);
        NewsForm form2 = new NewsForm();
        form2.setNewsId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3NewsId())));
        form2.setPsnId(currentUserId);
        form2.setContent(form.getMsg());
        form2.setBeSharedId(receiver);
        form2.setPlatform(2); // 好友
        newsOptService.addNewsShare(form2);
      }
    }
  }

  /**
   * 增加项目的分享数量
   * 
   * @param prjId 对应的项目id
   * @param num 要增加分享数的数量
   */
  private void addPrjShareStatistics(Long prjId, int num) {

    ProjectStatistics ps = projectStatisticsDao.get(prjId);
    if (ps == null) {
      ps = new ProjectStatistics();
      ps.setProjectId(prjId);
      ps.setShareCount(0);
    }
    if (ps.getShareCount() == null) {
      ps.setShareCount(0);
    }
    ps.setShareCount(ps.getShareCount() + num);
    projectStatisticsDao.save(ps);
  }

  /**
   * 将分享的记录增加到分享记录表中
   * 
   * @param psnId
   * @param sharePsnId
   * @param actionKey
   * @param actionType
   * @throws DynException
   */
  private void addShareRecord(Long psnId, Long sharePsnId, Long actionKey, Integer actionType) throws DynException {

    try {
      if (!DynamicConstants.SHARE_TYPE_MAP.containsKey(actionType)) {
        logger.warn("分享统计，分享类型actionType=" + actionType + "的记录，不需要保存");
        return;
      }
      ShareStatistics shareStatistics = new ShareStatistics();
      shareStatistics.setPsnId(psnId);
      shareStatistics.setSharePsnId(sharePsnId);
      shareStatistics.setActionKey(actionKey);
      shareStatistics.setActionType(actionType);
      Date nowDate = new Date();
      shareStatistics.setCreateDate(nowDate);
      shareStatistics.setFormateDate(DateUtils.getDateTime(nowDate));
      shareStatistics.setIp(Struts2Utils.getRemoteAddr());
      shareStatisticsDao.save(shareStatistics);
    } catch (Exception e) {
      logger.error("保存分享记录出错！PsnId=" + psnId + " sharePsnId=" + sharePsnId + " actionKey=" + actionKey + " actionType"
          + actionType, e);
      throw new DynException(e);
    }
  }

  /**
   * App 暂时未获取currentUserId
   */
  @Override
  public void appSendMsg(MsgShowForm form) throws Exception {
    // 调open接口发送消息
    Long currentUserId = SecurityUtils.getCurrentUserId();
    Map<String, Object> map = buildSendMsgParam(form);
    Object obj = restTemplate.postForObject(this.openResfulUrl, map, Object.class);
    sendMsgCallBack(obj, form);
  }

  /**
   * 构建发消息需要的参数
   *
   * @param form
   * @return
   */
  private Map<String, Object> buildSendMsgParam(MsgShowForm form) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> dataMap = new HashMap<String, Object>();
    map.put("openid", "99999999");
    map.put("token", "00000000msg77msg");
    dataMap.put(MsgConstants.MSG_TYPE, form.getMsgType());
    dataMap.put(MsgConstants.MSG_SENDER_ID, form.getPsnId());
    checkReceiverIds(dataMap, form.getReceiverIds(), form.getPsnId());
    dataMap.put(MsgConstants.MSG_GRP_ID, form.getGrpId());
    dataMap.put(MsgConstants.MSG_PUB_ID, form.getPubId());
    dataMap.put(MsgConstants.MSG_FILE_ID, form.getFileId());
    dataMap.put(MsgConstants.MSG_DES3_PRJ_ID, form.getDes3PrjId());
    dataMap.put(MsgConstants.MSG_DES3_NEWS_ID, form.getDes3NewsId());
    dataMap.put(MsgConstants.MSG_DES3_PSN_ID, form.getDes3SharePsnId());
    dataMap.put(MsgConstants.MSG_DES3_INS_ID, form.getDes3InsId());
    dataMap.put(MsgConstants.MSG_CONTENT, form.getContent());
    dataMap.put(MsgConstants.MSG_RELATION_ID, form.getMsgRelationId());
    if (MsgConstants.MSG_TYPE_SMATE_INSIDE_LETTER.equals(form.getMsgType())) {
      dataMap.put(MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE, form.getSmateInsideLetterType());
      dataMap.put(MsgConstants.MSG_BELONG_PERSON, form.getBelongPerson());
    }
    map.put("data", JacksonUtils.mapToJsonStr(dataMap));
    form.setReceiverIds(dataMap.get(MsgConstants.MSG_RECEIVER_IDS).toString());
    return map;
  }

  private void checkReceiverIds(Map<String, Object> dataMap, String receiverIds, Long psnId) throws Exception {
    if (StringUtils.isBlank(receiverIds)) {
      return;
    }
    StringBuilder sb = new StringBuilder("");
    String[] split = receiverIds.split(",");
    if (split != null && split.length > 0) {
      for (String s : split) {
        if (NumberUtils.isNumber(s)) {
          this.updatePsnRecent(psnId, NumberUtils.toLong(s));
          sb.append(s + ",");
        } else {
          String decodeFromDes3 = Des3Utils.decodeFromDes3(s);
          this.updatePsnRecent(psnId, NumberUtils.toLong(decodeFromDes3));
          sb.append(decodeFromDes3 + ",");
        }
      }
    }
    dataMap.put(MsgConstants.MSG_RECEIVER_IDS, sb.toString());
  }

  /**
   * 发送消息回调处理
   * 
   * @param obj
   * @param form
   */
  @SuppressWarnings("unchecked")
  private void sendMsgCallBack(Object obj, MsgShowForm form) throws Exception {
    Map<String, Object> resultMap = JacksonUtils.jsonToMap(obj.toString());
    Long msgRelationId = 0L;
    if (resultMap != null && "success".equals(resultMap.get("status"))) {
      List<Map<String, Object>> result = (List<Map<String, Object>>) resultMap.get("result");
      if (result != null && result.size() > 0) {
        if (result.get(0).get("notPermissionPsnIds") != null) {
          form.setNotPermissionPsnIds((List<Long>) result.get(0).get("notPermissionPsnIds"));
        } else {
          msgRelationId = NumberUtils.toLong(result.get(0).get("msgRelationId").toString());
        }
      }
    }
    form.setMsgRelationId(msgRelationId);
  }

  /**
   * 删除消息
   * 
   * @param form
   */
  @Override
  public void delMsg(MsgShowForm form) throws Exception {
    String msgRelationIds = form.getMsgRelationIds();
    if (SecurityUtils.getCurrentUserId() != null && SecurityUtils.getCurrentUserId() != 0L
        && StringUtils.isNotBlank(msgRelationIds)) {
      String[] split = msgRelationIds.split(",");
      if (split != null && split.length > 0) {
        for (String s : split) {
          if (StringUtils.isNotBlank(s) && NumberUtils.isNumber(s)) {
            msgRelationDao.delMsg(NumberUtils.toLong(s), form.getPsnId());
          }
        }
      }
    }

  }

  /**
   * 标记消息为已读
   * 
   * @param form
   */
  @Override
  public void setReadMsg(MsgShowForm form) throws Exception {
    Long userId = SecurityUtils.getCurrentUserId();
    if (userId == null || userId == 0L) {
      return;
    }
    if (1 == form.getReadMsgType()) {
      msgRelationDao.setReadMsg(form.getMsgRelationId(), userId);
    } else if (2 == form.getReadMsgType()) {
      msgRelationDao.setReadchatMsg(form.getSenderId(), userId);
    } else if (3 == form.getReadMsgType()) {
      msgRelationDao.setReadAllMsg(userId);
    }
  }

  @Override
  public void getChatPsnList(MsgShowForm form) throws Exception {
    List<MsgChatRelation> msgChatRelationList =
        msgChatRelationDao.getChatPsnList(form.getPsnId(), form.getPage(), form.getSearchKey());
    if (msgChatRelationList != null && msgChatRelationList.size() > 0) {
      form.setMsgShowInfoList(new ArrayList<MsgShowInfo>());
      for (MsgChatRelation m : msgChatRelationList) {
        buildChatPsnInfo(form, m);
      }
    }
  }

  /**
   * 构建回话人员列表信息
   * 
   * @param form
   * @param m
   * @throws Exception
   */
  private void buildChatPsnInfo(MsgShowForm form, MsgChatRelation m) throws Exception {
    if (m.getUpdateDate() == null) {
      m.setUpdateDate(new Date());
    }
    MsgShowInfo msi = new MsgShowInfo();
    msi.setMsgChatRelationId(m.getId());
    msi.setCreateDate(m.getUpdateDate() == null ? new Date() : m.getUpdateDate());
    if (isNow(m.getUpdateDate())) {
      msi.setShowDate(new SimpleDateFormat("HH:mm").format(m.getUpdateDate()));
    } else {
      msi.setShowDate(new SimpleDateFormat("MM-dd").format(m.getUpdateDate()));
    }
    msi.setSenderId(m.getSenderId());
    // app 列表添加是否是我的好友
    msi.setIsFriend(friendDao.isFriend(form.getPsnId(), m.getSenderId()).intValue() > 0 ? 1 : 0);
    msi.setMsgChatCount(msgRelationDao.getChatMsgCount(m.getSenderId(), form.getPsnId()));
    msi.setMsgChatReadCount(msgRelationDao.getReadChatMsgCount(m.getSenderId(), form.getPsnId()));
    msi.setContentNewest(m.getContentNewest());
    Person senderInfo = personDao.findPersonBase(m.getSenderId());
    if (senderInfo == null) {// 发送者不存在，直接返回。
      return;
    }
    buildPsnInfo(msi, senderInfo, "sender");
    PrivacySettings ps = privacySettingsDao.loadPsByPsnId(m.getSenderId(), "sendMsg");
    if (ps != null) {
      msi.setSendMsg(ps.getPermission());
    } else {
      msi.setSendMsg(0);
    }
    form.getMsgShowInfoList().add(msi);
  }

  private boolean isNow(Date date) {
    // 当前时间
    Date now = new Date();
    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
    // 获取今天的日期
    String nowDay = sf.format(now);
    // 对比的时间
    String day = sf.format(date);
    return day.equals(nowDay);
  }

  /**
   * 无
   */
  @Override
  public void buildMsgShowInfo(MsgShowForm form, MsgRelation m) throws Exception {
    // TODO Auto-generated method stub
  }

  /**
   * 全文请求接受/忽略 1=同意、2=拒绝/忽略
   */
  @Deprecated
  @Override
  public void optFulltextRequest(MsgShowForm form) throws Exception {

    MsgRelation msgRelation = msgRelationDao.get(form.getMsgRelationId());
    if (msgRelation == null) {
      throw new Exception("msgRelationId查询不到msgRelation对象");
    } else if (!form.getPsnId().equals(msgRelation.getReceiverId())) {
      // System.out.println("不是当前接受人员psnId=" + form.getPsnId() +
      // ";receiveId=" + msgRelation.getReceiverId());
      return;
    }
    if (form.getDealStatus() == 1) {
      msgRelation.setDealStatus(1);
      Map<String, Object> map = new HashMap<String, Object>();
      // 发送邮件
      Person sender = personDao.findPsnInfoForEmail(msgRelation.getReceiverId());
      Person receiver = personDao.findPsnInfoForEmail(msgRelation.getSenderId());
      try {
        buildSendEmailMapInfo(map, form.getPubId(), sender, receiver);
        mailInitDataService.saveMailInitData(map);
        // 全文站内信
        Map<String, Object> paramMap = new HashMap<String, Object>();
        buildSendFulltextReplyMap(form, sender, receiver, paramMap);
        Object obj = restTemplate.postForObject(this.openResfulUrl, paramMap, Object.class);
        logger.info(obj.toString());
      } catch (Exception e) {
        throw new Exception("全文请求同意，发送邮件失败 psnId=" + form.getPsnId() + " msgRelationId = " + form.getMsgRelationId());
      }

    } else {
      msgRelation.setDealStatus(2);
    }
    msgRelation.setDealDate(new Date());
    msgRelationDao.save(msgRelation);
  }

  /**
   * @param form
   * @param sender
   * @param receiver
   * @param paramMap
   */
  private void buildSendFulltextReplyMap(MsgShowForm form, Person sender, Person receiver,
      Map<String, Object> paramMap) {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    paramMap.put("openid", "99999999");
    paramMap.put("token", "00000000" + "msg77msg");
    dataMap.put("senderId", "" + sender.getPersonId());
    dataMap.put("receiverIds", "" + receiver.getPersonId());
    dataMap.put("msgType", "7");
    dataMap.put("smateInsideLetterType", "fulltext");
    dataMap.put("pubId", "" + form.getPubId());
    dataMap.put("belongPerson", "true");
    paramMap.put("data", JacksonUtils.mapToJsonStr(dataMap));
  }

  @Override
  public void delMsgChatRelation(MsgShowForm form) throws Exception {
    MsgChatRelation msgChatRelation = msgChatRelationDao.get(form.getMsgChatRelationId());
    if (SecurityUtils.getCurrentUserId().equals(msgChatRelation != null ? msgChatRelation.getReceiverId() : "")) {
      msgChatRelation.setStatus(1);
    }
  }

  /**
   * 
   * @param person
   * @param language zh=中文
   * @return
   */
  String getPersonName(Person person, String language) {
    if ("zh".equalsIgnoreCase(language) || "zh_CN".equalsIgnoreCase(language)) {
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

  /**
   * 
   * 
   * @param map
   */
  public void buildSendEmailMapInfo(Map<String, Object> map, Long pubId, Person sender, Person receiver)
      throws Exception {
    String psnShortUrl = "";
    String pubShortUrl = "";
    // 全文请求使用新模板
    if (sender == null || receiver == null || pubId == null) {
      throw new Exception("构建全文回复，邮件参数对象为空" + this.getClass().getName());
    }
    String language = "";
    language = receiver.getEmailLanguageVersion();
    if (StringUtils.isBlank(language)) {
      language = LocaleContextHolder.getLocale().toString();
    }
    PubSnsPO pubSns = pubSnsDAO.get(pubId);
    if (pubSns != null) {
      map.put("enPubTitle", pubSns.getTitle());
      map.put("zhPubTitle", pubSns.getTitle());
      map.put("authorNames", pubSns.getAuthorNames());
      map.put("briefDesc", pubSns.getBriefDesc());
      map.put("briefDescEn", pubSns.getBriefDesc());
    }
    // 发件人头衔
    if (StringUtils.isNotBlank(sender.getTitolo())) {
      map.put("frdTitlo", sender.getTitolo());
    }
    map.put("frdName", getPersonName(sender, "zh"));
    map.put("frdEnName", getPersonName(sender, "en"));
    // 主题
    String enTitle = "Reply to your fulltext request";
    String title = "回复你的全文请求";
    map.put("enTitle", enTitle);
    map.put("title", title);

    // 发件人主页
    PsnProfileUrl profileUrl = psnProfileUrlDao.get(sender.getPersonId());
    if (profileUrl != null && StringUtils.isNotBlank(profileUrl.getPsnIndexUrl())) {
      psnShortUrl = this.domainscm + "/" + ShortUrlConst.P_TYPE + "/" + profileUrl.getPsnIndexUrl();
    }
    map.put("frdUrl", psnShortUrl);
    // 成果详情
    PubIndexUrl pubIndexUrl = pubIndexUrlDao.get(pubId);
    if (pubIndexUrl != null && StringUtils.isNotBlank(pubIndexUrl.getPubIndexUrl())) {
      pubShortUrl = this.domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl();
    }
    map.put("pubsUrl", pubShortUrl);

    // 下载全文按钮跳转的链接
    // SCM-14409 hcj 采用统一短地址获取入口
    String fullTextShortUrl = fileDownUrlService.getShortDownloadUrl(FileTypeEnum.SNS_FULLTEXT, pubId);
    map.put("fulltextUrl", fullTextShortUrl);
    map.put("domainUrl", domainscm);

    map.put("mailSetUrl", this.domainscm + "/scmwebsns/user/setting/getMailTypeList");
    map.put("viewUrl", this.domainscm + "/scmwebsns/msgbox/smsMain?locale=zh_CN");
    if ("zh".equals(language) || "zh_CN".equals(language)) {
      map.put("psnName", getPersonName(receiver, "zh"));
      map.put(EmailConstants.EMAIL_SUBJECT_KEY, map.get("frdName") + "为你上传了论文全文" + ":" + map.get("zhPubTitle"));
      map.put(EmailConstants.EMAIL_TEMPLATE_KEY, "person_reply_fulltext_request_zh_CN.ftl");
    } else {
      map.put("psnName", getPersonName(receiver, "en"));
      map.put(EmailConstants.EMAIL_SUBJECT_KEY,
          map.get("frdEnName") + " uploaded full-text for you" + "：" + map.get("enPubTitle"));
      map.put(EmailConstants.EMAIL_TEMPLATE_KEY, "person_reply_fulltext_request_en_US.ftl");
    }
    map.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, receiver.getPersonId());
    map.put(EmailConstants.EMAIL_SENDER_PSNID_KEY, sender.getPersonId());
    map.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, receiver.getEmail());
  }

  @Override
  public void getChatPsnLastRecord(MsgShowForm form) throws Exception {

    List<MsgChatRelation> msgChatRelationList =
        msgChatRelationDao.getChatPsnLastRecode(form.getChatPsnId(), form.getPsnId());
    if (msgChatRelationList != null && msgChatRelationList.size() > 0) {
      form.setMsgShowInfoList(new ArrayList<MsgShowInfo>());
      for (MsgChatRelation m : msgChatRelationList) {
        buildChatPsnInfo(form, m);
      }
    }
  }

  @Override
  public void delChatMsg(MsgShowForm form) throws Exception {
    Long msgRelationId = form.getMsgRelationId();
    if (SecurityUtils.getCurrentUserId() != null && SecurityUtils.getCurrentUserId() != 0L && msgRelationId != null
        && msgRelationId != 0L) {
      msgRelationDao.delChatMsg(msgRelationId, form.getPsnId());
      MsgRelation msg = msgRelationDao.get(msgRelationId);
      Long senderId = msg.getSenderId();
      Long receiverId = msg.getReceiverId();
      Long newestContentId = msgRelationDao.getNewestContentId(senderId, receiverId);
      msgChatRelationDao.updateContent(senderId, receiverId, msgContentDao.getMsgContentByChat(newestContentId));
    }
  }

  @Override
  public void updatePsnRecent(Long senderId, Long recevierId) throws Exception {
    RecentSelected recentSelected = recentSelectedDao.getRecentSelected(senderId, recevierId);
    if (recentSelected == null) {
      recentSelected = new RecentSelected();
      recentSelected.setPsnId(senderId);
      recentSelected.setSelectedPsnId(recevierId);
      recentSelected.setSelectedDate(new Date());
    } else {
      recentSelected.setSelectedDate(new Date());
    }
  }

  @Override
  public void createMsgChat(MsgShowForm form) throws Exception {
    Long userId = SecurityUtils.getCurrentUserId();
    if (userId != null && userId != 0L) {
      MsgChatRelation m = msgChatRelationDao.getPsnLastRecode(form.getReceiverId(), userId);
      if (m == null) {
        m = new MsgChatRelation();
        m.setReceiverId(userId);
        m.setSenderId(form.getReceiverId());
        m.setStatus(0);
        m.setUpdateDate(new Date());
      } else {
        m.setStatus(0);
        m.setUpdateDate(new Date());
      }
      msgChatRelationDao.save(m);
    }
  }

  @Override
  public void menuMsgPrompt(MsgShowForm form) throws Exception {
    Long userId = SecurityUtils.getCurrentUserId();
    form.setResultMap(new HashMap<String, String>());
    if (userId != null && userId != 0L) {
      form.getResultMap().put("pCount", friendTempDao.getTempPsnCount(userId).toString());
      Long grpInvCount = grpProposerDao.getGrpInvCount(userId);
      Long grpReqCount = grpProposerDao.getGrpReqCount(userId);
      form.getResultMap().put("gCount", (grpInvCount + grpReqCount) + "");
    } else {
      form.getResultMap().put("pCount", "0");
      form.getResultMap().put("gCount", "0");
    }
  }

  @Override
  public void getMobileMsgInfo(MobileMsgShowForm form) throws Exception {
    List<MsgRelation> msgListByType = null;
    if (MsgConstants.MSG_TYPE_SMATE_INSIDE_LETTER.equals(form.getMsgType())) {// 站内信类型
      msgListByType = msgRelationDao.getMobileMsgList(form);
    }
    if (msgListByType != null && msgListByType.size() > 0) {
      for (MsgRelation msgRelation : msgListByType) {
        BuildMsgInfoService buildMsgInfoService = buildMsgInfoServiceMap.get(msgRelation.getType().toString());
        if (buildMsgInfoService != null) {
          buildMsgInfoService.buildMobileMsgShowInfo(form, msgRelation);
        }

      }
    }
  }

  @Override
  public int mobileSetReadMsg(MobileMsgShowForm form) throws Exception {
    String msgRelationIds = form.getMsgRelationIds();
    if (StringUtils.isNotBlank(msgRelationIds)) {
      String[] split = msgRelationIds.split(",");
      if (split != null && split.length > 0) {
        for (String msgId : split) {
          if (StringUtils.isNotBlank(msgId) && NumberUtils.isNumber(msgId)) {
            form.setMsgRelationId(NumberUtils.toLong(msgId));
            return msgRelationDao.mobileSetMsg(form);
          }
        }
      }
    }
    return 0;
  }

  @Override
  public void mobileDelMsg(MobileMsgShowForm form) throws Exception {
    String msgRelationIds = form.getMsgRelationIds();
    if (StringUtils.isNotBlank(msgRelationIds)) {
      String[] split = msgRelationIds.split(",");
      if (split != null && split.length > 0) {
        for (String msgId : split) {
          if (StringUtils.isNotBlank(msgId) && NumberUtils.isNumber(msgId)) {
            msgRelationDao.delMsg(NumberUtils.toLong(msgId), form.getPsnId());
          }
        }
      }
    }
  }

  @Override
  public Long mobileCountUnreadMsg(MobileMsgShowForm form) throws Exception {
    return msgRelationDao.mobileGetUnreadMsg(form.getPsnId());
  }

  @Override
  public Long getPubConfirmCount(Long psnId) {
    List<Long> confirmPubIds = pubAssignLogDao.queryPubConfirmCount(psnId);
    return pubPdwhDAO.getPubCount(confirmPubIds);
  }

  @Override
  public Long getFulltextCount(Long psnId) {
    return pubFulltextPsnRcmdDao.queryRcmdFulltextCount(psnId);
  }

  @Override
  public Map<String, String> countReadMsg(MsgShowForm form) throws Exception {
    form.getResultMap().putAll(msgRelationDao.countReadMsg(form));
    return null;
  }

  @Override
  public Map<String, String> countChatPsn(MsgShowForm form) throws Exception {
    msgChatRelationDao.getChatPsnCount(form.getPsnId(), form.getSearchKey());
    form.getResultMap().put("chatcount",
        msgChatRelationDao.getChatPsnCount(form.getPsnId(), form.getSearchKey()).toString());
    return null;
  }

  @Override
  public void getSearchChatPsnList(MsgShowForm form) throws Exception {
    List<MsgChatRelation> msgChatRelationList = msgChatRelationDao.getAllChatPsnList(form.getPsnId());
    if (msgChatRelationList != null && msgChatRelationList.size() > 0) {
      form.setMsgShowInfoList(new ArrayList<MsgShowInfo>());
      for (MsgChatRelation m : msgChatRelationList) {
        buildSearchChatPsnInfo(form, m);
      }
    }

  }

  private void buildSearchChatPsnInfo(MsgShowForm form, MsgChatRelation m) {
    Page page = new Page();
    Date date = new Date();
    List<MsgRelation> list =
        msgRelationDao.getMsgListByChatForSearch(m.getSenderId(), form.getPsnId(), date, form.getSearchKey(), page);
    Long msgcount = 0L;
    if (list != null && list.size() > 0) {
      for (MsgRelation mr : list) {
        MsgContent msgContent = msgContentDao.get(mr.getContentId());
        if (msgContent != null && JacksonUtils.isJsonString(msgContent.getContent())) {
          MsgShowInfo msi = (MsgShowInfo) JacksonUtils.jsonObject(msgContent.getContent(), MsgShowInfo.class);
          if (doSearchContent(form, msi)) {
            msgcount++;
          }
        }
      }

    }
    if (msgcount > 0) {
      MsgShowInfo msi = new MsgShowInfo();
      msi.setSearchCount(msgcount);
      msi.setMsgChatRelationId(m.getId());
      msi.setCreateDate(m.getUpdateDate() == null ? new Date() : m.getUpdateDate());
      if (isNow(m.getUpdateDate())) {
        msi.setShowDate(new SimpleDateFormat("HH:mm").format(m.getUpdateDate()));
      } else {
        msi.setShowDate(new SimpleDateFormat("MM-dd").format(m.getUpdateDate()));
      }
      msi.setSenderId(m.getSenderId());
      // app 列表添加是否是我的好友
      // msi.setIsFriend(friendDao.isFriend(form.getPsnId(),
      // m.getSenderId()).intValue() > 0 ? 1 : 0);
      // msi.setMsgChatCount(msgRelationDao.getChatMsgCount(m.getSenderId(),
      // form.getPsnId()));
      // msi.setMsgChatReadCount(msgRelationDao.getReadChatMsgCount(m.getSenderId(),
      // form.getPsnId()));
      msi.setContentNewest(msgcount + "条与<span style='color:red!important'>" + form.getSearchKey() + "</span>相关聊天记录");
      Person senderInfo = personDao.findPersonBase(m.getSenderId());
      buildPsnInfo(msi, senderInfo, "sender");
      form.getMsgShowInfoList().add(msi);
    }
  }

  private boolean doSearchContent(MsgShowForm form, MsgShowInfo msi) {
    String type = msi.getSmateInsideLetterType();
    String language = form.getLanguage();
    String content = "";
    boolean status = false;
    switch (type) {
      case "text":
        content = msi.getContent();
        if (StringUtils.isNotBlank(content) && content.indexOf(form.getSearchKey()) != -1) {
          status = true;
        }
        break;
      case "pub":
        if ("zh".equals(language)) {
          content = msi.getPubTitleZh();
          if (StringUtils.isNotBlank(content) && content.indexOf(form.getSearchKey()) != -1) {
            status = true;
          }
        } else {
          content = msi.getPubTitleEn();
          if (StringUtils.isNotBlank(content) && content.indexOf(form.getSearchKey()) != -1) {
            status = true;
          }
        }
        break;
      case "file":
        content = msi.getFileName();
        if (StringUtils.isNotBlank(content) && content.indexOf(form.getSearchKey()) != -1) {
          status = true;
        }
        break;
      case "fund":
        if ("zh".equals(language)) {
          content = msi.getFundZhTitle();
          if (StringUtils.isNotBlank(content) && content.indexOf(form.getSearchKey()) != -1) {
            status = true;
          }
        } else {
          content = msi.getFundEnTitle();
          if (StringUtils.isNotBlank(content) && content.indexOf(form.getSearchKey()) != -1) {
            status = true;
          }
        }
        break;
      default:
        break;
    }
    return status;
  }

  /**
   * 发送站内信消息 邮件 文件，文本，成果
   * 
   * @param form
   */
  public void sendInsideMsgEmail(MsgShowForm form) {
    Map<String, Object> map = new HashMap<>();
    Person sender = null;
    Person receiver = null;
    List<Long> receiveIdList = new ArrayList<>();
    parseReceiverIds(form, receiveIdList);
    try {
      sender = personDao.get(form.getPsnId());
      for (Long receiverId : receiveIdList) {
        receiver = personDao.get(receiverId);
        // 如果当天发送了，就要在发送邮件
        Boolean flag = msgRelationDao.hasSendEmail(sender.getPersonId(), receiverId);
        if (flag) {
          logger.debug("今天已经发送了，站内信邮件通知，sender=" + sender.getPersonId() + "  receiver=" + receiverId);
          continue;
        }
        restSendInsideMsgEmail(form, sender, receiver);
        // 更新今天发送邮件的，记录
        msgRelationDao.updateSendEmailStatus(form.getMsgRelationId());
      }
    } catch (Exception e) {
      logger.error("发送站内信消息邮件异常：psnId=" + form.getPsnId(), e);
    }
  }

  @Override
  public void sendPrjShareEmail(MsgShowForm form) {
    Person sender = null;
    Person receiver = null;
    List<Long> receiveIdList = new ArrayList<>();
    List<String> receiverEmailsList = new ArrayList<>();
    parseReceiverEmails(form, receiverEmailsList);
    parseReceiverIds(form, receiveIdList);
    try {
      sender = personDao.get(form.getPsnId());
      for (Long receiverId : receiveIdList) {
        receiver = personDao.get(receiverId);
        doSendMail(form, sender, receiver);// 发送分享项目通知邮件
      }
      for (String email : receiverEmailsList) {// 分享给站外人员直接发送
        receiver = new Person();// 设置默认接收人
        receiver.setPersonId(0L);
        receiver.setEmail(email);
        doSendMail(form, sender, receiver);// 直接发送通知邮件
      }
    } catch (Exception e) {
      logger.error("发送站内信消息邮件异常：psnId=" + form.getPsnId(), e);
    }

  }

  /**
   * 发送项目分享通知邮件
   * 
   * @param form
   * @param sender
   * @param receiver
   * @throws Exception
   */
  private void doSendMail(MsgShowForm form, Person sender, Person receiver) throws Exception {
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, Object> mailData = new HashMap<String, Object>();
    String languageVersion =
        StringUtils.isNotBlank(receiver.getEmailLanguageVersion()) ? receiver.getEmailLanguageVersion()
            : sender.getEmailLanguageVersion();
    if (StringUtils.isBlank(languageVersion)) {
      languageVersion = LocaleContextHolder.getLocale().toString();
    }
    String recvName = PsnInfoUtils.getPersonName(receiver, languageVersion);
    String psnName = PsnInfoUtils.getPersonName(sender, languageVersion);

    // 跟踪链接 根据key放置到模板中 所有的链接不再需要放到mailData中
    // 发件人主页
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(sender.getPersonId());
    String senderPersonUrl = "";
    if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
      senderPersonUrl = domainscm + "/" + ShortUrlConst.P_TYPE + "/" + psnProfileUrl.getPsnIndexUrl();
    }
    String prjDetail = domainscm + "/prjweb/project/detailsshow?des3PrjId=" + form.getDes3PrjId();
    List<String> linkList = new ArrayList<String>();
    MailLinkInfo l1 = new MailLinkInfo();
    l1.setKey("domainUrl");
    l1.setUrl(domainscm);
    l1.setUrlDesc("科研之友首页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));
    MailLinkInfo l2 = new MailLinkInfo();
    l2.setKey("senderPersonUrl");
    l2.setUrl(senderPersonUrl);
    l2.setUrlDesc("发件人主页链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l2));
    MailLinkInfo l3 = new MailLinkInfo();
    l3.setKey("viewUrl");
    l3.setUrl(domainscm + "/dynweb/showmsg/msgmain?model=chatMsg");
    l3.setUrlDesc("站内信链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l3));
    MailLinkInfo l4 = new MailLinkInfo();
    l4.setKey("prjDetail");
    l4.setUrl(prjDetail);
    l4.setUrlDesc("项目详情");
    linkList.add(JacksonUtils.jsonObjectSerializer(l4));
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));

    Long prjId = Long.valueOf(Des3Utils.decodeFromDes3(form.getDes3PrjId()));
    Project project = projectDao.get(prjId);
    if (project == null) {
      return;
    }
    String zhTitle = project.getZhTitle();
    String enTitle = project.getEnTitle();
    // 如果中文标题为空那么就取英文 反过来一样
    if (StringUtils.isBlank(zhTitle) || StringUtils.isBlank(enTitle)) {
      if (StringUtils.isBlank(zhTitle)) {
        zhTitle = enTitle;
      } else {
        enTitle = zhTitle;
      }
    }
    mailData.put("minZhShareTitle", "“" + zhTitle + "”");
    mailData.put("minEnShareTitle", "“" + enTitle + "”");
    mailData.put("mailContext", "");
    mailData.put("recvName", recvName);
    mailData.put("psnName", psnName);
    // 只能分享一条
    mailData.put("total", "1");
    mailData.put("type", "4");

    // 构造必需的参数
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    Integer tempcode = 10102;
    info.setSenderPsnId(sender.getPersonId());
    info.setReceiverPsnId(receiver.getPersonId());
    info.setReceiver(receiver.getEmail());
    info.setMsg("分享项目邮件");
    info.setMailTemplateCode(tempcode);
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializerNoNull(info));

    // 主题参数，添加如下：
    List<String> subjectParamLinkList = new ArrayList<String>();
    String subjectType = "";
    String subjectCount = mailData.get("total").toString();
    if ("zh".equals(languageVersion) || "zh_CN".equals(languageVersion)) {
      subjectType = "项目";
    } else {
      subjectType = "projects";
      if ("1".equals(subjectCount)) {
        subjectCount = "a";
        subjectType = "project";
      }
    }
    subjectParamLinkList.add(psnName);
    subjectParamLinkList.add(subjectCount);
    subjectParamLinkList.add(subjectType);
    mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));

    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
    restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
  }

  private String buildEmailTitle(Map<String, Object> ctxMap) {
    String msg = "";
    try {
      msg = FreeMarkerTemplateUtils.processTemplateIntoString(
          freemarkerConfiguration.getTemplate(ObjectUtils.toString(ctxMap.get("tmpUrl")), "utf-8"), ctxMap);
    } catch (IOException e) {
      logger.error("构建Email标题失败，没有找到对应的Email标题模板！", e);
    } catch (TemplateException e) {
      logger.error("构造Email标题失败,FreeMarker处理失败", e);
    }
    return msg;
  }

  private void parseReceiverIds(MsgShowForm form, List<Long> receiverIdList) {
    String ids = form.getReceiverIds();
    if (StringUtils.isNotBlank(ids)) {
      String[] split = ids.split(",");
      if (split.length > 0) {
        for (String str : split) {
          long id = NumberUtils.toLong(str);
          if (!NumberUtils.isNumber(str)) {
            id = NumberUtils.toLong(Des3Utils.decodeFromDes3(str));
          }
          if (id != 0L) {
            receiverIdList.add(id);
          }
        }
      }
    }
  }

  private void parseReceiverEmails(MsgShowForm form, List<String> receiveEmailsList) {
    String emails = form.getReceiverEmails();
    if (StringUtils.isNoneBlank(emails)) {
      String[] split = emails.split(",");
      if (split.length > 0) {
        for (String str : split) {
          if (isEmail(str)) {
            receiveEmailsList.add(str);
          }
        }
      }
    }
  }

  void restSendInsideMsgEmail(MsgShowForm form, Person sender, Person receiver) throws Exception {
    if (sender == null || receiver == null) {
      throw new Exception("构建邮件缺少必要的参数");
    }
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, Object> mailData = new HashMap<String, Object>();
    // 构造必需的参数
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    String language = "";
    language = receiver.getEmailLanguageVersion();
    if (StringUtils.isBlank(language)) {
      language = LocaleContextHolder.getLocale().toString();
    }
    String psnName = PsnInfoUtils.getPersonName(receiver, language);
    String sendPsnName = PsnInfoUtils.getPersonName(sender, language);
    // 跟踪链接 根据key放置到模板中 所有的链接不再需要放到mailData中
    // 人员站外地址
    PsnProfileUrl sendProfileUrl = psnProfileUrlDao.get(sender.getPersonId());
    PsnProfileUrl receProfileUrl = psnProfileUrlDao.get(receiver.getPersonId());
    String recevicesUrl = this.domainscm + "/P/" + receProfileUrl.getPsnIndexUrl();
    String sendPsnUrl = this.domainscm + "/P/" + sendProfileUrl.getPsnIndexUrl();
    String chatUrl = this.domainscm + "/dynweb/showmsg/msgmain?model=chatMsg";
    List<String> linkList = new ArrayList<String>();
    MailLinkInfo l1 = new MailLinkInfo();
    l1.setKey("domainUrl");
    l1.setUrl(domainscm);
    l1.setUrlDesc("科研之友首页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));
    MailLinkInfo l2 = new MailLinkInfo();
    l2.setKey("recevicesUrl");
    l2.setUrl(recevicesUrl);
    l2.setUrlDesc("收件人员站外地址");
    linkList.add(JacksonUtils.jsonObjectSerializer(l2));
    MailLinkInfo l3 = new MailLinkInfo();
    l3.setKey("sendPsnUrl");
    l3.setUrl(sendPsnUrl);
    l3.setUrlDesc("发件人员站外地址");
    linkList.add(JacksonUtils.jsonObjectSerializer(l3));
    MailLinkInfo l4 = new MailLinkInfo();
    l4.setKey("chatUrl");
    l4.setUrl(chatUrl);
    l4.setUrlDesc("查看消息地址");
    linkList.add(JacksonUtils.jsonObjectSerializer(l4));
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
    // 构建历史消息,最多三条
    form.setChatPsnId(receiver.getPersonId());
    List<MsgRelation> msgRelationList = msgRelationDao.getMsgListByInsideMsgEmail(form);
    List<MsgEmailForm> listMsg = new ArrayList<>();
    if (msgRelationList != null && msgRelationList.size() > 1) {
      for (int i = 1; i < msgRelationList.size(); i++) {
        MsgRelation msgRelation = msgRelationList.get(i);
        MsgContent content = msgContentDao.get(msgRelation.getContentId());
        Map contentMap = JacksonUtils.jsonToMap(content.getContent());
        String smateInsideLetterType = contentMap.get("smateInsideLetterType").toString();
        // HashMap<String, Object> tempMap = new HashMap<String, Object>();
        MsgEmailForm msgEmailForm = new MsgEmailForm();
        msgEmailForm.setType(smateInsideLetterType);
        msgEmailForm.setContent(contentMap.get("content") != null ? contentMap.get("content").toString() : "");
        if (msgRelation.getSenderId().longValue() == sender.getPersonId().longValue()) {
          msgEmailForm.setAvatars(sender.getAvatars());
          msgEmailForm.setSendPsnName(sendPsnName);
          msgEmailForm.setSendPsnUrl(sendPsnUrl != null ? sendPsnUrl : "");
        } else {
          msgEmailForm.setAvatars(receiver.getAvatars());
          msgEmailForm.setSendPsnName(psnName);
          msgEmailForm.setSendPsnUrl(recevicesUrl != null ? recevicesUrl : "");
        }
        listMsg.add(msgEmailForm);
      }
    }
    mailData.put("listMsg", listMsg);
    // 主题参数，添加如下：
    List<String> subjectParamLinkList = new ArrayList<String>();
    subjectParamLinkList.add(psnName);
    subjectParamLinkList.add(sendPsnName);
    mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));
    mailData.put("psnName", psnName);
    mailData.put("sendPsnName", sendPsnName);
    // 站内信类型 ： text pub file
    mailData.put("type", form.getSmateInsideLetterType());
    mailData.put("content", form.getContent());
    mailData.put("avatars", sender.getAvatars());
    Integer tempcode = 10003;
    info.setSenderPsnId(sender.getPersonId());
    info.setReceiverPsnId(receiver.getPersonId());
    info.setReceiver(receiver.getEmail());
    info.setMsg("站内信邮件");
    info.setMailTemplateCode(tempcode);
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializerNoNull(info));
    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
    restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
  }

  @Override
  public void showChatUI(MsgShowForm form) throws Exception {
    if (StringUtils.isBlank(form.getDes3ChatPsnId())) {
      throw new Exception("获取聊天对象的psnId失败，即将跳转会话列表。");
    } else {
      Long psnId = Long.parseLong(Des3Utils.decodeFromDes3(form.getDes3ChatPsnId()));
      Person person = personDao.findPerson(psnId);
      if (person == null) {
        throw new Exception("获取聊天对象已删除，即将跳转会话列表。");
      } else {
        form.setChatPsnName(StringUtils.isBlank(person.getName()) ? (person.getFirstName() + " " + person.getLastName())
            : person.getName());
        form.setDes3PsnId(Des3Utils.encodeToDes3(SecurityUtils.getCurrentUserId().toString()));
      }
    }
  }

  @Override
  public void showNewChatUI(MsgShowForm form) throws Exception {
    if (StringUtils.isNotBlank(form.getDes3ChatPsnId())) {
      Long psnId = Long.parseLong(Des3Utils.decodeFromDes3(form.getDes3ChatPsnId()));
      Person person = personDao.findPerson(psnId);
      if (person == null) {
        throw new Exception("获取聊天对象已删除，即将跳转会话列表。");
      } else {
        form.setChatPsnName(StringUtils.isBlank(person.getName()) ? (person.getFirstName() + " " + person.getLastName())
            : person.getName());
      }
    }
  }

  @Override
  public void addPsnFileShareRecord(MsgShowForm form) throws Exception {
    String receiverIds = form.getReceiverIds();
    String[] split = receiverIds.split(",");
    if (split != null && split.length > 0) {
      for (String receiverId : split) {
        if (NumberUtils.isNumber(receiverId)) {
          PsnFileShareBase shareBase = new PsnFileShareBase();
          Long id = psnFileShareBaseDao.getId();
          shareBase.setId(id);
          Date date = new Date();
          shareBase.setCreateDate(date);
          shareBase.setUpdateDate(date);
          shareBase.setStatus(0);
          // 站内信分享文件 没有文本内容
          shareBase.setShareContentRel("");
          shareBase.setSharerId(form.getPsnId());

          PsnFileShareRecord shareRecord = new PsnFileShareRecord();
          shareRecord.setCreateDate(date);
          shareRecord.setUpdateDate(date);
          shareRecord.setFileId(form.getFileId());
          // todo
          shareRecord.setMsgRelationId(form.getMsgRelationId());
          shareRecord.setShareBaseId(id);
          shareRecord.setSharerId(form.getPsnId());
          shareRecord.setReveiverId(NumberUtils.toLong(receiverId));
          shareRecord.setStatus(0);

          psnFileShareBaseDao.save(shareBase);
          psnFileShareRecordDao.save(shareRecord);
        }
      }

    }

  }

  /**
   * 构建接收者的信息，主要是将接收者为email格式的进行处理
   * 
   * @param form
   */
  @Override
  public void buildReceivers(MsgShowForm form) {
    if (StringUtils.isBlank(form.getReceiverEmails())) {
      return;// 没有输入邮件，不进行处理
    }
    String[] mailReceivers = form.getReceiverEmails().split(",");
    StringBuffer mailStr = new StringBuffer();
    StringBuffer des3PsnIds = new StringBuffer(form.getReceiverIds());
    for (int i = 0; i < mailReceivers.length; i++) {
      if (StringUtils.isNoneEmpty(mailReceivers[i]) && isEmail(mailReceivers[i])) {// 判断当前字符是不是邮箱
        User user = userDao.findByLoginName(mailReceivers[i]);// 判断当前邮件是不是在站内注册了
        if (user != null) {
          des3PsnIds.append(Des3Utils.encodeToDes3(user.getId().toString())).append(",");// 注册了的话就将当前人员加入到psnId中
        } else {
          mailStr.append(mailReceivers[i]).append(",");
        }
      }
    }
    form.setReceiverIds(des3PsnIds.toString());
    form.setReceiverEmails(mailStr.toString());
  }

  private boolean isEmail(String str) {
    String reg = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    return Pattern.matches(reg, str);
  }

  @Override
  public Long getTempPsnCount(Long psnId) {
    return friendTempDao.getTempPsnCount(psnId);
  }

}
