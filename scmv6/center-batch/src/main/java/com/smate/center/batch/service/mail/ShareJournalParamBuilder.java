package com.smate.center.batch.service.mail;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.smate.center.batch.constant.InsideMessageConstants;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.Message;


/**
 * 短信－分享期刊推荐.
 * 
 * @author pwl
 * 
 */
@Service("shareJournalParamBuilder")
public class ShareJournalParamBuilder extends CommonInsideMailParamBuilder implements InsideMailParamBuilder {

  @Override
  public Map<String, Object> builderParam(Message message, List<Long> receiverIdList) throws ServiceException {
    try {
      Map<String, Object> map = this.buildCommonParam(message, receiverIdList);
      map.put("zhSubject", StringUtils.isBlank(message.getTitle()) ? message.getEnTitle() : message.getTitle());
      map.put("enSubject", StringUtils.isBlank(message.getEnTitle()) ? message.getTitle() : message.getEnTitle());
      map.put("isProduceDynamic", 1);
      map.put("msgType", String.valueOf(InsideMessageConstants.MSG_TYPE_SHARE_JOURNAL));
      map.put("notSysUserTemplate", "Email_Share_Journal_NotSysUser_Template_${locale}.ftl");
      map.put("recommendUrl", message.getRecommendUrl());

      return map;
    } catch (Exception e) {
      logger.error("短信－构造分享期刊推荐所需参数出现异常：", e);
      throw new ServiceException(e);
    }
  }
}
