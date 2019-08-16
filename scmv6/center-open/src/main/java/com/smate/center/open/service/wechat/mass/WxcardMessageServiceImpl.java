package com.smate.center.open.service.wechat.mass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.constant.wechat.MsgSendingConstant;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.model.wechat.mass.Filter;
import com.smate.center.open.model.wechat.mass.Wxcard;
import com.smate.center.open.model.wechat.mass.WxcardMessage;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 卡券消息服务.
 * 
 * @author xys
 *
 */
@Service("wxcardMessageService")
@Transactional(rollbackFor = Exception.class)
public class WxcardMessageServiceImpl extends AbstractMass {

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> buildMessage(Map<String, Object> paramMap) throws OpenException {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    boolean isToGroup = (boolean) paramMap.get(MsgSendingConstant.IS_TO_GROUP_KEY);
    WxcardMessage wxcardMessage = new WxcardMessage();
    if (isToGroup) {// 根据分组进行群发
      wxcardMessage.setFilter((Filter) paramMap.get(MsgSendingConstant.FILTER_KEY));
    } else {
      wxcardMessage.setTouser((List<String>) paramMap.get(MsgSendingConstant.LIST_TOUSER_KEY));
    }
    String cardId = "";
    wxcardMessage.setWxcard(new Wxcard(cardId));
    wxcardMessage.setMsgtype(MsgSendingConstant.MSG_TYPE_WXCARD);
    resultMap.put(MsgSendingConstant.MSG_TOBESENT_KEY, JacksonUtils.jsonObjectSerializer(wxcardMessage));
    return resultMap;
  }

}
