package com.smate.center.open.service.psnconf;


/**
 * 个人配置：数据清理接口
 * 
 * @author zhuangyanming
 * 
 */
interface ComponentClean {

  /**
   * 清理数据
   * 
   * @param psnCnfBase
   * @throws ServiceException
   */
  void clean(Long runs, Long cnfId) throws Exception;

}
