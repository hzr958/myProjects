package com.smate.web.psn.service.friend;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.model.MailOriginalDataInfo;
import com.smate.core.base.consts.dao.ConstRegionDao;
import com.smate.core.base.consts.dao.InstitutionDao;
import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.dyn.dao.DynamicMsgDao;
import com.smate.core.base.dyn.dao.DynamicRelationDao;
import com.smate.core.base.dyn.model.DynamicMsg;
import com.smate.core.base.dyn.model.DynamicRelation;
import com.smate.core.base.dyn.model.DynamicRelationPk;
import com.smate.core.base.email.dao.MailInitDataDao;
import com.smate.core.base.email.model.MailInitData;
import com.smate.core.base.email.service.EmailCommonService;
import com.smate.core.base.email.service.MailInitDataService;
import com.smate.core.base.privacy.dao.PrivacySettingsDao;
import com.smate.core.base.privacy.model.PrivacySettings;
import com.smate.core.base.privacy.model.PrivacySettingsPK;
import com.smate.core.base.privacy.service.PublicPrivacyServiceImpl;
import com.smate.core.base.psn.dao.EducationHistoryDao;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.dao.PsnPubDAO;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.dto.PsnSendMail;
import com.smate.core.base.psn.dto.profile.Personal;
import com.smate.core.base.psn.model.EducationHistory;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.solr.service.SolrIndexService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.common.HqlUtils;
import com.smate.core.base.utils.constant.EmailConstants;
import com.smate.core.base.utils.constant.MsgConstants;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.security.PsnPrivateDao;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.psninfo.PsnInfoUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.MapBuilder;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.psn.build.factory.PsnInfoBuildFactory;
import com.smate.web.psn.build.factory.PsnInfoEnum;
import com.smate.web.psn.dao.attention.AttPersonDao;
import com.smate.web.psn.dao.friend.FriendDao;
import com.smate.web.psn.dao.friend.FriendFappraisalRecDao;
import com.smate.web.psn.dao.friend.FriendFappraisalSendDao;
import com.smate.web.psn.dao.friend.FriendGroupDao;
import com.smate.web.psn.dao.friend.FriendInGroupsDao;
import com.smate.web.psn.dao.friend.FriendReqRecordDao;
import com.smate.web.psn.dao.friend.FriendTempDao;
import com.smate.web.psn.dao.friend.FriendTempSysDao;
import com.smate.web.psn.dao.keywork.PsnScienceAreaDao;
import com.smate.web.psn.dao.profile.NodePersonDao;
import com.smate.web.psn.dao.profile.PsnDisciplineKeyDao;
import com.smate.web.psn.dao.profile.SyncPersonDao;
import com.smate.web.psn.dao.rcmdsync.RcmdSyncPsnInfoDao;
import com.smate.web.psn.dao.recommend.FriendSysRecommendDao;
import com.smate.web.psn.dao.recommend.PsnCoRmcRefreshDao;
import com.smate.web.psn.dao.statistics.VistStatisticsDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.exception.PsnBuildException;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.attention.AttPerson;
import com.smate.web.psn.model.dynamic.DynamicHandlerService;
import com.smate.web.psn.model.friend.Friend;
import com.smate.web.psn.model.friend.FriendForm;
import com.smate.web.psn.model.friend.FriendReqConst;
import com.smate.web.psn.model.friend.FriendReqRecord;
import com.smate.web.psn.model.friend.FriendTemp;
import com.smate.web.psn.model.friend.FriendTempSys;
import com.smate.web.psn.model.friend.PsnListViewForm;
import com.smate.web.psn.model.message.Message;
import com.smate.web.psn.model.psninfo.PsnInfo;
import com.smate.web.psn.model.psninfo.SyncPerson;
import com.smate.web.psn.model.rcmdsync.RcmdSyncPsnInfo;
import com.smate.web.psn.model.setting.SnsPersonSyncMessage;
import com.smate.web.psn.service.message.MessageService;
import com.smate.web.psn.service.profile.PersonEmailManager;
import com.smate.web.psn.service.profile.PersonManager;
import com.smate.web.psn.service.profile.PsnInfoImproveService;
import com.smate.web.psn.service.statistics.AttendStatisticsService;
import com.smate.web.psn.service.statistics.PsnStatisticsService;
import com.smate.web.psn.service.user.UserSettingService;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

/**
 * 联系人服务类
 *
 * @author zk
 *
 */
@Service("friendService")
@Transactional(rollbackFor = Exception.class)
public class FriendServiceImpl implements FriendService {
  private static final String ENCODING = "utf-8";
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private WorkHistoryDao workHistoryDao;
  @Autowired
  private NodePersonDao nodePersonDao;
  @Autowired
  private EducationHistoryDao educationHistoryDao;
  @Autowired
  private FriendDao friendDao;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private PsnInfoBuildFactory psnInfoBuildFactory;
  @Autowired
  private FriendFappraisalRecDao friendFappraisalRecDao;
  @Autowired
  private FriendFappraisalSendDao friendFappraisalSendDao;
  @Autowired
  private PrivacySettingsDao privacySettingDao;
  @Autowired
  private FriendTempDao friendTempDao;
  @Autowired
  private PersonEmailManager personEmailManager;
  @Autowired
  private FriendSysRecommendDao friendSysRecommendDao;
  @Autowired
  private PrivacySettingsDao privacySettingsDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private FriendTempSysDao friendTempSysDao;
  @Autowired
  private MessageService messageService;
  @Autowired
  private MailInitDataDao mailInitDataDao;
  @Autowired
  private FriendInGroupsDao friendInGroupsDao;
  @Autowired
  private AttPersonDao attPersonDao;
  @Autowired
  private PsnCoRmcRefreshDao psnCoRmcRefreshDao;
  @Autowired
  private PsnStatisticsService psnStatisticsService;
  @Autowired
  private RcmdSyncPsnInfoDao rcmdSyncPsnInfoDao;
  @Autowired
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String openResfulUrl;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private FriendGroupDao friendGroupDao;
  @Resource(name = "psnCacheService")
  private CacheService cacheService;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private UserSettingService userSettingService;
  @Autowired
  AttendStatisticsService attendStatisticsService;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private MailInitDataService mailInitDataService;
  @Autowired
  private VistStatisticsDao vistStatisticsDao;
  @Autowired
  private FriendReqRecordDao friendReqRecordDao;
  @Autowired
  private SolrIndexService solrIndexService;
  @Autowired
  private PsnScienceAreaDao psnScienceAreaDao;
  @Autowired
  private SyncPersonDao syncPersonDao;
  @Autowired
  private PsnDisciplineKeyDao psnDisciplineKeyDao;
  @Autowired
  private DynamicMsgDao dynamicMsgDao;
  @Autowired
  private DynamicRelationDao dynamicRelationDao;
  @Autowired
  private PsnInfoImproveService psnInfoImproveService;
  @Autowired
  private UserDao userDao;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDao;
  @Autowired
  private PsnPrivateDao psnPrivateDao;
  @Autowired
  private PsnPubDAO psnPubDAO;
  @Autowired
  private DynamicHandlerService dynamicHandlerService;
  @Value(value = "${sendEmail.restful.url}")
  private String sendRestfulUrl;


  @Override
  public SyncPerson syncPersonByIns(Long psnId) throws ServiceException {
    SyncPerson newSyncPsn = null;
    try {
      String ids = "";
      String names = "";
      newSyncPsn = new SyncPerson();
      List<WorkHistory> workList = workHistoryDao.findWorkInsByPsnId(psnId);
      if (CollectionUtils.isNotEmpty(workList)) {
        for (WorkHistory work : workList) {
          if (work.getInsId() == null) {
            if (StringUtils.isNotBlank(work.getInsName()) && names.indexOf(work.getInsName()) == -1) {
              names += work.getInsName() + ",";
            }
          } else if (ids.indexOf(String.valueOf(work.getInsId())) == -1) {
            ids += String.valueOf(work.getInsId()) + ",";
          }
        }
      }
      // 首要单位
      String psnInsName = workHistoryDao.getPrimaryWorkNameByPsnId(psnId);
      newSyncPsn.setPsnInsName(psnInsName);
      List<EducationHistory> eduList = educationHistoryDao.findEduInsByPsnId(psnId);
      if (CollectionUtils.isNotEmpty(eduList)) {
        for (EducationHistory edu : eduList) {
          if (edu.getInsId() == null) {
            if (StringUtils.isNotBlank(edu.getInsName()) && names.indexOf(edu.getInsName()) == -1) {
              names += edu.getInsName() + ",";
            }
          } else if (ids.indexOf(String.valueOf(edu.getInsId())) == -1) {
            ids += String.valueOf(edu.getInsId()) + ",";
          }
        }
      }
      ids = clearStrs(ids);
      names = clearStrs(names);
      Long regionId = nodePersonDao.getPsnRegionId(psnId);

      if (regionId != null) {
        newSyncPsn.setRegionId(regionId);
      }
      if (StringUtils.isNotBlank(ids)) {
        newSyncPsn.setPsnInsIdList(ids);
      }
      if (StringUtils.isNotBlank(names)) {
        newSyncPsn.setPsnInsNameList(names);
      }
    } catch (Exception e) {
      logger.error("获取psnId:{}的工作单位教育经历国家出错", psnId, e);
    }
    return newSyncPsn;
  }

  /**
   * 参数整理
   *
   * @param params
   * @return
   */
  private String clearStrs(String params) {
    if (StringUtils.isBlank(params)) {
      return "";
    }
    if (params.lastIndexOf(",") == params.length() - 1) {
      params = params.substring(0, params.lastIndexOf(","));
    }
    return params;
  }

  /**
   * 根据人员ID，获取人员联系人信息列表
   *
   * @throws ServiceException
   */
  @Override
  public void findFriendLis(FriendForm form) throws Exception {
    try {
      List<PsnInfo> psnInfos = new ArrayList<PsnInfo>();
      // 获取联系人的人员ID list
      List<Long> friendIds = friendDao.getFriendListByPsnId(form.getPsnId());
      for (Long psnId : friendIds) {
        // 获取联系人的人员信息
        Person psn = personManager.getPersonInfoByPsnIdForFriend(psnId);
        if (psn != null) {
          // psn.setInsInfo(personManager.getPsnViewWorkHisInfo(psnId));
          buildPsnShowInfo(psn);
          // 利用装饰模式构建人员信息
          PsnInfo psnInfo = new PsnInfo();
          psnInfo.setPerson(psn);
          psnInfoBuildFactory.buildPsnInfo(PsnInfoEnum.mobile.toInt(), psnInfo);
          psnInfos.add(psnInfo);
        }
      }
      form.setPsnInfoList(psnInfos);

    } catch (DaoException e) {
      logger.error("根据人员ID获取人员联系人ID列表失败, psnId=" + form.getPsnId());
      throw new DaoException(e);
    } catch (PsnBuildException e) {
      logger.error("装饰模式构建人员信息失败，psnId=" + form.getPsnId());
      throw new PsnBuildException(e);
    }
  }

