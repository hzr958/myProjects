package com.smate.web.v8pub.service.sns.psnconfigpub;

import com.smate.web.v8pub.exception.ServiceException;

/**
 * 个人配置服务接口
 * 
 * @author YJ
 *
 *         2018年8月14日
 */
public interface PsnConfigService {

  /**
   * 通过psnId获取cnfId
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  Long getCnfIdByPsnId(Long psnId) throws ServiceException;
}
