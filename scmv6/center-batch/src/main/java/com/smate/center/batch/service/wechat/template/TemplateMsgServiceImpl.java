package com.smate.center.batch.service.wechat.template;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.constant.wechat.TmpMsgConstant;
import com.smate.core.base.utils.constant.wechat.WeChatConstant;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.wechat.MessageUtil;

/**
 * 模板消息接口服务.
 * 
 * @author xys
 *
 */
@Service("templateMsgService")
@Transactional(rollbackFor = Exception.class)
public class TemplateMsgServiceImpl implements TemplateMsgService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @SuppressWarnings("rawtypes")
  @Override
  public String sendMessage(Map<String, Object> paramMap) throws SysServiceException {
    String fullReqUrl =
        MessageUtil.getFullReqUrl(TmpMsgConstant.REQ_URL, paramMap.get(WeChatConstant.ACCESS_TOKEN_KEY).toString());
    Map map = MessageUtil.httpRequest(fullReqUrl, WeChatConstant.REQ_METHOD_POST,
        paramMap.get(WeChatConstant.TEMPLATE_MSG_KEY).toString());
    int errcode = (int) map.get(WeChatConstant.ERRCODE_KEY);
    String errmsg = map.get(WeChatConstant.ERRMSG_KEY).toString();
    if (!(errcode == WeChatConstant.ERRCODE_0 && errmsg.equals(WeChatConstant.ERRMSG_OK))) {
      logger.info("The result from weChat:" + JacksonUtils.jsonObjectSerializer(map));
    }
    return JacksonUtils.jsonObjectSerializer(map);
  }

}
