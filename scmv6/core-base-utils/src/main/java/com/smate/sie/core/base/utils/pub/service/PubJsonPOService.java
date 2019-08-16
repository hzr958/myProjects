package com.smate.sie.core.base.utils.pub.service;

import com.smate.sie.core.base.utils.pub.dom.PubDetailDOM;
import com.smate.sie.core.base.utils.pub.exception.ServiceException;

/**
 * pubjson接口
 * 
 * @author ZSJ
 *
 * @date 2019年2月19日
 */
public interface PubJsonPOService {

  /**
   * 获取成果详情
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public PubDetailDOM getDOMByIdAndType(Long pubId, Integer pubType) throws ServiceException;

  /**
   * 保存成果详情
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public void savePubJson(PubJsonDTO pubJson) throws ServiceException;


  public PubJsonDTO getPubJsonDTOByIdAndType(Long pubId, Integer pubType) throws ServiceException;
}
