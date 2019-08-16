package com.smate.center.open.service.psnconf;


/**
 * 个人配置：数据重建接口
 * 
 * @author zhuangyanming
 * 
 */
interface ComponentInstall {

  /**
   * 重建数据
   * 
   * @param runs
   * @param cnfId
   * @param psnId
   * @throws ServiceException
   */
  void install(Long runs, Long cnfId, Long psnId) throws Exception;
}
