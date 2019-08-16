package com.smate.center.batch.service.mail;

import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.Message;


/**
 * 短信参数构造抽象类，Strategy(抽象策略类)．
 * 
 * @author pwl
 * 
 */
public interface InsideMailParamBuilder {

  Map<String, Object> builderParam(Message message, List<Long> receiverIdList) throws ServiceException;
}
