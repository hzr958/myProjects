package com.smate.center.open.service.wechat.log;

import com.smate.core.base.utils.exception.SysServiceException;

/**
 * smate微信关系日志服务.
 * 
 * @author xys
 *
 */
public interface SmateWeChatLogService {

  public void save(String weChatOpenId, Long smateOpenId, String type, int status, String weChatResult)
      throws SysServiceException;
}
