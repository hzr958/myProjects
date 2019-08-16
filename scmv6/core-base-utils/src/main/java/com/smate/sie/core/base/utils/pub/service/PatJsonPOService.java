package com.smate.sie.core.base.utils.pub.service;

import com.smate.sie.core.base.utils.pub.dom.PubDetailDOM;
import com.smate.sie.core.base.utils.pub.exception.ServiceException;

/**
 * patjson接口
 * 
 * @author lijianming
 *
 * @date 2019年3月14日
 */
public interface PatJsonPOService {

  /**
   * 保存专利详情
   * 
   * @param patId
   * @return
   * @throws ServiceException
   */
  public void savePatJson(PubJsonDTO patJson) throws ServiceException;

  /**
   * 获取专利详情
   * 
   * @param patId
   * @return
   * @throws ServiceException
   */
  public PubDetailDOM getDOMByIdAndType(Long patId) throws ServiceException;
}
