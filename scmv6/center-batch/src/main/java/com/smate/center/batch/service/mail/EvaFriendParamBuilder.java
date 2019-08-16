package com.smate.center.batch.service.mail;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.smate.center.batch.constant.InsideMessageConstants;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.Message;
import com.smate.core.base.utils.string.ServiceUtil;


/**
 * 短信－评价好友.
 * 
 * @author pwl
 * 
 */
@Service("evaFriendParamBuilder")
public class EvaFriendParamBuilder extends CommonInsideMailParamBuilder implements InsideMailParamBuilder {

  @Override
  public Map<String, Object> builderParam(Message message, List<Long> receiverIdList) throws ServiceException {
    try {
      Map<String, Object> map = this.buildCommonParam(message, receiverIdList);
      map.put("msgType", String.valueOf(InsideMessageConstants.MSG_TYPE_EVALUATION_OF));
      map.put("template", "Evaluate_Friend_Template_${locale}.ftl");
      map.put("isSendMsgMail", 0);
      map.put("tmpId", InsideMessageConstants.MSG_TEMPLATE_INSIDE_EVA_FRIEND); // 收件箱查看模板
      map.put("zhSubject", map.get("senderZhName") + "对您进行了评价");
      map.put("enSubject", map.get("senderEnName") + " has endorsed your work");
      map.put("title", "对您进行了评价");
      map.put("enTitle", "To you on the evaluation");
      map.put("content", message.getFappraisalSend().getContent());
      map.put("psnWork", message.getFappraisalSend().getFriendWork());
      map.put("viewUrl", "/scmwebsns/friendFappManage/loadMineFappMain");
      JSONObject extOtherInfoJson = JSONObject.fromObject(map.get("extOtherInfo"));
      if (message.getFappraisalSend().getFriendWorkId() != null) {
        extOtherInfoJson.accumulate("psnWorkId", message.getFappraisalSend().getFriendWorkId());
        extOtherInfoJson.accumulate("des3PsnWorkId",
            ServiceUtil.encodeToDes3(message.getFappraisalSend().getFriendWorkId() == null ? ""
                : message.getFappraisalSend().getFriendWorkId().toString()));
      }

      if (StringUtils.isNotBlank(message.getFappraisalSend().getFriendWork())) {
        extOtherInfoJson.accumulate("psnWork", message.getFappraisalSend().getFriendWork());
      }
      if (StringUtils.isNotBlank(message.getFappraisalSend().getRelations())) {
        extOtherInfoJson.accumulate("relation", message.getFappraisalSend().getRelations());
        extOtherInfoJson.accumulate("enRelation", message.getFappraisalSend().getRelationsEn());
      }
      map.put("extOtherInfo", extOtherInfoJson.toString());
      return map;
    } catch (Exception e) {
      logger.error("短信－构造评价好友所需参数出现异常：", e);
      throw new ServiceException(e);
    }

  }

  /*
   * private String getReceiverLanguage(Person person) throws ServiceException { String language =
   * person.getEmailLanguageVersion(); if (StringUtils.isBlank(language)) { language =
   * businessMethod.getLocale(person.getPersonId()); } return language; }
   */

}
