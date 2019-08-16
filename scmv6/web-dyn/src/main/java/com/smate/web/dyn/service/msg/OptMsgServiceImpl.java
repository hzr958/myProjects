package com.smate.web.dyn.service.msg;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.dyn.consts.DynamicConstants;
import com.smate.core.base.project.model.ProjectStatistics;
import com.smate.core.base.utils.constant.MsgConstants;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.dyn.dao.dynamic.DynamicSharePsnDao;
import com.smate.web.dyn.dao.dynamic.DynamicShareResDao;
import com.smate.web.dyn.dao.fund.FundStatisticsDao;
import com.smate.web.dyn.dao.pdwh.PdwhPubShareDao;
import com.smate.web.dyn.dao.pdwh.PdwhPubStatisticsDao;
import com.smate.web.dyn.dao.pub.ProjectStatisticsDao;
import com.smate.web.dyn.dao.pub.PubSimpleDao;
import com.smate.web.dyn.dao.pub.PubStatisticsDAO;
import com.smate.web.dyn.dao.share.ShareStatisticsDao;
import com.smate.web.dyn.form.msg.MsgShowForm;
import com.smate.web.dyn.model.dynamic.DynamicSharePsn;
import com.smate.web.dyn.model.dynamic.DynamicShareRes;
import com.smate.web.dyn.model.fund.FundStatistics;
import com.smate.web.dyn.model.pdwhpub.PdwhPubShare;
import com.smate.web.dyn.model.pdwhpub.PdwhPubStatistics;
import com.smate.web.dyn.model.pub.PubStatisticsPO;
import com.smate.web.dyn.model.share.ShareStatistics;
import com.smate.web.dyn.service.psn.PersonQueryservice;

/**
 * 消息操作的服务类
 * 
 * @author Administrator
 *
 */
@Service("optMsgService")
@Transactional(rollbackOn = Exception.class)
public class OptMsgServiceImpl implements OptMsgService {

  @Autowired
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String openResfulUrl;
  @Autowired
  private PubSimpleDao pubSimpleDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private ProjectStatisticsDao projectStatisticsDao;
  @Autowired
  private PubStatisticsDAO pubStatisticsDAO;
  @Autowired
  private FundStatisticsDao fundStatisticsDao;
  @Autowired
  private PdwhPubStatisticsDao pdwhPubStatisticsDao;
  @Autowired
  private PdwhPubShareDao pdwhPubShareDao;
  @Autowired
  private PersonQueryservice personQueryservic;
  @Autowired
  private DynamicShareResDao dynamicShareResDao;
  @Autowired
  private DynamicSharePsnDao dynamicSharePsnDao;

