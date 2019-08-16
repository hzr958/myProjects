package com.smate.center.open.service.data.wechat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.exception.OpenSerGetWeChatJsApiTicketException;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.wechat.WxJsApiTicketService;

public class WeChatJsApiTicketImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private WxJsApiTicketService wxJsApiTicketService;

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
    if (paramet.get("SMATE_URL") == null) {
      temp = super.errorMap("获取jsapi ticket 需要用户访问的url地址", paramet, "");
      return temp;
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    try {
      Map<String, Object> temp = new HashMap<String, Object>();
      List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
      dataList.add(wxJsApiTicketService.handleWxJsSdkSing(paramet.get("SMATE_URL").toString()));
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      temp.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_000);// 响应成功
      temp.put(OpenConsts.RESULT_DATA, dataList);
      return temp;
    } catch (Exception e) {
      logger.error("获取 微信 JSAPI Ticket 异常");
      throw new OpenSerGetWeChatJsApiTicketException(e);
    }
  }

}
