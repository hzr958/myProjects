package com.smate.sie.center.task.pdwh.service;

import java.io.Serializable;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.core.base.utils.pub.exception.ServiceException;

/**
 * 成果关键词service.
 * 
 * @author liqinghua
 */
public interface PubKeyWordsService extends Serializable {

  /**
   * 保存成果关键词.
   * 
   * @param pubId
   * @param psnId
   * @param zhKeywords
   * @throws ServiceException
   */
  public void savePubKeywords(Long pubId, Long insId, String zhKeywords) throws ServiceException;

  /**
   * 删除成果关键词.
   * 
   * @param pubId
   * @throws SysServiceException
   */
  public void delPubKeywords(Long pubId) throws SysServiceException;

  /**
   * 获取成果关键词(包括中英文).
   * 
   * @param pubId
   * @return
   * @throws SysServiceException
   */
  public String getPubkeywords(Long pubId) throws SysServiceException;

}
