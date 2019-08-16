package com.smate.center.batch.service.pub;

import java.io.Serializable;

import com.smate.center.batch.exception.pub.ServiceException;

/**
 * 人员参考文献期刊数据(用于阅读的期刊统计).
 * 
 * @author liqinghua
 * 
 */
public interface PublicationRefcJournalService extends Serializable {

  /**
   * 保存人员参考文献期刊.
   * 
   * @param pubId
   * @param psnId
   * @param issn
   * @param jname
   * @throws ServiceException
   */
  public void savePubRefcJournal(Long pubId, Long psnId, String issn, String jname, Long jnlId, Integer pubYear)
      throws ServiceException;

  /**
   * 删除人员参考文献期刊.
   * 
   * @param pubId
   * @throws ServiceException
   */
  public void delPubRefcJournal(Long pubId) throws ServiceException;

  /**
   * 更新人员最新收藏文献期刊.
   * 
   * @param psnId
   * @throws ServiceException
   */
  public void updatePsnJournalRefc(Long psnId) throws ServiceException;

  /**
   * 根据issn查询是否人员的收藏文献中的期刊
   * 
   * @param psnId
   * @param issn
   * @return
   * @throws ServiceException
   */
  int getPsnJnlByRefc(Long psnId, String issn) throws ServiceException;



}
