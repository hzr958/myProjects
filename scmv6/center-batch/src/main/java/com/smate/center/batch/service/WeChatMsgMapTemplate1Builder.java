package com.smate.center.batch.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.wechat.template.SmateWeChatTemplateDao;
import com.smate.core.base.utils.constant.wechat.TmpMsgConstant;
import com.smate.core.base.utils.constant.wechat.WeChatConstant;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 对不同微信模板template1构建对应消息map，Service实现
 * 
 * @author hzr
 * @version 6.0.1
 */
@Transactional(rollbackFor = Exception.class)
public class WeChatMsgMapTemplate1Builder implements WeChatMsgMapBuildService {

  @Autowired
  private SmateWeChatTemplateDao smateWeChatTemplateDao;

  @Override
  public Map<String, Object> buildMap(Map msgMap, Long templateId, String weChatOpenId, Long psnId, String token,
      String accesstokenForSendMsg) throws BatchTaskException {
    String weChatTemplateId = smateWeChatTemplateDao.getWechatTempId(templateId);

    // 删除已经取出的smateTemplateId
    msgMap.remove(TmpMsgConstant.SMATE_TEMPLATE_ID_KEY);

    Map dataMap = new HashMap();
    Map itemMaps = new HashMap();

    for (Object key : msgMap.keySet()) {
      Map itemMap = new HashMap();
      itemMap.put(TmpMsgConstant.VALUE, msgMap.get(key));
      // 为对应first节点添加颜色
      if ("keyword1".equalsIgnoreCase(String.valueOf(key))) {
        itemMap.put(TmpMsgConstant.COLOR, "#173177");
      }
      itemMaps.put(key, itemMap);
    }
    // 构造符合微信规范的data层
    dataMap.put("data", itemMaps);

    // 把必要信息放入map
    dataMap.put(TmpMsgConstant.TO_USER, weChatOpenId);
    dataMap.put(TmpMsgConstant.TEMPLATE_ID, weChatTemplateId);
    String templateJason = JacksonUtils.jsonObjectSerializer(dataMap);

    Map<String, Object> resultMap = new HashMap<String, Object>();
    resultMap.put(WeChatConstant.ACCESS_TOKEN_KEY, accesstokenForSendMsg);
    resultMap.put(WeChatConstant.TEMPLATE_MSG_KEY, templateJason);

    return resultMap;
  }


}
