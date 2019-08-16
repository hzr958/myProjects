package com.smate.center.open.service.wechat.mass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.constant.wechat.MsgSendingConstant;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.model.wechat.mass.Content;
import com.smate.center.open.model.wechat.mass.Filter;
import com.smate.center.open.model.wechat.mass.TextMessage;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 文本消息服务.
 * 
 * @author xys
 *
 */
@Service("textMassService")
@Transactional(rollbackFor = Exception.class)
public class TextMassServiceImpl extends AbstractMass {

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> buildMessage(Map<String, Object> paramMap) throws OpenException {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    boolean isToGroup = (boolean) paramMap.get(MsgSendingConstant.IS_TO_GROUP_KEY);
    TextMessage textMessage = new TextMessage();
    if (isToGroup) {// 根据分组进行群发
      textMessage.setFilter((Filter) paramMap.get(MsgSendingConstant.FILTER_KEY));
    } else {
      textMessage.setTouser((List<String>) paramMap.get(MsgSendingConstant.LIST_TOUSER_KEY));
    }
    String content = "这是群发消息测试,大家别介意哈!您也可以选择屏蔽此公众测试号的消息";
    textMessage.setText(new Content(content));
    textMessage.setMsgtype(MsgSendingConstant.MSG_TYPE_TEXT);
    resultMap.put(MsgSendingConstant.MSG_TOBESENT_KEY, JacksonUtils.jsonObjectSerializer(textMessage));
    return resultMap;
  }

}
