package com.smate.center.open.service.msg;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.dao.msg.MsgContentDao;
import com.smate.center.open.dao.msg.MsgRelationDao;
import com.smate.center.open.model.msg.MsgContent;
import com.smate.center.open.model.msg.MsgRelation;
import com.smate.core.base.utils.constant.MsgConstants;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 好友推荐服务实现类
 * 
 * @author ajb
 *
 */
@Transactional(rollbackFor = Exception.class)
public class CreateFriendRcmdMsgServiceImpl extends CreateMsgBase {
  @Autowired
  private MsgContentDao msgContentDao;
  @Autowired
  private MsgRelationDao msgRelationDao;

  @Override
  public Map<String, Object> parameterVeify(Map<String, Object> paramet) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>();
    Object friendIdListObj = paramet.get(MsgConstants.MSG_RCMD_FRIEND_ID_LIST);
    if (friendIdListObj == null || StringUtils.isBlank(friendIdListObj.toString())) {
      result = super.errorMap(OpenMsgCodeConsts.SCM_708, paramet, "friendId不能为空，！");
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
    contentMap.put(MsgConstants.MSG_RCMD_FRIEND_ID_LIST, dateMap.get(MsgConstants.MSG_RCMD_FRIEND_ID_LIST));
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
    mr.setType(Integer.parseInt(MsgConstants.MSG_TYPE_FRIEND__RCMD));
    msgRelationDao.save(mr);
  }
}
