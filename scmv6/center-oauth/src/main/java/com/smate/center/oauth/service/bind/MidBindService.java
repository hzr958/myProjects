package com.smate.center.oauth.service.bind;

import com.smate.center.oauth.model.bind.MidBindForm;

/**
 * 移动端绑定接口
 * 
 * @author zzx
 *
 */
public interface MidBindService {

  void checkIosBingWC(MidBindForm form) throws Exception;

  void wcLogin(MidBindForm form) throws Exception;

  void wcRegistered(MidBindForm form) throws Exception;

  void checkMidBingQQ(MidBindForm form) throws Exception;

  void qqLogin(MidBindForm form) throws Exception;

  void qqRegistered(MidBindForm form) throws Exception;

  void unbindQQ(MidBindForm form) throws Exception;

  void unbindWC(MidBindForm form) throws Exception;


}
