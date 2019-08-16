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
 * 短信－邀请某人为群组共享文件.
 * 
 * @author pwl
 * 
 */
@Service("shareFileForGroupParamBuilder")
public class ShareFileForGroupParamBuilder extends CommonInsideMailParamBuilder implements InsideMailParamBuilder {

  @Autowired
  private GroupService groupService;

  @Override
  public Map<String, Object> builderParam(Message message, List<Long> receiverIdList) throws ServiceException {
    try {
      Map<String, Object> map = this.buildCommonParam(message, receiverIdList);
      map.put("msgType", String.valueOf(InsideMessageConstants.MSG_TYPE_INVITE_ADDFILEFOR_GROUP));
      map.put("tmpId", InsideMessageConstants.MSG_TEMPLATE_INVITE_ADDFILEFOR_GROUP); // 收件箱查看模板
      map.put("template", "Group_Member_Share_File_${locale}.ftl");
      map.put("isSendMsgMail", 0);
      map.put("zhSubject", map.get("senderZhName") + "邀请您为群组“" + message.getGroupName() + "”添加文件");
      map.put("enSubject",
          map.get("senderEnName") + " has invited you to add file(s) for group \"" + message.getGroupName() + "\"");
      map.put("title", "请为群组“" + message.getGroupName() + "”添加文件");
      map.put("enTitle", "Please add file(s) for \"" + message.getGroupName() + "\"");
      map.put("groupName", message.getGroupName());
      GroupPsnNode groupPsnNode = groupService.findGroup(message.getGId());
      map.put("mailSetUrl", "/scmwebsns/user/setting/getMailTypeList");
      // 链接地址的跳转地址部分.
      String targetUrl = "/group/file?menuId=31&shareFile=true&isMailReq=true&groupPsn.des3GroupId="
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
