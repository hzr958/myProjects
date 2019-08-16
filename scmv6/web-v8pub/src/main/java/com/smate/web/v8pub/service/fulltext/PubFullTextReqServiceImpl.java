package com.smate.web.v8pub.service.fulltext;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.model.MailOriginalDataInfo;
import com.smate.core.base.email.service.EmailCommonService;
import com.smate.core.base.email.service.MailInitDataService;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.psn.service.psnpub.PsnPubService;
import com.smate.core.base.pub.dao.PubIndexUrlDao;
import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.pub.enums.PubFullTextReqStatusEnum;
import com.smate.core.base.pub.enums.PubPdwhStatusEnum;
import com.smate.core.base.pub.model.PubIndexUrl;
import com.smate.core.base.pub.model.fulltext.req.PubFullTextReqBase;
import com.smate.core.base.pub.model.fulltext.req.PubFullTextReqRecv;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.constant.MsgConstants;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.dao.sns.MsgRelationDao;
import com.smate.web.v8pub.dao.sns.PubFullTextReqBaseDao;
import com.smate.web.v8pub.dao.sns.PubFullTextReqRecvDao;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.po.sns.MsgRelation;
import com.smate.web.v8pub.po.sns.PubFullTextPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.po.sns.group.GroupPubPO;
import com.smate.web.v8pub.service.pdwh.PubPdwhService;
import com.smate.web.v8pub.service.sns.GroupPubService;
import com.smate.web.v8pub.service.sns.PubFullTextService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.vo.PubFulltextReqVO;

