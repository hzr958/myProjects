package com.smate.center.open.service.psnconf;


/**
 * 个人配置：重建数据接口
 * 
 * @author zhuangyanming
 * 
 */
public interface PsnCnfReBuildService extends ComponentStart {
  /**
   * 初始化人员配置.
   * 
   * @param psnId
   * @throws ServiceException
   */
  void init(Long psnId) throws Exception;
}
