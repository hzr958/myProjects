package com.smate.web.dyn.service.share;

import com.smate.web.dyn.model.share.SmateShareForm;

/**
 * 分享到动态服务抽象类
 * 
 * @author wsn
 * @date May 24, 2019
 */
public abstract class ShareToDynService {

  /**
   * 校验参数
   * 
   * @return
   */
  abstract boolean checkParams(SmateShareForm form);

  /**
   * 分享资源
   */
  abstract void shareRes(SmateShareForm form);


  /**
   * 分享资源到动态的流程
   */
  public void doShareResToDyn(SmateShareForm form) {
    if (checkParams(form)) {
      shareRes(form);
    }
  }
}
