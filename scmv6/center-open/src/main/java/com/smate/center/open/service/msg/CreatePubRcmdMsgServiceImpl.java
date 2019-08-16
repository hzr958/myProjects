package com.smate.center.open.service.msg;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.dao.msg.MsgContentDao;
import com.smate.center.open.dao.msg.MsgRelationDao;
import com.smate.center.open.dao.rcmd.pub.PubConfirmRolPubDao;
import com.smate.center.open.model.msg.MsgContent;
import com.smate.center.open.model.msg.MsgRelation;
import com.smate.center.open.model.rcmd.pub.PubConfirmRolPub;
import com.smate.core.base.utils.constant.MsgConstants;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 成果推荐服务实现类
 * 
 * @author ajb
 *
 */
@Transactional(rollbackFor = Exception.class)
public class CreatePubRcmdMsgServiceImpl extends CreateMsgBase {
  @Autowired
  private MsgContentDao msgContentDao;
  @Autowired
  private MsgRelationDao msgRelationDao;
  @Autowired
  private PubConfirmRolPubDao pubConfirmRolPubDao;

  @Override
  public Map<String, Object> parameterVeify(Map<String, Object> paramet) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>();
    Object rolpubIdObj = paramet.get(MsgConstants.MSG_ROLPUB_ID);
    if (rolpubIdObj == null || !NumberUtils.isNumber(rolpubIdObj.toString())) {
      result = super.errorMap(OpenMsgCodeConsts.SCM_721, paramet, "rolpubId不能为空，且必须为数字！");
    }
    return result;
  }

  @Override
  public void buildMsg(Map<String, Object> dateMap, Long senderId, String receiverIds) throws Exception {
    Long contentId = msgContentDao.getContentId();
    MsgContent mc = new MsgContent();
    mc.setContentId(contentId);
    Map<String, Object> contentMap = new HashMap<String, Object>();
    contentMap.put(MsgConstants.MSG_SENDER_ID, senderId);
    contentMap.put(MsgConstants.MSG_RECEIVER_IDS, receiverIds);
    // TODO 构造content数据
    Long rolpubId = NumberUtils.toLong(dateMap.get(MsgConstants.MSG_ROLPUB_ID).toString());
    contentMap.put(MsgConstants.MSG_ROLPUB_ID, rolpubId);

    PubConfirmRolPub pubConfirmRolPub = pubConfirmRolPubDao.get(rolpubId);
    if (pubConfirmRolPub != null) {
      contentMap.put(MsgConstants.MSG_PUB_TITLE_ZH,
          pubConfirmRolPub.getZhTitle() == null ? "" : pubConfirmRolPub.getZhTitle());
      contentMap.put(MsgConstants.MSG_PUB_TITLE_EN,
          pubConfirmRolPub.getEnTitle() == null ? "" : pubConfirmRolPub.getEnTitle());
      contentMap.put(MsgConstants.MSG_PUB_ID, rolpubId);
      contentMap.put(MsgConstants.MSG_PUB_BRIEF_DESC_ZH,
          pubConfirmRolPub.getBriefDesc() == null ? "" : pubConfirmRolPub.getBriefDesc());
      contentMap.put(MsgConstants.MSG_PUB_AUTHOR_NAME,
          pubConfirmRolPub.getAuthorNames() == null ? "" : pubConfirmRolPub.getAuthorNames());
    }
    mc.setContent(JacksonUtils.mapToJsonStr(contentMap));
    msgContentDao.save(mc);
    dateMap.put(MsgConstants.MSG_CONTENT_ID, contentId);
  }

  @Override
  public void produceMsg(Map<String, Object> dateMap, Long senderId, Long receiverId) throws Exception {
    Long contentId = NumberUtils.toLong(dateMap.get(MsgConstants.MSG_CONTENT_ID).toString());
    Date date = new Date();
    MsgRelation mr = new MsgRelation();
    mr.setContentId(contentId);
    mr.setCreateDate(date);
    mr.setDealDate(date);
    mr.setDealStatus(0);
    mr.setStatus(0);
    mr.setSenderId(senderId);
    mr.setReceiverId(receiverId);
    mr.setType(Integer.parseInt(MsgConstants.MSG_TYPE_PUB_RCMD));
    msgRelationDao.save(mr);
  }
}
