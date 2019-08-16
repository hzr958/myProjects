package com.smate.center.oauth.service.profile.psncnf;


/**
 * 个人配置：数据重建接口
 * 
 * @author zhuangyanming
 * 
 */
interface ComponentStart {

  /**
   * 重建数据
   * 
   * @param runs
   * @param cnfId
   * @param psnId
   * @throws ServiceException
   */
  void start(Long psnId) throws Exception;
}
