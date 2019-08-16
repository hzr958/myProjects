package com.smate.center.batch.service.mail;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.batch.constant.InsideMessageConstants;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.GroupPsnNode;
import com.smate.center.batch.model.sns.pub.Message;
import com.smate.center.batch.service.pub.GroupService;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 短信－邀请某人为成果共享成果.
 * 
 * @author pwl
 * 
 */
@Service("sharePubForGroupParamBuilder")
public class SharePubForGroupParamBuilder extends CommonInsideMailParamBuilder implements InsideMailParamBuilder {

  @Autowired
  private GroupService groupService;

  @Override
  public Map<String, Object> builderParam(Message message, List<Long> receiverIdList) throws ServiceException {
    try {
      Map<String, Object> map = this.buildCommonParam(message, receiverIdList);
      map.put("msgType", String.valueOf(InsideMessageConstants.MSG_TYPE_INVITE_ADDPUBFOR_GROUP));
      map.put("tmpId", InsideMessageConstants.MSG_TEMPLATE_INVITE_ADDPUBFOR_GROUP); // 收件箱查看模板
      map.put("template", "Group_Member_Share_Pub_${locale}.ftl");
      map.put("isSendMsgMail", 0);
      map.put("zhSubject", map.get("senderZhName") + "邀请您为群组“" + message.getGroupName() + "”添加成果");
      map.put("enSubject", map.get("senderEnName") + " has invited you to add publication(s) for group \""
          + message.getGroupName() + "\"");
      map.put("title", "请为群组“" + message.getGroupName() + "”添加成果");
      map.put("enTitle", "Please add publication(s) for \"" + message.getGroupName() + "\"");
      map.put("groupName", message.getGroupName());
      map.put("mailSetUrl", "/scmwebsns/user/setting/getMailTypeList");
      GroupPsnNode groupPsnNode = groupService.findGroup(message.getGId());
      // 链接地址的跳转地址部分.
      String targetUrl = "/group/pub?menuId=31&sharePub=true&isMailReq=true&groupPsn.des3GroupId="
          + java.net.URLEncoder.encode(groupPsnNode.getDes3GroupId(), "UTF-8") + "&groupPsn.des3GroupNodeId="
          + java.net.URLEncoder.encode(ServiceUtil.encodeToDes3(groupPsnNode.getNodeId().toString()), "UTF-8");
      map.put("targetUrl", targetUrl);
      map.put("casUrl", message.getCasUrl());

      JSONObject extOtherInfoJson = JSONObject.fromObject(map.get("extOtherInfo"));
      extOtherInfoJson.accumulate("groupId", message.getGroupId());
      extOtherInfoJson.accumulate("des3GroupId",
          ServiceUtil.encodeToDes3(message.getGroupId() == null ? "" : message.getGroupId().toString()));
      extOtherInfoJson.accumulate("groupName", message.getGroupName());
      extOtherInfoJson.accumulate("shareUrl", message.getShareUrl());
      map.put("extOtherInfo", extOtherInfoJson.toString());
      return map;
    } catch (Exception e) {
      logger.error("短信－构造邀请好友为群组共享成果参数出现异常：", e);
      throw new ServiceException();
    }
  }

}
