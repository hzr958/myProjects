package com.smate.web.dyn.service.msg;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.dyn.dao.msg.MsgContentDao;
import com.smate.web.dyn.form.msg.MsgShowForm;
import com.smate.web.dyn.model.msg.MsgContent;
import com.smate.web.dyn.model.msg.MsgRelation;
import com.smate.web.dyn.model.msg.MsgShowInfo;

/**
 * 5=好友推荐
 * 
 * @author zzx
 *
 */
public class BuildFriendRcmdMsgInfoServiceImpl extends BuildMsgInfoBase {
  @Resource
  private MsgContentDao msgContentDao;
  @Resource
  private PersonDao personDao;

  @Resource
  private PsnProfileUrlDao psnProfileUrlDao;
  @Value("${domainscm}")
  private String domainscm;

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
      if (StringUtils.isNotBlank(msi.getRcmdFriendIdList())) {
        String[] arrId = msi.getRcmdFriendIdList().split(",");
        if (arrId != null && arrId.length > 0) {
          Person psn = null;
          for (int i = arrId.length; i > 0; i--) {
            Long psnId = Long.parseLong(arrId[i - 1]);

            psn = personDao.findPersonBase(psnId);
            msi.getRcmdFriendZhNameList().add(psn.getZhName());
            msi.getRcmdFriendEnNameList().add(psn.getEnName());

            PsnProfileUrl profileUrl = psnProfileUrlDao.get(psnId);
            if (profileUrl != null && StringUtils.isNotEmpty(profileUrl.getPsnIndexUrl())) {
              msi.getRcmdFriendShortUrlList().add(domainscm + "/P/" + profileUrl.getPsnIndexUrl());
            } else {
              msi.getRcmdFriendShortUrlList().add("");
            }
          }
        }
      }
      form.getMsgShowInfoList().add(msi);
    }

  }

}
