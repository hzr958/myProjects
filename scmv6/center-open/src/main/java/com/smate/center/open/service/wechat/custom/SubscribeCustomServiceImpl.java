package com.smate.center.open.service.wechat.custom;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.constant.wechat.MsgSendingConstant;
import com.smate.center.open.exception.OpenException;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 订阅客服服务(消息).
 * 
 * @author xys
 *
 */
@Service("subscribeCustomService")
@Transactional(rollbackFor = Exception.class)
public class SubscribeCustomServiceImpl extends AbstractCustom {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${wechat.appid}")
  private String appid;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainMobile}")
  private String domainMobile;

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
      sb.append("感谢关注科研之友！ 绑定帐号获得更多基金机会， 更方便有效推广论文！");
      sb.append("\n\n<a href=\"");
      sb.append("https://open.weixin.qq.com/connect/oauth2/authorize");
      sb.append("?appid=");
      sb.append(appid);
      sb.append("&redirect_uri=");
      sb.append(domainMobile);
      sb.append("/dynweb/mobile/dynshow");
      sb.append("&response_type=code&scope=snsapi_base&state=1#wechat_redirect");
      sb.append("\">点击“我的科研”绑定</a>");
      textMap.put(MsgSendingConstant.CONTENT, sb.toString());
      msgMap.put(MsgSendingConstant.MSG_TYPE_TEXT, textMap);
      resultMap.put(MsgSendingConstant.MSG_TOBESENT_KEY, JacksonUtils.jsonObjectSerializer(msgMap));
      return resultMap;
    } catch (Exception e) {
      logger.error("Subscribe:build message error", e);
      throw new OpenException(e);
    }
  }

}