  @Autowired
  private ShareStatisticsDao shareStatisticsDao;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 发送全文请求站内信
   */
  @Override
  public void sendFulltextRequest(MsgShowForm form) throws Exception {

    Long pubPsnId = pubSimpleDao.getPsnIdByPubId(form.getPubId());
    // 权限判断
    if (pubPsnId == null || !pubPsnId.equals(form.getReceiverId())) {
      return;
    }

    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put(MsgConstants.MSG_SENDER_ID, form.getPsnId());
    dataMap.put(MsgConstants.MSG_RECEIVER_IDS, NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3ReceiverId())));
    dataMap.put(MsgConstants.MSG_TYPE, MsgConstants.MSG_TYPE_PUBFULLTEXT_REQUEST);
    dataMap.put(MsgConstants.MSG_PUB_ID, form.getPubId());
    Map<String, Object> map = buildSendMsgParam(form, dataMap);
    sendtMsg(form, map);
  }

  /**
   * 发送全文请求站内信
   * 
   * @param form
   * @throws Exception
   */
  private void sendtMsg(MsgShowForm form, Map<String, Object> map) throws Exception {
    // 调open接口发送消息
    Object obj = restTemplate.postForObject(this.openResfulUrl, map, Object.class);
    return;
  }

  /**
   * 发送成果分享 默认是个人成果
   */
  @Override
  public void sendPubShareToFriend(MsgShowForm form) throws Exception {
    if (form.getSmateInsideLetterType() == null) {
      throw new Exception("发送消息的类型smateInsideLetterType为空");
    }
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put(MsgConstants.MSG_SENDER_ID, form.getPsnId());
    String[] des3ReceiverArr = form.getDes3ReceiverIds().split(",");
    String reveiverList = "";
    for (String des3Receiver : des3ReceiverArr) {
      String receiver = "";
      if (NumberUtils.isCreatable(des3Receiver)) {
        receiver = des3Receiver + ",";
      } else {
        receiver = Des3Utils.decodeFromDes3(des3Receiver) + ",";
      }
      if (!receiver.equals(form.getPsnId().toString()))// 判断是否为分享给自己，不是自己才执行分享操作
        reveiverList += receiver;
    }
    dataMap.put(MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE, form.getSmateInsideLetterType());
    dataMap.put(MsgConstants.MSG_BELONG_PERSON, true);
    dataMap.put(MsgConstants.MSG_RECEIVER_IDS, reveiverList);
    dataMap.put(MsgConstants.MSG_TYPE, MsgConstants.MSG_TYPE_SMATE_INSIDE_LETTER);
    dataMap.put(MsgConstants.MSG_CONTENT, form.getContent());
    Map<String, Object> map = null;
    for (Long p : form.getPubIds()) {
      dataMap.put(MsgConstants.MSG_PUB_ID, p);
      map = buildSendMsgParam(form, dataMap);
      sendtMsg(form, map);
    }
    // 如果成果中有文字信息，则要在发文字信息。
    if (StringUtils.isNotBlank(form.getContent())) {
      Map<String, Object> contentMap = new HashMap<String, Object>();
      contentMap.put(MsgConstants.MSG_SENDER_ID, form.getPsnId());
      contentMap.put(MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE, MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_TEXT);
      contentMap.put(MsgConstants.MSG_RECEIVER_IDS, reveiverList);
      contentMap.put(MsgConstants.MSG_TYPE, MsgConstants.MSG_TYPE_SMATE_INSIDE_LETTER);
      contentMap.put(MsgConstants.MSG_CONTENT, form.getContent());
      map = buildSendMsgParam(form, contentMap);
      sendtMsg(form, map);
    }
  }

  /**
   * 构建发消息需要的参数
   * 
   * @param form
   * @return
   */
  private Map<String, Object> buildSendMsgParam(MsgShowForm form, Map<String, Object> dataMap) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("openid", "99999999");
    map.put("token", "00000000msg77msg");
    map.put("data", JacksonUtils.mapToJsonStr(dataMap));
    return map;
  }

  @Override
  public void dealEmailForPsn(MsgShowForm form) throws Exception {
    Long ownerPsnId = SecurityUtils.getCurrentUserId();
    if (StringUtils.isNotBlank(form.getReceiverEmails())) {
      String[] emailArr = form.getReceiverEmails().split(",");
      if (emailArr.length > 0) {
        List<Long> psnIdList = new ArrayList<Long>();
        for (String email : emailArr) {
          Long psnId = personDao.findPsnIdByEmail(email);
          if (psnId != null && psnId != 0 && !ownerPsnId.equals(psnId)) {
            psnIdList.add(psnId);
          }
        }
        if (psnIdList.size() > 0) {
          if (StringUtils.isNotBlank(form.getDes3ReceiverIds())) {
            form.setDes3ReceiverIds(form.getDes3ReceiverIds() + "," + StringUtils.join(psnIdList.toArray(), ","));
          } else {
            form.setDes3ReceiverIds(StringUtils.join(psnIdList.toArray(), ","));
          }
        }
      }

    }

  }

  @Override
  public void addShareRecord(MsgShowForm form) throws Exception {
    Integer resType = null;
    String type = form.getSmateInsideLetterType();
    String receiverIds = form.getDes3ReceiverIds().toString();
    String[] desReceivers = receiverIds.split(",");

    switch (type) {
      case "pub":
      case "fulltext":
        resType = 1;
        addPubShareStatistics(form);
        for (String desReceiver : desReceivers) {
          addShareStatisticsRecord(form.getPsnId(), new Long(Des3Utils.decodeFromDes3(desReceiver)), form.getResId(),
              resType);
        }
        break;
      case "pdwhpub":
        resType = 22;
        addPdwhPubShareStatistics(form);
        for (String desReceiver : desReceivers) {
          addShareStatisticsRecord(form.getPsnId(), new Long(Des3Utils.decodeFromDes3(desReceiver)), form.getResId(),
              resType);
        }
        break;
      case "fund":
        resType = 11;
        addFundShareStatistics(form);
        break;
      case "prj":
        resType = 4;
        addPrjShareStatistics(form);
        for (String desReceiver : desReceivers) {
          addShareStatisticsRecord(form.getPsnId(), new Long(Des3Utils.decodeFromDes3(desReceiver)), form.getResId(),
              resType);
        }
        break;
      default:
        break;
    }
    if (resType != null) {
      updateResPsn(form, resType);
    }

  }

  private void updateResPsn(MsgShowForm form, Integer resType) throws Exception {
    Person person = personQueryservic.findPerson(form.getPsnId());
    if (person == null) {
      throw new DynException("分享动态时，不能获取到分享人员的person对象");
    }
    Date now = new Date();
    DynamicShareRes dynamicShareRes = this.dynamicShareResDao.getDynamicShareRes(form.getResId(), resType, 1);
    // 更新资源的分享次数
    if (dynamicShareRes == null) {
      dynamicShareRes = new DynamicShareRes();
      dynamicShareRes.setResId(form.getResId());
      dynamicShareRes.setResType(resType);
      dynamicShareRes.setResNode(1);
      dynamicShareRes.setShareTimes(1l);
      dynamicShareRes.setUpdateDate(now);
    } else {
      dynamicShareRes.setShareTimes(dynamicShareRes.getShareTimes() + 1);
      dynamicShareRes.setUpdateDate(now);
    }
    dynamicShareResDao.save(dynamicShareRes);
    // 添加人员分享记录
    DynamicSharePsn dynamicSharePsn = new DynamicSharePsn();
    dynamicSharePsn.setShareId(dynamicShareRes.getShareId());
    dynamicSharePsn.setShareTitle("分享到他（她）的动态。");
    dynamicSharePsn.setShareEnTitle("share to him (her) dynamic.");
    dynamicSharePsn.setSharerPsnId(person.getPersonId());
    dynamicSharePsn.setSharerName(personQueryservic.getPsnName(person, "zh_CN"));
    dynamicSharePsn.setSharerEnName(personQueryservic.getPsnName(person, "en_US"));
    dynamicSharePsn.setSharerAvatar(person.getAvatars());
    dynamicSharePsn.setShareDate(now);
    dynamicSharePsnDao.save(dynamicSharePsn);

  }

  private void addPrjShareStatistics(MsgShowForm form) {
    ProjectStatistics ps = projectStatisticsDao.get(form.getResId());
    if (ps == null) {
      ps = new ProjectStatistics();
      ps.setProjectId(form.getResId());
      ps.setShareCount(0);
    }
    if (ps.getShareCount() == null) {
      ps.setShareCount(0);
    }
    ps.setShareCount(ps.getShareCount() + 1);
    projectStatisticsDao.save(ps);

  }

  private void addFundShareStatistics(MsgShowForm form) {
    FundStatistics fs = fundStatisticsDao.get(form.getResId());
    if (fs == null) {
      fs = new FundStatistics();
      fs.setFundId(form.getResId());
      fs.setShareCount(0);
    }
    if (fs.getShareCount() == null) {
      fs.setShareCount(0);
    }
    fs.setShareCount(fs.getShareCount() + 1);
    fundStatisticsDao.save(fs);

  }

  private void addPdwhPubShareStatistics(MsgShowForm form) {
    PdwhPubShare pdwhPubShare = new PdwhPubShare();
    pdwhPubShare.setSharePsnId(form.getPsnId());
    Date date = new Date();
    pdwhPubShare.setShareDate(date);
    pdwhPubShare.setResId(form.getResId());
    pdwhPubShare.setResType(22);
    pdwhPubShareDao.save(pdwhPubShare);
    PdwhPubStatistics s = pdwhPubStatisticsDao.get(form.getResId());
    if (s == null) {
      s = new PdwhPubStatistics();
      s.setPubId(form.getResId());
      s.setShareCount(0);
    }
    if (s.getShareCount() == null) {
      s.setShareCount(0);
    }
    s.setShareCount(s.getShareCount() + 1);
    pdwhPubStatisticsDao.save(s);

  }

  // ------------------------------------------------

  public void addShareStatisticsRecord(Long psnId, Long SharePsnId, Long actionKey, Integer actionType)
      throws DynException {

    try {
      if (!DynamicConstants.SHARE_TYPE_MAP.containsKey(actionType)) {
        logger.warn("分享统计，分享类型actionType=" + actionType + "的记录，不需要保存");
        return;
      }
      ShareStatistics shareStatistics = new ShareStatistics();
      shareStatistics.setPsnId(psnId);
      shareStatistics.setSharePsnId(SharePsnId);
      shareStatistics.setActionKey(actionKey);
      shareStatistics.setActionType(actionType);
      Date nowDate = new Date();
      shareStatistics.setCreateDate(nowDate);
      shareStatistics.setFormateDate(DateUtils.getDateTime(nowDate));
      shareStatistics.setIp(Struts2Utils.getRemoteAddr());
      shareStatisticsDao.save(shareStatistics);

    } catch (Exception e) {
      logger.error("保存分享记录出错！PsnId=" + psnId + " sharePsnId=" + SharePsnId + " actionKey=" + actionKey + " actionType"
          + actionType, e);
      throw new DynException(e);
    }
  }

  // -----------------------------------------------

  private void addPubShareStatistics(MsgShowForm form) {
    PubStatisticsPO ps = pubStatisticsDAO.get(form.getResId());
    if (ps == null) {
      ps = new PubStatisticsPO(form.getResId());
    }
    ps.setShareCount(ps.getShareCount() + 1);
    pubStatisticsDAO.save(ps);

  }

  @Override
  public void sendTextMsg(MsgShowForm form) throws Exception {
    if (StringUtils.isNotBlank(form.getContent()) && StringUtils.isNotBlank(form.getReceiverIds())) {
      String[] des3ReceiverArr = form.getReceiverIds().split(",");
      StringJoiner reveiverList = new StringJoiner(",");
      for (String des3Receiver : des3ReceiverArr) {
        String receiver = NumberUtils.isCreatable(des3Receiver) ? des3Receiver : Des3Utils.decodeFromDes3(des3Receiver);
        if (!Objects.toString(form.getPsnId(), "").equals(receiver)) {
          reveiverList.add(receiver);
        }
      }
      Map<String, Object> contentMap = new HashMap<String, Object>();
      contentMap.put(MsgConstants.MSG_SENDER_ID, form.getPsnId());
      contentMap.put(MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE, MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_TEXT);
      contentMap.put(MsgConstants.MSG_RECEIVER_IDS, reveiverList.toString());
      contentMap.put(MsgConstants.MSG_TYPE, MsgConstants.MSG_TYPE_SMATE_INSIDE_LETTER);
      contentMap.put(MsgConstants.MSG_CONTENT, form.getContent());
      Map<String, Object> map = buildSendMsgParam(form, contentMap);
      restTemplate.postForObject(this.openResfulUrl, map, Object.class);
    }
  }
}
