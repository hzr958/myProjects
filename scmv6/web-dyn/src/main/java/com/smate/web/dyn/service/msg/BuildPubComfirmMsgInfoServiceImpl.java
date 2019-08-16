package com.smate.web.dyn.service.msg;

import javax.annotation.Resource;

import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.dyn.dao.msg.MsgContentDao;
import com.smate.web.dyn.form.msg.MsgShowForm;
import com.smate.web.dyn.model.msg.MsgContent;
import com.smate.web.dyn.model.msg.MsgRelation;
import com.smate.web.dyn.model.msg.MsgShowInfo;

/**
 * 2=成果认领
 * 
 * @author zzx
 *
 */
public class BuildPubComfirmMsgInfoServiceImpl extends BuildMsgInfoBase {
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
      form.getMsgShowInfoList().add(msi);
    }

  }

}
