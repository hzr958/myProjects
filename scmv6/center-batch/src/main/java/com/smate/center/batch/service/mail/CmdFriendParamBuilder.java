package com.smate.center.batch.service.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.batch.constant.InsideMessageConstants;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.Message;
import com.smate.center.batch.service.psn.PersonManager;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.service.consts.SysDomainConst;
import com.smate.core.base.utils.string.ServiceUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 短信－好友推荐.
 * 
 * @author pwl
 * 
 */
@Service("cmdFriendParamBuilder")
public class CmdFriendParamBuilder extends CommonInsideMailParamBuilder implements InsideMailParamBuilder {

  @Autowired
  private PersonManager personManager;
  @Autowired
  private SysDomainConst sysDomainConst;

  @Override
  public Map<String, Object> builderParam(Message message, List<Long> receiverIdList) throws ServiceException {
    try {
      Map<String, Object> map = this.buildCommonParam(message, receiverIdList);
      map.put("msgType", String.valueOf(InsideMessageConstants.MSG_TYPE_FRIEND_RECOMMEND));
      map.put("tmpId", InsideMessageConstants.MSG_TEMPLATE_INSIDE_CMD_FRIEND);
      String[] recommenders = StringUtils.split(message.getRecommenders(), ",");
      map.put("zhSubject", map.get("senderZhName") + "为您推荐了" + recommenders.length + "名好友");
      map.put("enSubject", map.get("senderEnName") + " has suggested you to add a friend");
      map.put("template", "Recommend_Friend_Template_${locale}.ftl");
      map.put("title", "来自" + map.get("senderZhName") + "的好友推荐");
      map.put("enTitle", "From " + map.get("senderEnName") + "'s Friend Recommendation");
      map.put("mailSetUrl", "/scmwebsns/user/setting/getMailTypeList");

      if (!ArrayUtils.isEmpty(recommenders)) {
        JSONArray commenderJson = new JSONArray();
        List<Map<String, String>> friendList = new ArrayList<Map<String, String>>();
        JSONObject jsonObject = null;
        Map<String, String> friendMap = null;
        for (String recPsnId : recommenders) {
          jsonObject = new JSONObject();
          jsonObject.accumulate("psnId", recPsnId);
          commenderJson.add(jsonObject);
          friendMap = new HashMap<String, String>();
          Person frdPerson = this.getPersonService(Long.parseLong(recPsnId)).getPerson(Long.parseLong(recPsnId));
          if (frdPerson != null) {
            String frdZhName =
                StringUtils.isBlank(frdPerson.getName()) ? frdPerson.getFirstName() + " " + frdPerson.getLastName()
                    : frdPerson.getName();
            friendMap.put("friendZhName", frdZhName);

            String frdEnName = frdPerson.getFirstName() + " " + frdPerson.getLastName();
            if (StringUtils.isBlank(frdPerson.getFirstName()) || StringUtils.isBlank(frdPerson.getLastName())) {
              frdEnName = frdPerson.getName();
            }
            friendMap.put("friendEnName", frdEnName);
          }
          friendMap.put("friendUrl", getPersonResumeUrl(true, frdPerson.getPersonId()));
          friendMap.put("friendId", recPsnId);
          friendList.add(friendMap);
        }
        map.put("friendList", friendList);
        JSONObject extOtherInfoJson = JSONObject.fromObject(map.get("extOtherInfo"));
        extOtherInfoJson.accumulate("commenders", commenderJson);
        map.put("extOtherInfo", extOtherInfoJson.toString());
      }
      return map;
    } catch (Exception e) {
      logger.error("短信－构造好友推荐所需参数出现异常：", e);
      throw new ServiceException();
    }
  }

  private PersonManager getPersonService(Long psnId) {
    return this.personManager;
  }

  private String getPersonResumeUrl(boolean isOutter, Long psnId) throws ServiceException {
    try {
      if (isOutter) {
        String webUrl = sysDomainConst.getSnsDomain();
        return webUrl + "/resume/view?des3PsnId=" + ServiceUtil.encodeToDes3(psnId.toString());
      } else {
        return "/scmwebsns/resume/view?des3PsnId=" + ServiceUtil.encodeToDes3(psnId.toString());
      }
    } catch (Exception e) {
      logger.error("读取用户个人链接失败！", e);
      return null;
    }
  }
}
