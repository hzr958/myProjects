package com.smate.center.batch.service;

import java.util.Map;

import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 对不同微信模板构建对应消息map，Service接口；在实现接口时需要注意对应文字颜色的添加
 * 
 * @author hzr
 * @version 6.0.1
 */
public interface WeChatMsgMapBuildService {

  public Map<String, Object> buildMap(Map msgMap, Long templateId, String weChatOpenId, Long psnId, String token,
      String accesstokenForSendMsg) throws BatchTaskException;

}
