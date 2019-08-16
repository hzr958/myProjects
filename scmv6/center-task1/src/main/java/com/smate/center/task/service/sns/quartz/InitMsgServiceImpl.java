package com.smate.center.task.service.sns.quartz;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.msg.InitMsgFormDao;
import com.smate.center.task.dao.sns.msg.MsgChatRelationDao;
import com.smate.center.task.dao.sns.msg.MsgContentDao;
import com.smate.center.task.dao.sns.msg.MsgRelationDao;
import com.smate.center.task.dao.sns.msg.PubFullTextReqBaseDao;
import com.smate.center.task.dao.sns.msg.PubFulltextReqRecvDao;
import com.smate.center.task.dao.sns.quartz.GrpBaseInfoDao;
import com.smate.center.task.dao.sns.quartz.GrpFileDao;
import com.smate.center.task.dao.sns.quartz.GrpIndexUrlDao;
import com.smate.center.task.dao.sns.quartz.PubFulltextDao;
import com.smate.center.task.dao.sns.quartz.PubIndexUrlDao;
import com.smate.center.task.dao.sns.quartz.PubSimpleDao;
import com.smate.center.task.dao.sns.quartz.StationFileDao;
import com.smate.center.task.model.grp.GrpBaseinfo;
import com.smate.center.task.model.sns.msg.InitMsgForm;
import com.smate.center.task.model.sns.msg.MsgChatRelation;
import com.smate.center.task.model.sns.msg.MsgContent;
import com.smate.center.task.model.sns.msg.MsgRelation;
import com.smate.center.task.model.sns.quartz.GrpIndexUrl;
import com.smate.center.task.model.sns.quartz.PubFulltext;
import com.smate.center.task.model.sns.quartz.PubIndexUrl;
import com.smate.center.task.model.sns.quartz.PubSimple;
import com.smate.core.base.psn.model.StationFile;
import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.pub.enums.PubFullTextReqStatusEnum;
import com.smate.core.base.pub.model.fulltext.req.PubFullTextReqBase;
import com.smate.core.base.pub.model.fulltext.req.PubFullTextReqRecv;
import com.smate.core.base.utils.constant.MsgConstants;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 消息初始化数据构造服务
 * 
 * @author zzx
 *
 */
