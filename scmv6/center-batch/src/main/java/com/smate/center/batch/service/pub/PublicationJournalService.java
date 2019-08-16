package com.smate.center.batch.service.pub;

import java.io.Serializable;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PublicationJournal;

/**
 * 成果期刊服务.
 * 
 * @author liqinghua
 * 
 */
public interface PublicationJournalService extends Serializable {

  /**
   * 保存成果期刊.
   * 
   * @param pubId
   * @param psnId
   * @param issn
   * @param jname
   * @throws ServiceException
   */
  public void savePubJournal(Long pubId, Long psnId, String issn, String jname, Long jnlId, Integer pubYear)
      throws ServiceException;

  /**
   * 删除成果期刊.
   * 
   * @param pubId
   * @throws ServiceException
   */
  public void delPubJournal(Long pubId) throws ServiceException;

  /**
   * 判断成果期刊是否核心期刊.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public boolean isPubHxj(Long pubId) throws ServiceException;

  /**
   * 保存人员成果期刊.
   * 
   * @param psnId
   * @throws ServiceException
   */
  public void savePsnPubJournal(Long psnId) throws ServiceException;

  /**
   * 获取成果期刊信息.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public PublicationJournal getPubJournal(Long pubId) throws ServiceException;

}
