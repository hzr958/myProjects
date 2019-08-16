package com.smate.center.batch.service.rol.pub;

import java.io.Serializable;

import com.smate.center.batch.exception.pub.ServiceException;


/**
 * 成果关键词service.
 * 
 * @author liqinghua
 * 
 */
public interface PubRolKeyWordsService extends Serializable {

  /**
   * 保存成果关键词.
   * 
   * @param pubId
   * @param psnId
   * @param zhKeywords
   * @param enKeywords
   * @throws ServiceException
   */
  public void savePubKeywords(Long pubId, Long insId, String zhKeywords, String enKeywords) throws ServiceException;

  /**
   * 删除成果关键词.
   * 
   * @param pubId
   * @throws ServiceException
   */
  public void delPubKeywords(Long pubId) throws ServiceException;

  /**
   * 获取成果关键词(包括中英文).
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public String getPubkeywords(Long pubId) throws ServiceException;

}