  /**
   * 构建人员单位/部门/职位信息
   * 
   * @param psn
   * @param psnInfo
   */
  public void buildPsnShowInfo(Person psn) {
    StringBuffer stringBuffer = new StringBuffer();
    if (StringUtils.isNotEmpty(psn.getInsName())) {
      stringBuffer.append(psn.getInsName());
    }
    if (StringUtils.isNotEmpty(psn.getDepartment())) {
      if (stringBuffer.length() > 0) {
        stringBuffer.append(", ");
      }
      stringBuffer.append(psn.getDepartment());
    }
    if (StringUtils.isNotEmpty(psn.getPosition())) {
      if (stringBuffer.length() > 0) {
        stringBuffer.append(", ");
      }
      stringBuffer.append(psn.getPosition());
    }
    psn.setInsInfo(stringBuffer.toString());
  }

  /**
   * 根据psnId 获取联系人id
   */
  @Override
  public List<Long> getFriendListByPsnId(Long psnId) throws ServiceException {
    List<Long> friendIds = new ArrayList<Long>();
    try {
      friendIds = friendDao.getFriendListByPsnId(psnId);
    } catch (DaoException e) {
      logger.error("获取联系人Id失败, psnId =" + psnId, e);
    }
    return friendIds;
  }

  /**
   * 对人员姓名进行排序、分类
   */
  @Override
  public Map<String, List<PsnInfo>> sortFriendName(List<PsnInfo> psnList) {
    // 先进行排序
    Collections.sort(psnList);
    // 分类
    Map<String, List<PsnInfo>> personMap = groupSort(psnList);
    return personMap;
  }

  public Map<String, List<PsnInfo>> groupSort(List<PsnInfo> psnList) {
    Map<String, List<PsnInfo>> psnMap = new TreeMap<String, List<PsnInfo>>();

    for (PsnInfo p : psnList) {
      Person person = p.getPerson();
      String thisName = StringUtils.isNotBlank(person.getName()) ? person.getName() : person.getLastName();
      thisName = ServiceUtil.parseNameToUpperEn(thisName);
      if (StringUtils.isAlpha(thisName.substring(0, 1))) {
        List<PsnInfo> piList = psnMap.get(thisName.substring(0, 1));
        if (piList == null) {
          piList = new ArrayList<PsnInfo>();
          piList.add(p);
          psnMap.put(thisName.substring(0, 1), piList);
        } else {
          piList.add(p);
          // psnMap.put(thisName.substring(0, 1), piList);
        }
      } else {
        List<PsnInfo> piList = psnMap.get("others");
        if (piList == null) {
          piList = new ArrayList<PsnInfo>();
          piList.add(p);
          psnMap.put("others", piList);
        } else {
          piList.add(p);
          // psnMap.put("others", piList);
        }
      }
    }
    return psnMap;
  }

  /*
   * 判断是否为联系人
   *
   * @see com.smate.web.psn.service.friend.FriendService#isFriend(java.lang.Long, java.lang.Long)
   */
  @Override
  public boolean isFriend(Long currendPsnId, Long friendPsnId) {

    try {
      return friendDao.isFriend(currendPsnId, friendPsnId);
    } catch (DaoException e) {
      logger.error("判断 是否为联系人出错", e);
      throw new PsnException("判断 是否为联系人出错", e);
    }
  }

  /**
   * 查看用户是否设置允许加为联系人的权限.
   *
   * @return true-任何人都可以加其为联系人;false-不允许被加为联系人.
   */
  @Override
  public Boolean isPsnAddFrdPrivacy(Long psnId) throws ServiceException {
    try {
      PrivacySettings privacySettings = privacySettingDao.loadPsByPsnId(psnId, PrivacyType.ADD_FRD);
      if (privacySettings != null) {
        return privacySettings.getPermission() == 0;
      }
    } catch (Exception e) {
      logger.error("获取psnId:{}设置添加联系人隐私设置出错", psnId, e);
    }
    return false;
  }

  /**
   * 添加联系人
   */
  @Override
  public void addFriendReq(Long receiverId, String domainscm, Personal form) throws ServiceException {
    Long sendPsnId = form.getPsnId();
    if (sendPsnId == null || sendPsnId == 0) {
      sendPsnId = SecurityUtils.getCurrentUserId();
    }
    String receiverName = null;
    String psnUrl = null;

    try {
      Date date = new Date();
      // 检查是否有重复的联系人邀请，如果有，则将以前的联系人邀请重置为系统忽略状态
      if (sendPsnId != null) {
        List<FriendTemp> frinedLists = friendTempDao.getTempFriend(sendPsnId, receiverId);
        if (CollectionUtils.isNotEmpty(frinedLists)) {
          for (FriendTemp frinedList : frinedLists) {
            friendTempDao.delete(frinedList.getId());
          }
        }
        boolean hasUnDealedReqRecord = friendReqRecordDao.findNotDealFriendReqRecord(sendPsnId, receiverId);
        if (hasUnDealedReqRecord) {
          friendReqRecordDao.updateStatus(sendPsnId, receiverId, 3, date);
        }
      }

      // 个人主页链接
      PsnProfileUrl profileUrl = psnProfileUrlDao.get(sendPsnId);
      if (profileUrl != null && StringUtils.isNotBlank(profileUrl.getPsnIndexUrl())) {
        psnUrl = domainscm + "/" + ShortUrlConst.P_TYPE + "/" + profileUrl.getPsnIndexUrl();
      }
      form.setProfileUrl(psnUrl);
      // 第一步：保存向联系人发送的请求信息
      FriendTemp friendTemp = new FriendTemp();
      friendTemp.setPsnId(sendPsnId);
      friendTemp.setTempPsnId(receiverId);

      friendTemp.setSendMail(personEmailManager.getFristMail(receiverId));
      friendTempDao.save(friendTemp);

      // 保存到FriendReqRecord
      FriendReqRecord friendReqRecord = new FriendReqRecord();
      friendReqRecord.setSendPsnId(sendPsnId);
      friendReqRecord.setReceivePsnId(receiverId);
      friendReqRecord.setCreateTime(date);
      friendReqRecord.setDealTime(date);
      friendReqRecord.setStatus(0);
      friendReqRecordDao.save(friendReqRecord);

      // 获取接收者名字
      Person person = personDao.get(receiverId);
      if (StringUtils.isBlank(person.getEmailLanguageVersion())) {
        person.setEmailLanguageVersion(LocaleContextHolder.getLocale().toString());
      }
      if ("en_US".equals(person.getEmailLanguageVersion())) {
        receiverName = person.getEname();
      } else {
        receiverName = person.getName();
      }
      // 获取发送者信息
      form.setPerson(personDao.get(sendPsnId));
      // 发送站内信
      sendMsg(receiverId, sendPsnId);
      // 新的发件方式发送邮件
      restSendAddFriendEmail(receiverId, friendTemp.getSendMail(), receiverName, form,
          person.getEmailLanguageVersion());
    } catch (Exception e) {
      logger.error("添加联系人报错, currentPsnId={}, receiverPsnId={}", sendPsnId, receiverId, e);
      throw new ServiceException(e);
    }
  }


  /**
   * 发送站内信
   *
   * @param receiverId
   */
  public void sendMsg(Long receiverId, Long sendId) {
    try {
      // 调open接口发送消息
      Map<String, Object> map = buildSendMsgParam(receiverId, sendId);
      Object obj = restTemplate.postForObject(this.openResfulUrl, map, Object.class);
    } catch (Exception e) {
      logger.error("发送站内信异常：" + e.toString());
    }

  }

