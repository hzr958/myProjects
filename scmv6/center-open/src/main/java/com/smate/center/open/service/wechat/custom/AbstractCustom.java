package com.smate.center.open.service.wechat.custom;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.open.constant.wechat.MsgReceivingConstant;
import com.smate.center.open.constant.wechat.MsgSendingConstant;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.service.wechat.AccessTokenService;
import com.smate.core.base.utils.constant.wechat.WeChatConstant;
import com.smate.core.base.utils.dao.wechat.WeChatRelationDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.wechat.MessageUtil;

/**
 * 抽象客服接口(消息).
 * 
 * @author xys
 *
 */
public abstract class AbstractCustom implements CustomService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private WeChatRelationDao weChatRelationDao;
  @Autowired
  private AccessTokenService accessTokenService;

  public abstract Map<String, Object> buildMessage(Map<String, Object> paramMap) throws OpenException;

  @SuppressWarnings("rawtypes")
  @Override
  public String sendMessage(Map<String, Object> paramMap) throws OpenException {
    try {
      Map<String, Object> resultMap = null;
      boolean checkBinding = true;
      if (paramMap.get(MsgSendingConstant.IS_CHECK_BINDING_KEY) != null) {
        checkBinding = (boolean) paramMap.get(MsgSendingConstant.IS_CHECK_BINDING_KEY);
      }
      if (!checkBinding || checkUserBinding(paramMap)) {// 不需绑定或已绑定
        String msgSource = paramMap.get(WeChatConstant.MSG_SOURCE_KEY).toString();
        if (msgSource.equals(WeChatConstant.MSG_SOURCE_SMATE)) {
          paramMap.put(MsgSendingConstant.BINDING_SUCCESS_CONTENT, MessageUtil.buildBindingSuccessContent());
        }
        resultMap = buildMessage(paramMap);
      } else {
        resultMap = buildMsgBeforeBinding(paramMap);
      }
      String reqUrl = paramMap.get(MsgSendingConstant.REQ_URL_KEY).toString();
      String fullReqUrl = MessageUtil.getFullReqUrl(reqUrl, accessTokenService.getAccessToken());
      String msg = resultMap.get(MsgSendingConstant.MSG_TOBESENT_KEY).toString();
      Map map = MessageUtil.httpRequest(fullReqUrl, WeChatConstant.REQ_METHOD_POST, msg);
      int errcode = (int) map.get(WeChatConstant.ERRCODE_KEY);
      String errmsg = map.get(WeChatConstant.ERRMSG_KEY).toString();
      if (!(errcode == WeChatConstant.ERRCODE_0 && errmsg.equals(WeChatConstant.ERRMSG_OK))) {
        logger.info("The result from weChat:" + JacksonUtils.jsonObjectSerializer(map));
      }
      return JacksonUtils.jsonObjectSerializer(map);
    } catch (OpenException e) {
      logger.error("Custom:send message error", e);
      throw new OpenException(e);
    }
  }

  /**
   * 检查用户是否绑定.
   * 
   * @param paramMap
   * @return
   */
  private boolean checkUserBinding(Map<String, Object> paramMap) {
    return weChatRelationDao.checkWxOpenId(paramMap.get(MsgSendingConstant.TO_USER).toString());
  }

  /**
   * 发送未绑定消息.
   * 
   * @param paramMap
   * @return
   */
  private static Map<String, Object> buildMsgBeforeBinding(Map<String, Object> paramMap) {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    Map<String, Object> msgMap = new HashMap<String, Object>();
    String touser = paramMap.get(MsgSendingConstant.TO_USER).toString();
    msgMap.put(MsgSendingConstant.TO_USER, touser);
    msgMap.put(MsgSendingConstant.MSG_TYPE, MsgSendingConstant.MSG_TYPE_TEXT);
    Map<String, Object> textMap = new HashMap<String, Object>();
    String content = MessageUtil.bulidContentForBinding(paramMap.get(MsgSendingConstant.BIND_URL_KEY).toString(),
        touser, paramMap.get(MsgReceivingConstant.EVENT_KEY_KEY).toString());
    textMap.put(MsgSendingConstant.CONTENT, content);
    msgMap.put(MsgSendingConstant.MSG_TYPE_TEXT, textMap);
    resultMap.put(MsgSendingConstant.MSG_TOBESENT_KEY, JacksonUtils.jsonObjectSerializer(msgMap));
    return resultMap;
  }

}
