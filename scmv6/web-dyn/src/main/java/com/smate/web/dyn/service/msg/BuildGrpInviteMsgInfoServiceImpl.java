package com.smate.web.dyn.service.msg;

import javax.annotation.Resource;

import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.dyn.dao.msg.MsgContentDao;
import com.smate.web.dyn.form.msg.MsgShowForm;
import com.smate.web.dyn.model.msg.MsgContent;
import com.smate.web.dyn.model.msg.MsgRelation;
import com.smate.web.dyn.model.msg.MsgShowInfo;

/**
 * 9=邀请加入群组消息
 * 
 * @author zzx
 *
 */
public class BuildGrpInviteMsgInfoServiceImpl extends BuildMsgInfoBase {
  @Resource
  private MsgContentDao msgContentDao;
  @Resource
  private PersonDao personDao;

  @Override
  public void buildMsgShowInfo(MsgShowForm form, MsgRelation m) throws Exception {
    MsgShowInfo msi = null;
    MsgContent msgContent = msgContentDao.get(m.getContentId());
    if (msgContent != null && JacksonUtils.isJsonString(msgContent.getContent())) {
      msi = (MsgShowInfo) JacksonUtils.jsonObject(msgContent.getContent(), MsgShowInfo.class);
      msi.setType(m.getType());
      msi.setCreateDate(m.getCreateDate());
      msi.setMsgRelationId(m.getId());
      msi.setStatus(m.getStatus());
      Person senderInfo = personDao.findPersonBase(m.getSenderId());
      buildPsnInfo(msi, senderInfo, "sender");
      form.getMsgShowInfoList().add(msi);
    }

  }

}
