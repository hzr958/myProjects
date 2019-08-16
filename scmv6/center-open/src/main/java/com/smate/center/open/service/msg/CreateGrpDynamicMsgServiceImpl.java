package com.smate.center.open.service.msg;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
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
 * 群组动态消息 服务实现类
 * 
 * @author ajb
 *
 */
@Transactional(rollbackFor = Exception.class)
public class CreateGrpDynamicMsgServiceImpl extends CreateMsgBase {
  @Autowired
  private MsgContentDao msgContentDao;
  @Autowired
  private MsgRelationDao msgRelationDao;

  @Override
  public Map<String, Object> parameterVeify(Map<String, Object> paramet) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>();
    Object grpIdObj = paramet.get(MsgConstants.MSG_GRP_ID);
    Object grpMsgTypeObj = paramet.get(MsgConstants.MSG_GRP_MSG_TYPE);
    Object grpFileIdObj = paramet.get(MsgConstants.MSG_GRP_FILE_ID);
    Object grpPubIdObj = paramet.get(MsgConstants.MSG_GRP_PUB_ID);

    if (grpIdObj == null || !NumberUtils.isNumber(grpIdObj.toString())) {
      result = super.errorMap(OpenMsgCodeConsts.SCM_713, paramet, "grpId不能为空,且为数字");
    }
    if (grpMsgTypeObj == null || StringUtils.isBlank(grpMsgTypeObj.toString())) {
      result = super.errorMap(OpenMsgCodeConsts.SCM_714, paramet, "grpMsgType不能为空");
    }
    if (MsgConstants.MSG_GRP_MSG_ADD_COURSE.equals(grpMsgTypeObj.toString())
        || MsgConstants.MSG_GRP_MSG_ADD_WORK.equals(grpMsgTypeObj.toString())
        || MsgConstants.MSG_GRP_MSG_ADD_FILE.equals(grpMsgTypeObj.toString())) {
      if (grpFileIdObj == null || !NumberUtils.isNumber(grpFileIdObj.toString())) {
        result = super.errorMap(OpenMsgCodeConsts.SCM_716, paramet, "grpFileId不能为空,且为数字");
      }
    } else if (MsgConstants.MSG_GRP_MSG_ADD_PUB.equals(grpMsgTypeObj.toString())) {
      if (grpPubIdObj == null || !NumberUtils.isNumber(grpPubIdObj.toString())) {
        result = super.errorMap(OpenMsgCodeConsts.SCM_717, paramet, "grpPubId不能为空,且为数字");
      }
    } else {
      result = super.errorMap(OpenMsgCodeConsts.SCM_715, paramet, "grpMsgType 类型出错");
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

    contentMap.put(MsgConstants.MSG_GRP_ID, dateMap.get(MsgConstants.MSG_GRP_ID));
    contentMap.put(MsgConstants.MSG_GRP_MSG_TYPE, dateMap.get("MSG_GRP_MSG_TYPE"));
    contentMap.put(MsgConstants.MSG_GRP_FILE_ID,
        dateMap.get("MSG_GRP_FILE_ID") == null ? "" : dateMap.get("MSG_GRP_FILE_ID"));
    contentMap.put(MsgConstants.MSG_GRP_PUB_ID,
        dateMap.get("MSG_GRP_PUB_ID") == null ? "" : dateMap.get("MSG_GRP_PUB_ID"));

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
    mr.setType(Integer.parseInt(MsgConstants.MSG_TYPE_GRP_DYNAMIC));
    msgRelationDao.save(mr);
  }
}
