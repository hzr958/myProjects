package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PubKeyWords;

/**
 * 成果关键词service.
 * 
 * @author liqinghua
 * 
 */
public interface PubKeyWordsService extends Serializable {

  /**
   * 保存成果关键词.
   * 
   * @param pubId
   * @param psnId
   * @param zhKeywords
   * @param enKeywords
   * @throws ServiceException
   */
  public void savePubKeywords(Long pubId, Long psnId, String zhKeywords, String enKeywords) throws ServiceException;

  /**
   * 删除成果关键词.
   * 
   * @param pubId
   * @throws ServiceException
   */
  public void delPubKeywords(Long pubId) throws ServiceException;

  /**
   * 删除人员成果关键词.
   * 
   * @param pubId
   * @throws ServiceException
   */
  public void delPsnPubKw(Long pubId) throws ServiceException;

  /**
   * 保存成果关键词到人员成果关键词.
   * 
   * @param pubId
   * @param psnId
   * @throws ServiceException
   */
  public void savePubKwToPsnPubKw(Long pubId, Long psnId) throws ServiceException;

  /**
   * 获取成果关键词列表.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public List<PubKeyWords> getPubKws(Long pubId) throws ServiceException;
}
