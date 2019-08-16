package com.smate.center.task.service.sns.quartz;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.task.dao.rcmd.quartz.PubConfirmRolPubDao;
import com.smate.center.task.dao.rcmd.quartz.RecomMsgFormDao;
import com.smate.center.task.dao.sns.msg.MsgContentDao;
import com.smate.center.task.dao.sns.msg.MsgRelationDao;
import com.smate.center.task.dao.sns.quartz.PubIndexUrlDao;
import com.smate.center.task.dao.sns.quartz.PubSimpleDao;
import com.smate.center.task.dao.sns.quartz.PublicationDao;
import com.smate.center.task.model.rcmd.quartz.PubConfirmRolPub;
import com.smate.center.task.model.rcmd.quartz.RecomMsgForm;
import com.smate.center.task.model.sns.msg.MsgContent;
import com.smate.center.task.model.sns.msg.MsgRelation;
import com.smate.center.task.model.sns.quartz.Publication;
import com.smate.core.base.utils.constant.MsgConstants;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 
 * @author cht
 *
 */
@Service("recomMsgService")
@Transactional(rollbackFor = Exception.class)
public class RecomMsgServiceImpl implements RecomMsgService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String openResfulUrl;
  @Autowired
  private RecomMsgFormDao recomMsgFormDao;
  @Autowired
  private PubConfirmRolPubDao pubConfirmRolPubDao;
  @Autowired
  private PubSimpleDao pubSimpleDao;
  @Autowired
  private PublicationDao PublicationDao;
  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;
  @Autowired
  private MsgContentDao msgContentDao;
  @Autowired
  private MsgRelationDao msgRelationDao;
  @Value("${domainscm}")
  private String domainscm;


  public void sendMsg(String msgType, Long senderId, Long receiverId, Long pubId, Long resCount) throws Exception {
    Date date = new Date();
    MsgContent mc = new MsgContent();
    Long contentId = msgContentDao.getContentId();
    mc.setContentId(contentId);
    Map<String, Object> contentMap = new HashMap<String, Object>();
    contentMap.put(MsgConstants.MSG_SENDER_ID, senderId);
    contentMap.put(MsgConstants.MSG_RECEIVER_IDS, receiverId);
    contentMap.put(MsgConstants.MSG_PUB_ID, pubId);
    contentMap.put(MsgConstants.MSG_RES_COUNT, resCount);
    if ("2".equals(msgType)) {
      PubConfirmRolPub pubConfirmRolPub = pubConfirmRolPubDao.get(pubId);
      if (pubConfirmRolPub != null) {
        contentMap.put(MsgConstants.MSG_PUB_TITLE_ZH,
            pubConfirmRolPub.getZhTitle() == null ? "" : pubConfirmRolPub.getZhTitle());
        contentMap.put(MsgConstants.MSG_PUB_TITLE_EN,
            pubConfirmRolPub.getEnTitle() == null ? "" : pubConfirmRolPub.getEnTitle());
        contentMap.put(MsgConstants.MSG_PUB_ID, pubId);
      } else {
        throw new Exception("成果数据为空pubId=" + pubId);
      }
    } else if ("3".equals(msgType)) {
      Publication pubSimple = PublicationDao.get(pubId);
      if (pubSimple != null) {
        contentMap.put(MsgConstants.MSG_PUB_TITLE_ZH, pubSimple.getZhTitle() == null ? "" : pubSimple.getZhTitle());
        contentMap.put(MsgConstants.MSG_PUB_TITLE_EN, pubSimple.getEnTitle() == null ? "" : pubSimple.getEnTitle());

        contentMap.put(MsgConstants.MSG_PUB_ID, pubId);
      } else {
        throw new Exception("成果数据为空pubId=" + pubId);
      }
      /*
       * PubIndexUrl pubIndexUrl = pubIndexUrlDao.get(pubId); if (pubIndexUrl != null) { String url =
       * domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl();
       * contentMap.put(MsgConstants.MSG_PUB_SHORT_URL, url); }
       */
    }
    mc.setContent(JacksonUtils.mapToJsonStr(contentMap));
    msgContentDao.insertMsgContent(mc);
    MsgRelation mr = new MsgRelation();
    mr.setContentId(contentId);
    mr.setCreateDate(date);
    mr.setDealDate(date);
    mr.setDealStatus(0);
    mr.setStatus(0);
    mr.setSenderId(senderId);
    mr.setReceiverId(receiverId);
    mr.setType(Integer.parseInt(msgType));
    msgRelationDao.save(mr);
  }

  @Override
  public List<RecomMsgForm> getRecomMsgFormList(Long type, Integer maxResults) throws Exception {
    return recomMsgFormDao.findRecomMsgList(maxResults);
  }

  @Override
  public void buildRecomMsg(RecomMsgForm r) throws Exception {
    try {
      this.sendMsg(r.getType().toString(), r.getSenderId(), r.getReceiverId(), r.getResId(), r.getCount());
      r.setTaskStatus(1);
    } catch (Exception e) {
      r.setTaskStatus(2);
    }
    recomMsgFormDao.save(r);
  }

}