@Service("initMsgService")
@Transactional(rollbackFor = Exception.class)
public class InitMsgServiceImpl implements InitMsgService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MsgChatRelationDao msgChatRelationDao;
  @Autowired
  private MsgRelationDao msgRelationDao;
  @Autowired
  private MsgContentDao msgContentDao;
  @Autowired
  private InitMsgFormDao initMsgFormDao;
  @Autowired
  private PubSimpleDao pubSimpleDao;
  @Autowired
  private PubFulltextDao pubFulltextDao;
  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;
  @Autowired
  private GrpBaseInfoDao grpBaseInfoDao;
  @Autowired
  private GrpIndexUrlDao grpIndexUrlDao;
  @Autowired
  private StationFileDao stationFileDao;
  @Autowired
  private GrpFileDao grpFileDao;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private PubFulltextReqRecvDao pubFulltextReqRecvDao;
  @Autowired
  private PubFullTextReqBaseDao pubFullTextReqBaseDao;

  /**
   * 获取初始化数据列表
   */
  @Override
  public List<InitMsgForm> getList(Integer batchSize) {
    return initMsgFormDao.getList(batchSize);
  }

  /**
   * 开始初始化
   */
  @Override
  public void doInitMsg(InitMsgForm imf) {
    // 0=系统消息、1=请求添加好友消息、2=成果认领、3=全文认领、4=成果推荐、5=好友推荐、6=基金推荐、
    // 7，77=站内信、8=请求加入群组消息、9=邀请加入群组消息、10=群组动向, 11=请求全文消息
    int status = 1;
    try {
      switch (imf.getType()) {
        case 1:
          buildAddFriendMsg(imf);// content==暂无
          break;
        case 2:
          break;
        case 3:
          buildPubFullTextMsg(imf);
          break;
        case 4:
          break;
        case 5:
          break;
        case 6:
          break;
        case 7:
          buildChatMsg(imf.getContent(), imf);// content==resRecId，资源类型
                                              // type
                                              // :1成果，2文献，3文件，4项目，
          break;
        case 77:
          buildChatMsg("", imf);// 数据原因，设置站内信 text为77入口，进去保存的type还是7
          break;
        case 8:
          buildGrpRequestMsg(imf.getContent(), imf);// content==grpId
          break;
        case 9:
          buildGrpInviteMsg(imf.getContent(), imf);// content==grpId
          break;
        case 10:
          break;
        case 11:
          buildMsgFullText(imf.getContent(), imf);// content==pubId
          break;
        default:
          return;
      }
    } catch (Exception e) {
      logger.error("初始化消息出错！imf.getContent()=" + imf.getContent() + ",imf.getType=" + imf.getType());
      status = 2;
    }
    initMsgFormDao.get(imf.getId()).setTaskStatus(status);
  }

  private void buildPubFullTextMsg(InitMsgForm imf) throws Exception {
    if (StringUtils.isNotBlank(imf.getContent())) {
      Long pubId = NumberUtils.toLong(imf.getContent());
      buildMsgRelationInfo(imf);
      MsgContent mc = new MsgContent();
      mc.setContentId(imf.getContentId());
      Map<String, Object> contentMap = new HashMap<String, Object>();
      contentMap.put(MsgConstants.MSG_SENDER_ID, 0);
      contentMap.put(MsgConstants.MSG_RECEIVER_IDS, imf.getReceiverId());
      contentMap.put(MsgConstants.MSG_PUB_ID, pubId);
      PubSimple pubSimple = pubSimpleDao.get(pubId);
      if (pubSimple != null) {
        contentMap.put(MsgConstants.MSG_PUB_TITLE_ZH, pubSimple.getZhTitle() == null ? "" : pubSimple.getZhTitle());
        contentMap.put(MsgConstants.MSG_PUB_TITLE_EN, pubSimple.getEnTitle() == null ? "" : pubSimple.getEnTitle());
        contentMap.put(MsgConstants.MSG_PUB_ID, pubId);
      } else {
        throw new Exception("成果不存在pubId=" + pubId);
      }
      PubIndexUrl pubIndexUrl = pubIndexUrlDao.get(pubId);
      if (pubIndexUrl != null) {
        String url = this.domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl();
        contentMap.put(MsgConstants.MSG_PUB_SHORT_URL, url);
      }
      mc.setContent(JacksonUtils.mapToJsonStr(contentMap));
      msgContentDao.insertMsgContent(mc);
    }
  }

  /**
   * 构建全文信息记录表
   * 
   * @param imf
   */
  private void buildFullTextReqInfo(InitMsgForm imf) {
    Long reqId = pubFullTextReqBaseDao.getReqId();
    PubFullTextReqBase pb = new PubFullTextReqBase();
    pb.setReqId(reqId);
    pb.setReqPsnId(imf.getSenderId());
    pb.setUpdatePsnId(imf.getReceiverId());
    pb.setPubDb(PubDbEnum.valueOf("SNS"));
    pb.setPubId(NumberUtils.toLong(imf.getContent()));
    pb.setReqDate(imf.getCreateDate());
    pb.setUpdateDate(imf.getDealDate());
    pb.setStatus(PubFullTextReqStatusEnum.UNPROCESSED);
    pubFullTextReqBaseDao.insertPubFullTextReqBase(pb);
    PubFullTextReqRecv pr = new PubFullTextReqRecv();
    pr.setReqId(reqId);
    pr.setReqPsnId(imf.getSenderId());
    pr.setRecvPsnId(imf.getReceiverId());
    pr.setUpdatePsnId(imf.getReceiverId());
    pr.setPubDb(PubDbEnum.valueOf("SNS"));
    pr.setPubId(NumberUtils.toLong(imf.getContent()));
    pr.setReqDate(imf.getCreateDate());
    pr.setUpdateDate(imf.getDealDate());
    pr.setStatus(PubFullTextReqStatusEnum.UNPROCESSED);
    pr.setMsgId(imf.getId());
    pubFulltextReqRecvDao.save(pr);

  }

  /**
   * 构造站内信
   * 
   * @param resRecId
   * @param imf
   * @throws Exception
   */
  private void buildChatMsg(String resRecId, InitMsgForm imf) throws Exception {
    if (!imf.getSenderId().equals(imf.getReceiverId())) {// 发件人不能和接收人相同
      if (StringUtils.isNotBlank(resRecId)) {
        List<Object[]> resRecInfoList = initMsgFormDao.getResRecInfoList(NumberUtils.toLong(resRecId));
        if (resRecInfoList != null && resRecInfoList.size() > 0) {
          int resType = 0;// 用来区分是成果还是文件:1成果，2文献，3文件，4项目，
          Long resId = 0L;
          for (Object[] m : resRecInfoList) {
            resType = ((Number) m[1]).intValue();
            resId = ((Number) m[0]).longValue();
            // 构造消息内容--成果--文件
            buildChatMsgInfo(resId, resType, imf);
            // 构造消息关系
            buildChatResMsgRelationInfo(imf);
          }
        }
        // 构造更新会话
        updateChatInfo(imf);
      } else if (StringUtils.isNotBlank(imf.getContent()) && !matcherContent(imf.getContent())) {
        // 构造消息内容---文本
        buildChatTextMsgInfo(imf);
        // 构造消息关系
        buildChatMsgRelationInfo(imf, 7);
        // 构造更新会话
        updateChatInfo(imf);
      } else {
        throw new Exception("烂数据");
      }

    }
  }

  /**
   * 过滤烂数据
   * 
   * @param content
   * @return
   */
  private boolean matcherContent(String content) {
    Pattern p = Pattern.compile("推荐加入群组|来自.+的好友推荐|请求成为.+的管理员|对您进行了评价|分享简历|回复你的全文请求|请求加入群组|回复您的全文请求");// 获取正则表达式中的分组，每一组小括号为一组
    Matcher m = p.matcher(content);// 进行匹配
    return m.find();
  }

  private void buildChatTextMsgInfo(InitMsgForm imf) {
    MsgContent mc = new MsgContent();
    mc.setContentId(imf.getContentId());
    Map<String, Object> contentMap = new HashMap<String, Object>();
    contentMap.put(MsgConstants.MSG_SENDER_ID, imf.getSenderId());
    contentMap.put(MsgConstants.MSG_RECEIVER_IDS, imf.getReceiverId());
    contentMap.put(MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE, "text");
    contentMap.put(MsgConstants.MSG_CONTENT, imf.getContent());
    mc.setContent(JacksonUtils.mapToJsonStr(contentMap));
    msgContentDao.insertMsgContent(mc);
  }

  /**
   * 站内信-消息内容构造
   * 
   * @param resId
   * @param resType
   * @param imf
   * @throws Exception
   */
  private void buildChatMsgInfo(Long resId, int resType, InitMsgForm imf) throws Exception {
    MsgContent mc = new MsgContent();
    Long contentId = msgContentDao.getContentId();
    imf.setContentId(contentId);
    mc.setContentId(contentId);
    Map<String, Object> contentMap = new HashMap<String, Object>();
    contentMap.put(MsgConstants.MSG_SENDER_ID, imf.getSenderId());
    contentMap.put(MsgConstants.MSG_RECEIVER_IDS, imf.getReceiverId());
    if (resType == 1 || resType == 2 || resType == 4) {// 成果
      buildChatPubInfo(resId, imf, contentMap);
    } else if (resType == 3) {// 文件
      buildChatFileInfo(resId, imf, contentMap);
    }
    contentMap.put(MsgConstants.MSG_BELONG_PERSON, true);
    contentMap.put(MsgConstants.MSG_CONTENT, "");
    mc.setContent(JacksonUtils.mapToJsonStr(contentMap));
    msgContentDao.insertMsgContent(mc);
  }

  /**
   * 站内信--消息内容---成果信息
   * 
   * @param resId
   * @param imf
   * @param contentMap
   * @throws Exception
   */
  private void buildChatPubInfo(Long resId, InitMsgForm imf, Map<String, Object> contentMap) throws Exception {
    if (!imf.getStatus().equals(2)) {
      imf.setStatus(0);
    }
    contentMap.put(MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE, "pub");
    PubIndexUrl pubIndexUrl = pubIndexUrlDao.get(resId);
    if (pubIndexUrl != null) {
      String url = domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl();
      contentMap.put(MsgConstants.MSG_PUB_SHORT_URL, url);
    }
    PubSimple pubSimple = pubSimpleDao.get(resId);
    if (pubSimple != null) {
      contentMap.put(MsgConstants.MSG_PUB_TITLE_ZH, pubSimple.getZhTitle() == null ? "" : pubSimple.getZhTitle());
      contentMap.put(MsgConstants.MSG_PUB_TITLE_EN, pubSimple.getEnTitle() == null ? "" : pubSimple.getEnTitle());
      contentMap.put(MsgConstants.MSG_PUB_BRIEF_DESC_ZH,
          pubSimple.getBriefDesc() == null ? "" : pubSimple.getBriefDesc());
      contentMap.put(MsgConstants.MSG_PUB_BRIEF_DESC_EN,
          pubSimple.getBriefDescEn() == null ? "" : pubSimple.getBriefDescEn());
      contentMap.put(MsgConstants.MSG_PUB_AUTHOR_NAME,
          pubSimple.getAuthorNames() == null ? "" : pubSimple.getAuthorNames());
      contentMap.put(MsgConstants.MSG_PUB_ID, resId);
    } else {
      imf.setStatus(2);
      // 成果不存在设置为删除的消息
    }
    PubFulltext pubFulltext = pubFulltextDao.get(resId);
    if (pubFulltext != null) {
      contentMap.put(MsgConstants.MSG_HAS_PUB_FULLTEXT, true);
      contentMap.put(MsgConstants.MSG_PUB_FULLTEXT_ID, pubFulltext.getFulltextFileId());
      contentMap.put(MsgConstants.MSG_PUB_FULLTEXT_EXT, pubFulltext.getFulltextFileExt());
      contentMap.put(MsgConstants.MSG_PUB_FULLTEXT_IMAGE_PATH,
          pubFulltext.getFulltextImagePath() == null ? "" : pubFulltext.getFulltextImagePath());
    } else {
      contentMap.put(MsgConstants.MSG_HAS_PUB_FULLTEXT, false);
    }
  }

  /**
   * 站内信--消息内容---文件信息
   * 
   * @param fileId
   * @param imf
   * @param contentMap
   * @throws Exception
   */
  private void buildChatFileInfo(Long fileId, InitMsgForm imf, Map<String, Object> contentMap) throws Exception {
    if (!imf.getStatus().equals(2)) {
      imf.setStatus(0);
    }
    contentMap.put(MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE, "file");
    StationFile stationFile = stationFileDao.get(fileId);
    if (stationFile != null) {
      contentMap.put(MsgConstants.MSG_ARCHIVE_FILE_ID, stationFile.getArchiveFileId());
      contentMap.put(MsgConstants.MSG_FILE_NAME, stationFile.getFileName());
      contentMap.put(MsgConstants.MSG_FILE_TYPE, stationFile.getFileType());
      contentMap.put(MsgConstants.MSG_FILE_PATH, stationFile.getFilePath());
      contentMap.put(MsgConstants.MSG_FILE_ID, fileId);
    } else {
      imf.setStatus(2);
      // 文件不存在设置为删除的消息
      throw new Exception("文件不存在fileId=" + fileId);
    }
  }

  /**
   * 站内信--更新会话
   * 
   * @param imf
   */
  private void updateChatInfo(InitMsgForm imf) {
    // 保存关联信息
    Date date = null;
    MsgChatRelation sendChatRelation = null;
    MsgChatRelation receiveChatRelation = null;
    sendChatRelation = msgChatRelationDao.findMsgChatRelation(imf.getSenderId(), imf.getReceiverId());
    if (sendChatRelation == null) {
      sendChatRelation = new MsgChatRelation();
      sendChatRelation.setSenderId(imf.getSenderId());
      sendChatRelation.setReceiverId(imf.getReceiverId());
      date = imf.getCreateDate();
    } else {
      Long a = 0L;
      if (sendChatRelation.getUpdateDate() != null) {
        a = sendChatRelation.getUpdateDate().getTime();
      }
      Long b = 0L;
      if (imf.getCreateDate() != null) {
        b = imf.getCreateDate().getTime();
      }
      date = a > b ? sendChatRelation.getUpdateDate() : imf.getCreateDate();
    }
    sendChatRelation.setStatus(0);
    sendChatRelation.setUpdateDate(date);
    msgChatRelationDao.save(sendChatRelation);
    receiveChatRelation = msgChatRelationDao.findMsgChatRelation(imf.getReceiverId(), imf.getSenderId());
    if (receiveChatRelation == null) {
      receiveChatRelation = new MsgChatRelation();
      receiveChatRelation.setSenderId(imf.getReceiverId());
      receiveChatRelation.setReceiverId(imf.getSenderId());
      date = imf.getCreateDate();
    } else {
      Long a = 0L;
      if (sendChatRelation.getUpdateDate() != null) {
        a = sendChatRelation.getUpdateDate().getTime();
      }
      Long b = 0L;
      if (imf.getCreateDate() != null) {
        b = imf.getCreateDate().getTime();
      }
      date = a > b ? sendChatRelation.getUpdateDate() : imf.getCreateDate();
    }
    receiveChatRelation.setStatus(0);
    receiveChatRelation.setUpdateDate(date);
    msgChatRelationDao.save(receiveChatRelation);
  }

  /**
   * 添加好友 ---消息内容构造
   * 
   * @param imf
   */
  private void buildAddFriendMsg(InitMsgForm imf) {
    buildMsgRelationInfo(imf);
    MsgContent mc = new MsgContent();
    mc.setContentId(imf.getContentId());
    Map<String, Object> contentMap = new HashMap<String, Object>();
    contentMap.put(MsgConstants.MSG_SENDER_ID, imf.getSenderId());
    contentMap.put(MsgConstants.MSG_RECEIVER_IDS, imf.getReceiverId());
    mc.setContent(JacksonUtils.mapToJsonStr(contentMap));
    msgContentDao.insertMsgContent(mc);
  }

  /**
   * 群组请求--消息内容构造
   * 
   * @param grpId
   * @param m
   */
  private void buildGrpRequestMsg(String grpId, InitMsgForm m) {
    if (StringUtils.isNotBlank(grpId)) {
      buildMsgRelationInfo(m);
      MsgContent mc = new MsgContent();
      mc.setContentId(m.getContentId());
      Map<String, Object> contentMap = new HashMap<String, Object>();
      contentMap.put(MsgConstants.MSG_SENDER_ID, m.getSenderId());
      contentMap.put(MsgConstants.MSG_RECEIVER_IDS, m.getReceiverId());
      contentMap.put(MsgConstants.MSG_REQUEST_GRP_ID, grpId);
      GrpBaseinfo grpBaseinfo = grpBaseInfoDao.get(NumberUtils.toLong(grpId));
      if (grpBaseinfo != null) {
        contentMap.put(MsgConstants.MSG_GRP_NAME, grpBaseinfo.getGrpName());
        GrpIndexUrl grpIndexUrl = grpIndexUrlDao.get(grpBaseinfo.getGrpId());
        if (grpIndexUrl != null) {
          contentMap.put(MsgConstants.MSG_GRP_SHORT_URL,
              this.domainscm + "/" + ShortUrlConst.G_TYPE + "/" + grpIndexUrl.getGrpIndexUrl());
        }
      }
      mc.setContent(JacksonUtils.mapToJsonStr(contentMap));
      msgContentDao.insertMsgContent(mc);
    }
  }

  /**
   * 群组邀请--消息内容构造
   * 
   * @param grpId
   * @param m
   */
  private void buildGrpInviteMsg(String grpId, InitMsgForm m) {
    if (StringUtils.isNotBlank(grpId)) {
      buildMsgRelationInfo(m);
      MsgContent mc = new MsgContent();
      mc.setContentId(m.getContentId());
      Map<String, Object> contentMap = new HashMap<String, Object>();
      contentMap.put(MsgConstants.MSG_SENDER_ID, m.getSenderId());
      contentMap.put(MsgConstants.MSG_RECEIVER_IDS, m.getReceiverId());
      contentMap.put(MsgConstants.MSG_RCMD_GRP_ID, grpId);
      GrpBaseinfo grpBaseinfo = grpBaseInfoDao.get(NumberUtils.toLong(grpId));
      if (grpBaseinfo != null) {
        contentMap.put(MsgConstants.MSG_GRP_NAME, grpBaseinfo.getGrpName());
        GrpIndexUrl grpIndexUrl = grpIndexUrlDao.get(grpBaseinfo.getGrpId());
        if (grpIndexUrl != null) {
          contentMap.put(MsgConstants.MSG_GRP_SHORT_URL,
              this.domainscm + "/" + ShortUrlConst.G_TYPE + "/" + grpIndexUrl.getGrpIndexUrl());
        }
      }
      mc.setContent(JacksonUtils.mapToJsonStr(contentMap));
      msgContentDao.insertMsgContent(mc);
    }
  }

  /**
   * 构造消息关系信息--一般通用
   * 
   * @param i
   */
  private void buildMsgRelationInfo(InitMsgForm i) {
    MsgRelation m = new MsgRelation();
    m.setId(i.getId());
    m.setSenderId(i.getSenderId());
    m.setReceiverId(i.getReceiverId());
    m.setContentId(i.getContentId());
    m.setCreateDate(i.getCreateDate() == null ? i.getDealDate() : i.getCreateDate());
    m.setDealDate(i.getDealDate());
    m.setDealStatus(i.getDealStatus());
    m.setStatus(i.getStatus());
    m.setType(i.getType());
    msgRelationDao.insertMsgRelation(m);
  }

  /**
   * 构造消息关系信息--站内信--成果-文件使用
   * 
   * @param i
   */
  private void buildChatResMsgRelationInfo(InitMsgForm i) {
    MsgRelation m = new MsgRelation();
    m.setId(msgRelationDao.getMsgRelationId());
    m.setSenderId(i.getSenderId());
    m.setReceiverId(i.getReceiverId());
    Long contentId = i.getContentId();
    m.setContentId(contentId);
    m.setCreateDate(i.getCreateDate() == null ? i.getDealDate() : i.getCreateDate());
    m.setDealDate(i.getDealDate());
    m.setDealStatus(i.getDealStatus());
    m.setStatus(i.getStatus());
    m.setType(i.getType());
    msgRelationDao.insertMsgRelation(m);
  }

  /**
   * 构造消息关系信息站内信--text 使用
   * 
   * @param i
   */
  private void buildChatMsgRelationInfo(InitMsgForm i, Integer type) {
    MsgRelation m = new MsgRelation();
    m.setId(i.getId());
    m.setSenderId(i.getSenderId());
    m.setReceiverId(i.getReceiverId());
    m.setContentId(i.getContentId());
    m.setCreateDate(i.getCreateDate() == null ? i.getDealDate() : i.getCreateDate());
    m.setDealDate(i.getDealDate());
    m.setDealStatus(i.getDealStatus());
    m.setStatus(i.getStatus());
    m.setType(type);
    msgRelationDao.insertMsgRelation(m);
  }

  /**
   * 全文请求--消息构造
   * 
   * @param pubIdstr
   * @param m
   * @throws Exception
   */
  private void buildMsgFullText(String pubIdstr, InitMsgForm m) throws Exception {
    if (StringUtils.isNotBlank(pubIdstr)) {
      buildFullTextReqInfo(m);
      buildMsgRelationInfo(m);
      Long pubId = NumberUtils.toLong(pubIdstr);
      Map<String, Object> contentMap = new HashMap<String, Object>();
      contentMap.put(MsgConstants.MSG_SENDER_ID, m.getSenderId());
      contentMap.put(MsgConstants.MSG_RECEIVER_IDS, m.getReceiverId());
      PubSimple pubSimple = pubSimpleDao.get(pubId);
      if (pubSimple != null) {
        contentMap.put(MsgConstants.MSG_PUB_TITLE_ZH, pubSimple.getZhTitle() == null ? "" : pubSimple.getZhTitle());
        contentMap.put(MsgConstants.MSG_PUB_TITLE_EN, pubSimple.getEnTitle() == null ? "" : pubSimple.getEnTitle());
        contentMap.put(MsgConstants.MSG_PUB_BRIEF_DESC_ZH,
            pubSimple.getBriefDesc() == null ? "" : pubSimple.getBriefDesc());
        contentMap.put(MsgConstants.MSG_PUB_BRIEF_DESC_EN,
            pubSimple.getBriefDescEn() == null ? "" : pubSimple.getBriefDescEn());
        contentMap.put(MsgConstants.MSG_PUB_AUTHOR_NAME,
            pubSimple.getAuthorNames() == null ? "" : pubSimple.getAuthorNames());
        contentMap.put(MsgConstants.MSG_PUB_ID, pubId);
      } else {
        throw new Exception("成果不存在pubId=" + pubId);
      }
      PubFulltext pubFulltext = pubFulltextDao.get(pubId);
      if (pubFulltext != null) {
        contentMap.put(MsgConstants.MSG_HAS_PUB_FULLTEXT, true);
        contentMap.put(MsgConstants.MSG_PUB_FULLTEXT_ID, pubFulltext.getFulltextFileId());
        contentMap.put(MsgConstants.MSG_PUB_FULLTEXT_EXT, pubFulltext.getFulltextFileExt());
        contentMap.put(MsgConstants.MSG_PUB_FULLTEXT_IMAGE_PATH,
            pubFulltext.getFulltextImagePath() == null ? "" : pubFulltext.getFulltextImagePath());
      } else {
        contentMap.put(MsgConstants.MSG_HAS_PUB_FULLTEXT, false);
      }
      PubIndexUrl pubIndexUrl = pubIndexUrlDao.get(pubId);
      if (pubIndexUrl != null) {
        String url = this.domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl();
        contentMap.put(MsgConstants.MSG_PUB_SHORT_URL, url);
      }
      MsgContent mc = new MsgContent();
      mc.setContentId(m.getContentId());
      mc.setContent(JacksonUtils.mapToJsonStr(contentMap));
      msgContentDao.insertMsgContent(mc);
    }
  }

}
