package com.smate.center.task.service.sns.pub;

import java.io.Serializable;

import com.smate.center.task.exception.ServiceException;


/**
 * 人员参考文献期刊数据(用于阅读的期刊统计).
 * 
 * @author liqinghua
 * 
 */
public interface PublicationRefcJournalService extends Serializable {


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
