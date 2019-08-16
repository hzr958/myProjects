package com.smate.web.v8pub.service.searchimport;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.vo.searchimport.PubCitedVo;

/**
 * 更新成果引用接口
 * 
 * @author wsn
 * @date 2018年8月28日
 */
public interface PubCitedService {

  /**
   * 获取更新引用所需参数
   * 
   * @param vo
   * @return
   * @throws ServiceException
   */
  String getUpdatePubCitedParams(PubCitedVo vo) throws ServiceException;

  /**
   * 更新成果引用数
   * 
   * @param vo
   * @return
   * @throws ServiceException
   */
  String updatePubCited(PubCitedVo vo) throws ServiceException;
}
