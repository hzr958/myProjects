package com.smate.center.open.service.wechat.event;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.open.cache.OpenCacheService;
import com.smate.center.open.constant.wechat.MsgReceivingConstant;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.service.wechat.bind.WeChatBindService;
import com.smate.center.open.service.wechat.log.SmateWeChatLogService;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.constant.wechat.WeChatConstant;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 取消订阅事件消息服务.
 * 
 * @author xys
 *
 */
public class UnsubscribeEventServiceImpl extends AbstractEvent {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private WeChatBindService weChatBindService;
  @Autowired
  private SmateWeChatLogService smateWeChatLogService;
  @Autowired
  private OpenCacheService openCacheService;

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public String giveResponse(Map<String, String> msgMap) throws OpenException {
    Map map = new HashMap();
    String fromUserName = msgMap.get(MsgReceivingConstant.FROM_USER_NAME);
    int status = 0;
    try {
      try {
        weChatBindService.cancelBindUser(fromUserName);
        map.put(WeChatConstant.ERRCODE_KEY, WeChatConstant.ERRCODE_0);
        map.put(WeChatConstant.ERRMSG_KEY, WeChatConstant.ERRMSG_OK);
        status = WeChatConstant.LOG_STATUS_SUCCESS;
        Object sessionId = openCacheService.get(SecurityConstants.CACHE_SESSIONID, fromUserName);
        if (sessionId != null) {
          openCacheService.remove(SecurityConstants.USER_DETAILS_INFO_CACHE, sessionId.toString());
        }
      } catch (Exception e) {
        map.put(WeChatConstant.ERRCODE_KEY, -1);
        map.put(WeChatConstant.ERRMSG_KEY, "error");
        status = WeChatConstant.LOG_STATUS_FAIL;
        logger.error("cancelBindUser error", e);
      }
      try {
        smateWeChatLogService.save(fromUserName, null, WeChatConstant.LOG_TYPE_UNBINDING, status, null);
      } catch (SysServiceException e) {
        logger.error("save SmateWeChatLog error", e);
      }
    } catch (Exception e) {
      logger.error("response unsubscribe error", e);
      throw new OpenException(e);
    }
    return JacksonUtils.jsonObjectSerializer(map);
  }

}
