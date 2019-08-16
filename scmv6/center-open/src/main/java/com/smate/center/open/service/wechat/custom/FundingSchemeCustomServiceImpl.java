package com.smate.center.open.service.wechat.custom;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.center.open.constant.wechat.MsgSendingConstant;
import com.smate.center.open.exception.OpenException;
import com.smate.core.base.utils.constant.wechat.WeChatConstant;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 资助计划客服服务(消息).
 * 
 * @author xys
 *
 */
public class FundingSchemeCustomServiceImpl extends AbstractCustom {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  private String viewUrl;

  @Override
  public Map<String, Object> buildMessage(Map<String, Object> paramMap) throws OpenException {
    try {
      Map<String, Object> resultMap = new HashMap<String, Object>();
      Map<String, Object> msgMap = new HashMap<String, Object>();
      String touser = paramMap.get(MsgSendingConstant.TO_USER).toString();
      msgMap.put(MsgSendingConstant.TO_USER, touser);
      msgMap.put(MsgSendingConstant.MSG_TYPE, MsgSendingConstant.MSG_TYPE_TEXT);
      Map<String, Object> textMap = new HashMap<String, Object>();
      StringBuffer sb = new StringBuffer();
      if (paramMap.get(WeChatConstant.MSG_SOURCE_KEY).equals(WeChatConstant.MSG_SOURCE_SMATE)) {
        sb.append(paramMap.get(MsgSendingConstant.BINDING_SUCCESS_CONTENT));
      }
      sb.append("通过以下方式打开资助计划：");
      sb.append("\n\n<a href=\"");
      sb.append(viewUrl);
      sb.append("?openid=");
      sb.append(touser);
      sb.append("\">点击这里，立即打开资助计划</a>");
      textMap.put(MsgSendingConstant.CONTENT, sb.toString());
      msgMap.put(MsgSendingConstant.MSG_TYPE_TEXT, textMap);
      resultMap.put(MsgSendingConstant.MSG_TOBESENT_KEY, JacksonUtils.jsonObjectSerializer(msgMap));
      return resultMap;
    } catch (OpenException e) {
      logger.error("Funding Scheme:build message error", e);
      throw new OpenException(e);
    }
  }

  public void setViewUrl(String viewUrl) {
    this.viewUrl = viewUrl;
  }

}
