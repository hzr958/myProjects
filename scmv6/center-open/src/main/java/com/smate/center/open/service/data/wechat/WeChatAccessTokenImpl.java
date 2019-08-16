package com.smate.center.open.service.data.wechat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.exception.OpenSerGetWechatTokenException;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.wechat.AccessTokenService;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 获取 微信 交互授权码 方法
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */

@Transactional(rollbackFor = Exception.class)
public class WeChatAccessTokenImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private AccessTokenService accessTokenService;

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Object openId = paramet.get(OpenConsts.MAP_OPENID);
    if (!OpenConsts.SYSTEM_OPENID.toString().equals(openId.toString())) {
      temp = super.errorMap("该接口只对内部开放权限", paramet, "");
      return temp;
    }
    Object token = paramet.get(OpenConsts.MAP_TOKEN);
    if (!OpenConsts.SMATE_TOKEN.equals(token.toString())) {
      temp = super.errorMap("该接口只对内部开放权限", paramet, "");
      return temp;
    }
    Map<String, Object> data = JacksonUtils.jsonToMap((String) paramet.get("data"));
    String type = (String) data.get("type");
    if (type != null) {
      switch (type.toLowerCase()) {
        case "development":
        case "alpha":
        case "test":
        case "uat":
        case "run":
          break;
        default:
          temp = super.errorMap("type参数不正确！", paramet, "");
          return temp;
      }
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    doVerify(paramet);
    try {
      Map<String, Object> data = JacksonUtils.jsonToMap(paramet.get("data").toString());
      String type = (String) data.get("type");
      type = type == null ? "" : type;
      String accessToken = accessTokenService.getAccessToken(type);
      Map<String, Object> temp = new HashMap<String, Object>();
      List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
      Map<String, String> dataMap = new HashMap<String, String>();
      dataMap.put("accessToken", accessToken);
      dataList.add(dataMap);
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      temp.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_000);// 响应成功
      temp.put(OpenConsts.RESULT_DATA, dataList);
      return temp;
    } catch (OpenException e) {
      logger.error("获取 微信 交互授权码 异常");
      throw new OpenSerGetWechatTokenException(e);
    }
  }

}