@Service("pubFullTextReqService")
@Transactional(rollbackFor = Exception.class)
public class PubFullTextReqServiceImpl implements PubFullTextReqService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnPubService psnPubService;
  @Autowired
  private PubPdwhService pubPdwhService;
  @Autowired
  private PubSnsService pubSnsService;
  @Autowired
  private PubFullTextReqBaseDao pubFTReqBaseDao;
  @Autowired
  private PubFullTextReqRecvDao pubFTReqRecvDao;
  @Autowired
  private MsgRelationDao msgRelationDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private MailInitDataService mailInitDataService;
  @Autowired
  private EmailCommonService emailCommonService;
  @Autowired
  private PubFullTextService pubFullTextService;
  @Autowired
  private FileDownloadUrlService fileDownUrlService;
  @Autowired
  private GroupPubService groupPubService;
  @Autowired
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String openResfulUrl;
  @Value("${domainscm}")
  private String domainscm;
  @Value(value = "${sendEmail.restful.url}")
  private String sendRestfulUrl;

  @Override
  public Map<String, Object> dealRequest(PubFulltextReqVO req) throws ServiceException {
    Map<String, Object> map = new HashMap<>();
    // 基准库成果请求，只保存进请求主表v_pub_fulltext_req_base
    if ("pdwhpub".equals(req.getPubType()) || "pdwh".equals(req.getPubType())) {
      if (!checkPdwhPubId(req.getPubId())) {
        map.put("status", "isDel");
      } else {
        saveReqBase(req);
        map.put("status", "success");
      }
    }
    /*
     * 个人成果请求，如果初次请求，先发站内消息，然后在保存请求记录 如果不是初次请求，是重复请求的话，更新站内消息时间和状态，保存请求记录
     */
    else if ("sns".equals(req.getPubType())) {
      // 检查成果是否被删除
      if (!checkPubId(req.getPubId())) {
        map.put("status", "isDel");
      } else {
        /*
         * 先判断成果拥有者psnId与请求接收人psnId是否一致
         */
        Long pubOwnerPsnId = getPubOwnerPsnId(req);
        req.setRecvPsnId(pubOwnerPsnId);
        /*
         * if (pubOwnerPsnId == null || !pubOwnerPsnId.equals(req.getRecvPsnId())) {
         * logger.error("请求全文记录失败！pubOwnerPsnId={}, recvPsnId={}", pubOwnerPsnId, req.getRecvPsnId()); throw
         * new ServiceException( "请求全文失败！pubOwnerPsnId不存在或pubOwnerPsnId与recvPsnId不一致！"); }
         */
        saveReqBase(req);
        saveReqRecv(req);
        restSendPubFullTextReqEmail(req);
        map.put("status", "success");
      }
    } else {
      map.put("status", "error");
    }
    return map;
  }

  // 校验成果是否被删除
  public boolean checkPubId(Long pubId) {
    PubSnsPO pub = pubSnsService.get(pubId);
    if (pub == null || (pub != null && "DELETED".equals(pub.getStatus().toString()))) {
      return false;
    } else {
      return true;
    }
  }

  // 校验基准库成果是否存在
  public boolean checkPdwhPubId(Long pdwhPubId) {
    PubPdwhPO pdwhPub = pubPdwhService.get(pdwhPubId);
    if (pdwhPub == null || pdwhPub.getStatus().equals(PubPdwhStatusEnum.DELETED)) {
      return false;
    }
    return true;
  }

  @Override
  public Long getPubOwnerPsnId(PubFulltextReqVO req) {
    Long pubOwnerPsnId = psnPubService.getPubOwnerId(req.getPubId());
    if (NumberUtils.isNullOrZero(pubOwnerPsnId)) {
      GroupPubPO groupPub = groupPubService.getByPubId(req.getPubId());
      if (groupPub != null) {
        pubOwnerPsnId = groupPub.getOwnerPsnId();
      }
    }
    return pubOwnerPsnId;
  }

  /**
   * 保存全文请求到v_pub_fulltext_req_base表（主表）
   * 
   * @param req
   * @return
   */
  private Long saveReqBase(PubFulltextReqVO req) {

    // 同一请求人请求同一成果全文，请求没有被处理，重复请求的情况，将上一次请求处理状态设为 REPEAT
    String hql = "select r from PubFullTextReqBase r where r.pubId=? and r.reqPsnId=? and r.status = ?";
    PubFullTextReqBase lastReqBase = (PubFullTextReqBase) pubFTReqBaseDao.findUnique(hql, req.getPubId(),
        req.getReqPsnId(), PubFullTextReqStatusEnum.UNPROCESSED);
    if (lastReqBase != null) {
      lastReqBase.setStatus(PubFullTextReqStatusEnum.REPEAT);
      lastReqBase.setUpdateDate(new Date());
      lastReqBase.setUpdatePsnId(
          NumberUtils.isNullOrZero(req.getCurrentPsnId()) ? SecurityUtils.getCurrentUserId() : req.getCurrentPsnId());
      pubFTReqBaseDao.save(lastReqBase);
    }
    /*
     * 新建一次全文请求
     */
    PubFullTextReqBase pubFTReqBase = new PubFullTextReqBase();
    if (req.getReqPsnId() == null || req.getReqPsnId() == 0L) {
      req.setReqPsnId(SecurityUtils.getCurrentUserId());
    }
    // 设置请求人id
    pubFTReqBase.setReqPsnId(req.getReqPsnId());
    // 设置请求日期
    pubFTReqBase.setReqDate(new Date());
    // 设置请求的成果id
    pubFTReqBase.setPubId(req.getPubId());
    // 设置请求的成果所属的库
    pubFTReqBase.setPubDb(req.getPubType().equalsIgnoreCase("sns") ? PubDbEnum.SNS : PubDbEnum.PDWH);
    // 设置未处理
    pubFTReqBase.setStatus(PubFullTextReqStatusEnum.UNPROCESSED);
    pubFTReqBaseDao.save(pubFTReqBase);
    req.setReqId(pubFTReqBase.getReqId());

    return pubFTReqBase.getReqId();
  }

  private void saveReqRecv(PubFulltextReqVO req) throws ServiceException {
    Long msgId = 0L;
    /*
     * 同一请求人向同一被请求的人请求同一成果全文，请求没有被处理，重复请求的情况， 将上一次的请求处理状态设为 REPEAT，同时将上一次发送的站内信状态和时间进行更新
     */
    String hql = "select r from PubFullTextReqRecv r where r.pubId=? and r.reqPsnId=? and r.recvPsnId=? and r.status=?";
    PubFullTextReqRecv lastReqRecv = (PubFullTextReqRecv) pubFTReqBaseDao.findUnique(hql, req.getPubId(),
        req.getReqPsnId(), req.getRecvPsnId(), PubFullTextReqStatusEnum.UNPROCESSED);
    // 找到上次请求，本次请求属于重复请求
    if (lastReqRecv != null) {
      lastReqRecv.setStatus(PubFullTextReqStatusEnum.REPEAT);
      pubFTReqRecvDao.save(lastReqRecv);
      MsgRelation msgRelation = msgRelationDao.get(lastReqRecv.getMsgId());
      if (msgRelation != null) {
        msgRelation.setCreateDate(new Date()); // 更新消息创建时间
        msgRelation.setStatus(0); // 设置消息状态未读
        msgRelation.setDealStatus(0);// 设置处理状态未处理
        msgRelationDao.save(msgRelation);
        msgId = msgRelation.getId();
      } else { // 重新发送一条请求消息
        msgId = sendFullTextRequestMsg(req);
      }
    } // 未找到上次请求,本次请求属于初次请求，发送请求消息
    else {
      msgId = sendFullTextRequestMsg(req);
    }
    // 消息发送成功，保存请求接收记录
    if (msgId != 0L) {
      req.setMsgId(msgId);
      /*
       * 新建请求记录
       */
      PubFullTextReqRecv pubFulltextReqRecv = new PubFullTextReqRecv();
      // 设置关联的pub_fulltext_req_base表全文请求记录id
      pubFulltextReqRecv.setReqId(req.getReqId());
      // 设置请求人id
      pubFulltextReqRecv.setReqPsnId(req.getReqPsnId());
      // 设置接收者id
      pubFulltextReqRecv.setRecvPsnId(req.getRecvPsnId());
      // 设置成果所属的库sns/pdwh
      pubFulltextReqRecv.setPubDb(PubDbEnum.valueOf(req.getPubType().toUpperCase()));
      // 设置请求的成果id
      pubFulltextReqRecv.setPubId(req.getPubId());
      // 设置请求日期
      pubFulltextReqRecv.setReqDate(new Date());
      // 设置站内消息id
      pubFulltextReqRecv.setMsgId(msgId);
      // 设置处理状态-未处理
      pubFulltextReqRecv.setStatus(PubFullTextReqStatusEnum.UNPROCESSED);
      pubFTReqRecvDao.save(pubFulltextReqRecv);
    } else {
      throw new ServiceException("消息发送失败！");
    }

  }

  @SuppressWarnings("unchecked")
  @Override
  public Long sendFullTextRequestMsg(PubFulltextReqVO req) throws ServiceException {
    try {
      Map<String, Object> map = buildFullTextReqMsgParam(req);
      // 调open接口发送消息
      Object resultData = restTemplate.postForObject(this.openResfulUrl, map, Object.class);
      Map<String, Object> resultMap = JacksonUtils.jsonToMap(resultData.toString());
      if (resultMap != null && "success".equals(resultMap.get("status"))) {
        List<Map<String, Object>> mapList = (List<Map<String, Object>>) resultMap.get("result");
        Map msgMap = mapList.get(0);
        return NumberUtils.toLong(msgMap.get("msgRelationId").toString());
      } else {
        logger.error("全文请求站内信发送失败！原因：{}", resultMap == null ? "" : resultMap.get("msg"));
        return 0L;
      }
    } catch (Exception e) {
      logger.error("全文请求站内信发送失败！");
      throw e;
    }
  }

  /**
   * 调用接口发送全文请求邮件
   * 
   * @param req
   */
  private void restSendPubFullTextReqEmail(PubFulltextReqVO req) {
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, Object> mailData = new HashMap<String, Object>();
    // 构造必需的参数
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    Person receiverPerson = personProfileDao.getPersonForEmail(req.getRecvPsnId());
    Person sendPerson;
    try {
      sendPerson = personDao.get(req.getReqPsnId());
      if (receiverPerson != null && sendPerson != null && StringUtils.isNotBlank(receiverPerson.getEmail())) {
        if (StringUtils.isBlank(receiverPerson.getEmailLanguageVersion())) {
          receiverPerson.setEmailLanguageVersion(LocaleContextHolder.getLocale().toString());
        }
        String psnShortUrl = "";
        Integer tempcode = 10048;
        PubSnsPO pub = pubSnsService.get(req.getPubId());
        if (pub != null) {
          info.setSenderPsnId(sendPerson.getPersonId());
          info.setReceiverPsnId(receiverPerson.getPersonId());
          info.setReceiver(receiverPerson.getEmail());
          info.setMsg("全文请求邮件");
          info.setMailTemplateCode(tempcode);
          paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializerNoNull(info));
          // 接收人姓名
          mailData.put("psnName", emailCommonService.getPsnName(receiverPerson));
          // 请求人姓名
          String requestPsnName =
              emailCommonService.getPsnNameByEmailLangage(sendPerson, receiverPerson.getEmailLanguageVersion());
          mailData.put("requestPsnName", requestPsnName);
          // 发件人主页
          PsnProfileUrl profileUrl = psnProfileUrlDao.get(sendPerson.getPersonId());
          if (profileUrl != null && StringUtils.isNotBlank(profileUrl.getPsnIndexUrl())) {
            psnShortUrl = this.domainscm + "/" + ShortUrlConst.P_TYPE + "/" + profileUrl.getPsnIndexUrl();
          }
          List<String> linkList = new ArrayList<String>();
          MailLinkInfo l1 = new MailLinkInfo();
          l1.setKey("domainUrl");
          l1.setUrl(domainscm);
          l1.setUrlDesc("科研之友首页");
          linkList.add(JacksonUtils.jsonObjectSerializer(l1));
          MailLinkInfo l2 = new MailLinkInfo();
          l2.setKey("requestPsnUrl");
          l2.setUrl(psnShortUrl);
          l2.setUrlDesc("发件人主页地址");
          linkList.add(JacksonUtils.jsonObjectSerializer(l2));
          this.handlePub(mailData, pub, receiverPerson, linkList);
          mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
          // 主题参数，添加如下：
          List<String> subjectParamLinkList = new ArrayList<String>();
          subjectParamLinkList.add(requestPsnName);
          mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));
          paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
          restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
        }
      }
    } catch (Exception e) {
      logger.error("请求全文邮件发送出错------", e);
      e.printStackTrace();
    }
  }

  /**
   * 处理成果
   * 
   * @param mailMap
   * @param pub
   * @param linkList
   * @throws ServiceException
   */
  private void handlePub(Map<String, Object> mailData, PubSnsPO pub, Person receiverPerson, List<String> linkList)
      throws ServiceException {
    try {
      // 成果标题
      mailData.put("pubTitle", pub.getTitle());
      // 成果作者
      Map<String, Object> authors = getPubAuthor(pub, 3);
      if (authors.get("authors") != null) {
        mailData.put("author", authors.get("authors"));
        // 是否有多余3个作者
        mailData.put("authorNum", authors.get("authorNum"));
      }
      // 成果来源

      mailData.put("pubOther", pub.getBriefDesc());

      // 成果详情url
      // 改成上传全文到全文详情页面
      String pubShortUrl = "";
      // 成果详情
      PubIndexUrl pubIndexUrl = pubIndexUrlDao.get(pub.getPubId());
      if (pubIndexUrl != null && StringUtils.isNotBlank(pubIndexUrl.getPubIndexUrl())) {
        pubShortUrl = this.domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl();
      }
      String fullImg = this.domainscm + "/resmod/images_v5/images2016/file_img.jpg";
      String pubFulltextImage = null;
      PubFullTextPO fullText = pubFullTextService.get(pub.getPubId());
      if (fullText != null) {
        pubFulltextImage = fullText.getThumbnailPath();
      }
      if (StringUtils.isBlank(pubFulltextImage)) {
        if (fullText != null && fullText.getFileId() != null) {
          fullImg = this.domainscm + "/resmod/images_v5/images2016/file_img1.jpg";
        } else {
          fullImg = this.domainscm + "/resmod/images_v5/images2016/file_img.jpg";
        }
      } else {
        fullImg = this.domainscm + pubFulltextImage;
      }
      mailData.put("fullImg", fullImg);
      MailLinkInfo l3 = new MailLinkInfo();
      l3.setKey("pubDetailUrl");
      l3.setUrl(pubShortUrl);
      l3.setUrlDesc("成果短地址");
      linkList.add(JacksonUtils.jsonObjectSerializer(l3));
      MailLinkInfo l4 = new MailLinkInfo();
      l4.setKey("uploadUrl");
      l4.setUrl(this.domainscm + "/dynweb/showmsg/msgmain?model=reqFullTextMsg");
      l4.setUrlDesc("上传地址");
      linkList.add(JacksonUtils.jsonObjectSerializer(l4));
      MailLinkInfo l5 = new MailLinkInfo();
      l5.setKey("emailUrl");
      l5.setUrl(this.domainscm + "/dynweb/showmsg/msgmain?model=chatMsg");
      l5.setUrlDesc("邮件地址");
      linkList.add(JacksonUtils.jsonObjectSerializer(l5));
    } catch (Exception e) {
      logger.error("请求全文邮件构造成果相关参数出错------", e);
      e.printStackTrace();
    }
  }

  /**
   * 构建全文请求消息需要的参数
   * 
   * @param form
   * @return
   */
  private Map<String, Object> buildFullTextReqMsgParam(PubFulltextReqVO req) throws ServiceException {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put(MsgConstants.MSG_SENDER_ID, req.getReqPsnId());
    dataMap.put(MsgConstants.MSG_RECEIVER_IDS, req.getRecvPsnId());
    dataMap.put(MsgConstants.MSG_TYPE, MsgConstants.MSG_TYPE_PUBFULLTEXT_REQUEST);
    dataMap.put(MsgConstants.MSG_PUB_ID, req.getPubId());
    map.put("openid", "99999999");
    map.put("token", "00000000msg77msg");
    map.put("data", JacksonUtils.mapToJsonStr(dataMap));
    return map;
  }

  public Map<String, Object> getPubAuthor(PubSnsPO pub, Integer num) {

    Map<String, Object> authorMap = new HashMap<String, Object>();
    StringBuffer sb = new StringBuffer();
    if (StringUtils.isNotBlank(pub.getAuthorNames())) {
      if (num == 0) {
        num = 3;
      }
      String[] names = StringUtils.split(HtmlUtils.htmlUnescape(pub.getAuthorNames()), ";");
      sb.append(names[0]);
      authorMap.put("authorNum", names.length);
      for (int i = 1; i < num && i < names.length; i++) {
        sb.append(";");
        sb.append(names[i]);
      }
      authorMap.put("authors", sb.toString());
    }
    return authorMap;
  }

  @Override
  public void updateStatus(Long msgRelationId, Long pubId, PubFullTextReqStatusEnum dealStatus, Long currentPsnId)
      throws ServiceException {
    try {
      if (dealStatus == PubFullTextReqStatusEnum.UNPROCESSED) {
        return;
      }
      /*
       * 根据成果id和消息id查找PubFullTextReqRecv表对应记录，筛选未处理的记录，然后更新状态
       */
      PubFullTextReqRecv lastReqRecv = pubFTReqRecvDao.getUnprocessed(msgRelationId, pubId);
      MsgRelation msgRelation = msgRelationDao.getUnprocessed(msgRelationId);
      if (msgRelation != null) {
        // 必须保证当前人员是消息接收人的id
        if (CommonUtils.compareLongValue(msgRelation.getReceiverId(), currentPsnId)) {
          updateMsgRelationDealStatus(msgRelation, dealStatus);
          if (dealStatus == PubFullTextReqStatusEnum.AGREE || dealStatus == PubFullTextReqStatusEnum.UPLOAD) {
            Long emailSenderId = msgRelation.getReceiverId();
            Long emailReceiverId = msgRelation.getSenderId();
            // 发送全文同意的站内信
            sendFullTextAgreeReplyMsg(emailReceiverId, emailSenderId, pubId);
            // SCM-15896 同意他人的全文请求，站内信自动给对方发送一个全文提示
            sendFullTextAgreeReplyTextMsg(emailReceiverId, emailSenderId, pubId);
            // 发送邮件通知
            restSendFullTextAgreeEmail(emailReceiverId, emailSenderId, pubId);
          }
        } else {
          return;
        }
      } else {
        logger.error("在MsgRelation表没有找到该未处理的消息记录！msgRelationId={}", msgRelationId);
      }
      if (lastReqRecv != null) {
        PubFullTextReqBase lastReqBase = pubFTReqBaseDao.getUnprocessed(lastReqRecv.getReqId());
        Date date = new Date();
        if (lastReqBase != null) {
          lastReqBase.setStatus(dealStatus); // 设置处理状态
          lastReqBase.setUpdateDate(date); // 设置更新时间
          lastReqBase.setUpdatePsnId(SecurityUtils.getCurrentUserId()); // 设置处理人id
          pubFTReqBaseDao.save(lastReqBase);
        }

        lastReqRecv.setStatus(dealStatus); // 设置处理状态
        lastReqRecv.setUpdateDate(date); // 设置更新时间
        lastReqRecv.setUpdatePsnId(SecurityUtils.getCurrentUserId()); // 设置处理人id
        pubFTReqRecvDao.save(lastReqRecv);
      } else {
        logger.error("在PubFullTextReqRecv表没有找到相关未处理的请求记录！pubId={},msgRelationId={},status=0", pubId, msgRelationId);
      }
    } catch (Exception e) {
      logger.error("处理全文请求出错！原因: {}", e.getMessage());
    }
  }

  /**
   * 发送全文请求同意回复的站内信
   * 
   * @param receiverId
   * @param senderId
   * @param pubId
   */
  private void sendFullTextAgreeReplyMsg(Long receiverId, Long senderId, Long pubId) {
    Map<String, Object> param = buildFullTextAgreeReplyMsgParam(receiverId, senderId, pubId);
    // 调open接口发送消息
    Object resultData = restTemplate.postForObject(this.openResfulUrl, param, Object.class);
    Map<String, Object> resultMap = JacksonUtils.jsonToMap(resultData.toString());
    if (resultMap != null && "success".equals(resultMap.get("status"))) {
      logger.info(resultData.toString());
    } else {
      logger.error("全文请求站内信发送失败！原因：{}", resultMap == null ? "" : resultMap.get("msg"));
      throw new ServiceException("调用open接口发送站内消息失败！");
    }
  }

  /**
   * 构建全文请求同意回复的站内信参数
   * 
   * @param receiverId
   * @param senderId
   * @param pubId
   * @return
   */
  private Map<String, Object> buildFullTextAgreeReplyMsgParam(Long receiverId, Long senderId, Long pubId) {
    Map<String, Object> paramMap = new HashMap<String, Object>();
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put(MsgConstants.MSG_SENDER_ID, senderId);
    dataMap.put(MsgConstants.MSG_RECEIVER_IDS, receiverId);
    dataMap.put(MsgConstants.MSG_TYPE, MsgConstants.MSG_TYPE_SMATE_INSIDE_LETTER);
    dataMap.put(MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE, MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_FULLTEXT);
    dataMap.put(MsgConstants.MSG_PUB_ID, pubId);
    dataMap.put(MsgConstants.MSG_BELONG_PERSON, "true");
    paramMap.put("openid", "99999999");
    paramMap.put(MsgConstants.MSG_TOKEN, "00000000msg77msg");
    paramMap.put("data", JacksonUtils.mapToJsonStr(dataMap));
    return paramMap;
  }

  private void sendFullTextAgreeReplyTextMsg(Long emailReceiverId, Long emailSenderId, Long pubId) {
    try {
      PubSnsPO pub = pubSnsService.get(pubId);
      String title = "";
      if (pub != null) {
        title = pub.getTitle();
      }
      String content = StringUtils.isBlank(title) ? "我为你上传了成果全文。" : "我为你上传了“" + title + "”成果的全文。";
      Map<String, Object> contentMap = new HashMap<String, Object>();
      contentMap.put(MsgConstants.MSG_SENDER_ID, emailSenderId);
      contentMap.put(MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE, MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_TEXT);
      contentMap.put(MsgConstants.MSG_RECEIVER_IDS, emailReceiverId);
      contentMap.put(MsgConstants.MSG_TYPE, MsgConstants.MSG_TYPE_SMATE_INSIDE_LETTER);
      contentMap.put(MsgConstants.MSG_CONTENT, content);
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("openid", "99999999");
      map.put("token", "00000000msg77msg");
      map.put("data", JacksonUtils.mapToJsonStr(contentMap));
      Object obj = restTemplate.postForObject(this.openResfulUrl, map, Object.class);
    } catch (Exception e) {
      logger.error("同意全文请求发送的文本提示出错", e);
    }
  }

  /**
   * 发送全文请求同意邮件通知
   * 
   * @param emailReceiverId
   * @param emailSenderId
   * @param pubId
   * @throws Exception
   */
  private void restSendFullTextAgreeEmail(Long emailReceiverId, Long emailSenderId, Long pubId) throws Exception {
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, Object> mailData = new HashMap<String, Object>();
    // 构造必需的参数
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    String psnShortUrl = "";
    String pubShortUrl = "";
    Person sender = personDao.findPsnInfoForEmail(emailSenderId);
    Person receiver = personDao.findPsnInfoForEmail(emailReceiverId);
    // 全文请求使用新模板
    if (sender == null || receiver == null || pubId == null) {
      throw new Exception("发送全文请求回复通知邮件，邮件参数对象为空！" + this.getClass().getName());
    }
    String language = "";
    language = receiver.getEmailLanguageVersion();
    if (StringUtils.isBlank(language)) {
      language = LocaleContextHolder.getLocale().toString();
    }
    PubSnsPO pub = pubSnsService.get(pubId);
    String pubTitle = "";
    if (pub != null) {
      Integer tempcode = 10038;
      info.setSenderPsnId(sender.getPersonId());
      info.setReceiverPsnId(receiver.getPersonId());
      info.setReceiver(receiver.getEmail());
      info.setMsg("全文请求回复邮件");
      info.setMailTemplateCode(tempcode);
      paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializerNoNull(info));
      // 发件人头衔
      if (StringUtils.isNotBlank(sender.getTitolo())) {
        mailData.put("frdTitlo", sender.getTitolo());
      }
      String frdName = emailCommonService.getPsnNameByEmailLangage(sender, language);
      String psnName = emailCommonService.getPsnNameByEmailLangage(receiver, language);
      String fullImg = buildFullImg(pub.getPubId());
      pubTitle = constructParams(pub.getTitle());
      // 标题
      String enTitle = "Reply to your fulltext request";
      String title = "回复你的全文请求";
      mailData.put("frdName", frdName);
      mailData.put("enTitle", enTitle);
      mailData.put("title", title);
      mailData.put("psnName", psnName);
      mailData.put("pubTitle", pubTitle);
      mailData.put("authorNames", pub.getAuthorNames());
      mailData.put("briefDesc", pub.getBriefDesc());
      mailData.put("fullImg", fullImg);
      // 发件人主页
      PsnProfileUrl profileUrl = psnProfileUrlDao.get(sender.getPersonId());
      if (profileUrl != null && StringUtils.isNotBlank(profileUrl.getPsnIndexUrl())) {
        psnShortUrl = this.domainscm + "/" + ShortUrlConst.P_TYPE + "/" + profileUrl.getPsnIndexUrl();
      }
      // 成果详情
      PubIndexUrl pubIndexUrl = pubIndexUrlDao.get(pubId);
      if (pubIndexUrl != null && StringUtils.isNotBlank(pubIndexUrl.getPubIndexUrl())) {
        pubShortUrl = this.domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl();
      }
      // 下载全文按钮跳转的链接
      // SCM-14409 hcj 采用统一短地址获取入口
      String fullTextShortUrl = fileDownUrlService.getShortDownloadUrl(FileTypeEnum.SNS_FULLTEXT, pubId, pubId);
      // 跟踪链接 根据key放置到模板中 所有的链接不再需要放到mailData中
      List<String> linkList = new ArrayList<String>();
      MailLinkInfo l1 = new MailLinkInfo();
      l1.setKey("domainUrl");
      l1.setUrl(domainscm);
      l1.setUrlDesc("科研之友首页");
      linkList.add(JacksonUtils.jsonObjectSerializer(l1));
      MailLinkInfo l2 = new MailLinkInfo();
      l2.setKey("frdUrl");
      l2.setUrl(psnShortUrl);
      l2.setUrlDesc("发件人主页链接");
      linkList.add(JacksonUtils.jsonObjectSerializer(l2));
      MailLinkInfo l3 = new MailLinkInfo();
      l3.setKey("pubsUrl");
      l3.setUrl(pubShortUrl);
      l3.setUrlDesc("成果详情链接");
      linkList.add(JacksonUtils.jsonObjectSerializer(l3));
      MailLinkInfo l4 = new MailLinkInfo();
      l4.setKey("fulltextUrl");
      l4.setUrl(fullTextShortUrl);
      l4.setUrlDesc("下载全文按钮跳转的链接");
      linkList.add(JacksonUtils.jsonObjectSerializer(l4));
      // 主题参数，添加如下：
      List<String> subjectParamLinkList = new ArrayList<String>();
      subjectParamLinkList.add(frdName);
      subjectParamLinkList.add(HtmlUtils.htmlUnescape(pubTitle));
      mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
      mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));
      paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
      restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
    }
  }

  /**
   * 构建全文缩略图
   * 
   * @param pubId
   * @return
   */
  private String buildFullImg(Long pubId) {
    PubFullTextPO fullText = pubFullTextService.get(pubId);
    String pubFulltextImage = "";
    if (fullText != null) {
      pubFulltextImage = fullText.getThumbnailPath();
    }
    if (StringUtils.isBlank(pubFulltextImage)) {
      if (fullText != null && fullText.getFileId() != null) {
        return this.domainscm + "/resmod/images_v5/images2016/file_img1.jpg";
      } else {
        return this.domainscm + "/resmod/images_v5/images2016/file_img.jpg";
      }
    } else {
      return this.domainscm + pubFulltextImage;
    }
  }

  /**
   * 更新全文请求消息的状态
   * 
   * @param msgRelation
   * @param dealStatus
   */
  private void updateMsgRelationDealStatus(MsgRelation msgRelation, PubFullTextReqStatusEnum dealStatus) {
    switch (dealStatus) {
      case UPLOAD:
      case AGREE:
        msgRelation.setDealStatus(1);
        break;
      case IGNORE:
        msgRelation.setDealStatus(2);
        break;
      default:
        break;
    }
    msgRelation.setDealDate(new Date());
    msgRelationDao.save(msgRelation);
  }

  private String constructParams(String title) {
    title = StringUtils.trimToEmpty(title);
    title = StringUtils.substring(title, 0, 400);
    return title;
  }
}