  /**
   * 构建发消息需要的参数
   *
   * @param receiverId
   * @return
   */
  private Map<String, Object> buildSendMsgParam(Long receiverId, Long sendId) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> dataMap = new HashMap<String, Object>();
    map.put("openid", "99999999");
    map.put("token", "00000000msg77msg");
    dataMap.put(MsgConstants.MSG_TYPE, "1");
    dataMap.put(MsgConstants.MSG_SENDER_ID, sendId);
    dataMap.put(MsgConstants.MSG_RECEIVER_IDS, receiverId);
    dataMap.put("requestFriendId", receiverId);
    map.put("data", JacksonUtils.mapToJsonStr(dataMap));
    return map;
  }


  /**
   * 新的的发件方式调接口
   *
   * @param receiverId
   * @param email
   * @param receiverName
   * @param psnInfo
   */
  public void restSendAddFriendEmail(Long receiverId, String email, String receiverName, Personal psnInfo,
      String emailLanguage) {
    Map<String, Object> mailData = new HashMap<String, Object>();
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    if (receiverId != null && psnInfo.getPerson() != null) {
      Long sendId = SecurityUtils.getCurrentUserId();
      Integer templateCode = 10006;
      String msg = "请求成为你的联系人";
      // 构造必需的参数
      MailOriginalDataInfo info = new MailOriginalDataInfo();
      info.setSenderPsnId(sendId);
      info.setMsg(msg);
      info.setReceiverPsnId(receiverId);
      info.setMailTemplateCode(templateCode);
      info.setReceiver(email);
      paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializer(info));
      // 查找地区
      if (psnInfo.getPerson().getRegionId() != null) {
        ConstRegion constRegion = constRegionDao.findRegionNameById(psnInfo.getPerson().getRegionId());
        if (constRegion != null) {
          if ("en_US".equals(emailLanguage)) {
            mailData.put("regions", constRegion.getEnName());
          } else {
            mailData.put("regions", constRegion.getZhName());
          }
        }
      } else {
        mailData.put("regions", "");
      }
      if (!psnInfo.getPerson().getAvatars().isEmpty()) {
        mailData.put("avatars", psnInfo.getPerson().getAvatars());
      }
      String psnName = PsnInfoUtils.getPersonName(psnInfo.getPerson(), emailLanguage);
      mailData.put("psnName", psnName);
      mailData.put("invitePsnName", receiverName);
      mailData.put("titolo", psnInfo.getPerson().getTitolo());
      mailData.put("toemail", email);
      List<String> linkList = new ArrayList<String>();
      MailLinkInfo l1 = new MailLinkInfo();
      l1.setKey("domainUrl");
      l1.setUrl(domainscm);
      l1.setUrlDesc("科研之友主页");
      linkList.add(JacksonUtils.jsonObjectSerializer(l1));
      MailLinkInfo l2 = new MailLinkInfo();
      l2.setKey("inviteUrl");
      l2.setUrl(domainscm + "/psnweb/friend/main?model=rec");
      l2.setUrlDesc("加为联系人链接");
      linkList.add(JacksonUtils.jsonObjectSerializer(l2));
      MailLinkInfo l3 = new MailLinkInfo();
      l3.setKey("psnUrl");
      l3.setUrl(psnInfo.getProfileUrl());
      l3.setUrlDesc("发送添加好友的人主页地址");
      linkList.add(JacksonUtils.jsonObjectSerializer(l3));
      MailLinkInfo l4 = new MailLinkInfo();
      l4.setKey("concernUrl");
      l4.setUrl(psnInfo.getProfileUrl());
      l4.setUrlDesc("发送添加好友的人主页地址");
      linkList.add(JacksonUtils.jsonObjectSerializer(l4));
      mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
      // 主题参数，添加如下：
      List<String> subjectParamLinkList = new ArrayList<String>();
      subjectParamLinkList.add(psnName);
      mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));
      paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
      restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
    }

  }

  /**
   * 保存发送邮件数据
   */
  @Override
  public void saveMailInitData(Map<String, Object> dataMap) throws ServiceException {
    MailInitData mid = new MailInitData();
    mid.setCreateDate(new Date());
    mid.setFromNodeId(1);
    mid.setMailData(JacksonUtils.jsonObjectSerializer(dataMap));
    mid.setStatus(1);
    mid.setToAddress(dataMap.get("toemail").toString());
    mailInitDataDao.saveMailData(mid);
  }

  @Override
  @Deprecated
  public void delPersonFappraisal(Long psnId, Long workId) {
    this.friendFappraisalRecDao.delPersonFappraisalRec(psnId, workId);
    this.friendFappraisalSendDao.delPersonFappraisalSend(psnId, workId);
  }

  /**
   * 可能认识的人
   */
  @Override
  public List<Person> findMayKnowPersonListByPsnIds(Page<Person> page, Long psnId, boolean firstPage, String pubCpt,
      String prjCpt) throws ServiceException {
    try {
      psnId = psnId == null ? SecurityUtils.getCurrentUserId() : psnId;
      /*
       * pubCpt = StringUtils.isBlank(pubCpt) ? "" : "4"; prjCpt = StringUtils.isBlank(prjCpt) ? "" : "5";
       * List<Long> copartnerList = cooperationDao.getPsnKnowCopartner(psnId, pubCpt, prjCpt); if
       * (CollectionUtils.isNotEmpty(copartnerList)) { // 最大500人_zk if (copartnerList.size() <= 500) {
       * list.addAll(copartnerList); } else { list.addAll(copartnerList.subList(0, 500)); } }
       */
      List<Long> frdRcmdList = friendSysRecommendDao.findFriendListTempAutoSysPsnIds(psnId);
      List<Long> list = new ArrayList<Long>();
      if (CollectionUtils.isNotEmpty(frdRcmdList)) {
        for (Long frdRcmdPsnId : frdRcmdList) {
          if (!list.contains(frdRcmdPsnId) && list.size() < 500) {
            list.add(frdRcmdPsnId);
          }

        }
      }
      if (CollectionUtils.isEmpty(list)) {
        return null;
      }
      // 排除掉不允许任何人加联系人的人员的ID
      List<Long> canNotAddFr = privacySettingsDao.getPrivacySettingByPsnIds(list, PrivacyType.ADD_FRD, 0);
      list.removeAll(canNotAddFr);
      List<Person> person = personDao.getPersonBasePage(list);
      return person;
    } catch (Exception e) {
      logger.error("联系人推荐-获取推荐人员出错了（合作者排在前面）", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 可能认识的人
   */
  @Override
  public List<Person> findPersonMayKnowByCurrentPsnId(Long psnId, boolean firstPage, Page<Person> page)
      throws ServiceException {
    psnId = psnId == null ? SecurityUtils.getCurrentUserId() : psnId;
    List<Long> psnIdList = friendTempSysDao.findLocalPersonIds(psnId, firstPage, page);
    if (psnIdList.size() >= 500) {
      psnIdList = psnIdList.subList(0, 500);
    }
    return personDao.getPersonBaseIncludeIns(psnIdList);
  }

  /**
   *
   */
  /**
   * 基础信息的构造
   */
  @Override
  public void bulidBaseInfo(List<Person> knowList, Page<PsnInfo> page, boolean firstPage, String strCount) {
    Locale locale = LocaleContextHolder.getLocale();
    List<PsnInfo> psnList = new ArrayList<PsnInfo>();
    Integer mayCount = 0;
    if (StringUtils.isNotBlank(strCount)) {
      mayCount = Integer.parseInt(strCount);
    }
    try {
      if (firstPage != true) {
        int number = page.getFirst() - 1 - mayCount;
        // 判断是否超出可能认识的人的列表上限。
        int lastIndex = (10 + number) > knowList.size() ? knowList.size() : (10 + number);
        for (int i = number; i < lastIndex; i++) {
          PsnInfo psnInfo = new PsnInfo();
          Person item = knowList.get(i);
          this.bulidInformation(item, psnInfo, locale);
          psnList.add(psnInfo);
        }
      } else {
        for (int i = 0; i < 5; i++) {
          PsnInfo psnInfo = new PsnInfo();
          if (i >= knowList.size()) {
            break;
          }
          Person item = knowList.get(i);
          this.bulidInformation(item, psnInfo, locale);
          psnList.add(psnInfo);
        }
      }
      page.setResult(psnList);
    } catch (Exception e) {
      logger.error("构建基础信息出错", e);
    }

  }

  /**
   * 基本信息的拼接
   */
  private void bulidInformation(Person item, PsnInfo psnInfo, Locale locale) throws Exception {

    String name = "";
    String insName = "";
    if (Locale.CHINA.equals(locale)) {
      name = StringUtils.isNotBlank(item.getName()) ? item.getName() : item.getEname();
      insName = StringUtils.isNotBlank(item.getInsName()) ? item.getInsName() : item.getEnInsName();
      psnInfo.setName(name);
      psnInfo.setInsName(insName);
    } else if (Locale.US.equals(locale)) {
      name = StringUtils.isNotBlank(item.getEname()) ? item.getEname() : item.getName();
      insName = StringUtils.isNotBlank(item.getEnInsName()) ? item.getEnInsName() : item.getInsName();
      psnInfo.setName(name);
      psnInfo.setInsName(insName);
    }
    psnInfo.setDes3PsnId(item.getPersonDes3Id());
    psnInfo.setAvatarUrl(item.getAvatars());
    psnInfo.setIsFriend(item.getIsFriend());
    psnInfo.setPsnId(item.getPersonId());
  }

  @Override
  public List<Long> findFriendAndHasReqFriendIds(Long psnId) throws ServiceException {
    return friendDao.findFriendAndReqFriendIds(psnId);
  }

  @Override
  public void delFriend(Long psnId, Long friendId) throws ServiceException {
    try {
      // 看是否有联系人记录
      Friend friend = friendDao.findFriendByPsnIdAndFriendId(psnId, friendId);
      if (friend == null) {
        return;
      }
      // 获取当前联系人数
      Long psnFrdSum = friendDao.getFriendCount(psnId);
      // 删除联系人分组记录-------PSN_FRIEND_IN_GROUP的friend_id不是联系人的ID,而是指联系人表psn_friend的ID
      // friendInGroupsDao.deleteByFriendId(friend.getId());
      // 删除联系人记录
      friendDao.delete(friend.getId());
      // 删除关注记录
      AttPerson attPsn = attPersonDao.find(psnId, friendId);
      if (attPsn != null) {
        attPersonDao.delete(attPsn);
      }
      // 删除相同关键词投票记录----该表对应功能已废弃

      // 标记需要刷新合作者推荐
      psnCoRmcRefreshDao.setPsnCoRmcRefresh(psnId);
      // 更新自己的联系人统计数
      Integer frdSum = psnFrdSum.intValue() - 1;// 联系人数减一
      psnStatisticsService.updatePsnStatisticsByFrd(psnId, frdSum);
      // 同步到rcmd
      RcmdSyncPsnInfo rsp = rcmdSyncPsnInfoDao.get(psnId);
      if (rsp == null) {
        rsp = new RcmdSyncPsnInfo(psnId);
      }
      rsp.setFriendFlag(1);
      rcmdSyncPsnInfoDao.save(rsp);

      // 删除添加好友请求临时存储表
      friendTempDao.deleteFriendTemp(psnId, friendId);

      // 重置联系人请求记录状态
      friendReqRecordDao.updateRecordStatus(friendId, psnId, FriendReqConst.FRIEND_REQ_DELET,
          FriendReqConst.FRIEND_REQ_AGREE, new Date());
    } catch (Exception e) {
      logger.error("删除联系人出错，psnId = " + psnId + "， friendId = " + friendId, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void delFriendByPsnIds(String friendPsnIds) throws ServiceException {
    try {
      Long userId = SecurityUtils.getCurrentUserId();
      String[] psnIds = friendPsnIds.split(",");
      if (psnIds != null && psnIds.length > 0) {
        for (String desId : psnIds) {
          Long friendPsnId = Long.valueOf(ServiceUtil.decodeFromDes3(desId));
          // 删除登录用户的联系人
          this.delFriend(userId, friendPsnId);
          // 删除登录用户联系人中的自己
          this.delFriend(friendPsnId, userId);
          // 处理动态.
          dynamicHandlerService.minusAttentionVisible(friendPsnId);
        }
      }
    } catch (Exception e) {
      logger.error("删除联系人时出错,联系人psnIds：{}", friendPsnIds, e);
    }
  }

  @Override
  public void autoAddFriend(Long reqPsnId) throws ServiceException {
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    try {
      // 检查两人是否已经是联系人
      boolean isFriend = this.isFriend(currentPsnId, reqPsnId);
      // 两人还不是联系人
      if (!isFriend) {
        // 处理自己和联系人的相关数据
        this.acceptSomeOneAddFriendRequest(currentPsnId, reqPsnId);
        this.acceptSomeOneAddFriendRequest(reqPsnId, currentPsnId);
        // 发送邮件
        this.restSendApproveAddFriendRequestEmail(currentPsnId, reqPsnId);
        Map<String, Object> map = buildSendMsg(currentPsnId, reqPsnId);
        restTemplate.postForObject(this.openResfulUrl, map, Object.class);
      }
      // 删除掉联系人请求记录
      friendTempDao.deleteFriendTemp(currentPsnId, reqPsnId);
      friendTempDao.deleteFriendTemp(reqPsnId, currentPsnId);
      // 重置联系人请求消息状态为同意_____按sxj要求,联系人请求列表的忽略或同意操作都不用更新v_msg_relation表
      // 重置friend_req_record状态为接受, 若互相发送了联系人请求，将自己发送的请求置为4
      friendReqRecordDao.updateStatus(reqPsnId, currentPsnId, FriendReqConst.FRIEND_REQ_AGREE, new Date());
      friendReqRecordDao.updateStatus(currentPsnId, reqPsnId, FriendReqConst.FRIEND_REQ_DEAL_ONE, new Date());
    } catch (Exception e) {
      logger.error("接受联系人请求处理出错， 当前登录用户ID为：" + currentPsnId + ", 受邀请联系人的用户ID为：" + reqPsnId, e);
      throw new ServiceException(e);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Long> getFriendIds(PsnListViewForm form) throws Exception {
    friendDao.queryFriendList(form);
    return form.getPage().getResult();
  }

  @Override
  public void sortFriendByReg(PsnListViewForm form) throws Exception {
    form.setRegionList(personManager.sortPsnByReg(form));
  }

  @Override
  public Map<String, Object> getFriendsCallBack(PsnListViewForm form) throws Exception {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    List<Map<String, Object>> insCount = friendDao.friendsCountCallBack(form, 1);// 机构统计数
    Map<String, Object> regMap = new HashMap<String, Object>();
    if (CollectionUtils.isNotEmpty(insCount)) {
      for (Map<String, Object> map : insCount) {
        Institution institution = institutionDao.get(NumberUtils.toLong(map.get("insId") + ""));
        if (institution != null) {
          regMap.put(map.get("insId").toString(), map.get("count"));
        }
      }
    }
    resultMap.put("insId", regMap);
    // List<Map<String, Object>> regionCount =
    // friendDao.friendsCountCallBack(form, 2);// 地区统计数
    // Map<String, Object> regMap2 = new HashMap<String, Object>();
    // if (CollectionUtils.isNotEmpty(regionCount)) {
    // for (Map<String, Object> map : regionCount) {
    // ConstRegion constRegion = constRegionDao
    // .findRegionNameById(NumberUtils.toLong(map.get("regionId") + ""));
    // if (constRegion != null) {
    // regMap2.put(map.get("regionId").toString(), map.get("count"));
    // }
    // }
    // }
    // resultMap.put("regionId", regMap2);
    return resultMap;
  }

  /**
   * 根据psnId获取可能认识的人
   *
   * @param psnId
   * @return
   */
  @Override
  public List<Long> getRecommendPsnListByPsnId(Long psnId) {
    List<Long> list = new ArrayList<Long>();
    try {
      list = getRecommendPsnIds(psnId, list);

    } catch (DaoException e) {
      logger.error("获取可能认识的人出错,psnId=" + psnId, e);
    }
    return list;
  }

  /**
   * 移动端根据psnId获取可能认识的人
   *
   * @param psnId
   * @return
   */
  @Override
  public List<Long> getMobileRecommendPsnList(Long psnId) {
    List<Long> list = new ArrayList<Long>();
    try {
      list = getRecommendPsnIds(psnId, list);

    } catch (DaoException e) {
      logger.error("获取可能认识的人出错,psnId=" + psnId, e);
    }
    return list;
  }

  /**
   * 移动端获取可能认识的人分页操作
   *
   * @return
   */
  @Override
  public List<Long> getMobileRecommendPsn(Integer pageNo, List<Long> recommendIds) {
    if (pageNo != null) {

      if (pageNo * 10 > recommendIds.size()) {
        recommendIds = recommendIds.subList((pageNo - 1) * 10, recommendIds.size());
      } else {
        recommendIds = recommendIds.subList((pageNo - 1) * 10, pageNo * 10);
      }

    }
    return recommendIds;
  }

  /**
   * 获取推荐人员id
   *
   * @param psnId
   * @param list
   * @return
   * @throws DaoException
   */
  private List<Long> getRecommendPsnIds(Long psnId, List<Long> list) throws DaoException {
    if (CollectionUtils.isEmpty(list)) {
      list = new ArrayList<Long>();
    }
    List<Long> frdRcmdList = friendSysRecommendDao.findFriendListTempAutoSysPsnIds(psnId);
    // 查询出来超过3次的需要排除掉 SCM-16675,查询出来的类型始终是BigDecimal，所以只能这样强制转换一下
    List<BigDecimal> removeRecommendedIds = friendReqRecordDao.getReceivePsnId(psnId);
    List<Long> recommendedIds = new ArrayList<Long>();
    for (BigDecimal removeRecommendedId : removeRecommendedIds) {
      recommendedIds.add(removeRecommendedId.longValue());
    }
    if (CollectionUtils.isNotEmpty(frdRcmdList)) {
      // 最大500人_zk
      if (frdRcmdList.size() <= 500) {
        list.addAll(frdRcmdList);
      } else {
        frdRcmdList.subList(500, frdRcmdList.size()).clear();
        list.addAll(frdRcmdList);
      }
      // 排除掉不允许任何人加联系人的人员的ID
      List<Long> canNotAddFr = privacySettingsDao.getPrivacySettingByPsnIds(list, PrivacyType.ADD_FRD, 0);
      list.removeAll(recommendedIds);
      list.removeAll(canNotAddFr);
    }
    if (CollectionUtils.isEmpty(list)) {
      // 先查找person表里面不排除,防止sql语句执行太慢
      /*
       * Long min = 10000L; Long max = 100000L; Long random = ThreadLocalRandom.current().nextLong(min,
       * max); Long threadLongBound = 0L; if (psnId > random) { threadLongBound = psnId -
       * ThreadLocalRandom.current().nextLong(min, max); }
       */
      list = friendTempSysDao.findPersonIds(psnId);
      // 需要排除的人员
      List<Long> notRecommendPsnIds = friendTempSysDao.getNotRecommendPsnIds(psnId);
      list.removeAll(notRecommendPsnIds);
      list.removeAll(recommendedIds);

      Collections.shuffle(list);
      /*
       * // 排除PsnPrivate人员 List<Long> psnPrivateIds = psnPrivateDao.isPsnPrivate(list);
       * list.removeAll(psnPrivateIds); // 排除Friend人员 List<Long> firendPsnIds =
       * friendDao.getFriendListByPsnId(psnId); list.removeAll(firendPsnIds); // 排除FriendTemp人员 List<Long>
       * friendTempSysPsnIds = friendTempDao.getTempFriendIds(psnId); list.removeAll(friendTempSysPsnIds);
       */
      // 排除自己
      list.remove(psnId);
      if (list.size() >= 500) {
        list.subList(500, list.size()).clear();
      }
    }
    return list;
  }

  @Override
  public void sendMail(Personal form) throws Exception {
    PsnSendMail psnSendMail = form.getPsnSendMail();
    Long userId = SecurityUtils.getCurrentUserId();
    Long receiverId = null;
    // 1：发送邀请,返回邀请ID
    Long sendReqId = addFriendReqMail(receiverId, psnSendMail.getMsg(), psnSendMail.getEmail());
    // ///////////////////////////
    // 2：发送短消息??
    Message message = new Message();
    Locale locale = LocaleContextHolder.getLocale();
    // if (receiverId != null) {
    // message.setReceivers(String.valueOf(receiverId));
    // }
    if (Locale.US.equals(locale)) {
      message.setTitle(" invites you to be contacts with him/her");
    } else {
      message.setTitle("邀请您加为联系人");
    }
    message.setSendPsnId(userId);
    message.setContent(psnSendMail.getMsg());
    message.setInviteId(sendReqId);
    message.setInviteType(0);// 0 联系人邀请类型
    messageService.sendInviteMessage(message);
    // ///////////////////////////
    // 3：发送邮件
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("inviteId", sendReqId);// 邀请Id
    map.put("psnId", userId);
    // map.put("mailId", Long.valueOf(message.getMailId()));
    // map.put("inboxId", Long.valueOf(message.getRecvId()));
    // if (receiverId == null) {//
    // 收件人sendpsnId为空需要传递inviteMailbox的maidId和inviteInbox中的inboxId
    // }
    // map.put("casUrl", casUrl);
    // 新的发件方式调接口发送邮件
    restSendAddOutsideFriendMail(map);
  }

  private void restSendAddOutsideFriendMail(Map<String, Object> map) throws Exception {
    Long sendPersonId = SecurityUtils.getCurrentUserId();
    Map<String, Object> mailData = new HashMap<String, Object>();
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    FriendTemp frdTmp = friendTempDao.get(NumberUtils.toLong(map.get("inviteId") + ""));
    Person person = personDao.get(frdTmp.getPsnId());
    Integer templateCode = 10007;
    String inviteUrl = null;// 按钮请求链接地址.
    String psnShortUrl = null;// 个人站外主页地址
    String languageVersion = person.getEmailLanguageVersion();
    if (StringUtils.isBlank(languageVersion)) {
      languageVersion = LocaleContextHolder.getLocale().toString();
    }
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    info.setMailTemplateCode(templateCode);
    info.setReceiverPsnId(0L); // 0就是站外人员
    info.setSenderPsnId(sendPersonId);
    info.setReceiver(frdTmp.getSendMail());
    info.setMsg("请求站外人员加为联系人");
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializer(info));
    // 邮件内容_emailTypeKey
    mailData.put(EmailConstants.EMAIL_TYPE_KEY, EmailConstants.COMMON_EMAIL);
    // 邮件内容_email
    mailData.put("email", java.net.URLEncoder.encode(frdTmp.getSendMail(), EmailCommonService.ENCODING));
    // 邮件内容_psnName
    String psnName = getPsnNameByEmailLangage(person, languageVersion);
    mailData.put("psnName", psnName);
    // 邮件内容_inviteUrl
    // String mailId = map.get("mailId").toString();
    // String inboxId = map.get("inboxId").toString();
    PsnProfileUrl profileUrl = psnProfileUrlDao.get(frdTmp.getPsnId());
    if (profileUrl != null && StringUtils.isNotBlank(profileUrl.getPsnIndexUrl())) {
      psnShortUrl = domainscm + "/" + ShortUrlConst.P_TYPE + "/" + profileUrl.getPsnIndexUrl();
    }
    mailData.put("psnShortUrl", psnShortUrl);
    // inviteUrl = this.getUnValidatedInviteUrl(frdTmp, mailId, inboxId, languageVersion);
    inviteUrl = this.getUnValidatedInviteUrl(frdTmp, null, null, languageVersion);
    mailData.put("inviteUrl", inviteUrl);
    // 邮件内容_domainUrl
    mailData.put("domainUrl", domainscm);
    // 邮件内容_avatars
    mailData.put("avatars", person.getAvatars());
    // 邮件内容_titolo
    getTitolo(person, mailData);
    // 邮件内容_regions
    if (person.getRegionId() != null && !"".equals(person.getRegionId().toString())) {
      getRegion(person.getRegionId(), languageVersion, mailData);
    }
    List<String> linkList = new ArrayList<String>();
    MailLinkInfo l1 = new MailLinkInfo();
    l1.setKey("domainUrl");
    l1.setUrl(domainscm);
    l1.setUrlDesc("科研之友主页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));
    MailLinkInfo l2 = new MailLinkInfo();
    l2.setKey("psnShortUrl");
    l2.setUrl(psnShortUrl);
    l2.setUrlDesc("发送邀请人的主页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l2));
    MailLinkInfo l3 = new MailLinkInfo();
    l3.setKey("inviteUrl");
    l3.setUrl(inviteUrl);
    l3.setUrlDesc("接受邀请链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l3));
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
    // 主题参数，添加如下：
    List<String> subjectParamLinkList = new ArrayList<String>();
    subjectParamLinkList.add(psnName);
    mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));
    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
    restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
  }


  /**
   * 构建人员单位+部门+职称
   *
   * @param person
   * @param mailMap
   */
  private void getTitolo(Person person, Map<String, Object> mailMap) {
    String insName = person.getInsName();
    String department = person.getDepartment();
    String position = person.getPosition();
    String insAndDep = "";
    String pos = "";
    // 构建单位+部门信息
    if (StringUtils.isBlank(insName) || StringUtils.isBlank(department)) {
      insAndDep = (StringUtils.isBlank(insName) ? "" : insName) + (StringUtils.isBlank(department) ? "" : department);
    } else {
      insAndDep = insName + ", " + department;
    }
    // 构建职称+头衔信息
    if (StringUtils.isNotBlank(position)) {
      pos = position;
    }
    if (StringUtils.isBlank(insAndDep) || StringUtils.isBlank(pos)) {
      mailMap.put("titolo", (StringUtils.isBlank(insAndDep) ? "" : insAndDep) + (StringUtils.isBlank(pos) ? "" : pos));
    } else {
      mailMap.put("titolo", (insAndDep + ", " + pos));
    }
  }

  /**
   * 根据设置邮件的接收语言，获取地区
   *
   * @param regionId
   * @param languageVersion
   * @param mailMap
   */
  private void getRegion(Long regionId, String languageVersion, Map<String, Object> mailMap) {
    ConstRegion constRegion = constRegionDao.findRegionNameById(regionId);
    if (constRegion != null) {
      if ("zh_CN".equals(languageVersion)) {
        mailMap.put("regions", constRegion.getZhName());
      } else {
        mailMap.put("regions", constRegion.getEnName());
      }
    }

  }

  /**
   * 根据设置邮件的接收语言，获取用户名
   *
   * @param person
   * @param emailLangage
   * @return
   */
  private String getPsnNameByEmailLangage(Person person, String emailLangage) {
    if (person == null) {
      return null;
    }
    if (emailLangage == null) {
      emailLangage = LocaleContextHolder.getLocale().toString();
    }
    String psnName = "";
    if ("zh_CN".equals(emailLangage)) {
      psnName = person.getName();
      if (StringUtils.isBlank(psnName)) {
        psnName = person.getFirstName() + " " + person.getLastName();
      }
    } else {
      psnName = person.getFirstName() + " " + person.getLastName();
      if (StringUtils.isBlank(person.getFirstName()) || StringUtils.isBlank(person.getLastName())) {
        psnName = person.getName();
      }
    }
    return psnName;
  }

  @SuppressWarnings("unchecked")
  private String getUnValidatedInviteUrl(FriendTemp frdTmp, String mailId, String inboxId, String languageVersion)
      throws Exception {
    String urlMain = domainscm + "/psnweb/friendreq/addfriend?";
    String key = this.getInviteKey();
    String inviteUrl = urlMain + "key=" + key + "&locale=" + languageVersion;

    // 拼装邀请码.
    String des3InviteId =
        java.net.URLEncoder.encode(ServiceUtil.encodeToDes3(frdTmp.getId().toString()), EmailCommonService.ENCODING);
    String des3PsnId =
        java.net.URLEncoder.encode(ServiceUtil.encodeToDes3(frdTmp.getPsnId().toString()), EmailCommonService.ENCODING);
    // String des3MailId = java.net.URLEncoder.encode(ServiceUtil.encodeToDes3(mailId),
    // EmailCommonService.ENCODING);
    // String des3InboxId = java.net.URLEncoder.encode(ServiceUtil.encodeToDes3(inboxId),
    // EmailCommonService.ENCODING);
    /*
     * Map<String, String> paramMap = MapBuilder.getInstance().put(INVITATION_PARAM_INVITEID,
     * des3InviteId) .put(INVITATION_PARAM_NODEID, "1").put(INVITATION_PARAM_MAILID, des3MailId)
     * .put(INVITATION_PARAM_INBOXID, des3InboxId).put(INVITATION_PARAM_PSNID, des3PsnId).getMap();
     */

    Map<String, String> paramMap = MapBuilder.getInstance().put(INVITATION_PARAM_INVITEID, des3InviteId)
        .put(INVITATION_PARAM_NODEID, "1").put(INVITATION_PARAM_PSNID, des3PsnId).getMap();
    String invitationCode = this.getInvitationCodeByRule(paramMap);
    // 更新保存邀请码.
    frdTmp.setInviteCode(invitationCode);
    friendTempDao.save(frdTmp);
    inviteUrl += "&invitationCode=" + invitationCode;

    return inviteUrl;
  }

  /**
   * 获取邀请的类型标示.
   *
   * @return
   * @throws UnsupportedEncodingException
   */
  private String getInviteKey() throws UnsupportedEncodingException {
    String key = java.net.URLEncoder.encode(ServiceUtil.encodeToDes3(FRIEND_INVITE_KEY), EmailCommonService.ENCODING);
    return key;
  }

  /**
   * 生成联系人邀请的邀请码.
   *
   * @param paramMap
   * @return
   */
  private String getInvitationCodeByRule(Map<String, String> paramMap) {
    String[] rule = FRIEND_INVITATION_CODE_RULE.split("-");
    String invitationCode = FRIEND_INVITATION_CODE_RULE;
    for (String iName : rule) {
      invitationCode = invitationCode.replaceAll(iName, paramMap.get(iName));
    }
    return invitationCode;
  }

  /**
   * 获取请求加为联系人的邮件标题.
   *
   * @param psName
   * @param languageVersion
   * @return
   */
  private String getInviteMailSubject(String psName, String languageVersion) {
    String subject = null;
    if (Locale.CHINA.toString().equals(languageVersion)) {
      subject = psName + "邀请你加入科研之友";
    } else {
      subject = psName + " invites you to join ScholarMate";
    }
    return subject;
  }

  private Long addFriendReqMail(Long receiverId, String msg, String sendMail) {
    try {
      if (StringUtils.isBlank(sendMail)) {
        return null;
      }
      Long userId = SecurityUtils.getCurrentUserId();
      FriendTemp friendTemp = new FriendTemp();
      friendTemp.setPsnId(userId);
      if (receiverId != null) {
        friendTemp.setTempPsnId(receiverId);
      }
      if (StringUtils.isNotBlank(msg)) {
        friendTemp.setReqContent(msg.trim());
      }
      friendTemp.setSendMail(sendMail.trim());
      friendTempDao.save(friendTemp);
      return friendTemp.getId();
    } catch (Exception e) {
      logger.error("发送邮件——添加联系人请求出错", e);
    }
    return null;
  }

  /**
   * 取消发送请求
   */
  @Override
  public void removeAddFriendReq(Long receiverId) throws Exception {
    Long sendPsnId = SecurityUtils.getCurrentUserId();
    if (receiverId != null && sendPsnId != null) {
      Date date = new Date();
      friendTempDao.delTempFriend(sendPsnId, receiverId);
      friendReqRecordDao.updateStatus(sendPsnId, receiverId, 5, date);

      /* msgRelationDao.delAddFriendMsg(sendPsnId, receiverId); */
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Long> recommendPsn(PsnListViewForm form) throws Exception {
    List<Long> list = null;
    // 先取缓存
    if (form.getPsnId() != null) {
      list = getRecommendByPsnCache(form.getPsnId());
    } else {
      list = getRecommendPsnCache();
    }
    if (CollectionUtils.isEmpty(list)) {
      List<Long> sublist = getRecommendPsnIds(form.getPsnId(), list);
      list = new ArrayList<Long>();// list为null时,不能list.addAll
      list.addAll(sublist);// 把subList转换为ArrayList，因为subList方法出来的List是RandomAccessSubList，没有序列化的
      if (form.getPsnId() != null) {
        putRecommendByPsnCache(list, form.getPsnId());
      } else {
        putRecommendPsnCache(list);
      }
    }
    if (CollectionUtils.isNotEmpty(list)) {
      friendDao.queryRecommendPsn(list, form);
    }
    return form.getPage().getResult();
  }

  /**
   * 获取未被处理的历史联系人请求的Id
   *
   * @return
   */
  @Override
  public List<Long> findSendFriendReqHistory(PsnListViewForm form) throws Exception {
    List<Long> foreIds = new ArrayList<Long>();
    List<Long> reIds = new ArrayList<Long>();
    List<Long> receiverIds = friendTempDao.getTempFriendIds(form.getPsnId());
    if (CollectionUtils.isNotEmpty(receiverIds)) {
      for (Long reId : receiverIds) {
        if (!reIds.contains(reId)) {
          reIds.add(reId);
        }
      }
      int sendCount = reIds.size();
      form.setSendCount(sendCount);
      if (sendCount > 3) {
        for (int i = 0; i < 3; i++) {
          foreIds.add(reIds.get(i));
        }
        if (form.getIsAll() == 1) {// 查看更多
          reIds.removeAll(foreIds);
          return reIds;
        } else {
          return foreIds;
        }
      } else {
        return reIds;// 小于三条
      }
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  private List<Long> getRecommendPsnCache() {
    // SCM-16289
    return (List<Long>) cacheService.get(RECOMMEND_PSN_CACHE, RECOMMEND_PSNIDS + SecurityUtils.getCurrentUserId());
  }

  // 他人查看的时候 缓存得是 他人的ID
  private List<Long> getRecommendByPsnCache(Long psnId) {

    return (List<Long>) cacheService.get(RECOMMEND_PSN_CACHE, RECOMMEND_PSNIDS + psnId);
  }

  // 他人查看的时候 缓存得是 他人的ID
  @SuppressWarnings("rawtypes")
  private void putRecommendByPsnCache(List<Long> recommendPsnIds, Long psnId) {// ArrayList<Long>
    // SCM-16289
    cacheService.put(RECOMMEND_PSN_CACHE, CacheService.EXP_HOUR_1, RECOMMEND_PSNIDS + psnId,
        (ArrayList) recommendPsnIds);
  }

  @SuppressWarnings("rawtypes")
  private void putRecommendPsnCache(List<Long> recommendPsnIds) {// ArrayList<Long>
    // SCM-16289
    cacheService.put(RECOMMEND_PSN_CACHE, CacheService.EXP_HOUR_1, RECOMMEND_PSNIDS + SecurityUtils.getCurrentUserId(),
        (ArrayList) recommendPsnIds);
  }

  private Map<String, Object> buildSendMsg(Long psnId, Long receivedPsnID) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    if (psnId == null || receivedPsnID == null) {
      return map;
    }
    Map<String, Object> dataMap = new HashMap<String, Object>();
    map.put("openid", "99999999");
    map.put("token", "00000000msg77msg");
    dataMap.put(MsgConstants.MSG_TYPE, "7");
    dataMap.put(MsgConstants.MSG_SENDER_ID, psnId);
    dataMap.put(MsgConstants.MSG_RECEIVER_IDS, receivedPsnID.toString());
    dataMap.put(MsgConstants.MSG_CONTENT, "我接受了你的联系人请求，现在我们可以开始聊天了");
    dataMap.put(MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE, "text");
    map.put("data", JacksonUtils.mapToJsonStr(dataMap));
    return map;
  }

  @Override
  public List<FriendTemp> checkFriendTempExists(Long reqPsnId, Long currentPsnId) {
    List<FriendTemp> friendTempList = friendTempDao.checkFriendTempExists(currentPsnId, reqPsnId);

    return friendTempList;
  }

  @Override
  public String acceptAddFriendRequest(Long reqPsnId, Long currentPsnId) throws ServiceException {
    String result = "";
    try {
      // 检查两人是否已经是联系人
      boolean isFriend = this.isFriend(currentPsnId, reqPsnId);
      // 检查联系人请求记录是否还存在
      List<FriendTemp> friendTempList = friendTempDao.checkFriendTempExists(currentPsnId, reqPsnId);
      // 添加联系人请求记录不存在的话直接返回
      if (CollectionUtils.isEmpty(friendTempList)) {
        // 查询是否是对方取消的请求
        boolean isCancelReq = friendReqRecordDao.findDealReqPsnIds(reqPsnId, currentPsnId);
        if (isCancelReq) {
          result = "msg";
        } else {
          result = "overdue";
        }
        return result;
      }
      // 两人还不是联系人
      if (!isFriend) {
        // 处理自己和联系人的相关数据
        this.acceptSomeOneAddFriendRequest(currentPsnId, reqPsnId);
        this.acceptSomeOneAddFriendRequest(reqPsnId, currentPsnId);
        // 添加加联系人添加记录NEW_ADD_FRIEND_LOG-------由于该表生产的记录在13年12月后就没有处理过了，故不操作
        // 发送邮件
        this.restSendApproveAddFriendRequestEmail(currentPsnId, reqPsnId);
        Map<String, Object> map = buildSendMsg(currentPsnId, reqPsnId);
        restTemplate.postForObject(this.openResfulUrl, map, Object.class);
      }
      // 删除掉联系人请求记录
      friendTempDao.deleteFriendTemp(currentPsnId, reqPsnId);
      friendTempDao.deleteFriendTemp(reqPsnId, currentPsnId);
      // 重置联系人请求消息状态为同意_____按sxj要求,联系人请求列表的忽略或同意操作都不用更新v_msg_relation表
      // 重置friend_req_record状态为接受, 若互相发送了联系人请求，将自己发送的请求置为4
      friendReqRecordDao.updateStatus(reqPsnId, currentPsnId, FriendReqConst.FRIEND_REQ_AGREE, new Date());
      friendReqRecordDao.updateStatus(currentPsnId, reqPsnId, FriendReqConst.FRIEND_REQ_DEAL_ONE, new Date());
      result = "success";
    } catch (Exception e) {
      logger.error("接受联系人请求处理出错， 当前登录用户ID为：" + currentPsnId + ", 请求加为联系人的用户ID为：" + reqPsnId, e);
      throw new ServiceException(e);
    }
    return result;
  }

  /**
   * 构造一个Friend对象
   *
   * @param psnId
   * @return
   */
  private Friend buildPsnFriend(Long psnId, Long friendId) {
    Friend friend = new Friend();
    friend.setPsnId(psnId);
    friend.setFriendPsnId(friendId);
    friend.setFriendNode(1);
    friend.setStatus(0);
    friend.setCreateDate(new Date());
    return friend;
  }

  @Override
  public void acceptSomeOneAddFriendRequest(Long psnId, Long reqPsnId) throws ServiceException {
    try {
      // 获取当前联系人数
      Long psnFrdSum = friendDao.getFriendCount(psnId);
      // 构造保存Friend记录
      Friend friend = this.buildPsnFriend(psnId, reqPsnId);
      friendDao.save(friend);
      // 标记需要刷新合作者推荐
      psnCoRmcRefreshDao.setPsnCoRmcRefresh(psnId);
      // 更新联系人统计数（sns和rcmd）
      Integer frdSum = psnFrdSum.intValue() + 1;// 联系人数加一
      psnStatisticsService.updatePsnStatisticsByFrd(psnId, frdSum);
      // 同步到rcmd
      RcmdSyncPsnInfo rsp = rcmdSyncPsnInfoDao.get(psnId);
      if (rsp == null) {
        rsp = new RcmdSyncPsnInfo(psnId);
      }
      rsp.setFriendFlag(1);
      rcmdSyncPsnInfoDao.save(rsp);
      // 添加关注
      userSettingService.buildAndSaveAttPerson(psnId, reqPsnId);
      // 处理动态，让动态互相可见 ---------由于现在的动态逻辑是生成动态的时候才会
      // 后台论文添加动态 2019-03
      dynamicHandlerService.addAttentionVisible(psnId, reqPsnId);

    } catch (Exception e) {
      logger.error("添加联系人处理出错, psnId=" + psnId, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 发送好友邀请成功邮件
   *
   * @param senderId
   * @param receiverId
   */
  private void restSendApproveAddFriendRequestEmail(Long senderId, Long receiverId) {
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, String> mailData = new HashMap<String, String>();
    try {
      // 获取发送者和接收者信息
      Person sender = personDao.findPsnInfoForEmail(senderId);
      Person receiver = personDao.findPsnInfoForEmail(receiverId);
      if (sender != null && receiver != null) {
        // 设置语言版本
        String receiverLocal = receiver.getEmailLanguageVersion();
        if (StringUtils.isBlank(receiverLocal)) {
          receiverLocal = LocaleContextHolder.getLocale().toString();
        }
        // 获取姓名
        String senderName = personManager.getPsnName(sender, receiverLocal);
        String receiverName = personManager.getPsnName(receiver, receiverLocal);
        // 构建人员单位部门职称
        String senderInsInfo = personManager.getPsnWrokInfo(sender, receiverLocal);
        // 构建地区信息
        String regionInfo = personManager.getPsnRegionInfo(sender, receiverLocal);
        // templateCode
        Integer templateCode = 10091;
        // 个人主页短地址
        String psnHomeUrl = "";
        PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(sender.getPersonId());
        if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
          psnHomeUrl = domainscm + "/" + ShortUrlConst.P_TYPE + "/" + psnProfileUrl.getPsnIndexUrl();
        }
        // 构造必需的参数
        MailOriginalDataInfo info = new MailOriginalDataInfo();
        info.setMsg("好友邀请成功邮件");
        info.setMailTemplateCode(templateCode);
        info.setReceiver(receiver.getEmail());
        info.setSenderPsnId(senderId);
        info.setReceiverPsnId(receiverId);
        paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializer(info));
        // 站内信链接
        String inboxMsgUrl = domainscm + "/dynweb/showmsg/msgmain?model=chatMsg&des3ChatPsnId="
            + ServiceUtil.encodeToDes3(senderId.toString());
        // 地区信息
        mailData.put("regions", regionInfo);
        mailData.put("inboxMsgUrl", inboxMsgUrl);
        // 头像
        mailData.put("avatars", sender.getAvatars());
        // 职位信息
        mailData.put("titolo", senderInsInfo);
        // 发件人姓名
        mailData.put("confirmPsnName", senderName);
        // 收件人姓名
        mailData.put("psnName", receiverName);
        // 定义要跟踪的链接集，系统会生成短地址并跟踪访问记录
        List<String> linkList = new ArrayList<String>();
        MailLinkInfo l1 = new MailLinkInfo();
        l1.setKey("domainUrl");
        l1.setUrl(domainscm);
        l1.setUrlDesc("科研之友主页");
        linkList.add(JacksonUtils.jsonObjectSerializer(l1));
        // 站内信链接
        MailLinkInfo l2 = new MailLinkInfo();
        l2.setKey("inboxMsgUrl");
        l2.setUrl(inboxMsgUrl);
        l2.setUrlDesc("站内信链接");
        linkList.add(JacksonUtils.jsonObjectSerializer(l2));
        // 发件人主页地址----短地址
        MailLinkInfo l3 = new MailLinkInfo();
        l3.setKey("recvUrl");
        l3.setUrl(psnHomeUrl);
        l3.setUrlDesc("发件人主页地址");
        linkList.add(JacksonUtils.jsonObjectSerializer(l3));
        mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
        // 主题参数，添加如下：
        List<String> subjectParamLinkList = new ArrayList<String>();
        subjectParamLinkList.add(senderName);
        mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));
        paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
        restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
      }
    } catch (Exception e) {
      logger.error("发送好友邀请成功邮件失败， 同意者ID=" + senderId + ", 请求加联系人者ID=" + receiverId, e);
    }
  }

  @Override
  public void addFriendTempSys(PsnListViewForm form) throws Exception {
    FriendTempSys friendTempSys = friendTempSysDao.getFriendTempSys(form.getPsnId(), form.getTempPsnId());
    if (friendTempSys == null) {
      friendTempSys = new FriendTempSys(form.getPsnId(), form.getTempPsnId());
      friendTempSysDao.save(friendTempSys);
    }
  }

  @Override
  public List<Long> showFriendRequest(PsnListViewForm form) throws Exception {
    List<Long> friendIds = new ArrayList<Long>();
    List<Long> tempPsnIds = friendTempDao.getPsnIdsByTempPsnId(form.getPsnId());
    if (CollectionUtils.isNotEmpty(tempPsnIds)) {
      for (Long psnId : tempPsnIds) {
        if (!friendIds.contains(psnId)) {
          friendIds.add(psnId);
        }
      }
    }
    return friendIds;
  }

  @Override
  public void negletFriendReq(PsnListViewForm form) throws Exception {
    // 1.请求记录置为忽略_____按sxj要求,联系人请求列表的忽略或同意操作都不用更新v_msg_relation表
    // ==================================此处为联系人请求列表忽略操作,所以发送者id为联系人id,接受者id为本人,所以这里两者条件调换
    // List<MsgRelation> msgList =
    // msgRelationDao.findMsgRelations(form.getTempPsnId(), form.getPsnId(),
    // 1, 0);
    // if (CollectionUtils.isNotEmpty(msgList)) {
    // for (MsgRelation msgRelation : msgList) {
    // msgRelation.setDealStatus(2);
    // msgRelation.setDealDate(new Date());
    // msgRelationDao.save(msgRelation);
    // }
    // }
    // 2.删除请求记录
    // ==此处为联系人请求列表忽略操作,所以发送者id为联系人id,接受者id为本人,所以这里两者条件调换
    friendTempDao.delTempFriend(form.getTempPsnId(), form.getPsnId());
    // 3.在发送请求和其他的联系人请求处理的时候都得更新下这个表friend_req_record
    friendReqRecordDao.updateStatus(form.getTempPsnId(), form.getPsnId(), 2, new Date());
  }

  @SuppressWarnings("rawtypes")
  @Override
  public void getMsgChatPsnList(PsnListViewForm form) throws Exception {
    if ("friend".equals(form.getPsnTypeForChat())) {
      List<Object[]> list =
          personDao.findFriendList(SecurityUtils.getCurrentUserId(), form.getSearchKey(), form.getPage());
      buildFriendListInfoForMsg(list, form);
    } else if ("all".equals(form.getPsnTypeForChat())) {
      List<Person> personList = personDao.findPersonList(form.getSearchKey(), form.getPage());
      buildAllListInfoForMsg(personList, form);
    }
  }

  private void buildAllListInfoForMsg(List<Person> personList, PsnListViewForm form) {
    if (CollectionUtils.isNotEmpty(personList)) {
      form.setPsnInfoList(new ArrayList<PsnInfo>());
      PsnInfo psnInfo = null;
      for (Person p : personList) {
        psnInfo = new PsnInfo();
        psnInfo.setPsnId(p.getPersonId());
        psnInfo.setZhName(p.getName() != null ? p.getName()
            : (p.getFirstName() != null ? p.getFirstName() : "") + (p.getLastName() != null ? p.getLastName() : ""));
        psnInfo.setEnName(p.getEname() != null ? p.getEname() : "");
        psnInfo.setAvatarUrl(p.getAvatars());
        form.getPsnInfoList().add(psnInfo);
      }
    }
  }

  private void buildFriendListInfoForMsg(List<Object[]> list, PsnListViewForm form) {
    if (CollectionUtils.isNotEmpty(list)) {
      form.setPsnInfoList(new ArrayList<PsnInfo>());
      PsnInfo psnInfo = null;
      for (Object[] o : list) {
        psnInfo = new PsnInfo();
        psnInfo.setPsnId(((BigDecimal) (o[0])).longValue());
        buildPsnInfoName(o, psnInfo);
        psnInfo.setAvatarUrl(o[5] != null ? (String) (o[5]) : "");
        psnInfo.setInsName(o[6] != null ? (String) (o[6]) : "");
        form.getPsnInfoList().add(psnInfo);
      }
    }
  }

  private void buildPsnInfoName(Object[] o, PsnInfo psnInfo) {
    psnInfo.setZhName(o[1] != null ? (String) (o[1])
        : (o[3] != null ? (String) (o[3]) + " " : "") + (o[4] != null ? (String) (o[4]) : ""));
    psnInfo.setEnName(o[2] != null ? (String) (o[2]) : "");
    if (StringUtils.isBlank(psnInfo.getZhName())) {
      psnInfo.setZhName(psnInfo.getEnName());
    }
    if (StringUtils.isBlank(psnInfo.getEnName())) {
      psnInfo.setEnName(psnInfo.getZhName());
    }
  }

  @Override
  public Long getReqFriendNumber(Long psnId) throws Exception {
    Long psnFrdSum = friendTempDao.getReqFriendNumber(psnId);
    return psnFrdSum;
  }

  @Override
  public void getVistStatisticsPsnList(PsnListViewForm form) throws Exception {
    Long userId = SecurityUtils.getCurrentUserId();
    vistStatisticsDao.getVistPsnList(form.getPage(), userId);
    List<Long> result = form.getPage().getResult();
    if (CollectionUtils.isNotEmpty(result)) {
      form.setPsnInfoList(new ArrayList<PsnInfo>());
      PsnInfo psnInfo = null;
      String locale = LocaleContextHolder.getLocale().toString();
      for (Long id : result) {
        psnInfo = new PsnInfo();
        Person person = personDao.findPersonBaseIncludeIns(id);
        if ("en_US".equals(locale)) {
          psnInfo.setName(person.getEname());
          if (StringUtils.isBlank(psnInfo.getName())) {
            psnInfo.setName((person.getFirstName() == null ? "" : person.getFirstName() + " ")
                + (person.getLastName() == null ? "" : person.getLastName()));
          }
          if (StringUtils.isBlank(psnInfo.getName())) {
            psnInfo.setName(person.getName());
          }
        } else {
          psnInfo.setName(person.getName());
          if (StringUtils.isBlank(psnInfo.getName())) {
            psnInfo.setName((person.getFirstName() == null ? "" : person.getFirstName() + " ")
                + (person.getLastName() == null ? "" : person.getLastName()));
          }
          if (StringUtils.isBlank(psnInfo.getName())) {
            psnInfo.setName(person.getEname());
          }
        }
        psnInfo.setIsFriend(friendDao.isFriend(userId, id));
        psnInfo.setInsName(person.getInsName());
        psnInfo.setAvatarUrl(person.getAvatars());
        psnInfo.setPsnId(id);
        PsnProfileUrl profileUrl = psnProfileUrlDao.get(id);
        if (profileUrl != null && StringUtils.isNotEmpty(profileUrl.getPsnIndexUrl())) {
          psnInfo.setPsnShortUrl(domainscm + "/P/" + profileUrl.getPsnIndexUrl());
        } else {
          String psnUrl = domainscm + "/psnweb/homepage/show?des3PsnId=" + Des3Utils.encodeToDes3(id.toString());
          psnInfo.setPsnShortUrl(psnUrl);
        }
        form.getPsnInfoList().add(psnInfo);
      }
    }
  }

  @Override
  public void queryFriendsReq(PsnListViewForm form) throws Exception {
    List<Long> list = new ArrayList<Long>();
    List<Long> psnIds = friendTempDao.getPsnIdsByTempPsnIdPage(form.getPsnId(), form.getPage());
    if (CollectionUtils.isNotEmpty(psnIds)) {
      for (Long psnId : psnIds) {
        if (!list.contains(psnId)) {
          list.add(psnId);
        }
      }
    }
    form.getPage().setTotalCount(list.size());
    buildFriendsReqInfo(list, form, form.getIsAll());
  }

  private void buildFriendsReqInfo(List<Long> psnIds, PsnListViewForm form, int isAll) {
    if (psnIds != null && psnIds.size() > 0) {
      form.setPsnInfoList(new ArrayList<PsnInfo>());
      PsnInfo psnInfo = null;
      Person person = null;
      for (int i = 0; i < psnIds.size(); i++) {
        Long psnId = psnIds.get(i);
        person = personDao.findPersonInsAndPos(psnId);
        if (person != null) {
          psnInfo = new PsnInfo();
          psnInfo.setDes3PsnId(Des3Utils.encodeToDes3(psnId.toString()));
          psnInfo.setPerson(person);
          PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(psnId);
          if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
            psnInfo.setPsnShortUrl(domainscm + "/" + ShortUrlConst.P_TYPE + "/" + psnProfileUrl.getPsnIndexUrl());
          }
          String locale = LocaleContextHolder.getLocale().toString();
          if ("en_US".equals(locale)) {
            psnInfo.setName(person.getEname());
            if (StringUtils.isBlank(psnInfo.getName())) {
              psnInfo.setName((StringUtils.isBlank(person.getFirstName()) ? "" : person.getFirstName()) + " "
                  + (StringUtils.isBlank(person.getLastName()) ? "" : person.getLastName()));
            }
            if (StringUtils.isBlank(psnInfo.getName())) {
              psnInfo.setName(person.getName());
            }
          } else {
            psnInfo.setName(person.getName());
            if (StringUtils.isBlank(psnInfo.getName())) {
              psnInfo.setName((StringUtils.isBlank(person.getFirstName()) ? "" : person.getFirstName()) + " "
                  + (StringUtils.isBlank(person.getLastName()) ? "" : person.getLastName()));
            }
            if (StringUtils.isBlank(psnInfo.getName())) {
              psnInfo.setName(person.getEname());
            }
          }
          form.getPsnInfoList().add(psnInfo);
          if (isAll != 1 && i >= 1) {
            break;
          }
        }
      }
    }
  }

  @Override
  public List<Person> getContactfriend(String mobile) throws ServiceException {
    return personDao.getPersonByMobile(mobile);
  }

  @Override
  public Person getPsnInfo(Long psnId) {
    return personDao.get(psnId);
  }

  /**
   * 批量获取人员信息
   */
  @Override
  public List<Person> getPersonBasePage(List<Long> psnIds) throws ServiceException {
    return personDao.getPersonBasePage(psnIds);
  }

  @Override
  public List<Long> newFriendRequest(PsnListViewForm form) throws Exception {
    List<Long> foreIds = new ArrayList<Long>();
    List<Long> reqIds = new ArrayList<Long>();
    List<Long> tempPsnIds = friendTempDao.getPsnIdsByTempPsnId(form.getPsnId());
    if (CollectionUtils.isNotEmpty(tempPsnIds)) {
      // 去重
      for (Long psnId : tempPsnIds) {
        if (!reqIds.contains(psnId)) {
          reqIds.add(psnId);
        }
      }
      Integer reqCount = reqIds.size();// 联系人请求总数
      form.setReqCount(reqCount);
      if (form.getIsAll() == 0 && reqCount > 3) { // 默认只显示前3个
        for (int i = 0; i < 3; i++) {
          foreIds.add(reqIds.get(i));
        }
      } else { // 点击更多，显示全部
        foreIds.addAll(reqIds);
      }
      return foreIds;
    }
    return null;
  }

  @Override
  public void updatePersonInfo(SnsPersonSyncMessage msg) {
    try {
      SyncPerson syncPerson = syncPersonDao.get(msg.getPsnId());
      if (syncPerson != null) {
        syncPerson.setPsnName(msg.getNameByLang());
        // 提取姓名首字母
        Character c = this.getFirstLetterByName(syncPerson.getPsnName());
        syncPerson.setFirstLetter(c != null ? c.toString() : "0");
        syncPerson.setPsnFirstName(msg.getFirstName());
        syncPerson.setPsnLastName(msg.getLastName());
        syncPerson.setPsnOtherName(msg.getOtherName());
        syncPerson.setPsnTitle(msg.getTitolo());
        syncPerson.setPsnHeadUrl(msg.getAvatars());
        syncPerson.setPsnTel(msg.getTel());
        syncPerson.setPsnMobile(msg.getMobile());
        syncPerson.setPsnQQ(msg.getQqNo());
        syncPerson.setPsnMsn(msg.getMsnNo());
        syncPerson.setPsnEmail(msg.getEmail());
        syncPerson.setPsnInsName(msg.getInsName());
        syncPerson.setPsnSkype(msg.getSkype());
        if (msg.getIsSyncIns() != null && msg.getIsSyncIns()) {
          syncPerson.setRegionId(msg.getRegionId());
          syncPerson.setPsnInsIdList(HqlUtils.insIdsFormat(msg.getInsIdList()));
          if (msg.getInsNameList() != null) {
            syncPerson.setPsnInsNameList(
                msg.getInsNameList().length() > 500 ? msg.getInsNameList().substring(0, 500) : msg.getInsNameList());
          }
        }
        syncPersonDao.save(syncPerson);
      }
    } catch (Exception e) {
      logger.error("同步个人信息出错", e);
    }
  }

  /**
   * 提取群组名称首字母.
   *
   * @param name
   * @return
   */
  private Character getFirstLetterByName(String name) {
    if (name != null && name.length() > 0) {
      HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
      format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

      char[] names = name.trim().toCharArray();
      for (char c : names) {
        if (Character.isLetter(c)) {
          if (String.valueOf(c).matches("^[a-zA-Z]{1}$")) {
            return Character.toUpperCase(c);
          }
          try {
            String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
            if (pinyin != null && pinyin.length > 0) {
              return (StringUtils.upperCase((pinyin[0])).toCharArray())[0];
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }

    return null;
  }

  @Override
  public void autoFollowingPsn() throws Exception {
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId == null) {
      logger.error("动态主页显示：自动关注人员出错，没有获取到psnId!");
      return;
    }
    // 判断是否需要弹出完善信息框,如果需要弹出信息框，则不在此自动关注
    Map<String, Boolean> needImprove = psnInfoImproveService.psnHasScienceAreaAndKeywords(psnId);
    if (needImprove.get("needArea") == true || needImprove.get("needKeyword") == true
        || needImprove.get("needWorkEdu") == true) {
      return;
    }

    // 关注人数大于15人，就不再自动关注
    Integer followingPsnCount = this.attPersonDao.getFollowingPsnCount(psnId) == null ? 0
        : Integer.parseInt(this.attPersonDao.getFollowingPsnCount(psnId).toString());
    if (followingPsnCount >= 15) {
      List<Long> psnList = attPersonDao.getFollowingPsnIds(psnId);
      if (CollectionUtils.isNotEmpty(psnList)) {// 获取关注人的动态
        this.generatePsnDyn(psnId, psnList);
      }
      return;
    }
    Person person = this.personManager.getPerson(psnId);
    if (person == null) {
      logger.error("动态主页显示：自动关注人员出错，Person表没有获取到对应人员信息! psnId =" + psnId);
      return;
    }
    List<Long> followingPsnList = new ArrayList<Long>();
    String psnScienceAreasStr = this.psnScienceAreaDao.queryScienceAreaIds(psnId);
    String psnKeywordsStr = this.psnDisciplineKeyDao.getPsnKeywordsStr(psnId);
    // 最多关注50人
    followingPsnList = this.attPersonDao.getFollowingPsnIds(psnId);
    if (followingPsnList.size() < 50) {
      Map<String, Object> rsMap = this.solrIndexService.getPsnRecommendForFollow(person.getInsName(),
          psnScienceAreasStr, psnKeywordsStr, 60 - followingPsnCount, followingPsnList, psnId);
      List<Long> rsPsnIds_1 = this.getPsnIdsFromSolrResult(rsMap);
      if (rsPsnIds_1 != null && rsPsnIds_1.size() > 0) {
        followingPsnList.addAll(rsPsnIds_1);
      }
      if ((Long) rsMap.get("numFound") < 60 - followingPsnCount) {
        Map<String, Object> rsMapSuppliment = this.solrIndexService.getPsnRecommendForFollow(null, null, psnKeywordsStr,
            50 - followingPsnList.size(), followingPsnList, psnId);
        List<Long> rsPsnIds_2 = this.getPsnIdsFromSolrResult(rsMapSuppliment);
        if (rsPsnIds_2 != null && rsPsnIds_2.size() > 0) {
          for (Long one : rsPsnIds_2) {
            if (followingPsnList.size() < 50 && !psnPrivateDao.existsPsnPrivate(one)) {
              followingPsnList.add(one);
            }
          }
        }
      }
    }
    if (followingPsnList.size() > 0) {
      this.followingPsn(psnId, followingPsnList);
      // 刷新后主页显示部分最新动态
      this.generatePsnDyn(psnId, followingPsnList);
    }
  }

  private List<Long> getPsnIdsFromSolrResult(Map<String, Object> rsMap) {
    SolrDocumentList rsList = (SolrDocumentList) rsMap.get("items");
    if (rsList == null || rsList.size() == 0) {
      return null;
    }

    List<Long> psnIds = new ArrayList<Long>();
    Iterator<SolrDocument> it = rsList.iterator();
    while (it.hasNext()) {
      SolrDocument sd = it.next();
      if (sd.getFieldValue("psnId") != null) {
        Long psnId = (Long) sd.getFieldValue("psnId");
        psnIds.add(psnId);
      }
    }
    return psnIds;
  }

  private void followingPsn(Long psnId, List<Long> followedPsnIds) throws DaoException {
    List<Person> psList = this.personDao.getPersonBasePage(followedPsnIds);
    for (Person fPsn : psList) {

      AttPerson ap = this.attPersonDao.find(psnId, fPsn.getPersonId());
      if (ap == null) {
        // SCM-16395,添加记录到att_statistic
        try {
          attendStatisticsService.addAttRecord(psnId, fPsn.getPersonId(), 1);
        } catch (ServiceException e) {
          logger.error("自动关注人员，添加记录到att_statistic出错！psnid：" + psnId + ",attpsnId:" + fPsn.getPersonId());
        }
        ap = new AttPerson(psnId, fPsn.getPersonId());
        ap.setRefPsnName(fPsn.getName());
        ap.setRefFirstName(fPsn.getFirstName());
        ap.setRefLastName(fPsn.getLastName());
        ap.setRefInsName(fPsn.getInsName());
        ap.setRefHeadUrl(fPsn.getAvatars());
        ap.setRefTitle(fPsn.getTitolo());
        this.attPersonDao.save(ap);
      } else {
        continue;
      }
    }
  }

  private void generatePsnDyn(Long psnId, List<Long> followedPsnIds) {
    List<DynamicMsg> dynamicMsgList = this.dynamicMsgDao.getDynIdsByPsnIds(followedPsnIds, 50);
    for (DynamicMsg dynMsg : dynamicMsgList) {
      if ("B3TEMP".equalsIgnoreCase(dynMsg.getDynType()) && !checkAddRefDynPermit(dynMsg.getProducer())) {
        // 隐私动态，
        continue;
      }
      DynamicRelationPk pk = new DynamicRelationPk(dynMsg.getDynId(), psnId);
      if (dynamicRelationDao.getDynRelation(pk) == null) {
        DynamicRelation relation = new DynamicRelation();
        relation.setId(pk);
        relation.setDealStatus(0);
        dynamicRelationDao.save(relation);
      }
    }
  }

  /**
   * 检查上传文献的权限， 2=仅本人
   *
   * @param producer
   * @return
   */
  public Boolean checkAddRefDynPermit(Long producer) {
    PrivacySettingsPK pk = new PrivacySettingsPK();
    pk.setPsnId(producer);
    pk.setPrivacyAction("vMyLiter");
    PrivacySettings ps = privacySettingsDao.get(pk);
    if (ps != null && ps.getPermission() == 2) {
      return false;
    }
    return true;
  }

  @Override
  public boolean IsExistsEmail(String email) {
    User user = userDao.findByLoginName(email);
    if (user != null) {
      return true;
    }
    return false;
  }

  @Override
  public List<Long> getInviteFriendIds(Long pdwhPubId, Long psnId) throws Exception {
    List<Long> resultList = new ArrayList<Long>();
    // Long psnId = SecurityUtils.getCurrentUserId();
    List<Long> snsPubIdList = pubPdwhSnsRelationDao.getSnsPubIdListByPdwhId(pdwhPubId);
    if (CollectionUtils.isNotEmpty(snsPubIdList)) {
      // 1.查找这些成果拥有者(排除自己)
      List<Long> pubOwnerPsnIds = psnPubDAO.getPubOwnerPsnIds(snsPubIdList, psnId);
      if (CollectionUtils.isNotEmpty(pubOwnerPsnIds)) {
        // 2.排除不符合条件的psnId
        // 2.1排除联系人id
        List<Long> friendIds = friendDao.getFriendListByPsnId(psnId);
        if (CollectionUtils.isNotEmpty(friendIds)) {
          pubOwnerPsnIds.removeAll(friendIds);
        }
        // 2.2排除发送过联系人请求id
        if (CollectionUtils.isNotEmpty(pubOwnerPsnIds)) {
          List<Long> tempFriendIds = friendTempDao.getTempFriendIdsByTime(psnId, 3L);
          if (CollectionUtils.isNotEmpty(tempFriendIds)) {
            pubOwnerPsnIds.removeAll(tempFriendIds);
          }
        }
        // 2.3排除3天内已添加联系人记录id
        if (CollectionUtils.isNotEmpty(pubOwnerPsnIds)) {
          List<Long> reqRecordList = friendReqRecordDao.getFriendReqRecordByTime(psnId, 3L);
          if (CollectionUtils.isNotEmpty(reqRecordList)) {
            pubOwnerPsnIds.removeAll(reqRecordList);
          }
        }
        // 2.4 排除隐私人员
        if (CollectionUtils.isNotEmpty(pubOwnerPsnIds)) {
          List<Long> psnPrivateIds = psnPrivateDao.isPsnPrivate(pubOwnerPsnIds);
          if (CollectionUtils.isNotEmpty(psnPrivateIds)) {
            pubOwnerPsnIds.removeAll(psnPrivateIds);
          }
        }
        // 2.5最后获取允许加联系人的id
        if (CollectionUtils.isNotEmpty(pubOwnerPsnIds)) {
          resultList =
              privacySettingDao.getPsnIdsByPrivacySetting(pubOwnerPsnIds, PublicPrivacyServiceImpl.reqAddFrd, 0);
        }
      }
    }
    return resultList;
  }

  @Override
  public Long getPsnFriendCount(Long psnId) throws ServiceException {
    return friendDao.getFriendCount(psnId);
  }

  @Override
  public Map<String, String> dealWithAddFriendReq(FriendForm form) throws ServiceException {
    Map<String, String> resultMap = new HashMap<String, String>();
    String status = "error";
    // 先校验参数
    Map<String, Object> checkResult = checkAddFriendParams(form);
    String errorMsg = Objects.toString(checkResult.get("errorMsg"), "");
    if (StringUtils.isBlank(errorMsg)) {
      List<Long> psnIds = (List<Long>) checkResult.get("psnIds");
      List<Long> canReceiveFriendReq = (List<Long>) checkResult.get("canReceiveFriendReq");
      for (Long psnId : canReceiveFriendReq) {
        addFriendReq(psnId, domainscm, new Personal().setPsnId(form.getPsnId()));
      }
      // SCM-6680,发送添加联系人请求成功后清除缓存中的该对象
      List<Long> knowpsnList = getRecommendPsnCache();
      if (CollectionUtils.isNotEmpty(knowpsnList)) {
        knowpsnList.removeAll(psnIds);
        putRecommendPsnCache(knowpsnList);
      }
      // 统计不接收好友请求人数
      psnIds.removeAll(canReceiveFriendReq);
      status = "success";
      resultMap.put("privateCount", Objects.toString(psnIds.size()));
    }
    resultMap.put("result", status);
    resultMap.put("errorMsg", errorMsg);
    resultMap.put("errorCode", Objects.toString(checkResult.get("errorCode"), ""));
    return resultMap;
  }

  // 校验加好友的参数
  private Map<String, Object> checkAddFriendParams(FriendForm form) {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    // 请求添加好友人员加密ID为空
    if (StringUtils.isBlank(form.getDes3Id())) {
      resultMap.put("errorMsg", "request friend des3Ids is null");
      resultMap.put("errorCode", "1");
      return resultMap;
    }
    // 先转换成list
    List<Long> psnIds = new ArrayList<Long>();
    String[] reqPsnIds = form.getDes3Id().split(",");
    for (String psnIdStr : reqPsnIds) {
      Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(psnIdStr), 0L);
      if (NumberUtils.isNotNullOrZero(psnId)) {
        psnIds.add(psnId);
      }
    }
    // 转换成正常人员ID为空
    if (CollectionUtils.isEmpty(psnIds)) {
      resultMap.put("errorMsg", "request friend ids is null");
      resultMap.put("errorCode", "2");
      return resultMap;
    }
    // 请求的好友是自己
    psnIds.remove(form.getPsnId());
    if (CollectionUtils.isEmpty(psnIds)) {
      resultMap.put("errorMsg", "request friend is yourself");
      resultMap.put("errorCode", "3");
      return resultMap;
    }
    // 所有请求的好友都不接收好友请求
    List<Long> canReceiveFriendReq =
        privacySettingDao.getPsnIdsByPrivacySetting(psnIds, PublicPrivacyServiceImpl.reqAddFrd, 0);
    if (CollectionUtils.isEmpty(canReceiveFriendReq)) {
      resultMap.put("errorMsg", "all psn do not want receive request");
      resultMap.put("errorCode", "4");
      return resultMap;
    }
    resultMap.put("psnIds", psnIds);
    resultMap.put("canReceiveFriendReq", canReceiveFriendReq);
    return resultMap;
  }

}
