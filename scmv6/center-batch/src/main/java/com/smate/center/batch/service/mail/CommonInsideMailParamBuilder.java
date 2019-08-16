package com.smate.center.batch.service.mail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.constant.InsideMessageConstants;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.Message;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 短信－共同参数构建.
 * 
 * @author pwl
 * 
 */
public class CommonInsideMailParamBuilder {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PersonProfileDao personProfileDao;

  public Map<String, Object> buildCommonParam(Message message, List<Long> receiverIdList) throws ServiceException {
    Map<String, Object> map = new HashMap<String, Object>();

    map.put("isSendMsgMail", 1); // 是否发送站内信邮件
    map.put("isProduceDynamic", 0); // 是否产生动态
    map.put("tmpId", InsideMessageConstants.MSG_TEMPLATE_INSIDE_MESSAGE); // 收件箱查看模板
    map.put("template", "Person_Receive_Message_Template_${locale}.ftl");
    // 任务中无法获取当前人ID
    Long id = 0L;
    if (SecurityUtils.getCurrentUserId() == null || SecurityUtils.getCurrentUserId() == 0) {
      id = message.getPsnId();
    } else {
      id = SecurityUtils.getCurrentUserId();
    }

    Person sender = personProfileDao.get(id);
    String senderZhName =
        StringUtils.isBlank(sender.getName()) ? sender.getFirstName() + " " + sender.getLastName() : sender.getName();
    map.put("zhSubject", senderZhName + "给您发了一条站内信。");
    map.put("senderZhName", senderZhName);

    String senderEnName = sender.getFirstName() + " " + sender.getLastName();
    if (StringUtils.isBlank(sender.getFirstName()) || StringUtils.isBlank(sender.getLastName())) {
      senderEnName = sender.getName();
    }
    map.put("enSubject", senderEnName + " has sent you a message on ScholarMate.");
    map.put("senderEnName", senderEnName);
    map.put("viewUrl", "/scmwebsns/msgbox/smsMain");

    JSONObject receiversJson = new JSONObject();
    if (CollectionUtils.isNotEmpty(receiverIdList)) {
      JSONArray jsonArray = new JSONArray();
      JSONObject jsonObject = null;
      for (Long psnId : receiverIdList) {
        jsonObject = new JSONObject();
        jsonObject.accumulate("psnId", psnId);
        jsonArray.add(jsonObject);
      }

      receiversJson.accumulate("receivers", jsonArray);
    }
    map.put("extOtherInfo", receiversJson.toString());

    map.put("title", message.getTitle());
    map.put("enTitle", message.getTitle());
    map.put("content", message.getContent());
    // 把当前PUB所属人放入map，与老系统有差别
    map.put("newPsnId", id);
    return map;
  }
}
