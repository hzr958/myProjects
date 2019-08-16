package com.smate.center.batch.service.mail;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.smate.center.batch.constant.InsideMessageConstants;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.Message;
import com.smate.core.base.utils.security.SecurityUtils;


/**
 * 短信－祝贺新工作.
 * 
 * @author pwl
 * 
 */
@Service("congratutionJobParamBuilder")
public class CongratutionJobParamBuilder extends CommonInsideMailParamBuilder implements InsideMailParamBuilder {

  @Override
  public Map<String, Object> builderParam(Message message, List<Long> receiverIdList) throws ServiceException {
    try {
      message.setMsgType(InsideMessageConstants.MSG_TYPE_CONGRATULATION_JOB);
      Map<String, Object> map = this.buildCommonParam(message, receiverIdList);
      map.put("template", "person_receive_congratulationJob_${locale}.ftl");
      map.put("msgType", String.valueOf(InsideMessageConstants.MSG_TYPE_CONGRATULATION_JOB));
      map.put("tmpId", InsideMessageConstants.MSG_TEMPLATE_INSIDE_CONGRATUTION_JOB);
      map.put("title", "祝贺您的新工作（" + message.getWork() + "）");
      map.put("zhSubject", map.get("senderZhName") + "祝贺了您的新工作");
      map.put("enTitle", "Congratulation for your new job(" + message.getWork() + ")");
      map.put("enSubject", map.get("senderEnName") + " congratulated your new job");
      JSONObject extOtherInfoJson = JSONObject.fromObject(map.get("extOtherInfo"));
      extOtherInfoJson.accumulate("congratulationPsnId", SecurityUtils.getCurrentUserId());
      extOtherInfoJson.accumulate("psnWork", message.getWork());
      map.put("extOtherInfo", extOtherInfoJson.toString());
      return map;
    } catch (Exception e) {
      logger.error("短信－构造祝贺新工作所需参数出现异常：", e);
      throw new ServiceException(e);
    }
  }
}
