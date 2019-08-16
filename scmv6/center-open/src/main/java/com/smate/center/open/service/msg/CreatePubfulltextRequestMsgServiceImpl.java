package com.smate.center.open.service.msg;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.consts.V8pubQueryPubConst;
import com.smate.center.open.dao.msg.MsgContentDao;
import com.smate.center.open.dao.msg.MsgRelationDao;
import com.smate.center.open.model.msg.MsgContent;
import com.smate.center.open.model.msg.MsgRelation;
import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.utils.constant.MsgConstants;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 全文请求服务实现类
 * 
 * @author ajb
 *
 */
@Transactional(rollbackFor = Exception.class)
public class CreatePubfulltextRequestMsgServiceImpl extends CreateMsgBase {
  @Autowired
  private MsgContentDao msgContentDao;
  @Autowired
  private MsgRelationDao msgRelationDao;

  @Override
  public Map<String, Object> parameterVeify(Map<String, Object> paramet) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>();
    Object pubIdObj = paramet.get(MsgConstants.MSG_PUB_ID);
    if (pubIdObj == null || !NumberUtils.isNumber(pubIdObj.toString())) {
      result = super.errorMap(OpenMsgCodeConsts.SCM_706, paramet, "pubId不能为空，且必须为数字！");
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
    Long pubId = NumberUtils.toLong(dateMap.get(MsgConstants.MSG_PUB_ID).toString());

    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setSearchPubId(pubId);
    pubQueryDTO.setServiceType(V8pubQueryPubConst.QUERY_PUB_BY_PUB_ID_SERVICE);
    Map<String, Object> pubInfo = getRemotePubInfo(pubId, pubQueryDTO);
    if (pubInfo != null && pubInfo.get("pubId") != null) {
      String title = constructParams(String.valueOf(pubInfo.get("title") == null ? "" : pubInfo.get("title")));
      contentMap.put(MsgConstants.MSG_PUB_TITLE_ZH, title);
      contentMap.put(MsgConstants.MSG_PUB_TITLE_EN, title);
      contentMap.put(MsgConstants.MSG_PUB_BRIEF_DESC_ZH, pubInfo.get("briefDesc"));
      contentMap.put(MsgConstants.MSG_PUB_BRIEF_DESC_EN, pubInfo.get("briefDesc"));
      contentMap.put(MsgConstants.MSG_PUB_AUTHOR_NAME, pubInfo.get("authorNames"));
      contentMap.put(MsgConstants.MSG_PUB_SHORT_URL, pubInfo.get("pubIndexUrl"));
      contentMap.put(MsgConstants.MSG_PUB_ID, pubId);
    }
    if ("1".equals(pubInfo.get("hasFulltext").toString())) {
      contentMap.put(MsgConstants.MSG_HAS_PUB_FULLTEXT, true);
      contentMap.put(MsgConstants.MSG_PUB_FULLTEXT_ID, pubInfo.get("fullTextFieId"));
      contentMap.put(MsgConstants.MSG_PUB_FULLTEXT_EXT, "");
      contentMap.put(MsgConstants.MSG_PUB_FULLTEXT_IMAGE_PATH, pubInfo.get("fullTextImgUrl"));

    } else {
      contentMap.put(MsgConstants.MSG_HAS_PUB_FULLTEXT, false);
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
    mr.setType(Integer.parseInt(MsgConstants.MSG_TYPE_PUBFULLTEXT_REQUEST));
    msgRelationDao.save(mr);
    dateMap.put(MsgConstants.MSG_RELATION_ID, mr.getId());
  }

  private String constructParams(String title) {
    title = StringUtils.trimToEmpty(title);
    title = StringUtils.substring(title, 0, 500);
    return title;
  }
}
