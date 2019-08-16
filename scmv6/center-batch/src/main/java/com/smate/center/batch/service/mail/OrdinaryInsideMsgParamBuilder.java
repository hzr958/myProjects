package com.smate.center.batch.service.mail;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.smate.center.batch.constant.InsideMessageConstants;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.Message;


/**
 * 短信－普通站内信.
 * 
 * @author pwl
 * 
 */
@Service("ordinaryInsideMsgParamBuilder")
public class OrdinaryInsideMsgParamBuilder extends CommonInsideMailParamBuilder implements InsideMailParamBuilder {

  @Override
  public Map<String, Object> builderParam(Message message, List<Long> receiverIdList) throws ServiceException {
    try {
      Map<String, Object> map = this.buildCommonParam(message, receiverIdList);
      map.put("zhSubject", StringUtils.isBlank(message.getTitle()) ? message.getEnTitle() : message.getTitle());
      map.put("enSubject", StringUtils.isBlank(message.getEnTitle()) ? message.getTitle() : message.getEnTitle());
      map.put("msgType", String.valueOf(InsideMessageConstants.MSG_TYPE_INSIDE_MESSAGE));
      map.put("mailSetUrl", "/scmwebsns/user/setting/getMailTypeList");
      map.put("recommendUrl", message.getRecommendUrl());

      if (StringUtils.isNotBlank(message.getJsonFile())) {
        JSONObject extOtherInfoJson = JSONObject.fromObject(map.get("extOtherInfo"));
        extOtherInfoJson.accumulate("fileList", JSONArray.fromObject(message.getJsonFile()));
        map.put("extOtherInfo", extOtherInfoJson.toString());
      }
      return map;
    } catch (Exception e) {
      logger.error("短信－构造普通站内信所需参数出现异常：", e);
      throw new ServiceException(e);
    }
  }

}
