package com.smate.center.open.service.wechat.mass;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.open.constant.wechat.MsgSendingConstant;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.model.wechat.mass.Filter;
import com.smate.center.open.service.wechat.AccessTokenService;
import com.smate.core.base.utils.constant.wechat.WeChatConstant;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.wechat.MessageUtil;

/**
 * 抽象群发消息接口.
 * 
 * @author xys
 *
 */
public abstract class AbstractMass implements MassService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private AccessTokenService accessTokenService;

  public abstract Map<String, Object> buildMessage(Map<String, Object> paramMap) throws OpenException;

  @SuppressWarnings("rawtypes")
  @Override
  public String sendMessage(Map<String, Object> paramMap) throws OpenException {
    String reqUrl = MsgSendingConstant.REQ_URL_MASS_SEND;
    boolean isToGroup = (boolean) paramMap.get(MsgSendingConstant.IS_TO_GROUP_KEY);
    if (isToGroup) {// 根据分组进行群发
      reqUrl = MsgSendingConstant.REQ_URL_MASS_SENDALL;
      paramMap.put(MsgSendingConstant.FILTER_KEY, buildFilter(paramMap));
    }
    Map<String, Object> resultMap = buildMessage(paramMap);
    String fullReqUrl = MessageUtil.getFullReqUrl(reqUrl, accessTokenService.getAccessToken());
    String msg = resultMap.get(MsgSendingConstant.MSG_TOBESENT_KEY).toString();
    Map map = MessageUtil.httpRequest(fullReqUrl, WeChatConstant.REQ_METHOD_POST, msg);
    int errcode = (int) map.get(WeChatConstant.ERRCODE_KEY);
    String errmsg = map.get(WeChatConstant.ERRMSG_KEY).toString();
    if (!(errcode == WeChatConstant.ERRCODE_0 && errmsg.equals(WeChatConstant.ERRMSG_JOB_SUCCESS))) {
      logger.info("The result from weChat:" + JacksonUtils.jsonObjectSerializer(map));
    }
    return JacksonUtils.jsonObjectSerializer(map);
  }

  private Filter buildFilter(Map<String, Object> paramMap) {
    Filter filter = new Filter();
    boolean isToAll = (boolean) paramMap.get(MsgSendingConstant.IS_TO_ALL_KEY);
    if (isToAll) {// 向全部用户发送
      filter.setIs_to_all(true);
    } else {
      String groupId = paramMap.get(MsgSendingConstant.GROUP_ID_KEY).toString();
      filter.setIs_to_all(false);
      filter.setGroup_id(groupId);
    }
    return filter;
  }

}
