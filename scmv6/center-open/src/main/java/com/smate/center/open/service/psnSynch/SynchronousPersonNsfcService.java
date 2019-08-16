package com.smate.center.open.service.psnSynch;

import com.smate.center.open.model.third.psn.ThirdPsnInfo;

/**
 * 人员同步接口
 * 
 * @author AiJiangBin
 *
 */
public interface SynchronousPersonNsfcService {

  /**
   * 处理数据方法
   * 
   * @param personRegister
   */
  public void handleNsfcData(ThirdPsnInfo thirdPsnInfo);

}
